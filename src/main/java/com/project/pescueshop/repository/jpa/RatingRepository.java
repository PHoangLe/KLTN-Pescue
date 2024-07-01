package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    @Query("select r from Rating r where r.productId = ?1")
    List<Rating> findRatingByProductId(String productId);

    @Query("select r from Rating r where r.productId = ?1 and r.user.userId = ?2")
    Optional<Rating> findRatingByProductIdAndUserId(String productId, String userId);
}
