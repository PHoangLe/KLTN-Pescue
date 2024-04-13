package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "attribute", pluralNoun = "attributeList")
public class CreateVarietyAttributeRequest {
    private String type;
    private String name;
    private String value;
}
