package com.kits.project.photocheck.photodata.mapper;

import java.util.List;
import java.util.Map;

import com.kits.project.photocheck.photodata.domain.TPhotodata;

/**
 * 图片信息Mapper接口
 * 
 * @author gm
 * @date 2021-07-19
 */
public interface TPhotodataMapper 
{
    /**
     * 查询图片信息
     * 
     * @param id 图片信息ID
     * @return 图片信息
     */
    public TPhotodata selectTPhotodataById(Long id);

    /**
     * 查询图片信息列表
     * 
     * @param tPhotodata 图片信息
     * @return 图片信息集合
     */
    public List<TPhotodata> selectTPhotodataList(TPhotodata tPhotodata);

    /**
     * 新增图片信息
     * 
     * @param tPhotodata 图片信息
     * @return 结果
     */
    public Long insertTPhotodata(TPhotodata tPhotodata);

    /**
     * 修改图片信息
     * 
     * @param tPhotodata 图片信息
     * @return 结果
     */
    public int updateTPhotodata(TPhotodata tPhotodata);

    /**
     * 删除图片信息
     * 
     * @param id 图片信息ID
     * @return 结果
     */
    public int deleteTPhotodataById(Long id);

    /**
     * 批量删除图片信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTPhotodataByIds(String[] ids);

    /**
     * 根据photoID删除对应的数据
     * @param photoID
     * @return
     */
    public int deleteTPhotodataByPhotoID(String photoID);

    /**
     * 根据photoID查询数据
     * @param photoID
     * @return
     */
    public TPhotodata selectTPhotodataByPhotoID(String photoID);

    /**
     * 根据id将数据改为删除状态
     * @param ids
     * @return
     */
    public int putBackTPhotodataByIds(String[] ids);

    /**
     * 根据id列表查询数据
     * @param ids
     * @return
     */
    public List<TPhotodata> selectTPhotodataByIds(String[] ids);

    /**
     * 删除所有标记为删除状态的数据
     * @return
     */
    public int deleteTPhotodataByIsDelete();

    /**
     * 根据id将数据改为未删除状态(恢复操作)
     * @param ids
     * @return
     */
    public int recoverTPhotodataByIds(String[] ids);

    /**
     * 回收站数据全部恢复(恢复操作)
     * @return
     */
    public int recoverTPhotodata();

    /**
     * 根据id将数据改为对应的审核状态
     * @param paramMap
     * @return
     */
    public int updateTPhotodataCheckStatus(Map paramMap);

}
