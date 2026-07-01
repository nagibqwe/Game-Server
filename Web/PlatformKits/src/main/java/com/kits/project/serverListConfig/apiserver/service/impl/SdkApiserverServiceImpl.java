package com.kits.project.serverListConfig.apiserver.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.apiserver.mapper.SdkApiserverMapper;
import com.kits.project.serverListConfig.apiserver.domain.SdkApiserver;
import com.kits.project.serverListConfig.apiserver.service.ISdkApiserverService;
import com.kits.common.utils.text.Convert;

/**
 * APIServer地址管理Service业务层处理
 * 
 * @author gm
 * @date 2021-06-24
 */
@Service
public class SdkApiserverServiceImpl implements ISdkApiserverService 
{
    @Autowired
    private SdkApiserverMapper sdkApiserverMapper;

    /**
     * 查询APIServer地址管理
     * 
     * @param id APIServer地址管理ID
     * @return APIServer地址管理
     */
    @Override
    public SdkApiserver selectSdkApiserverById(Long id)
    {
        return sdkApiserverMapper.selectSdkApiserverById(id);
    }

    /**
     * 查询APIServer地址管理列表
     * 
     * @param sdkApiserver APIServer地址管理
     * @return APIServer地址管理
     */
    @Override
    public List<SdkApiserver> selectSdkApiserverList(SdkApiserver sdkApiserver)
    {
        return sdkApiserverMapper.selectSdkApiserverList(sdkApiserver);
    }

    /**
     * 新增APIServer地址管理
     * 
     * @param sdkApiserver APIServer地址管理
     * @return 结果
     */
    @Override
    public int insertSdkApiserver(SdkApiserver sdkApiserver)
    {
        return sdkApiserverMapper.insertSdkApiserver(sdkApiserver);
    }

    /**
     * 修改APIServer地址管理
     * 
     * @param sdkApiserver APIServer地址管理
     * @return 结果
     */
    @Override
    public int updateSdkApiserver(SdkApiserver sdkApiserver)
    {
        return sdkApiserverMapper.updateSdkApiserver(sdkApiserver);
    }

    /**
     * 删除APIServer地址管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkApiserverByIds(String ids)
    {
        return sdkApiserverMapper.deleteSdkApiserverByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除APIServer地址管理信息
     * 
     * @param id APIServer地址管理ID
     * @return 结果
     */
    @Override
    public int deleteSdkApiserverById(Long id)
    {
        return sdkApiserverMapper.deleteSdkApiserverById(id);
    }
}
