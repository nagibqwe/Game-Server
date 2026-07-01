package com.gm.project.gmtool.announce.service.impl;

import java.util.List;
import com.gm.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.announce.mapper.AnnounceMapper;
import com.gm.project.gmtool.announce.domain.Announce;
import com.gm.project.gmtool.announce.service.IAnnounceService;
import com.gm.common.utils.text.Convert;

/**
 * 即时公告Service业务层处理
 * 
 * @author gm
 * @date 2021-10-21
 */
@Service
public class AnnounceServiceImpl implements IAnnounceService 
{
    @Autowired
    private AnnounceMapper announceMapper;

    /**
     * 查询即时公告
     * 
     * @param id 即时公告ID
     * @return 即时公告
     */
    @Override
    public Announce selectAnnounceById(Integer id)
    {
        return announceMapper.selectAnnounceById(id);
    }

    /**
     * 查询即时公告列表
     * 
     * @param announce 即时公告
     * @return 即时公告
     */
    @Override
    public List<Announce> selectAnnounceList(Announce announce)
    {
        return announceMapper.selectAnnounceList(announce);
    }

    /**
     * 新增即时公告
     * 
     * @param announce 即时公告
     * @return 结果
     */
    @Override
    public int insertAnnounce(Announce announce)
    {
//        announce.setCreateTime(DateUtils.getNowDate());
        return announceMapper.insertAnnounce(announce);
    }

    /**
     * 修改即时公告
     * 
     * @param announce 即时公告
     * @return 结果
     */
    @Override
    public int updateAnnounce(Announce announce)
    {
        return announceMapper.updateAnnounce(announce);
    }

    /**
     * 删除即时公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAnnounceByIds(String ids)
    {
        return announceMapper.deleteAnnounceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除即时公告信息
     * 
     * @param id 即时公告ID
     * @return 结果
     */
    @Override
    public int deleteAnnounceById(Integer id)
    {
        return announceMapper.deleteAnnounceById(id);
    }
}
