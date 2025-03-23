package com.milotnt.mapper;

import com.milotnt.pojo.Discussion;
import com.milotnt.pojo.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiscussionMapper {

    @Select("SELECT * FROM discussion ORDER BY create_time DESC")
    List<Discussion> findAll();

    @Select("SELECT * FROM discussion WHERE id = #{id}")
    Discussion findById(Integer id);

    @Insert("INSERT INTO discussion (member_id, member_name, title, content, create_time, reply_count) " +
            "VALUES (#{memberId}, #{memberName}, #{title}, #{content}, #{createTime}, #{replyCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Discussion discussion);

    @Update("UPDATE discussion SET title = #{title}, content = #{content} WHERE id = #{id}")
    void update(Discussion discussion);

    @Delete("DELETE FROM discussion WHERE id = #{id}")
    void delete(Integer id);

    @Select("SELECT * FROM reply WHERE discussion_id = #{discussionId} ORDER BY create_time ASC")
    List<Reply> findRepliesByDiscussionId(Integer discussionId);

    @Insert("INSERT INTO reply (discussion_id, member_id, member_name, content, create_time) " +
            "VALUES (#{discussionId}, #{memberId}, #{memberName}, #{content}, #{createTime})")
    void addReply(Reply reply);

    @Delete("DELETE FROM reply WHERE id = #{replyId}")
    void deleteReply(Integer replyId);

    @Delete("DELETE FROM reply WHERE discussion_id = #{discussionId}")
    void deleteRepliesByDiscussionId(Integer discussionId);

    @Select("SELECT * FROM reply WHERE id = #{replyId}")
    Reply findReplyById(Integer replyId);

    @Update("UPDATE discussion SET reply_count = reply_count + 1 WHERE id = #{discussionId}")
    void incrementReplyCount(Integer discussionId);

    @Update("UPDATE discussion SET reply_count = reply_count - 1 WHERE id = #{discussionId}")
    void decrementReplyCount(Integer discussionId);

    @Select("SELECT * FROM discussion WHERE member_id = #{memberId} ORDER BY create_time DESC")
    List<Discussion> findByMemberId(Integer memberId);

    @Select("SELECT * FROM reply WHERE member_id = #{memberId} ORDER BY create_time DESC")
    List<Reply> findRepliesByMemberId(Integer memberId);
}