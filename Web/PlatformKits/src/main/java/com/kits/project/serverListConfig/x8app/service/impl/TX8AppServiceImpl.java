package com.kits.project.serverListConfig.x8app.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import com.kits.project.serverList.domain.PublicServerGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8app.mapper.TX8AppMapper;
import com.kits.project.serverListConfig.x8app.domain.TX8App;
import com.kits.project.serverListConfig.x8app.service.ITX8AppService;
import com.kits.common.utils.text.Convert;

/**
 * 游戏应用Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8AppServiceImpl implements ITX8AppService 
{
    @Autowired
    private TX8AppMapper tX8AppMapper;

    /**
     * 查询游戏应用
     * 
     * @param appId 游戏应用ID
     * @return 游戏应用
     */
    @Override
    public TX8App selectTX8AppById(Long appId)
    {
        return tX8AppMapper.selectTX8AppById(appId);
    }

    /**
     * 查询游戏应用列表
     * 
     * @param tX8App 游戏应用
     * @return 游戏应用
     */
    @Override
    public List<TX8App> selectTX8AppList(TX8App tX8App)
    {
        return tX8AppMapper.selectTX8AppList(tX8App);
    }

    /**
     * 新增游戏应用
     * 
     * @param tX8App 游戏应用
     * @return 结果
     */
    @Override
    public int insertTX8App(TX8App tX8App)
    {
        tX8App.setCreateTime(DateUtils.getNowDate());
        return tX8AppMapper.insertTX8App(tX8App);
    }

    /**
     * 修改游戏应用
     * 
     * @param tX8App 游戏应用
     * @return 结果
     */
    @Override
    public int updateTX8App(TX8App tX8App)
    {
        tX8App.setUpdateTime(DateUtils.getNowDate());
        return tX8AppMapper.updateTX8App(tX8App);
    }

    /**
     * 删除游戏应用对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8AppByIds(String ids)
    {
        return tX8AppMapper.deleteTX8AppByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除游戏应用信息
     * 
     * @param appId 游戏应用ID
     * @return 结果
     */
    @Override
    public int deleteTX8AppById(Long appId)
    {
        return tX8AppMapper.deleteTX8AppById(appId);
    }

    /**
     * 查询公共游戏服信息
     * @return
     */
    @Override
    public List<PublicServerGrid> selectPublicInfo() {
        return tX8AppMapper.selectPublicInfo();
    }
}
