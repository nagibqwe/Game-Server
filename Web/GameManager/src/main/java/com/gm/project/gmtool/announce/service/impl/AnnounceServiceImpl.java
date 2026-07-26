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
 * Мгновенное объявлениеService业务层处理
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
     * 查询Мгновенное объявление
     * 
     * @param id Мгновенное объявлениеID
     * @return Мгновенное объявление
     */
    @Override
    public Announce selectAnnounceById(Integer id)
    {
        return announceMapper.selectAnnounceById(id);
    }

    /**
     * 查询Мгновенное объявление列表
     * 
     * @param announce Мгновенное объявление
     * @return Мгновенное объявление
     */
    @Override
    public List<Announce> selectAnnounceList(Announce announce)
    {
        return announceMapper.selectAnnounceList(announce);
    }

    /**
     * ДобавитьМгновенное объявление
     * 
     * @param announce Мгновенное объявление
     * @return Результат
     */
    @Override
    public int insertAnnounce(Announce announce)
    {
//        announce.setCreateTime(DateUtils.getNowDate());
        return announceMapper.insertAnnounce(announce);
    }

    /**
     * ИзменитьМгновенное объявление
     * 
     * @param announce Мгновенное объявление
     * @return Результат
     */
    @Override
    public int updateAnnounce(Announce announce)
    {
        return announceMapper.updateAnnounce(announce);
    }

    /**
     * УдалитьМгновенное объявление对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteAnnounceByIds(String ids)
    {
        return announceMapper.deleteAnnounceByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьМгновенное объявлениеИнформация
     * 
     * @param id Мгновенное объявлениеID
     * @return Результат
     */
    @Override
    public int deleteAnnounceById(Integer id)
    {
        return announceMapper.deleteAnnounceById(id);
    }
}
