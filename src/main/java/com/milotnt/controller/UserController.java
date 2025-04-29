package com.milotnt.controller;

import com.milotnt.pojo.ClassOrder;
import com.milotnt.pojo.ClassTable;
import com.milotnt.pojo.Member;
import com.milotnt.service.ClassOrderService;
import com.milotnt.service.ClassTableService;
import com.milotnt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2025/3/15
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ClassTableService classTableService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ClassOrderService classOrderService;


    //跳转个人信息页面
    @RequestMapping("/toUserInfo")
    public String toUserInformation(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        model.addAttribute("member", member);
        return "userInformation";
    }

    //跳转修改个人信息页面
    @RequestMapping("/toUpdateInfo")
    public String toUpdateUserInformation(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("user");
        model.addAttribute("member", member);
        return "updateUserInformation";
    }

    //修改个人信息
    @RequestMapping("/updateInfo")
    public String updateUserInformation(HttpSession session, Member member) {
        Member member1 = (Member) session.getAttribute("user");

        member.setMemberAccount(member1.getMemberAccount());
        member.setCardClass(member1.getCardClass());
        member.setCardTime(member1.getCardTime());
        member.setCardNextClass(member1.getCardNextClass());

        memberService.updateMemberByMemberAccount(member);
        return "userInformation";
    }

    //跳转我的课程页面
    @RequestMapping("/toUserClass")
    public String toUserClass(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        model.addAttribute("member", member);
        Integer memberAccount = member.getMemberAccount();
        List<ClassOrder> classOrderList = classOrderService.selectClassOrderByMemberAccount(memberAccount);
        model.addAttribute("classOrderList", classOrderList);
        return "userClass";
    }

    //退课
    @RequestMapping("delUserClass")
    public String deleteUserClass(Integer classOrderId) {
        classOrderService.deleteByClassOrderId(classOrderId);
        return "redirect:toUserClass";
    }

    //跳转报名选课页面
    @RequestMapping("/toApplyClass")
    public String toUserApplyClass(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        List<ClassTable> classList = classTableService.findAll();
        model.addAttribute("member", member);
        model.addAttribute("classList", classList);
        return "userApplyClass";
    }

    //报名选课
    @RequestMapping("/applyClass")
    public String userApplyClass(Integer classId, Model model, HttpSession session) {
        ClassTable classTable = classTableService.selectByClassId(classId);
        Member member = (Member) session.getAttribute("user");

        Integer classId1 = classTable.getClassId();
        String className = classTable.getClassName();
        String coach = classTable.getCoach();
        String classBegin = classTable.getClassBegin();
        String memberName = member.getMemberName();
        Integer memberAccount = member.getMemberAccount();


        // 创建ClassOrder实例，用于后续的操作
        ClassOrder classOrder = new ClassOrder(classId1, className, coach, memberName, memberAccount, classBegin);

        // 获取当前成员的账户信息
        Integer memberAccount1 = member.getMemberAccount();

        // 使用类ID和成员账户信息查询对应的ClassOrder记录
        ClassOrder classOrder1 = classOrderService.selectMemberByClassIdAndMemberAccount(classId1, memberAccount1);

        if (classOrder1 == null) {
            classOrderService.insertClassOrder(classOrder);
        }

        return "redirect:toUserClass";
    }

    // 打卡功能
    @RequestMapping("/checkin")
    public String checkin(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("user");
        if (member == null) {
            return "redirect:/toUserLogin";
        }
        Integer memberAccount = member.getMemberAccount();

        // 获取累计打卡次数
        Integer totalCheckins = memberService.getCheckinCount(memberAccount);
        model.addAttribute("totalCheckins", totalCheckins);

        // 获取连续打卡天数（假设已有方法）
        Integer continuousDays = memberService.getContinuousCheckinDays(memberAccount);
        model.addAttribute("continuousDays", continuousDays);

        // 获取当前排名
        List<Map<String, Object>> ranking = memberService.getCheckinRanking();
        model.addAttribute("ranking", ranking);
        
        int userRank = 1;
        for (Map<String, Object> entry : ranking) {
            if (entry.get("memberName").equals(member.getMemberName())) {
                break;
            }
            userRank++;
        }
        model.addAttribute("userRank", userRank);

        Boolean success = memberService.checkin(memberAccount);
        if (success) {
            model.addAttribute("msg", "打卡成功！");
        } else {
            model.addAttribute("msg", "今天已经打过卡了！");
        }
        return "userCheckin";
    }

    // 打卡排行榜
    @RequestMapping("/checkinRanking")
    public String checkinRanking(Model model) {
        List<Map<String, Object>> ranking = memberService.getCheckinRanking();
        model.addAttribute("ranking", ranking);
        return "checkinRanking";
    }
}
