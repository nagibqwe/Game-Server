package com.gm.project.gmtool.banAccount.mapper;

import java.util.List;
import com.gm.project.gmtool.banAccount.domain.BanAccount;

/**
 * Блокировка аккаунтаMapper接口
 * 
 * @author gm
 * @date 2021-11-21
 */
public interface BanAccountMapper 
{
    /**
     * 查询Блокировка аккаунта
     * 
     * @param id Блокировка аккаунтаID
     * @return Блокировка аккаунта
     */
    public BanAccount selectBanAccountById(Long id);

    /**
     * 查询Блокировка аккаунта列表
     * 
     * @param banAccount Блокировка аккаунта
     * @return Блокировка аккаунта集合
     */
    public List<BanAccount> selectBanAccountList(BanAccount banAccount);

    /**
     * ДобавитьБлокировка аккаунта
     * 
     * @param banAccount Блокировка аккаунта
     * @return Результат
     */
    public int insertBanAccount(BanAccount banAccount);

    /**
     * ИзменитьБлокировка аккаунта
     * 
     * @param banAccount Блокировка аккаунта
     * @return Результат
     */
    public int updateBanAccount(BanAccount banAccount);

    /**
     * УдалитьБлокировка аккаунта
     * 
     * @param id Блокировка аккаунтаID
     * @return Результат
     */
    public int deleteBanAccountById(Long id);

    /**
     * 批量УдалитьБлокировка аккаунта
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteBanAccountByIds(String[] ids);
}
