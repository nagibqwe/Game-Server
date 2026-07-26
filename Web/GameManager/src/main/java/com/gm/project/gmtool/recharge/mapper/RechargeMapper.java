package com.gm.project.gmtool.recharge.mapper;

import java.util.List;
import com.gm.project.gmtool.recharge.domain.Recharge;

/**
 * Тестовое пополнение через GMMapper接口
 * 
 * @author gm
 * @date 2021-11-28
 */
public interface RechargeMapper 
{
    /**
     * 查询Тестовое пополнение через GM
     * 
     * @param id Тестовое пополнение через GMID
     * @return Тестовое пополнение через GM
     */
    public Recharge selectRechargeById(Long id);

    /**
     * 查询Тестовое пополнение через GM列表
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Тестовое пополнение через GM集合
     */
    public List<Recharge> selectRechargeList(Recharge recharge);

    /**
     * ДобавитьТестовое пополнение через GM
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Результат
     */
    public int insertRecharge(Recharge recharge);

    /**
     * ИзменитьТестовое пополнение через GM
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Результат
     */
    public int updateRecharge(Recharge recharge);

    /**
     * УдалитьТестовое пополнение через GM
     * 
     * @param id Тестовое пополнение через GMID
     * @return Результат
     */
    public int deleteRechargeById(Long id);

    /**
     * 批量УдалитьТестовое пополнение через GM
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRechargeByIds(String[] ids);
}
