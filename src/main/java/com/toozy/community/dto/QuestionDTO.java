package com.toozy.community.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.toozy.community.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private User user;
}
