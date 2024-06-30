package com.project.pescueshop.model.elastic.document;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class ViewAuditLogData {
    private String viewAuditLogId;
    private String objectId;
    private String viewerId;
    private Long timestamp;
    private String objectType;
}
