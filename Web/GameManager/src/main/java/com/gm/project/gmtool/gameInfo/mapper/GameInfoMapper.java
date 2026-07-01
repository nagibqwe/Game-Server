package com.gm.project.gmtool.gameInfo.mapper;

import java.util.List;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;

/**
 * 游戏参数信息Mapper接口
 * 
 * @author gm
 * @date 2021-11-15
 */
public interface GameInfoMapper 
{
    /**
     * 查询游戏参数信息
     * 
     * @param gameId 游戏参数信息ID
     * @return 游戏参数信息
     */
    public GameInfo selectGameInfoById(Integer gameId);

    /**
     * 查询游戏参数信息列表
     * 
     * @param gameInfo 游戏参数信息
     * @return 游戏参数信息集合
     */
    public List<GameInfo> selectGameInfoList(GameInfo gameInfo);

    /**
     * 新增游戏参数信息
     * 
     * @param gameInfo 游戏参数信息
     * @return 结果
     */
    public int insertGameInfo(GameInfo gameInfo);

    /**
     * 修改游戏参数信息
     * 
     * @param gameInfo 游戏参数信息
     * @return 结果
     */
    public int updateGameInfo(GameInfo gameInfo);

    /**
     * 删除游戏参数信息
     * 
     * @param gameId 游戏参数信息ID
     * @return 结果
     */
    public int deleteGameInfoById(Integer gameId);

    /**
     * 批量删除游戏参数信息
     * 
     * @param gameIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteGameInfoByIds(String[] gameIds);
}
