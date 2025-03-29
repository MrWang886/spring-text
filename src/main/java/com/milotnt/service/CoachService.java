package com.milotnt.service;

import com.milotnt.pojo.ClassTable;

public interface CoachService {
    public ClassTable coachLogin(ClassTable classTable);
    // 查询教练信息
    public ClassTable selectByAccount(Integer coachAccount);
    // 更新教练信息
    public Boolean updateCoachInfo(ClassTable classTable);
}