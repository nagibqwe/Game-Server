package com.kits.project.serverListConfig.serverExtra.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.common.utils.DateUtils;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.serverExtra.mapper.SdkServerExtraMapper;
import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 服务器额外信息Service业务层处理
 *
 * @author gm
 * @date 2021-05-07
 */
@Service
public class SdkServerExtraServiceImpl implements ISdkServerExtraService
{
    private ConcurrentHashMap<Long,SdkServerExtra> hashMap = new ConcurrentHashMap<Long,SdkServerExtra>();

    @Autowired
    private SdkServerExtraMapper sdkServerExtraMapper;

    @Autowired
    private ISdkServerService sdkServerService;

    /**
     * 项目启动时，初始化服务器额外信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkServerExtra> sdkServerExtraList = sdkServerExtraMapper.selectSdkServerExtraList(new SdkServerExtra());
        for (SdkServerExtra sdkServerExtra : sdkServerExtraList)
        {
            hashMap.put(sdkServerExtra.getId(), sdkServerExtra);
        }
    }

    /**
     * 获取服务器额外信息map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkServerExtra> getServerExtraHashMap() {
        return hashMap;
    }

    /**
     * 查询服务器额外信息
     *
     * @param serverListId 服务器额外信息ID
     * @return 服务器额外信息
     */
    @Override
    public List<SdkServerExtra> selectSdkServerExtraById(Long serverListId)
    {
        return sdkServerExtraMapper.selectSdkServerExtraById(serverListId);
    }

    /**
     * 查询服务器额外信息列表
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 服务器额外信息
     */
    @Override
    public List<SdkServerExtra> selectSdkServerExtraList(SdkServerExtra sdkServerExtra)
    {
        return sdkServerExtraMapper.selectSdkServerExtraList(sdkServerExtra);
    }

    /**
     * 新增服务器额外信息
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 结果
     */
    @Override
    public int insertSdkServerExtra(SdkServerExtra sdkServerExtra)
    {
        if (sdkServerExtra.getCreateTime() == null){
            sdkServerExtra.setCreateTime(DateUtils.getNowDate());
        }
        int row = sdkServerExtraMapper.insertSdkServerExtra(sdkServerExtra);
        if (row > 0){
            hashMap.put(sdkServerExtra.getId(),sdkServerExtra);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改服务器额外信息
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 结果
     */
    @Override
    public int updateSdkServerExtra(SdkServerExtra sdkServerExtra)
    {
        sdkServerExtra.setUpdateTime(DateUtils.getNowDate());
        int row = sdkServerExtraMapper.updateSdkServerExtra(sdkServerExtra);
        if (row > 0){
            hashMap.put(sdkServerExtra.getId(),sdkServerExtra);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器额外信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerExtraByIds(String ids)
    {
        String[] serverListIdsArr = Convert.toStrArray(ids);//服务器列表的id(serverListId)
        for (String id:serverListIdsArr){
            List<SdkServerExtra> sdkServerExtraList = selectSdkServerExtraByServerListId(Long.valueOf(id));
            if (null != sdkServerExtraList && sdkServerExtraList.size() > 0){
                for (SdkServerExtra sdkServerExtra:sdkServerExtraList){
                    hashMap.remove(sdkServerExtra.getId());
                }
            }
        }

        int row = sdkServerExtraMapper.deleteSdkServerExtraByIds(Convert.toStrArray(ids));
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器额外信息信息
     *
     * @param serverListId 服务器额外信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerExtraById(Long serverListId)
    {
        List<SdkServerExtra> sdkServerExtraList = selectSdkServerExtraByServerListId(serverListId);
        if (null != sdkServerExtraList && sdkServerExtraList.size() > 0){
            for (SdkServerExtra sdkServerExtra:sdkServerExtraList){
                hashMap.remove(sdkServerExtra.getId());
            }
        }
        int row = sdkServerExtraMapper.deleteSdkServerExtraById(serverListId);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据服务器ID列表查询服务器额外信息
     * @param
     * @return
     */
    @Override
    public List<SdkServerExtra> selectServerExtraByServerIds(Map paramMap) {
        return sdkServerExtraMapper.selectServerExtraByServerIds(paramMap);
    }

    /**
     * 保存修改后的服务器额外信息
     * @param sdkServerExtra
     * @return
     */
    @Override
    public int saveServerExtra(SdkServerExtra sdkServerExtra) {
        int row = sdkServerExtraMapper.saveServerExtra(sdkServerExtra);
        if (row > 0){
            hashMap.put(sdkServerExtra.getId(),sdkServerExtra);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据服务器列表ID获取该列表下的服务器ID
     * @param serverListId
     * @return
     */
    @Override
    public List<Long> selectServerIdsByServerListId(Long serverListId) {
        return sdkServerExtraMapper.selectServerIdsByServerListId(serverListId);
    }

    /**
     * 根据服务器列表Id和服务器Id删除对应的服务器额外信息
     * @param paramMap
     * @return
     */
    @Override
    public int deleteServerExtraByServerListIdAndServerId(Map paramMap) {
        List<SdkServerExtra> sdkServerExtraList = sdkServerExtraMapper.selectSdkServerExtraByServerListIdAndServerId(paramMap);
        if (null != sdkServerExtraList && sdkServerExtraList.size() > 0){
            for (SdkServerExtra sdkServerExtra:sdkServerExtraList){
                hashMap.remove(sdkServerExtra.getId());
            }
        }
        int row = sdkServerExtraMapper.deleteServerExtraByServerListIdAndServerId(paramMap);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据服务器Id删除对应的服务器额外信息
     * @param paramMap
     * @return
     */
    @Override
    public int deleteServerExtraByServerId(Map paramMap) {
        List<SdkServerExtra> sdkServerExtraList = sdkServerExtraMapper.selectSdkServerExtraByServerId(paramMap);
        if (null != sdkServerExtraList && sdkServerExtraList.size() > 0){
            for (SdkServerExtra sdkServerExtra:sdkServerExtraList){
                hashMap.remove(sdkServerExtra.getId());
            }
        }
        int row = sdkServerExtraMapper.deleteServerExtraByServerId(paramMap);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据服务器列表ID查询服务器额外信息
     * @param serverListId
     * @return
     */
    @Override
    public List<SdkServerExtra> selectSdkServerExtraByServerListId(Long serverListId){
        return sdkServerExtraMapper.selectSdkServerExtraByServerListId(serverListId);
    }

    /**
     * 获取服务器额外信息的id列表
     * @return
     */
    @Override
    public List<Long> selectSdkServerExtraId() {
        return sdkServerExtraMapper.selectSdkServerExtraId();
    }

    /**
     * 根据服务器ID获取服务器额外信息
     * @param paramMap
     * @return
     */
    @Override
    public List<SdkServerExtra> selectSdkServerExtraByServerId(Map paramMap) {
        return sdkServerExtraMapper.selectSdkServerExtraByServerId(paramMap);
    }

    @Override
    public void reloadSdkServerExtra() {
        init();
    }
}
