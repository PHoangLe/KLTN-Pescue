package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CreateRatingDTO;
import com.project.pescueshop.model.dto.ProductDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Rating;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.RatingService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class RatingController {
    private final RatingService ratingService;

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    public ResponseEntity<ResponseDTO<Rating>> addRating(@RequestBody CreateRatingDTO createRatingDTO) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        Rating rating = ratingService.addRating(user, createRatingDTO);

        ResponseDTO<Rating> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, rating);
        return ResponseEntity.ok(result);
    }
}
