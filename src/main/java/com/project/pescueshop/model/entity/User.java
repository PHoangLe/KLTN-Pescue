package com.project.pescueshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.pescueshop.model.dto.RegisterDTO;
import com.project.pescueshop.model.dto.UserDTO;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.util.constant.EnumStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "USERS")
@Entity
@Builder
@AllArgsConstructor
@Name(prefix = "USER")
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String userId;
    private String userEmail;
    @JsonIgnore
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userPhoneNumber;
    private String userAvatar;
    private Boolean isSocial;
    private String status = "IN_ACTIVE";
    private Integer mainAddressId;
    private Long memberPoint;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    )
    private List<Role> userRoles;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    private List<Address> addressList;

    public User(UserDTO dto){
        this.userEmail = dto.getUserEmail();
        this.userPassword = dto.getUserPassword();
        this.userFirstName = dto.getUserFirstName();
        this.userLastName = dto.getUserLastName();
        this.userPhoneNumber = dto.getUserPhoneNumber();
        this.userAvatar = dto.getUserAvatar();
        this.status = dto.getStatus();
        this.memberPoint = dto.getMemberPoint();
    }

    public User(RegisterDTO dto){
        this.userEmail = dto.getUserEmail();
        this.userPassword = dto.getUserPassword();
        this.userFirstName = dto.getUserFirstName();
        this.userLastName = dto.getUserLastName();
        this.userPhoneNumber = dto.getUserPhoneNumber();
        this.userAvatar = dto.getUserAvatar();
        this.status = EnumStatus.INACTIVE.getValue();
        this.memberPoint = 0L;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        userRoles.forEach(_role -> {
            roles.add(new SimpleGrantedAuthority(_role.getRoleName()));
        });
        return roles;
    }
    @Override
    @JsonIgnore
    public String getPassword() {
        return this.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isLocked(){return this.status == "LOCKED";}
    public boolean isInActive(){return this.status == "IN_ACTIVE";}
    public boolean isDeleted(){return this.status == "DELETED";}

    public boolean isMerchant() {
        return userRoles.stream().anyMatch(role -> role.getRoleName().equals("ROLE_MERCHANT"));
    }
}