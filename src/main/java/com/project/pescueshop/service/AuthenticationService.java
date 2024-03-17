package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.AuthenticationDTO;
import com.project.pescueshop.model.dto.RegisterDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.dto.UserDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.model.exception.UnauthenticatedException;
import com.project.pescueshop.config.security.JwtService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final ThreadService threadService;

    public static User getCurrentLoggedInUser() throws FriendlyException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null)
            throw new FriendlyException(EnumResponseCode.NOT_LOGGED_IN);

        return principal;
    }

    public ResponseEntity<ResponseDTO<UserDTO>> userRegister(RegisterDTO request) throws FriendlyException {
        User user = userService.findByEmail(request.getUserEmail());
        if (user != null){
            if (!user.isInActive()) {
                throw new UnauthenticatedException(EnumResponseCode.ACCOUNT_EXISTED, request.getUserEmail());
            }
            else {
                throw new UnauthenticatedException(EnumResponseCode.ACCOUNT_INACTIVE, request.getUserEmail());
            }
        }

        user = new User(request);

        //Set default for new User
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setIsSocial(false);
        user.setMemberPoint(0L);
        user.setStatus(EnumStatus.INACTIVE.getValue());

        userService.addUser(user);

        threadService.createNeededInfoForNewUser(user, false);

        ResponseDTO<UserDTO> response = new ResponseDTO<>(EnumResponseCode.CREATED_ACCOUNT_SUCCESSFUL, new UserDTO(user));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ResponseDTO<UserDTO>> authenticate(AuthenticationDTO request) throws FriendlyException {
        try{
            User user = userService.findByEmail(request.getUserEmail());

            if (user != null) {
                EnumStatus status = EnumStatus.getByValue(user.getStatus());

                switch (status) {
                    case INACTIVE -> {
                        throw new UnauthenticatedException(EnumResponseCode.ACCOUNT_INACTIVE, request.getUserEmail());
                    }
                    case LOCKED -> {
                        throw new UnauthenticatedException(EnumResponseCode.ACCOUNT_LOCKED, request.getUserEmail());
                    }
                    case DELETED -> {
                        throw new UnauthenticatedException(EnumResponseCode.ACCOUNT_NOT_FOUND, request.getUserEmail());
                    }
                }
            }

            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            var jwtToken = jwtService.generateJwtToken(user);
            log.trace("Successfully authenticate user: " + request.getUserEmail());

            ResponseDTO<UserDTO> response = new ResponseDTO<>(EnumResponseCode.AUTHENTICATE_SUCCESSFUL, new UserDTO(user, jwtToken));
            return ResponseEntity.ok(response);
        }
        catch (AuthenticationException e){
            log.trace(e.getMessage());
            ResponseDTO<UserDTO> response = new ResponseDTO<>(EnumResponseCode.BAD_CREDENTIAL);
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<ResponseDTO<UserDTO>> googleUserAuthenticate(RegisterDTO request){
        User user = userService.findByEmail(request.getUserEmail());

        if (user == null){
            user = new User(request);

            user.setIsSocial(true);
            user.setMemberPoint(0L);
            user.setStatus(EnumStatus.ACTIVE.getValue());

            userService.addUser(user);
            threadService.createNeededInfoForNewUser(user, true);
        }

        var jwtToken = jwtService.generateJwtToken(user);
        log.trace("Successfully authenticate user: " + request.getUserEmail());

        ResponseDTO<UserDTO> response = new ResponseDTO<>(EnumResponseCode.AUTHENTICATE_SUCCESSFUL, new UserDTO(user, jwtToken));
        return ResponseEntity.ok(response);
    }
}