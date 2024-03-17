package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "CHAT_ROOM")
@Entity
@Builder
@AllArgsConstructor
@Name(prefix = "CHRO", noun = "chatRoom", pluralNoun = "chatRoomList")
public class ChatRoom {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String chatRoomId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "firstUserId", referencedColumnName = "userId")
    private User firstUser;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "secondUserId", referencedColumnName = "userId")
    private User secondUser;

    public ChatRoom(User user1, User user2){
        this.firstUser = user1;
        this.secondUser = user2;
    }
}
