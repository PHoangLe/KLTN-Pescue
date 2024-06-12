package com.project.pescueshop.model.entity.live;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LIVE_SESSION")
@Entity
@Builder
@Name(prefix = "LISS", noun = "liveSession")
public class LiveSession {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String sessionId;
    private String sessionKey;
    private String userId;
    private String merchantId;
    private String title;
    private String thumbnail;
    private Date startTime;
    private Date endTime;
    private String status;
}
