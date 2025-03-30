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
    public ClassTable coachLogin(String coachAccount, String coachPassword) {
        return coachMapper.selectByAccountAndPassword(coachAccount, coachPassword);
    }

    @Override
    public ClassTable selectByAccount(Integer coachAccount) {
        return coachMapper.selectByAccount(coachAccount);
    }

    @Override
    public Boolean updateCoachInfo(ClassTable classTable) {
        return coachMapper.updateCoachInfo(classTable);
    }
}