package com.gm.project.gmtool.gameInfo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.gameInfo.mapper.GameInfoMapper;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;
import com.gm.project.gmtool.gameInfo.service.IGameInfoService;
import com.gm.common.utils.text.Convert;

/**
 * Параметры игрыService业务层处理
 * 
 * @author gm
 * @date 2021-11-15
 */
@Service
public class GameInfoServiceImpl implements IGameInfoService 
{
    @Autowired
    private GameInfoMapper gameInfoMapper;

    /**
     * 查询Параметры игры
     * 
     * @param gameId Параметры игрыID
     * @return Параметры игры
     */
    @Override
    public GameInfo selectGameInfoById(Integer gameId)
    {
        return gameInfoMapper.selectGameInfoById(gameId);
    }

    /**
     * 查询Параметры игры列表
     * 
     * @param gameInfo Параметры игры
     * @return Параметры игры
     */
    @Override
    public List<GameInfo> selectGameInfoList(GameInfo gameInfo)
    {
        return gameInfoMapper.selectGameInfoList(gameInfo);
    }

    /**
     * ДобавитьПараметры игры
     * 
     * @param gameInfo Параметры игры
     * @return Результат
     */
    @Override
    public int insertGameInfo(GameInfo gameInfo)
    {
        return gameInfoMapper.insertGameInfo(gameInfo);
    }

    /**
     * ИзменитьПараметры игры
     * 
     * @param gameInfo Параметры игры
     * @return Результат
     */
    @Override
    public int updateGameInfo(GameInfo gameInfo)
    {
        return gameInfoMapper.updateGameInfo(gameInfo);
    }

    /**
     * УдалитьПараметры игры对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteGameInfoByIds(String ids)
    {
        return gameInfoMapper.deleteGameInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьПараметры игрыИнформация
     * 
     * @param gameId Параметры игрыID
     * @return Результат
     */
    @Override
    public int deleteGameInfoById(Integer gameId)
    {
        return gameInfoMapper.deleteGameInfoById(gameId);
    }
}
