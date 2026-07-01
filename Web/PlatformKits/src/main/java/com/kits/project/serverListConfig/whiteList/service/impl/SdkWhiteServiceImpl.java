package com.kits.project.serverListConfig.whiteList.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.common.utils.DateUtils;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.whiteList.mapper.SdkWhiteMapper;
import com.kits.project.serverListConfig.whiteList.domain.SdkWhite;
import com.kits.project.serverListConfig.whiteList.service.ISdkWhiteService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 白名单Service业务层处理
 * 
 * @author gm
 * @date 2021-04-26
 */
@Service
public class SdkWhiteServiceImpl implements ISdkWhiteService 
{
    private ConcurrentHashMap<Long,SdkWhite> hashMap = new ConcurrentHashMap<Long,SdkWhite>();

    @Autowired
    private SdkWhiteMapper sdkWhiteMapper;

    @Autowired
    private ISdkServerService sdkServerService;

    /**
     * 项目启动时，初始化白名单信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkWhite> sdkWhiteList = sdkWhiteMapper.selectSdkWhiteList(new SdkWhite());
        for (SdkWhite sdkWhite : sdkWhiteList)
        {
            hashMap.put(sdkWhite.getId(), sdkWhite);
        }
    }

    /**
     * 获取白名单信息map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkWhite> getWhiteHashMap() {
        return hashMap;
    }

    /**
     * 查询白名单
     * 
     * @param id 白名单ID
     * @return 白名单
     */
    @Override
    public SdkWhite selectSdkWhiteById(Long id)
    {
        return sdkWhiteMapper.selectSdkWhiteById(id);
    }

    /**
     * 查询白名单列表
     * 
     * @param sdkWhite 白名单
     * @return 白名单
     */
    @Override
    public List<SdkWhite> selectSdkWhiteList(SdkWhite sdkWhite)
    {
        return sdkWhiteMapper.selectSdkWhiteList(sdkWhite);
    }

    /**
     * 新增白名单
     * 
     * @param sdkWhite 白名单
     * @return 结果
     */
    @Override
    public int insertSdkWhite(SdkWhite sdkWhite)
    {
        sdkWhite.setCreateTime(DateUtils.getNowDate());
        int row = sdkWhiteMapper.insertSdkWhite(sdkWhite);
        if (row > 0){
            hashMap.put(sdkWhite.getId(),sdkWhite);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改白名单
     * 
     * @param sdkWhite 白名单
     * @return 结果
     */
    @Override
    public int updateSdkWhite(SdkWhite sdkWhite)
    {
        sdkWhite.setUpdateTime(DateUtils.getNowDate());
        int row = sdkWhiteMapper.updateSdkWhite(sdkWhite);
        if (row > 0){
            hashMap.put(sdkWhite.getId(),sdkWhite);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除白名单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkWhiteByIds(String ids)
    {
        String[] idsArr = Convert.toStrArray(ids);
        int row = sdkWhiteMapper.deleteSdkWhiteByIds(Convert.toStrArray(ids));
        if (row > 0){
            for (String id:idsArr){
                hashMap.remove(Long.valueOf(id));
            }
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除白名单信息
     * 
     * @param id 白名单ID
     * @return 结果
     */
    @Override
    public int deleteSdkWhiteById(Long id)
    {
        int row = sdkWhiteMapper.deleteSdkWhiteById(id);
        if (row > 0){
            hashMap.remove(id);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    @Override
    public void reloadSdkWhite() {
        init();
    }
}
