package com.kits.project.serverListConfig.x8app.service;

import java.util.List;

import com.kits.project.serverList.domain.PublicServerGrid;
import com.kits.project.serverListConfig.x8app.domain.TX8App;

/**
 * 游戏应用Service接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface ITX8AppService 
{
    /**
     * 查询游戏应用
     * 
     * @param appId 游戏应用ID
     * @return 游戏应用
     */
    public TX8App selectTX8AppById(Long appId);

    /**
     * 查询游戏应用列表
     * 
     * @param tX8App 游戏应用
     * @return 游戏应用集合
     */
    public List<TX8App> selectTX8AppList(TX8App tX8App);

    /**
     * 新增游戏应用
     * 
     * @param tX8App 游戏应用
     * @return 结果
     */
    public int insertTX8App(TX8App tX8App);

    /**
     * 修改游戏应用
     * 
     * @param tX8App 游戏应用
     * @return 结果
     */
    public int updateTX8App(TX8App tX8App);

    /**
     * 批量删除游戏应用
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8AppByIds(String ids);

    /**
     * 删除游戏应用信息
     * 
     * @param appId 游戏应用ID
     * @return 结果
     */
    public int deleteTX8AppById(Long appId);

    /**
     * 查询公共游戏服信息
     * @return
     */
    public List<PublicServerGrid> selectPublicInfo();
}
