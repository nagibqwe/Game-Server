package com.gm.project.gmtool.rechargeItem.service;

import java.util.List;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;

/**
 * Настройки пополненияService接口
 * 
 * @author gm
 * @date 2021-08-25
 */
public interface IRechargeItemService 
{
    /**
     * 查询Настройки пополнения
     * 
     * @param goodsId Настройки пополненияID
     * @return Настройки пополнения
     */
    public RechargeItem selectRechargeItemById(Integer goodsId);

    /**
     * 查询Настройки пополнения列表
     * 
     * @param rechargeItem Настройки пополнения
     * @return Настройки пополнения集合
     */
    public List<RechargeItem> selectRechargeItemList(RechargeItem rechargeItem);

    /**
     * ДобавитьНастройки пополнения
     * 
     * @param rechargeItem Настройки пополнения
     * @return Результат
     */
    public int insertRechargeItem(RechargeItem rechargeItem);

    /**
     * ИзменитьНастройки пополнения
     * 
     * @param rechargeItem Настройки пополнения
     * @return Результат
     */
    public int updateRechargeItem(RechargeItem rechargeItem);

    /**
     * 批量УдалитьНастройки пополнения
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRechargeItemByIds(String ids);

    /**
     * УдалитьНастройки пополненияИнформация
     * 
     * @param goodsId Настройки пополненияID
     * @return Результат
     */
    public int deleteRechargeItemById(Integer goodsId);

    /**
     * 普通Пополнение查询
     * @return
     */
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel();

    /**
     * №三方Настройки пополнения查询
     * @return
     */
    public List<RechargeItem> selectRechargeItemBygoodsPayChannel3();

    /**
     * Удалить普通Пополнение
     * @return
     */
    public int deleteRechargeItemBygoodsPayChannel();
    /**
     * Удалить№三方Пополнение
     * @return
     */
    public int deleteRechargeItemBygoodsPayChannel3();

    /**
     * 清空Настройки пополненияДанные
     * @param tableName
     */
    public void clearRechargeItem(String tableName);
}
