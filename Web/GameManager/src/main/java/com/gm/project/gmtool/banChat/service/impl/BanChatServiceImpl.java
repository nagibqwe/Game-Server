package com.gm.project.gmtool.banChat.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.banChat.mapper.BanChatMapper;
import com.gm.project.gmtool.banChat.domain.BanChat;
import com.gm.project.gmtool.banChat.service.IBanChatService;
import com.gm.common.utils.text.Convert;

/**
 * 聊День封禁Service业务层处理
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
     * 查询聊День封禁
     * 
     * @param id 聊День封禁ID
     * @return 聊День封禁
     */
    @Override
    public BanChat selectBanChatById(Long id)
    {
        return banChatMapper.selectBanChatById(id);
    }

    /**
     * 查询聊День封禁列表
     * 
     * @param banChat 聊День封禁
     * @return 聊День封禁
     */
    @Override
    public List<BanChat> selectBanChatList(BanChat banChat)
    {
        return banChatMapper.selectBanChatList(banChat);
    }

    /**
     * Добавить聊День封禁
     * 
     * @param banChat 聊День封禁
     * @return Результат
     */
    @Override
    public int insertBanChat(BanChat banChat)
    {
        return banChatMapper.insertBanChat(banChat);
    }

    /**
     * Изменить聊День封禁
     * 
     * @param banChat 聊День封禁
     * @return Результат
     */
    @Override
    public int updateBanChat(BanChat banChat)
    {
        return banChatMapper.updateBanChat(banChat);
    }

    /**
     * Удалить聊День封禁对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteBanChatByIds(String ids)
    {
        return banChatMapper.deleteBanChatByIds(Convert.toStrArray(ids));
    }

    /**
     * Удалить聊День封禁Информация
     * 
     * @param id 聊День封禁ID
     * @return Результат
     */
    @Override
    public int deleteBanChatById(Long id)
    {
        return banChatMapper.deleteBanChatById(id);
    }

}
