package com.kits.project.photocheck.photodata.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.photocheck.photodata.mapper.TPhotodataMapper;
import com.kits.project.photocheck.photodata.domain.TPhotodata;
import com.kits.project.photocheck.photodata.service.ITPhotodataService;
import com.kits.common.utils.text.Convert;

/**
 * 图片信息Service业务层处理
 * 
 * @author gm
 * @date 2021-07-19
 */
@Service
public class TPhotodataServiceImpl implements ITPhotodataService 
{
    @Autowired
    private TPhotodataMapper tPhotodataMapper;

    /**
     * 查询图片信息
     * 
     * @param id 图片信息ID
     * @return 图片信息
     */
    @Override
    public TPhotodata selectTPhotodataById(Long id)
    {
        return tPhotodataMapper.selectTPhotodataById(id);
    }

    /**
     * 查询图片信息列表
     * 
     * @param tPhotodata 图片信息
     * @return 图片信息
     */
    @Override
    public List<TPhotodata> selectTPhotodataList(TPhotodata tPhotodata)
    {
        return tPhotodataMapper.selectTPhotodataList(tPhotodata);
    }

    /**
     * 新增图片信息
     * 
     * @param tPhotodata 图片信息
     * @return 结果
     */
    @Override
    public Long insertTPhotodata(TPhotodata tPhotodata)
    {
        Long id = 0L;
        Long row = tPhotodataMapper.insertTPhotodata(tPhotodata);
        if (row > 0){
            id = tPhotodata.getId();
        }
        return id;
    }

    /**
     * 修改图片信息
     * 
     * @param tPhotodata 图片信息
     * @return 结果
     */
    @Override
    public int updateTPhotodata(TPhotodata tPhotodata)
    {
        return tPhotodataMapper.updateTPhotodata(tPhotodata);
    }

    /**
     * 删除图片信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTPhotodataByIds(String ids)
    {
        return tPhotodataMapper.deleteTPhotodataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图片信息信息
     * 
     * @param id 图片信息ID
     * @return 结果
     */
    @Override
    public int deleteTPhotodataById(Long id)
    {
        return tPhotodataMapper.deleteTPhotodataById(id);
    }

    /**
     * 根据photoID删除对应的数据
     * @param photoID
     * @return
     */
    @Override
    public int deleteTPhotodataByPhotoID(String photoID) {
        return tPhotodataMapper.deleteTPhotodataByPhotoID(photoID);
    }

    /**
     * 根据photoID查询数据
     * @param photoID
     * @return
     */
    @Override
    public TPhotodata selectTPhotodataByPhotoID(String photoID) {
        return tPhotodataMapper.selectTPhotodataByPhotoID(photoID);
    }

    /**
     * 根据id将数据改为删除状态
     * @param ids
     * @return
     */
    @Override
    public int putBackTPhotodataByIds(String ids) {
        return tPhotodataMapper.putBackTPhotodataByIds(Convert.toStrArray(ids));
    }

    /**
     * 根据id列表查询数据
     * @param ids
     * @return
     */
    @Override
    public List<TPhotodata> selectTPhotodataByIds(String ids) {
        return tPhotodataMapper.selectTPhotodataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除所有标记为删除状态的数据
     * @return
     */
    @Override
    public int deleteTPhotodataByIsDelete() {
        return tPhotodataMapper.deleteTPhotodataByIsDelete();
    }

    /**
     * 根据id将数据改为未删除状态(恢复操作)
     * @param ids
     * @return
     */
    @Override
    public int recoverTPhotodataByIds(String ids) {
        return tPhotodataMapper.recoverTPhotodataByIds(Convert.toStrArray(ids));
    }

    /**
     * 回收站数据全部恢复(恢复操作)
     * @return
     */
    @Override
    public int recoverTPhotodata() {
        return tPhotodataMapper.recoverTPhotodata();
    }

    /**
     * 根据id将数据改为对应的审核状态
     * @param paramMap
     * @return
     */
    @Override
    public int updateTPhotodataCheckStatus(Map paramMap) {
        return tPhotodataMapper.updateTPhotodataCheckStatus(paramMap);
    }
}
