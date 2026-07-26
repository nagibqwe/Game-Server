package com.gm.project.monitor.online.service;

import java.util.Date;
import java.util.List;
import com.gm.project.monitor.online.domain.UserOnline;

/**
 * В сети用户 服务层
 * 
 * @author ruoyi
 */
public interface IUserOnlineService
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
    public void deleteOnlineById(String sessionId);

    /**
     * 通过会话№УдалитьИнформация
     * 
     * @param sessions 会话ID集合
     * @return В сети用户Информация
     */
    public void batchDeleteOnline(List<String> sessions);

    /**
     * Сохранить会话Информация
     * 
     * @param online 会话Информация
     */
    public void saveOnline(UserOnline online);

    /**
     * 查询会话集合
     * 
     * @param userOnline 分页参数
     * @return 会话集合
     */
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline);

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    public void forceLogout(String sessionId);

    /**
     * 清理用户缓存
     * 
     * @param loginName Логин
     * @param sessionId 会话ID
     */
    public void removeUserCache(String loginName, String sessionId);

    /**
     * 查询会话集合
     * 
     * @param expiredDate 有效期
     * @return 会话集合
     */
    public List<UserOnline> selectOnlineByExpired(Date expiredDate);
}
