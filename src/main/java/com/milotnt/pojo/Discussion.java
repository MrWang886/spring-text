package com.milotnt.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Discussion {
    private Integer id;
    private Integer memberId;      // 发帖人ID
    private String memberName;     // 发帖人姓名
    private String title;          // 帖子标题
    private String content;        // 帖子内容
    private Date createTime;       // 发帖时间
    private Integer replyCount;    // 回复数量
}