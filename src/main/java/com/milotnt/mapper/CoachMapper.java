package com.milotnt.mapper;


import com.milotnt.pojo.ClassTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachMapper {
    ClassTable selectByAccountAndPassword(Integer coachAccount, String coachPassword);

}