package com.milotnt.service.impl;

import com.milotnt.mapper.DiscussionMapper;
import com.milotnt.pojo.Discussion;
import com.milotnt.pojo.Reply;
import com.milotnt.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    @Autowired
    private DiscussionMapper discussionMapper;

    @Override
    public List<Discussion> findAll() {
        return discussionMapper.findAll();
    }

    @Override
    public Discussion findById(Integer id) {
        return discussionMapper.findById(id);
    }

    @Override
    @Transactional
    public void create(Discussion discussion) {
        discussionMapper.create(discussion);
    }

    @Override
    @Transactional
    public void update(Discussion discussion) {
        discussionMapper.update(discussion);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 先删除帖子的所有回复
        discussionMapper.deleteRepliesByDiscussionId(id);
        // 再删除帖子
        discussionMapper.delete(id);
    }

    @Override
    public List<Reply> findRepliesByDiscussionId(Integer discussionId) {
        return discussionMapper.findRepliesByDiscussionId(discussionId);
    }

    @Override
    @Transactional
    public void addReply(Reply reply) {
        // 添加回复
        discussionMapper.addReply(reply);
        // 更新帖子回复数
        discussionMapper.incrementReplyCount(reply.getDiscussionId());
    }

    @Override
    public Reply findReplyById(Integer replyId) {
        return discussionMapper.findReplyById(replyId);
    }

    @Override
    @Transactional
    public void deleteReply(Integer replyId) {
        // 获取回复信息
        Reply reply = discussionMapper.findReplyById(replyId);
        if (reply != null) {
            // 删除回复
            discussionMapper.deleteReply(replyId);
            // 减少帖子回复数
            discussionMapper.decrementReplyCount(reply.getDiscussionId());
        }
    }

    @Override
    public List<Discussion> findByMemberId(Integer memberId) {
        return discussionMapper.findByMemberId(memberId);
    }

    @Override
    public List<Reply> findRepliesByMemberId(Integer memberId) {
        return discussionMapper.findRepliesByMemberId(memberId);
    }

    @Override
    @Transactional
    public void deleteRepliesByDiscussionId(Integer discussionId) {
        discussionMapper.deleteRepliesByDiscussionId(discussionId);
    }
}