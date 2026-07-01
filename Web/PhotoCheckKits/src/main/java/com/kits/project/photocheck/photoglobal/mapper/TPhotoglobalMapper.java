package com.kits.project.photocheck.photoglobal.mapper;

import java.util.List;
import com.kits.project.photocheck.photoglobal.domain.TPhotoglobal;

/**
 * 图片全局配置Mapper接口
 * 
 * @author gm
 * @date 2021-07-23
 */
public interface TPhotoglobalMapper 
{
    /**
     * 查询图片全局配置
     * 
     * @param keyStr 图片全局配置ID
     * @return 图片全局配置
     */
    public TPhotoglobal selectTPhotoglobalById(String keyStr);

    /**
     * 查询图片全局配置列表
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 图片全局配置集合
     */
    public List<TPhotoglobal> selectTPhotoglobalList(TPhotoglobal tPhotoglobal);

    /**
     * 新增图片全局配置
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 结果
     */
    public int insertTPhotoglobal(TPhotoglobal tPhotoglobal);

    /**
     * 修改图片全局配置
     * 
     * @param tPhotoglobal 图片全局配置
     * @return 结果
     */
    public int updateTPhotoglobal(TPhotoglobal tPhotoglobal);

    /**
     * 删除图片全局配置
     * 
     * @param keyStr 图片全局配置ID
     * @return 结果
     */
    public int deleteTPhotoglobalById(String keyStr);

    /**
     * 批量删除图片全局配置
     * 
     * @param keyStrs 需要删除的数据ID
     * @return 结果
     */
    public int deleteTPhotoglobalByIds(String[] keyStrs);
}
