package com.gm.project.gmtool.gameInfo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.gameInfo.mapper.GameInfoMapper;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;
import com.gm.project.gmtool.gameInfo.service.IGameInfoService;
import com.gm.common.utils.text.Convert;

/**
 * 游戏参数信息Service业务层处理
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
     * 查询游戏参数信息
     * 
     * @param gameId 游戏参数信息ID
     * @return 游戏参数信息
     */
    @Override
    public GameInfo selectGameInfoById(Integer gameId)
    {
        return gameInfoMapper.selectGameInfoById(gameId);
    }

    /**
     * 查询游戏参数信息列表
     * 
     * @param gameInfo 游戏参数信息
     * @return 游戏参数信息
     */
    @Override
    public List<GameInfo> selectGameInfoList(GameInfo gameInfo)
    {
        return gameInfoMapper.selectGameInfoList(gameInfo);
    }

    /**
     * 新增游戏参数信息
     * 
     * @param gameInfo 游戏参数信息
     * @return 结果
     */
    @Override
    public int insertGameInfo(GameInfo gameInfo)
    {
        return gameInfoMapper.insertGameInfo(gameInfo);
    }

    /**
     * 修改游戏参数信息
     * 
     * @param gameInfo 游戏参数信息
     * @return 结果
     */
    @Override
    public int updateGameInfo(GameInfo gameInfo)
    {
        return gameInfoMapper.updateGameInfo(gameInfo);
    }

    /**
     * 删除游戏参数信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGameInfoByIds(String ids)
    {
        return gameInfoMapper.deleteGameInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除游戏参数信息信息
     * 
     * @param gameId 游戏参数信息ID
     * @return 结果
     */
    @Override
    public int deleteGameInfoById(Integer gameId)
    {
        return gameInfoMapper.deleteGameInfoById(gameId);
    }
}
