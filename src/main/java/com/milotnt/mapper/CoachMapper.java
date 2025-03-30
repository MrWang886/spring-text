package com.milotnt.mapper;

import com.milotnt.pojo.ClassTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachMapper {
    ClassTable selectByAccountAndPassword(String coachAccount, String coachPassword);
    // 查询教练信息
    ClassTable selectByAccount(Integer coachAccount);
    // 更新教练信息
    Boolean updateCoachInfo(ClassTable classTable);
}