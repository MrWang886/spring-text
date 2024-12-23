
package com.milotnt.controller;

import com.milotnt.pojo.Member;
import com.milotnt.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/16
 */

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //查询会员
    @RequestMapping("/selMember")
    public String selectMember(Model model) {
        List<Member> memberList = memberService.findAll();
        model.addAttribute("memberList", memberList);
        return "selectMember";
    }

    //跳转新增会员页面
    @RequestMapping("/toAddMember")
    public String toAddMember() {
        return "addMember";
    }

    //新增会员
    @RequestMapping("/addMember")
    public String addMember(Member member) {
        //会员账号&卡号随机生成
        Random random = new Random();
        String account1 = "2021";
        for (int i = 0; i < 5; i++) {
            account1 += random.nextInt(10);
        }
        Integer account = Integer.parseInt(account1);

        //初始密码
        String password = "123456";

        //获取当前日期
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = simpleDateFormat.format(date);

        Integer nextClass = member.getCardClass();

        member.setMemberAccount(account);
        member.setMemberPassword(password);
        member.setCardTime(nowDay);
        member.setCardNextClass(nextClass);

        memberService.insertMember(member);

        return "redirect:selMember";

    }

    //删除会员
    @RequestMapping("/delMember")
    public String deleteMember(Integer memberAccount) {
        memberService.deleteByMemberAccount(memberAccount);
        return "redirect:selMember";
    }

    //跳转会员修改页面
    @RequestMapping("/toUpdateMember")
    public String toUpdateMember(Integer memberAccount, Model model) {
        List<Member> memberList = memberService.selectByMemberAccount(memberAccount);
        model.addAttribute("memberList", memberList);
        return "updateMember";
    }

    //修改会员信息
    @RequestMapping("/updateMember")
    public String updateMember(Member member) {
        memberService.updateMemberByMemberAccount(member);
        return "redirect:selMember";
    }


    //跳转会员卡查询页面
    @RequestMapping("/toSelByCard")
    public String toSelectByCardId() {
        return "selectByMemberAccount";
    }

    //根据会员卡号查询
    @RequestMapping("/selByCard")
    public String selectByCardId(Model model, Integer memberAccount) {
        List<Member> memberList = memberService.selectByMemberAccount(memberAccount);
        if (memberList != null) {
            model.addAttribute("memberList", memberList);
        } else {
            String message = "会员卡号不存在！";
            model.addAttribute("noMessage", message);
        }
        return "selectByMemberAccount";
    }
    @PostMapping("/userRegister")
    public String registerMember(@ModelAttribute Member member, Model model) {
        log.info("Received registration request for member: {}", member);
        try {
            if (memberService.registerMember(member)) {
                model.addAttribute("message", "注册成功！");
                return "redirect:/member/toAddMember"; // 注册成功后重定向到登录页面
            } else {
                model.addAttribute("error", "注册失败，请重试！");
                model.addAttribute("member", member); // 将已输入的信息回显给用户
                return "redirect:/member/toAddMember"; // 返回注册页面并显示错误信息
            }
        } catch (Exception e) {
            log.error("System error during registration: ", e);
            model.addAttribute("error", "系统错误：" + e.getMessage());
            model.addAttribute("member", member); // 将已输入的信息回显给用户
            return "redirect:/member/toAddMember"; // 返回注册页面并显示错误信息
        }
    }


}
