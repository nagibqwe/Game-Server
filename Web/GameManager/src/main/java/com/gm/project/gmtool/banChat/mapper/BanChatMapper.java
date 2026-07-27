package com.gm.project.gmtool.banChat.mapper;

import java.util.List;
import com.gm.project.gmtool.banChat.domain.BanChat;

/**
 * 聊天封禁Mapper接口
 * 
 * @author gm
 * @date 2021-11-20
 */
public interface BanChatMapper 
{
    /**
     * 查询聊天封禁
     * 
     * @param id 聊天封禁ID
     * @return 聊天封禁
     */
    public BanChat selectBanChatById(Long id);

    /**
     * 查询聊天封禁列表
     * 
     * @param banChat 聊天封禁
     * @return 聊天封禁集合
     */
    public List<BanChat> selectBanChatList(BanChat banChat);

    /**
     * 新增聊天封禁
     * 
     * @param banChat 聊天封禁
     * @return 结果
     */
    public int insertBanChat(BanChat banChat);

    /**
     * 修改聊天封禁
     * 
     * @param banChat 聊天封禁
     * @return 结果
     */
    public int updateBanChat(BanChat banChat);

    /**
     * 删除聊天封禁
     * 
     * @param id 聊天封禁ID
     * @return 结果
     */
    public int deleteBanChatById(Long id);

    /**
     * 批量删除聊天封禁
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBanChatByIds(String[] ids);
}
