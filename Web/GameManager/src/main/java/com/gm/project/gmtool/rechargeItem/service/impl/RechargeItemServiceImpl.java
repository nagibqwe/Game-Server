package com.gm.project.gmtool.rechargeItem.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.rechargeItem.mapper.RechargeItemMapper;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;
import com.gm.project.gmtool.rechargeItem.service.IRechargeItemService;
import com.gm.common.utils.text.Convert;

/**
 * 充值配置Service业务层处理
 * 
 * @author gm
 * @date 2021-08-25
 */
@Service
public class RechargeItemServiceImpl implements IRechargeItemService 
{
    @Autowired
    private RechargeItemMapper rechargeItemMapper;

    /**
     * 查询充值配置
     * 
     * @param goodsId 充值配置ID
     * @return 充值配置
     */
    @Override
    public RechargeItem selectRechargeItemById(Integer goodsId)
    {
        return rechargeItemMapper.selectRechargeItemById(goodsId);
    }

    /**
     * 查询充值配置列表
     * 
     * @param rechargeItem 充值配置
     * @return 充值配置
     */
    @Override
    public List<RechargeItem> selectRechargeItemList(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.selectRechargeItemList(rechargeItem);
    }

    /**
     * 新增充值配置
     * 
     * @param rechargeItem 充值配置
     * @return 结果
     */
    @Override
    public int insertRechargeItem(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.insertRechargeItem(rechargeItem);
    }

    /**
     * 修改充值配置
     * 
     * @param rechargeItem 充值配置
     * @return 结果
     */
    @Override
    public int updateRechargeItem(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.updateRechargeItem(rechargeItem);
    }

    /**
     * 删除充值配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRechargeItemByIds(String ids)
    {
        return rechargeItemMapper.deleteRechargeItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除充值配置信息
     * 
     * @param goodsId 充值配置ID
     * @return 结果
     */
    @Override
    public int deleteRechargeItemById(Integer goodsId)
    {
        return rechargeItemMapper.deleteRechargeItemById(goodsId);
    }

    /**
     * 普通充值查询
     * @return
     */
    @Override
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel() {
        return rechargeItemMapper.selectRechargeItemBygoodsPayChannel();
    }

    /**
     * 第三方充值配置查询
     * @return
     */
    @Override
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel3() {
        return rechargeItemMapper.selectRechargeItemBygoodsPayChannel3();
    }

    /**
     * 删除普通充值
     * @return
     */
    @Override
    public int deleteRechargeItemBygoodsPayChannel() {
        return rechargeItemMapper.deleteRechargeItemBygoodsPayChannel();
    }

    /**
     * 删除第三方充值
     * @return
     */
    @Override
    public int deleteRechargeItemBygoodsPayChannel3() {
        return rechargeItemMapper.deleteRechargeItemBygoodsPayChannel3();
    }

    /**
     * 清空充值配置数据
     * @param tableName
     */
    @Override
    public void clearRechargeItem(String tableName) {
        rechargeItemMapper.clearRechargeItem(tableName);
    }
}
