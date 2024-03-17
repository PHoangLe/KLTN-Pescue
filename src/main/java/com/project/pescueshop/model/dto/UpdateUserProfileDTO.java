package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "user", pluralNoun = "userList")
public class UpdateUserProfileDTO {
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userPhoneNumber;
    private String userAvatar;
}
