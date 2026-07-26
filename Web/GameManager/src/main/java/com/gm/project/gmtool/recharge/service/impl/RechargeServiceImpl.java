package com.gm.project.gmtool.recharge.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.recharge.mapper.RechargeMapper;
import com.gm.project.gmtool.recharge.domain.Recharge;
import com.gm.project.gmtool.recharge.service.IRechargeService;
import com.gm.common.utils.text.Convert;

/**
 * Тестовое пополнение через GMService业务层处理
 * 
 * @author gm
 * @date 2021-11-28
 */
@Service
public class RechargeServiceImpl implements IRechargeService 
{
    @Autowired
    private RechargeMapper rechargeMapper;

    /**
     * 查询Тестовое пополнение через GM
     * 
     * @param id Тестовое пополнение через GMID
     * @return Тестовое пополнение через GM
     */
    @Override
    public Recharge selectRechargeById(Long id)
    {
        return rechargeMapper.selectRechargeById(id);
    }

    /**
     * 查询Тестовое пополнение через GM列表
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Тестовое пополнение через GM
     */
    @Override
    public List<Recharge> selectRechargeList(Recharge recharge)
    {
        return rechargeMapper.selectRechargeList(recharge);
    }

    /**
     * ДобавитьТестовое пополнение через GM
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Результат
     */
    @Override
    public int insertRecharge(Recharge recharge)
    {
        return rechargeMapper.insertRecharge(recharge);
    }

    /**
     * ИзменитьТестовое пополнение через GM
     * 
     * @param recharge Тестовое пополнение через GM
     * @return Результат
     */
    @Override
    public int updateRecharge(Recharge recharge)
    {
        return rechargeMapper.updateRecharge(recharge);
    }

    /**
     * УдалитьТестовое пополнение через GM对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteRechargeByIds(String ids)
    {
        return rechargeMapper.deleteRechargeByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьТестовое пополнение через GMИнформация
     * 
     * @param id Тестовое пополнение через GMID
     * @return Результат
     */
    @Override
    public int deleteRechargeById(Long id)
    {
        return rechargeMapper.deleteRechargeById(id);
    }
}
