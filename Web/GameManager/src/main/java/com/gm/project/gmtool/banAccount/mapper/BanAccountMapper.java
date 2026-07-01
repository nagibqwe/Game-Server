package com.gm.project.gmtool.banAccount.mapper;

import java.util.List;
import com.gm.project.gmtool.banAccount.domain.BanAccount;

/**
 * 账号封禁Mapper接口
 * 
 * @author gm
 * @date 2021-11-21
 */
public interface BanAccountMapper 
{
    /**
     * 查询账号封禁
     * 
     * @param id 账号封禁ID
     * @return 账号封禁
     */
    public BanAccount selectBanAccountById(Long id);

    /**
     * 查询账号封禁列表
     * 
     * @param banAccount 账号封禁
     * @return 账号封禁集合
     */
    public List<BanAccount> selectBanAccountList(BanAccount banAccount);

    /**
     * 新增账号封禁
     * 
     * @param banAccount 账号封禁
     * @return 结果
     */
    public int insertBanAccount(BanAccount banAccount);

    /**
     * 修改账号封禁
     * 
     * @param banAccount 账号封禁
     * @return 结果
     */
    public int updateBanAccount(BanAccount banAccount);

    /**
     * 删除账号封禁
     * 
     * @param id 账号封禁ID
     * @return 结果
     */
    public int deleteBanAccountById(Long id);

    /**
     * 批量删除账号封禁
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBanAccountByIds(String[] ids);
}
