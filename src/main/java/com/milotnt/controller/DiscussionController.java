package com.milotnt.controller;

import com.milotnt.pojo.Admin;
import com.milotnt.pojo.Discussion;
import com.milotnt.pojo.Member;
import com.milotnt.pojo.Reply;
import com.milotnt.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    // 显示讨论列表
    @GetMapping("/list")
    public String list(Model model) {
        List<Discussion> discussions = discussionService.findAll();
        model.addAttribute("discussions", discussions);
        return "discussionList";
    }

    // 显示发帖页面
    @GetMapping("/toCreate")
    public String toCreate() {
        return "discussionCreate";
    }

    // 发布新帖子
    @PostMapping("/create")
    public String create(Discussion discussion, HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        // 根据 Member 类的实际属性名修改
        discussion.setMemberId(member.getMemberAccount());  // 使用会员账号
        discussion.setMemberName(member.getMemberName());
        discussion.setCreateTime(new Date());
        discussion.setReplyCount(0);
        discussionService.create(discussion);
        return "redirect:/discussion/list";
    }

    // 显示帖子详情
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Discussion discussion = discussionService.findById(id);
        List<Reply> replies = discussionService.findRepliesByDiscussionId(id);
        model.addAttribute("discussion", discussion);
        model.addAttribute("replies", replies);
        return "discussionDetail";
    }

    // 发表回复
    @PostMapping("/reply")
    public String reply(Reply reply, HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        // 根据 Member 类的实际属性名修改
        reply.setMemberId(member.getMemberAccount());  // 如果是使用会员账号作为ID
        reply.setMemberName(member.getMemberName());
        reply.setCreateTime(new Date());
        discussionService.addReply(reply);
        return "redirect:/discussion/detail/" + reply.getDiscussionId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        // 获取当前登录用户
        Object user = session.getAttribute("user");
        if (user instanceof Admin) {
            discussionService.delete(id);
            return "redirect:/discussion/list";
        } else if (user instanceof Member) {
            Member member = (Member) user;
            Discussion discussion = discussionService.findById(id);
            // 验证权限：只有帖子作者才能删除
            if (discussion != null && discussion.getMemberId().equals(member.getMemberAccount())) {
                discussionService.delete(id);
                return "redirect:/discussion/list";
            } else {
                // 可以添加一个错误消息
                return "redirect:/discussion/list?error=unauthorized";
            }
        } else {
            return "redirect:/adminLogin";
        }
    }

    @GetMapping("/delete-reply/{replyId}")
    public String deleteReply(@PathVariable Integer replyId, HttpSession session) {
        // 获取当前登录用户
        Object user = session.getAttribute("user");
        if (user instanceof Admin) {
            discussionService.deleteReply(replyId);
            return "redirect:/discussion/list";
        } else if (user instanceof Member) {
            // 获取回复信息
            Reply reply = discussionService.findReplyById(replyId);
            if (reply != null) {
                Member member = (Member) user;
                // 验证权限：只有回复作者才能删除
                if (reply.getMemberId().equals(member.getMemberAccount())) {
                    discussionService.deleteReply(replyId);
                    return "redirect:/discussion/detail/{id}";
                } else {
                    // 可以添加一个错误消息
                    return "redirect:/discussion/detail/{id}?error=unauthorized";
                }
            }
        } else {
            return "redirect:/adminLogin";
        }
        return "redirect:/discussion/detail/{id}";
    }
}