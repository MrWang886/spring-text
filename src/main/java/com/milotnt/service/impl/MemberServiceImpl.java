package com.milotnt.service.impl;

import com.milotnt.mapper.MemberMapper;
import com.milotnt.pojo.Member;
import com.milotnt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/11
 */

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<Member> findAll() {
        return memberMapper.findAll();
    }

    @Override
    public Boolean insertMember(Member member) {
        return memberMapper.insertMember(member);
    }

    @Override
    public Boolean updateMemberByMemberAccount(Member member) {
        return memberMapper.updateMemberByMemberAccount(member);
    }

    @Override
    public Member userLogin(Member member) {
        return memberMapper.selectByAccountAndPassword(member);
    }

    @Override
    public Boolean deleteByMemberAccount(Integer memberAccount) {
        return memberMapper.deleteByMemberAccount(memberAccount);
    }

    @Override
    public Integer selectTotalCount() {
        return memberMapper.selectTotalCount();
    }

    @Override
    public List<Member> selectByMemberAccount(Integer memberAccount) {
        return memberMapper.selectByMemberAccount(memberAccount);
    }

    @Override
    public boolean registerMember(Member member) {
        // 设置办卡时间，默认为当前时间
        member.setCardTime(String.valueOf(new Date()));

        // 其他默认值设置...

        memberMapper.insertMember(member);
        return true;
    }

}
