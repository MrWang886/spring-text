package com.milotnt.service;

import com.milotnt.pojo.Discussion;
import com.milotnt.pojo.Reply;

import java.util.List;

public interface DiscussionService {
    // 查询所有讨论帖子
    List<Discussion> findAll();

    // 根据ID查询帖子
    Discussion findById(Integer id);

    // 创建新帖子
    void create(Discussion discussion);

    // 更新帖子
    void update(Discussion discussion);

    // 删除帖子
    void delete(Integer id);

    // 查询指定帖子的所有回复
    List<Reply> findRepliesByDiscussionId(Integer discussionId);

    // 添加回复
    void addReply(Reply reply);

    // 删除回复
    void deleteReply(Integer replyId);

    // 查询用户的所有帖子
    List<Discussion> findByMemberId(Integer memberId);

    // 查询用户的所有回复
    List<Reply> findRepliesByMemberId(Integer memberId);
}