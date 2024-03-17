package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.CreateRatingDTO;
import com.project.pescueshop.model.dto.RatingResultDTO;
import com.project.pescueshop.model.entity.Rating;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.repository.dao.RatingDAO;
import com.project.pescueshop.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingDAO ratingDAO;

    public Rating addRating(User user, CreateRatingDTO dto){
        Rating rating = Rating.builder()
                .score(dto.getScore())
                .productId(dto.getProductId())
                .date(Util.getCurrentDate())
                .user(user)
                .message(dto.getMessage())
                .isBought(true)
                .build();

        ratingDAO.saveAndFlushRating(rating);

        return rating;
    }

    public List<RatingResultDTO> getRatingByProductId(String productId){
        List<Rating> ratingList = ratingDAO.getRatingByProductId(productId);

        return ratingList.stream()
                .map(rating -> RatingResultDTO.builder()
                        .ratingId(rating.getRatingId())
                        .productId(rating.getProductId())
                        .userId(rating.getUser().getUserId())
                        .userFirstName(rating.getUser().getUserFirstName())
                        .userLastName(rating.getUser().getUserLastName())
                        .userAvatar(rating.getUser().getUserAvatar())
                        .score(rating.getScore())
                        .date(rating.getDate())
                        .message(rating.getMessage())
                        .isBought(rating.getIsBought())
                        .build())
                .toList();
    }
}
