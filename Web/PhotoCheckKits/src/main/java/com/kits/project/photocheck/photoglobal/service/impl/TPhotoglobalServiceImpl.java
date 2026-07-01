package com.kits.project.photocheck.photoglobal.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.photocheck.photoglobal.mapper.TPhotoglobalMapper;
import com.kits.project.photocheck.photoglobal.domain.TPhotoglobal;
import com.kits.project.photocheck.photoglobal.service.ITPhotoglobalService;
import com.kits.common.utils.text.Convert;

/**
 * 图片全局配置Service业务层处理
 * 
 * @author gm
 * @date 2021-07-23
 */
@Service
public class TPhotoglobalServiceImpl implements ITPhotoglobalService 
{
    @Autowired
    private TPhotoglobalMapper tPhotoglobalMapper;

    /**
     * 查询图片全局配置
     * 
     * @param keyStr 图片全局配置ID
     * @return 图片全局配置
     */
    @Override
    public TPhotoglobal selectTPhotoglobalById(String keyStr)
    {
        return tPhotoglobalMapper.selectTPhotoglobalById(keyStr);
    }

    /**
     * 查询图片全局配置列表
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 图片全局配置
     */
    @Override
    public List<TPhotoglobal> selectTPhotoglobalList(TPhotoglobal tPhotoglobal)
    {
        return tPhotoglobalMapper.selectTPhotoglobalList(tPhotoglobal);
    }

    /**
     * 新增图片全局配置
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 结果
     */
    @Override
    public int insertTPhotoglobal(TPhotoglobal tPhotoglobal)
    {
        return tPhotoglobalMapper.insertTPhotoglobal(tPhotoglobal);
    }

    /**
     * 修改图片全局配置
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 结果
     */
    @Override
    public int updateTPhotoglobal(TPhotoglobal tPhotoglobal)
    {
        return tPhotoglobalMapper.updateTPhotoglobal(tPhotoglobal);
    }

    /**
     * 删除图片全局配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTPhotoglobalByIds(String ids)
    {
        return tPhotoglobalMapper.deleteTPhotoglobalByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图片全局配置信息
     * 
     * @param keyStr 图片全局配置ID
     * @return 结果
     */
    @Override
    public int deleteTPhotoglobalById(String keyStr)
    {
        return tPhotoglobalMapper.deleteTPhotoglobalById(keyStr);
    }
}
