package com.project.pescueshop.model.elastic.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewAuditLogData {
    private String viewAuditLogId;
    private String objectId;
    private String viewerId;
    private Long timestamp;
    private String objectType;
}
