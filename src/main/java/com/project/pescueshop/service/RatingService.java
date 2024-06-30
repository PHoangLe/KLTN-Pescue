package com.project.pescueshop.service;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.project.pescueshop.model.dto.CreateRatingDTO;
import com.project.pescueshop.model.dto.RatingResultDTO;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.ProductData;
import com.project.pescueshop.model.elastic.document.RatingData;
import com.project.pescueshop.model.entity.Rating;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.RatingDAO;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingDAO ratingDAO;

    public Rating addRating(User user, CreateRatingDTO dto) throws FriendlyException {
        Rating existedRating = ratingDAO.getRatingByProductIdAndUserId(dto.getProductId(), user.getUserId());
        if (existedRating != null){
            throw new FriendlyException("You have already rated this product");
        }

        Rating rating = Rating.builder()
                .score(dto.getScore())
                .productId(dto.getProductId())
                .date(Util.getCurrentDate())
                .user(user)
                .message(dto.getMessage())
                .isBought(true)
                .build();

        ratingDAO.saveAndFlushRating(rating);

        CompletableFuture.runAsync(() -> pushRatingToElasticSearch(rating));

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

    public void pushRatingToElasticSearch(Rating rating){
        RatingData data = RatingData.fromRating(rating);

        IndexRequest<RatingData> request = IndexRequest.of(i -> i
                .index(EnumElasticIndex.RATING_DATA.getName())
                .id(data.getRatingId())
                .document(data));
        try {
            ElasticClient.get().index(request);
            log.info("Push product to elastic search: {}", data);
        } catch (IOException e) {
            log.error("Error when push product to elastic search document: {} error: ", data, e);
        }
    }
}
