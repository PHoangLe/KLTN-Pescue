package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Rating;
import com.project.pescueshop.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RatingDAO {
    private final RatingRepository ratingRepository;

    public void saveAndFlushRating(Rating rating){
        ratingRepository.saveAndFlush(rating);
    }

    public List<Rating> getRatingByProductId(String productId){
        return ratingRepository.findRatingByProductId(productId);
    }
}
