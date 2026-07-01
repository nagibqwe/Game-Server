package com.gm.project.gmtool.banAccount.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.banAccount.mapper.BanAccountMapper;
import com.gm.project.gmtool.banAccount.domain.BanAccount;
import com.gm.project.gmtool.banAccount.service.IBanAccountService;
import com.gm.common.utils.text.Convert;

/**
 * 账号封禁Service业务层处理
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
     * 查询账号封禁
     * 
     * @param id 账号封禁ID
     * @return 账号封禁
     */
    @Override
    public BanAccount selectBanAccountById(Long id)
    {
        return banAccountMapper.selectBanAccountById(id);
    }

    /**
     * 查询账号封禁列表
     * 
     * @param banAccount 账号封禁
     * @return 账号封禁
     */
    @Override
    public List<BanAccount> selectBanAccountList(BanAccount banAccount)
    {
        return banAccountMapper.selectBanAccountList(banAccount);
    }

    /**
     * 新增账号封禁
     * 
     * @param banAccount 账号封禁
     * @return 结果
     */
    @Override
    public int insertBanAccount(BanAccount banAccount)
    {
        return banAccountMapper.insertBanAccount(banAccount);
    }

    /**
     * 修改账号封禁
     * 
     * @param banAccount 账号封禁
     * @return 结果
     */
    @Override
    public int updateBanAccount(BanAccount banAccount)
    {
        return banAccountMapper.updateBanAccount(banAccount);
    }

    /**
     * 删除账号封禁对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBanAccountByIds(String ids)
    {
        return banAccountMapper.deleteBanAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除账号封禁信息
     * 
     * @param id 账号封禁ID
     * @return 结果
     */
    @Override
    public int deleteBanAccountById(Long id)
    {
        return banAccountMapper.deleteBanAccountById(id);
    }
}
