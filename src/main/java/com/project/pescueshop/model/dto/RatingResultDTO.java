package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "rating", pluralNoun = "ratingList")
public class RatingResultDTO {
    private String ratingId;
    private String productId;
    private String userId;
    private String userAvatar;
    private String userFirstName;
    private String userLastName;
    private Integer score;
    private Date date;
    private String message;
    private Boolean isBought;
}
