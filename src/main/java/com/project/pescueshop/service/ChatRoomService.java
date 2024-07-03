package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.MerchantInfo;
import com.project.pescueshop.model.entity.ChatMessage;
import com.project.pescueshop.model.entity.ChatRoom;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.ChatDAO;
import com.project.pescueshop.util.constant.EnumMessageStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatDAO chatDAO;
    private final UserService userService;
    private final MerchantService merchantService;

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist){
        User sender = userService.findById(senderId);
        User recipient = userService.findById(recipientId);

        return chatDAO
                .findRoomByUser1AndUser2(senderId, recipientId)
                .map(ChatRoom::getChatRoomId)
                .or(() -> {
                    if(!createIfNotExist)
                        return Optional.empty();

                    ChatRoom newChatRoom = new ChatRoom(sender, recipient);

                    chatDAO.saveAndFlushRoom(newChatRoom);
                    return Optional.of(newChatRoom.getChatRoomId());
                });
    }

    public List<ChatRoom> findAllChatRoomByUserID(String userID) {
        List<ChatRoom> chatRooms = chatDAO.findAllRoomByUser(userID);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (ChatRoom chatRoom : chatRooms) {
            executorService.submit(() -> {
                User firstUser = chatRoom.getFirstUser();
                User secondUser = chatRoom.getSecondUser();

                if (firstUser.isMerchant()) {
                    Merchant merchant = merchantService.getMerchantByUserId(firstUser.getUserId());
                    chatRoom.setMerchantInfo(MerchantInfo.builder()
                            .merchantAvatar(merchant.getMerchantAvatar())
                            .merchantName(merchant.getMerchantName())
                            .userId(merchant.getUserId())
                            .build());
                }

                if (secondUser.isMerchant()) {
                    Merchant merchant = merchantService.getMerchantByUserId(secondUser.getUserId());
                    chatRoom.setMerchantInfo(MerchantInfo.builder()
                            .merchantAvatar(merchant.getMerchantAvatar())
                            .merchantName(merchant.getMerchantName())
                            .userId(merchant.getUserId())
                            .build());
                }
            });
        }

        executorService.shutdown();
        return chatRooms;
    }

    public List<ChatMessage> findAllChatMessageByChatRoomID(String chatRoomID, String senderID, String recipientID) {
        updateStatus(recipientID, senderID, EnumMessageStatus.RECEIVED.toString());
        return chatDAO.findAllMessageByRoomID(chatRoomID);
    }

    public void updateStatus(String senderId, String recipientId, String status) {
        chatDAO.updateChatStatus(senderId, recipientId, status);
    }

    @Transactional
    public void createChatRoomForNewUser(String userId) {
        try {
            User adminUser = userService.getAdminUser();
            User newUser = userService.findById(userId);

            ChatRoom newChatRoom = new ChatRoom(newUser, adminUser);
            chatDAO.saveAndFlushRoom(newChatRoom);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
