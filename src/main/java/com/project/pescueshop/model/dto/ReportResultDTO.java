package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "reportResult", pluralNoun = "reportList")
public class ReportResultDTO {
    private Date reportTime;
    private Long totalSell;
    private Long totalImport;
}
