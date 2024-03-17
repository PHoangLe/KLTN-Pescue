package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.dto.ErrorLogDTO;
import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "ERROR_LOG")
@Entity
@AllArgsConstructor
@Builder
@Name(prefix = "ERRL")
public class ErrorLog {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String errorLogId;
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column(columnDefinition = "TEXT")
    private String stackTrace;
    private String client;
    private String url;
    private String locale;
    private String email;
    private Date date;

    public ErrorLog(ErrorLogDTO dto){
        this.message = dto.getMessage();
        this.stackTrace = dto.getStackTrace();
        this.client = dto.getClient();
        this.url = dto.getUrl();
        this.locale = dto.getLocale();
        this.email = dto.getEmail();
        this.date = dto.getDate();
    }
}
