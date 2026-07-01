package com.kits.project.serverListConfig.loginServer.mapper;

import java.util.List;
import com.kits.project.serverListConfig.loginServer.domain.SdkLoginServer;

/**
 * 登录服信息Mapper接口
 * 
 * @author gm
 * @date 2021-10-20
 */
public interface SdkLoginServerMapper 
{
    /**
     * 查询登录服信息
     * 
     * @param id 登录服信息ID
     * @return 登录服信息
     */
    public SdkLoginServer selectSdkLoginServerById(Long id);

    /**
     * 查询登录服信息列表
     * 
     * @param sdkLoginServer 登录服信息
     * @return 登录服信息集合
     */
    public List<SdkLoginServer> selectSdkLoginServerList(SdkLoginServer sdkLoginServer);

    /**
     * 新增登录服信息
     * 
     * @param sdkLoginServer 登录服信息
     * @return 结果
     */
    public int insertSdkLoginServer(SdkLoginServer sdkLoginServer);

    /**
     * 修改登录服信息
     * 
     * @param sdkLoginServer 登录服信息
     * @return 结果
     */
    public int updateSdkLoginServer(SdkLoginServer sdkLoginServer);

    /**
     * 删除登录服信息
     * 
     * @param id 登录服信息ID
     * @return 结果
     */
    public int deleteSdkLoginServerById(Long id);

    /**
     * 批量删除登录服信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkLoginServerByIds(String[] ids);

    /**
     * 获取页面展示登录服组(已有的登录服数据)
     * @param ids
     * @return
     */
    public List<SdkLoginServer> selectShowLoginServers(String[] ids);
}
