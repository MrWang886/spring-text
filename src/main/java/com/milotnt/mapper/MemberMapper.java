package com.milotnt.mapper;

import com.milotnt.pojo.Member;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/10
 */

@Mapper
public interface MemberMapper {

    //查询会员信息
    List<Member> findAll();

    //新增会员信息
    Boolean insertMember(Member member);

    //根据会员账号修改会员信息
    Boolean updateMemberByMemberAccount(Member member);

    //查询会员账号密码
    Member selectByAccountAndPassword(Member member);

    //根据会员账号删除会员信息
    Boolean deleteByMemberAccount(Integer memberAccount);

    //查询会员数
    Integer selectTotalCount();

    //根据会员账号查询会员
    List<Member> selectByMemberAccount(Integer memberAccount);

    // 插入打卡记录
    Boolean insertCheckin(@Param("memberAccount") Integer memberAccount, @Param("checkinDate") Date checkinDate);

    // 查询会员的打卡天数
    Integer selectCheckinCountByMemberAccount(@Param("memberAccount") Integer memberAccount);

    // 查询所有会员的打卡天数（用于排行榜）
    List<Map<String, Object>> selectCheckinRanking();

    @Insert("INSERT INTO member(member_account, member_password, member_name, member_gender, member_age, " +
            "member_height, member_weight, member_phone, card_time, card_class, card_next_class) " +
            "VALUES(#{memberAccount}, #{memberPassword}, #{memberName}, #{memberGender}, #{memberAge}, " +
            "#{memberHeight}, #{memberWeight}, #{memberPhone}, #{cardTime}, #{cardClass}, #{cardNextClass})")
    @Options(useGeneratedKeys = true, keyProperty = "memberAccount")
    void insMember(Member member);

    // 修改MemberMapper接口，新增获取最后一次打卡时间的方法
    public Date selectLastCheckinTimeByMemberAccount(@Param("memberAccount") Integer memberAccount);

}
