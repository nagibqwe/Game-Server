package com.gm.project.gmtool.gameInfo.mapper;

import java.util.List;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;

/**
 * Параметры игрыMapper接口
 * 
 * @author gm
 * @date 2021-11-15
 */
public interface GameInfoMapper 
{
    /**
     * 查询Параметры игры
     * 
     * @param gameId Параметры игрыID
     * @return Параметры игры
     */
    public GameInfo selectGameInfoById(Integer gameId);

    /**
     * 查询Параметры игры列表
     * 
     * @param gameInfo Параметры игры
     * @return Параметры игры集合
     */
    public List<GameInfo> selectGameInfoList(GameInfo gameInfo);

    /**
     * ДобавитьПараметры игры
     * 
     * @param gameInfo Параметры игры
     * @return Результат
     */
    public int insertGameInfo(GameInfo gameInfo);

    /**
     * ИзменитьПараметры игры
     * 
     * @param gameInfo Параметры игры
     * @return Результат
     */
    public int updateGameInfo(GameInfo gameInfo);

    /**
     * УдалитьПараметры игры
     * 
     * @param gameId Параметры игрыID
     * @return Результат
     */
    public int deleteGameInfoById(Integer gameId);

    /**
     * 批量УдалитьПараметры игры
     * 
     * @param gameIds 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteGameInfoByIds(String[] gameIds);
}
