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
    public String selectClassOrder(HttpSession session, Model model) {
        // 获取当前登录教练的账号
        ClassTable coach = (ClassTable) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toAoachLogin"; // 如果未登录，重定向到登录页面
        }
        Integer coachAccount = Integer.valueOf(coach.getCoachAccount());

        // 根据教练账号查询其负责的课程
        List<ClassTable> classList = classTableService.findByCoachAccount(coachAccount);

        // 根据课程查询报名信息
        List<ClassOrder> classOrderList = new ArrayList<>();
        for (ClassTable classTable : classList) {
            classOrderList.addAll(classOrderService.selectMemberOrderList(classTable.getClassId()));
        }

        model.addAttribute("classOrderList", classOrderList);
        model.addAttribute("classList", classList); // 添加教练负责的课程列表到模型
        return "coachMain"; // 返回教练主页
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