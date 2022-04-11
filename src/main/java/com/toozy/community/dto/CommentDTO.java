package com.toozy.community.dto;

import com.toozy.community.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private User user;
}
