package com.milotnt.service.impl;

import com.milotnt.mapper.MemberMapper;
import com.milotnt.pojo.Member;
import com.milotnt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public Boolean checkin(Integer memberAccount) {
        // 获取最后一次打卡时间
        Date lastCheckinTime = memberMapper.selectLastCheckinTimeByMemberAccount(memberAccount);
        
        // 如果没有记录或间隔超过24小时则允许打卡
        if (lastCheckinTime == null || 
            new Date().getTime() - lastCheckinTime.getTime() >= 24 * 60 * 60 * 1000) {
            // 使用当前日期插入（注意：需要确保checkin_date存储的是日期而非具体时间）
            return memberMapper.insertCheckin(memberAccount, new Date());
        }
        return false;
    }

    @Override
    public Integer getCheckinCount(Integer memberAccount) {
        return memberMapper.selectCheckinCountByMemberAccount(memberAccount);
    }

    @Override
    public List<Map<String, Object>> getCheckinRanking() {
        return memberMapper.selectCheckinRanking();
    }

    // 在MemberServiceImpl实现连续打卡天数逻辑（示例，需根据实际数据库设计调整）
    @Override
    public Integer getContinuousCheckinDays(Integer memberAccount) {
        // 假设需要查询连续天数，这里需要根据业务逻辑实现具体查询
        // 示例返回总打卡次数作为占位符
        return memberMapper.selectCheckinCountByMemberAccount(memberAccount);
    }
}
