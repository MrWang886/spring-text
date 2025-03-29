package com.milotnt.service.impl;

import com.milotnt.mapper.CoachMapper;
import com.milotnt.pojo.ClassTable;
import com.milotnt.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public ClassTable coachLogin(ClassTable classTable) {
        return coachMapper.selectByAccountAndPassword(Integer.valueOf(classTable.getCoachAccount()), classTable.getCoachPassword());
    }
}