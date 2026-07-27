package com.gm.project.gmtool.rechargeItem.mapper;

import java.util.List;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;

/**
 * 充值配置Mapper接口
 * 
 * @author gm
 * @date 2021-08-25
 */
public interface RechargeItemMapper 
{
    /**
     * 查询充值配置
     * 
     * @param goodsId 充值配置ID
     * @return 充值配置
     */
    public RechargeItem selectRechargeItemById(Integer goodsId);

    /**
     * 查询充值配置列表
     * 
     * @param rechargeItem 充值配置
     * @return 充值配置集合
     */
    public List<RechargeItem> selectRechargeItemList(RechargeItem rechargeItem);

    /**
     * 新增充值配置
     * 
     * @param rechargeItem 充值配置
     * @return 结果
     */
    public int insertRechargeItem(RechargeItem rechargeItem);

    /**
     * 修改充值配置
     * 
     * @param rechargeItem 充值配置
     * @return 结果
     */
    public int updateRechargeItem(RechargeItem rechargeItem);

    /**
     * 删除充值配置
     * 
     * @param goodsId 充值配置ID
     * @return 结果
     */
    public int deleteRechargeItemById(Integer goodsId);

    /**
     * 批量删除充值配置
     * 
     * @param goodsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRechargeItemByIds(String[] goodsIds);

    /**
     * 普通充值查询
     * @return
     */
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel();

    /**
     * 第三方充值配置查询
     * @return
     */
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel3();

    /**
     * 删除普通充值
     * @return
     */
    public int deleteRechargeItemBygoodsPayChannel();
    /**
     * 删除第三方充值
     * @return
     */
    public int deleteRechargeItemBygoodsPayChannel3();


    /**
     * 清空充值配置数据
     * @param tableName
     */
    public void clearRechargeItem(String tableName);
}
