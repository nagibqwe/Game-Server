package com.game.db.mapper;

import com.game.db.bean.ShopBean;
import com.game.db.bean.ShopBeanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShopBeanMapper {
    long countByExample(ShopBeanExample example);

    int deleteByExample(ShopBeanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShopBean record);

    int insertSelective(ShopBean record);

    List<ShopBean> selectByExample(ShopBeanExample example);

    ShopBean selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShopBean record, @Param("example") ShopBeanExample example);

    int updateByExample(@Param("record") ShopBean record, @Param("example") ShopBeanExample example);

    int updateByPrimaryKeySelective(ShopBean record);

    int updateByPrimaryKey(ShopBean record);
}