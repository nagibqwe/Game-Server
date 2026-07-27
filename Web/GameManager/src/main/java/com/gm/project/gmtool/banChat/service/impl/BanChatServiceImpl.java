package com.gm.project.gmtool.banChat.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.banChat.mapper.BanChatMapper;
import com.gm.project.gmtool.banChat.domain.BanChat;
import com.gm.project.gmtool.banChat.service.IBanChatService;
import com.gm.common.utils.text.Convert;

/**
 * 聊天封禁Service业务层处理
 * 
 * @author gm
 * @date 2021-11-20
 */
@Service
public class BanChatServiceImpl implements IBanChatService 
{
    @Autowired
    private BanChatMapper banChatMapper;

    /**
     * 查询聊天封禁
     * 
     * @param id 聊天封禁ID
     * @return 聊天封禁
     */
    @Override
    public BanChat selectBanChatById(Long id)
    {
        return banChatMapper.selectBanChatById(id);
    }

    /**
     * 查询聊天封禁列表
     * 
     * @param banChat 聊天封禁
     * @return 聊天封禁
     */
    @Override
    public List<BanChat> selectBanChatList(BanChat banChat)
    {
        return banChatMapper.selectBanChatList(banChat);
    }

    /**
     * 新增聊天封禁
     * 
     * @param banChat 聊天封禁
     * @return 结果
     */
    @Override
    public int insertBanChat(BanChat banChat)
    {
        return banChatMapper.insertBanChat(banChat);
    }

    /**
     * 修改聊天封禁
     * 
     * @param banChat 聊天封禁
     * @return 结果
     */
    @Override
    public int updateBanChat(BanChat banChat)
    {
        return banChatMapper.updateBanChat(banChat);
    }

    /**
     * 删除聊天封禁对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBanChatByIds(String ids)
    {
        return banChatMapper.deleteBanChatByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除聊天封禁信息
     * 
     * @param id 聊天封禁ID
     * @return 结果
     */
    @Override
    public int deleteBanChatById(Long id)
    {
        return banChatMapper.deleteBanChatById(id);
    }

}
