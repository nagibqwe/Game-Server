package com.gm.project.monitor.online.mapper;

import java.util.List;
import com.gm.project.monitor.online.domain.UserOnline;

/**
 * В сети用户 Данные层
 * 
 * @author ruoyi
 */
public interface UserOnlineMapper
{
    /**
     * 通过会话№查询Информация
     * 
     * @param sessionId 会话ID
     * @return В сети用户Информация
     */
    public UserOnline selectOnlineById(String sessionId);

    /**
     * 通过会话№УдалитьИнформация
     * 
     * @param sessionId 会话ID
     * @return В сети用户Информация
     */
    public int deleteOnlineById(String sessionId);

    /**
     * Сохранить会话Информация
     * 
     * @param online 会话Информация
     * @return Результат
     */
    public int saveOnline(UserOnline online);

    /**
     * 查询会话集合
     * 
     * @param userOnline 会话参数
     * @return 会话集合
     */
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline);

    /**
     * 查询过期会话集合
     * 
     * @param lastAccessTime 过期Время
     * @return 会话集合
     */
    public List<UserOnline> selectOnlineByExpired(String lastAccessTime);
}
