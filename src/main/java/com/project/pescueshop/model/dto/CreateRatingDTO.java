package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "rating", pluralNoun = "ratingList")
public class CreateRatingDTO {
    private String productId;
    private Integer score;
    private String message;
}
