package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "newUser", pluralNoun = "newUserList")
public class RegisterDTO {
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userPhoneNumber;
    private String userAvatar;
    @JsonIgnore
    private Boolean isSocial;
}
