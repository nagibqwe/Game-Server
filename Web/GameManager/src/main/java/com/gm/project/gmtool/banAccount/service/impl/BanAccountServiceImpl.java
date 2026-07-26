package com.gm.project.gmtool.banAccount.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.banAccount.mapper.BanAccountMapper;
import com.gm.project.gmtool.banAccount.domain.BanAccount;
import com.gm.project.gmtool.banAccount.service.IBanAccountService;
import com.gm.common.utils.text.Convert;

/**
 * Блокировка аккаунтаService业务层处理
 * 
 * @author gm
 * @date 2021-11-21
 */
@Service
public class BanAccountServiceImpl implements IBanAccountService 
{
    @Autowired
    private BanAccountMapper banAccountMapper;

    /**
     * 查询Блокировка аккаунта
     * 
     * @param id Блокировка аккаунтаID
     * @return Блокировка аккаунта
     */
    @Override
    public BanAccount selectBanAccountById(Long id)
    {
        return banAccountMapper.selectBanAccountById(id);
    }

    /**
     * 查询Блокировка аккаунта列表
     * 
     * @param banAccount Блокировка аккаунта
     * @return Блокировка аккаунта
     */
    @Override
    public List<BanAccount> selectBanAccountList(BanAccount banAccount)
    {
        return banAccountMapper.selectBanAccountList(banAccount);
    }

    /**
     * ДобавитьБлокировка аккаунта
     * 
     * @param banAccount Блокировка аккаунта
     * @return Результат
     */
    @Override
    public int insertBanAccount(BanAccount banAccount)
    {
        return banAccountMapper.insertBanAccount(banAccount);
    }

    /**
     * ИзменитьБлокировка аккаунта
     * 
     * @param banAccount Блокировка аккаунта
     * @return Результат
     */
    @Override
    public int updateBanAccount(BanAccount banAccount)
    {
        return banAccountMapper.updateBanAccount(banAccount);
    }

    /**
     * УдалитьБлокировка аккаунта对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteBanAccountByIds(String ids)
    {
        return banAccountMapper.deleteBanAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьБлокировка аккаунтаИнформация
     * 
     * @param id Блокировка аккаунтаID
     * @return Результат
     */
    @Override
    public int deleteBanAccountById(Long id)
    {
        return banAccountMapper.deleteBanAccountById(id);
    }
}
