package com.gm.project.gmtool.rechargeItem.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.rechargeItem.mapper.RechargeItemMapper;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;
import com.gm.project.gmtool.rechargeItem.service.IRechargeItemService;
import com.gm.common.utils.text.Convert;

/**
 * Настройки пополненияService业务层处理
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
     * 查询Настройки пополнения
     * 
     * @param goodsId Настройки пополненияID
     * @return Настройки пополнения
     */
    @Override
    public RechargeItem selectRechargeItemById(Integer goodsId)
    {
        return rechargeItemMapper.selectRechargeItemById(goodsId);
    }

    /**
     * 查询Настройки пополнения列表
     * 
     * @param rechargeItem Настройки пополнения
     * @return Настройки пополнения
     */
    @Override
    public List<RechargeItem> selectRechargeItemList(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.selectRechargeItemList(rechargeItem);
    }

    /**
     * ДобавитьНастройки пополнения
     * 
     * @param rechargeItem Настройки пополнения
     * @return Результат
     */
    @Override
    public int insertRechargeItem(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.insertRechargeItem(rechargeItem);
    }

    /**
     * ИзменитьНастройки пополнения
     * 
     * @param rechargeItem Настройки пополнения
     * @return Результат
     */
    @Override
    public int updateRechargeItem(RechargeItem rechargeItem)
    {
        return rechargeItemMapper.updateRechargeItem(rechargeItem);
    }

    /**
     * УдалитьНастройки пополнения对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteRechargeItemByIds(String ids)
    {
        return rechargeItemMapper.deleteRechargeItemByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьНастройки пополненияИнформация
     * 
     * @param goodsId Настройки пополненияID
     * @return Результат
     */
    @Override
    public int deleteRechargeItemById(Integer goodsId)
    {
        return rechargeItemMapper.deleteRechargeItemById(goodsId);
    }

    /**
     * 普通Пополнение查询
     * @return
     */
    @Override
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel() {
        return rechargeItemMapper.selectRechargeItemBygoodsPayChannel();
    }

    /**
     * №三方Настройки пополнения查询
     * @return
     */
    @Override
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel3() {
        return rechargeItemMapper.selectRechargeItemBygoodsPayChannel3();
    }

    /**
     * Удалить普通Пополнение
     * @return
     */
    @Override
    public int deleteRechargeItemBygoodsPayChannel() {
        return rechargeItemMapper.deleteRechargeItemBygoodsPayChannel();
    }

    /**
     * Удалить№三方Пополнение
     * @return
     */
    @Override
    public int deleteRechargeItemBygoodsPayChannel3() {
        return rechargeItemMapper.deleteRechargeItemBygoodsPayChannel3();
    }

    /**
     * 清空Настройки пополненияДанные
     * @param tableName
     */
    @Override
    public void clearRechargeItem(String tableName) {
        rechargeItemMapper.clearRechargeItem(tableName);
    }
}
