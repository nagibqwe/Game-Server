package com.kits.project.serverListConfig.serverExtra.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;

/**
 * 服务器额外信息Service接口
 *
 * @author gm
 * @date 2021-05-07
 */
public interface ISdkServerExtraService
{
    /**
     * 获取服务器额外信息map
     *
     * @return
     */
    public ConcurrentHashMap<Long, SdkServerExtra> getServerExtraHashMap();

    /**
     * 查询服务器额外信息
     *
     * @param serverListId 服务器额外信息ID
     * @return 服务器额外信息
     */
    public List<SdkServerExtra> selectSdkServerExtraById(Long serverListId);

    /**
     * 查询服务器额外信息列表
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 服务器额外信息集合
     */
    public List<SdkServerExtra> selectSdkServerExtraList(SdkServerExtra sdkServerExtra);

    /**
     * 新增服务器额外信息
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 结果
     */
    public int insertSdkServerExtra(SdkServerExtra sdkServerExtra);

    /**
     * 修改服务器额外信息
     *
     * @param sdkServerExtra 服务器额外信息
     * @return 结果
     */
    public int updateSdkServerExtra(SdkServerExtra sdkServerExtra);

    /**
     * 批量删除服务器额外信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerExtraByIds(String ids);

    /**
     * 删除服务器额外信息信息
     *
     * @param serverListId 服务器额外信息ID
     * @return 结果
     */
    public int deleteSdkServerExtraById(Long serverListId);

    /**
     *根据服务器ID列表查询服务器额外信息
     * @param
     * @return
     */
    public List<SdkServerExtra> selectServerExtraByServerIds(Map paramMap);

    /**
     * 保存修改后的服务器额外信息
     * @param sdkServerExtra
     * @return
     */
    public int saveServerExtra(SdkServerExtra sdkServerExtra);

    /**
     * 根据服务器列表ID获取该列表下的服务器ID
     * @param serverListId
     * @return
     */
    public List<Long> selectServerIdsByServerListId(Long serverListId);

    /**
     * 根据服务器列表Id和服务器Id删除对应的服务器额外信息
     * @param paramMap
     * @return
     */
    public int deleteServerExtraByServerListIdAndServerId(Map paramMap);

    /**
     * 根据服务器Id删除对应的服务器额外信息
     * @param paramMap
     * @return
     */
    public int deleteServerExtraByServerId(Map paramMap);

    /**
     * 根据服务器列表ID查询服务器额外信息
     * @param serverListId
     * @return
     */
    public List<SdkServerExtra> selectSdkServerExtraByServerListId(Long serverListId);

    /**
     * 获取服务器额外信息的id列表
     * @return
     */
    public List<Long> selectSdkServerExtraId();

    /**
     * 根据服务器ID获取服务器额外信息
     * @param paramMap
     * @return
     */
    public List<SdkServerExtra> selectSdkServerExtraByServerId(Map paramMap);

    public void reloadSdkServerExtra();
}
