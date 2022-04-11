package com.toozy.community.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Getter
@Setter
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //通知的人
    private Long notifier;
    //接受消息的人
    private Long receiver;
    //外键id，结合type来判断到底是评论，回复，还是点赞
    private Long outerid;

    private Integer type;
    //已读未读
    private Integer status;

    private String notifierName;

    private String outerTitle;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;


}
