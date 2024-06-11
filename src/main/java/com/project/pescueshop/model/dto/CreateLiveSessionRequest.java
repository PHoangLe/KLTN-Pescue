package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "createLiveSessionRequest", pluralNoun = "createLiveSessionRequest")
@Data
@Builder
public class CreateLiveSessionRequest {
    String liveSessionTitle;
    List<LiveItemRequest> liveItemList;
}
