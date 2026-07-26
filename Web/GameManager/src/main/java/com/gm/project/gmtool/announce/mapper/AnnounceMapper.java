package com.gm.project.gmtool.announce.mapper;

import java.util.List;
import com.gm.project.gmtool.announce.domain.Announce;

/**
 * Мгновенное объявлениеMapper接口
 * 
 * @author gm
 * @date 2021-10-21
 */
public interface AnnounceMapper 
{
    /**
     * 查询Мгновенное объявление
     * 
     * @param id Мгновенное объявлениеID
     * @return Мгновенное объявление
     */
    public Announce selectAnnounceById(Integer id);

    /**
     * 查询Мгновенное объявление列表
     * 
     * @param announce Мгновенное объявление
     * @return Мгновенное объявление集合
     */
    public List<Announce> selectAnnounceList(Announce announce);

    /**
     * ДобавитьМгновенное объявление
     * 
     * @param announce Мгновенное объявление
     * @return Результат
     */
    public int insertAnnounce(Announce announce);

    /**
     * ИзменитьМгновенное объявление
     * 
     * @param announce Мгновенное объявление
     * @return Результат
     */
    public int updateAnnounce(Announce announce);

    /**
     * УдалитьМгновенное объявление
     * 
     * @param id Мгновенное объявлениеID
     * @return Результат
     */
    public int deleteAnnounceById(Integer id);

    /**
     * 批量УдалитьМгновенное объявление
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteAnnounceByIds(String[] ids);
}
