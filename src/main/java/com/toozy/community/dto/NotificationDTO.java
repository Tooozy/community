package com.toozy.community.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by codedrinker on 2019/6/14.
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Long outerid;
    private Integer type;
    private String typeName;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private LocalDateTime gmtCreate;
}
