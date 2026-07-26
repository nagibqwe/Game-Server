package com.gm.project.gmtool.banChat.mapper;

import java.util.List;
import com.gm.project.gmtool.banChat.domain.BanChat;

/**
 * 聊День封禁Mapper接口
 * 
 * @author gm
 * @date 2021-11-20
 */
public interface BanChatMapper 
{
    /**
     * 查询聊День封禁
     * 
     * @param id 聊День封禁ID
     * @return 聊День封禁
     */
    public BanChat selectBanChatById(Long id);

    /**
     * 查询聊День封禁列表
     * 
     * @param banChat 聊День封禁
     * @return 聊День封禁集合
     */
    public List<BanChat> selectBanChatList(BanChat banChat);

    /**
     * Добавить聊День封禁
     * 
     * @param banChat 聊День封禁
     * @return Результат
     */
    public int insertBanChat(BanChat banChat);

    /**
     * Изменить聊День封禁
     * 
     * @param banChat 聊День封禁
     * @return Результат
     */
    public int updateBanChat(BanChat banChat);

    /**
     * Удалить聊День封禁
     * 
     * @param id 聊День封禁ID
     * @return Результат
     */
    public int deleteBanChatById(Long id);

    /**
     * 批量Удалить聊День封禁
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteBanChatByIds(String[] ids);
}
