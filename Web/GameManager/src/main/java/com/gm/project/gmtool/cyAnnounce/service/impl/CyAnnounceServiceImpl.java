package com.gm.project.gmtool.cyAnnounce.service.impl;

import java.util.List;
import com.gm.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.cyAnnounce.mapper.CyAnnounceMapper;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;
import com.gm.project.gmtool.cyAnnounce.service.ICyAnnounceService;
import com.gm.common.utils.text.Convert;

/**
 * 循环公告Service业务层处理
 * 
 * @author gm
 * @date 2021-10-27
 */
@Service
public class CyAnnounceServiceImpl implements ICyAnnounceService 
{
    @Autowired
    private CyAnnounceMapper cyAnnounceMapper;

    /**
     * 查询循环公告
     * 
     * @param id 循环公告ID
     * @return 循环公告
     */
    @Override
    public CyAnnounce selectCyAnnounceById(Integer id)
    {
        return cyAnnounceMapper.selectCyAnnounceById(id);
    }

    /**
     * 查询循环公告列表
     * 
     * @param cyAnnounce 循环公告
     * @return 循环公告
     */
    @Override
    public List<CyAnnounce> selectCyAnnounceList(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.selectCyAnnounceList(cyAnnounce);
    }

    /**
     * 新增循环公告
     * 
     * @param cyAnnounce 循环公告
     * @return 结果
     */
    @Override
    public int insertCyAnnounce(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.insertCyAnnounce(cyAnnounce);
    }

    /**
     * 修改循环公告
     * 
     * @param cyAnnounce 循环公告
     * @return 结果
     */
    @Override
    public int updateCyAnnounce(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.updateCyAnnounce(cyAnnounce);
    }

    /**
     * 删除循环公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCyAnnounceByIds(String ids)
    {
        return cyAnnounceMapper.deleteCyAnnounceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除循环公告信息
     * 
     * @param id 循环公告ID
     * @return 结果
     */
    @Override
    public int deleteCyAnnounceById(Integer id)
    {
        return cyAnnounceMapper.deleteCyAnnounceById(id);
    }

    /**
     * 查询循环公告列表(禁用列表)
     * @param cyAnnounce
     * @return
     */
    @Override
    public List<CyAnnounce> selectCyAnnounceDisableList(CyAnnounce cyAnnounce) {
        return cyAnnounceMapper.selectCyAnnounceDisableList(cyAnnounce);
    }
}
