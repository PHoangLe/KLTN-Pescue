package com.project.pescueshop.model.elastic.document;

import com.project.pescueshop.model.entity.Rating;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class RatingData{
    private String ratingId;
    private String productId;
    private String userId;
    private Integer score;
    private Long timestamp;
    private String message;
    private Boolean isBought;

    public static RatingData fromRating(Rating rating){
        return RatingData.builder()
            .ratingId(rating.getRatingId())
            .productId(rating.getProductId())
            .userId(rating.getUserId())
            .score(rating.getScore())
            .timestamp(rating.getDate().getTime())
            .message(rating.getMessage())
            .isBought(rating.getIsBought())
            .build();
    }
}
