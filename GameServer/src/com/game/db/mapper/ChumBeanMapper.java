package com.game.db.mapper;

import com.game.db.bean.ChumBean;
import com.game.db.bean.ChumBeanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChumBeanMapper {
    long countByExample(ChumBeanExample example);

    int deleteByExample(ChumBeanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChumBean record);

    int insertSelective(ChumBean record);

    List<ChumBean> selectByExampleWithBLOBs(ChumBeanExample example);

    List<ChumBean> selectByExample(ChumBeanExample example);

    ChumBean selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChumBean record, @Param("example") ChumBeanExample example);

    int updateByExampleWithBLOBs(@Param("record") ChumBean record, @Param("example") ChumBeanExample example);

    int updateByExample(@Param("record") ChumBean record, @Param("example") ChumBeanExample example);

    int updateByPrimaryKeySelective(ChumBean record);

    int updateByPrimaryKeyWithBLOBs(ChumBean record);

    int updateByPrimaryKey(ChumBean record);
}