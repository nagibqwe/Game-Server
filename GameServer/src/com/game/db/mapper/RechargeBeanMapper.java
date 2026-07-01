package com.game.db.mapper;

import com.game.db.bean.RechargeBean;
import com.game.db.bean.RechargeBeanExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RechargeBeanMapper {
    long countByExample(RechargeBeanExample example);

    int deleteByExample(RechargeBeanExample example);

    int deleteByPrimaryKey(String orderNo);

    int insert(RechargeBean record);

    int insertSelective(RechargeBean record);

    List<RechargeBean> selectByExampleWithBLOBs(RechargeBeanExample example);

    List<RechargeBean> selectByExample(RechargeBeanExample example);

    RechargeBean selectByPrimaryKey(String orderNo);

    int updateByExampleSelective(@Param("record") RechargeBean record, @Param("example") RechargeBeanExample example);

    int updateByExampleWithBLOBs(@Param("record") RechargeBean record, @Param("example") RechargeBeanExample example);

    int updateByExample(@Param("record") RechargeBean record, @Param("example") RechargeBeanExample example);

    int updateByPrimaryKeySelective(RechargeBean record);

    int updateByPrimaryKeyWithBLOBs(RechargeBean record);

    int updateByPrimaryKey(RechargeBean record);

    Long selectRechargeByRoleId(HashMap<String, Object> map);
}