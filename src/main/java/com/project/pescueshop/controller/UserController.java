package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.AddressInputDTO;
import com.project.pescueshop.model.dto.UpdateUserProfileDTO;
import com.project.pescueshop.model.dto.UserDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.UserService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class UserController {
    private final UserService userService;

    @PostMapping("/address")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<Address>> addUserAddress(@RequestBody AddressInputDTO dto) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        Address address = userService.addUserAddress(user, dto);
        ResponseDTO<Address> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, address, "address");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/address")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<Address>>> getUserAddress() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        List<Address> address = userService.getAddressListByUser(user.getUserId());
        ResponseDTO<List<Address>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, address, "addressList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<UserDTO>> getCurrentUserInfo() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        UserDTO dto = new UserDTO(user);
        ResponseDTO<UserDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, dto, "userInfo");
        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUserInfo(@RequestBody UpdateUserProfileDTO updateUserProfileDTO) throws FriendlyException {
        UserDTO user = userService.updateUserProfile(updateUserProfileDTO);
        ResponseDTO<UserDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, user, "userInfo");
        return ResponseEntity.ok(result);
    }
}
