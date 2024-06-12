package com.project.pescueshop.model.entity.live;


import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIVE_ITEM")
@Entity
@Name(prefix = "LIIT", noun = "liveItem")
public class LiveItem {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String liveItemId;
    private String liveSessionId;
    private String varietyId;
    private String name;
    private String coverImage;
    private Long initialPrice;
    private Long livePrice;
    private Integer initialStock;
    private Integer currentStock;
}
