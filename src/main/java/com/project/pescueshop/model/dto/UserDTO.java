package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.Role;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.annotation.Name;
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
public class UserDTO {
    private String userId;
    private String userEmail;
    private String merchantId;
    @JsonIgnore
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userFullName;
    private String userPhoneNumber;
    private String userAvatar;
    private Boolean isSocial;
    private String status;
    private String jwtToken;
    private Integer mainAddressId;
    private Long memberPoint;
    private List<Address> addressList;
    private List<Role> userRoles;

    public UserDTO(User user){
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.userFullName = this.userFirstName + " " + this.userLastName;
        this.userPhoneNumber = user.getUserPhoneNumber();
        this.userAvatar = user.getUserAvatar();
        this.isSocial = user.getIsSocial();
        this.status = user.getStatus();
        this.mainAddressId = user.getMainAddressId();
        this.memberPoint = user.getMemberPoint();
        this.addressList = user.getAddressList();
        this.userRoles = user.getUserRoles();
    }

    public UserDTO(User user, String jwtToken){
        this(user);
        this.jwtToken = jwtToken;
    }
}
