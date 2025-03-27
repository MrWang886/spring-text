package com.milotnt.controller;

import com.milotnt.pojo.Discussion;
import com.milotnt.pojo.Reply;
import com.milotnt.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/manage-comments")
public class AdminController {

    @Autowired
    private DiscussionService discussionService;

    @GetMapping
    public String manageComments(Model model) {
        List<Discussion> discussions = discussionService.findAll();
        model.addAttribute("discussions", discussions);
        return "admin/manageComments";
    }

    @GetMapping("/view-post/{id}")
    public String viewPost(@PathVariable Integer id, Model model) {
        Discussion discussion = discussionService.findById(id);
        List<Reply> replies = discussionService.findRepliesByDiscussionId(id);
        model.addAttribute("discussion", discussion);
        model.addAttribute("replies", replies);
        return "admin/viewPost"; // 假设有一个新的视图文件来显示帖子详情
    }

    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable Integer id) {
        discussionService.delete(id);
        return "redirect:/admin/manage-comments";
    }

    @GetMapping("/delete-replies/{id}")
    public String deleteReplies(@PathVariable Integer id) {
        discussionService.deleteRepliesByDiscussionId(id);
        return "redirect:/admin/manage-comments";
    }
}