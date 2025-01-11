package com.milotnt.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class Reply {
    private Integer id;
    private Integer discussionId;  // 关联的帖子ID
    private Integer memberId;      // 回复人ID
    private String memberName;     // 回复人姓名
    private String content;        // 回复内容
    private Date createTime;       // 回复时间
}