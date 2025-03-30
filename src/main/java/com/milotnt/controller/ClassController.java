package com.milotnt.controller;

import com.milotnt.pojo.ClassOrder;
import com.milotnt.pojo.ClassTable;
import com.milotnt.service.ClassOrderService;
import com.milotnt.service.ClassTableService;
import com.milotnt.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassTableService classTableService;

    @Autowired
    private ClassOrderService classOrderService;

    @Autowired
    private CoachService coachService;

    // 查询课程
    @RequestMapping("/selClass")
    public String selectClass(Model model) {
        List<ClassTable> classList = classTableService.findAll();
        model.addAttribute("classList", classList);
        return "selectClass";
    }

    // 跳转新增课程页面
    @RequestMapping("/toAddClass")
    public String toAddClass() {
        return "addClass";
    }

    // 新增课程
    @RequestMapping("/addClass")
    public String addClass(ClassTable classTable) {
        classTableService.insertClass(classTable);
        return "redirect:selClass";
    }

    // 删除课程
    @RequestMapping("/delClass")
    public String deleteClass(Integer classId) {
        classTableService.deleteClassByClassId(classId);
        classTableService.deleteOrderByClassId(classId);
        return "redirect:selClass";
    }

    // 查询课程报名信息
    @RequestMapping("/selClassOrder")
    public String selectClassOrder(@RequestParam("classId") Integer classId,
                             HttpSession session,
                             Model model) {
        // 权限判断保持不变
        if (session.getAttribute("admin") == null && session.getAttribute("coach") == null) {
            return "redirect:/adminLogin";
        }

        // 根据 classId 查询具体课程的报名信息
        List<ClassOrder> classOrderList = classOrderService.selectMemberOrderList(classId);
        ClassTable classTable = classTableService.selectByClassId(classId);

        model.addAttribute("classOrderList", classOrderList);
        model.addAttribute("class", classTable); // 传递课程详情对象

        return "selectClassOrder"; 
    }
    // //获取用户报名信息课程和教练教练课程id一样的数据
    @RequestMapping("/selectCoachMember")
    public String selectCoachMember(HttpSession session, Model model) {
        // 从session获取教练信息
        ClassTable coach = (ClassTable) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toAoachLogin";
        }
        
        // 获取教练账号
        Integer coachAccount = Integer.valueOf(coach.getCoachAccount());
        
        // 查询教练负责的所有课程
        List<ClassTable> classList = classTableService.findByCoachAccount(coachAccount);
        model.addAttribute("classList", classList);
        
        // 为每个课程收集报名信息
        List<ClassOrder> combinedOrders = new ArrayList<>();
        for (ClassTable ct : classList) {
            List<ClassOrder> orders = classOrderService.selectMemberOrderList(ct.getClassId());
            combinedOrders.addAll(orders);
        }
        model.addAttribute("classOrderList", combinedOrders);
        
        return "coachMain";
    }

    // 跳转教练个人信息页面
    @RequestMapping("/toCoachInfo")
    public String toCoachInfo(HttpSession session, Model model) {


        ClassTable coach = (ClassTable) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toAoachLogin"; // 如果未登录，重定向到登录页面
        }
        Integer coachAccount = Integer.valueOf(coach.getCoachAccount());
        ClassTable coachInfo = coachService.selectByAccount(coachAccount);
        model.addAttribute("coachInfo", coachInfo);
        return "coachInformation";
    }

    // 教练信息更新后保留数据（如果存在类似表单提交场景）
    @RequestMapping("/updateCoachInfo")
    public String updateCoachInfo(HttpSession session, 
                             ClassTable classTable, 
                             Model model) {
        coachService.updateCoachInfo(classTable);
        session.setAttribute("coach", classTable);
        
        // 保留表单数据
        model.addAttribute("coachInfo", classTable);
        
        return "redirect:toCoachInfo";
    }
}