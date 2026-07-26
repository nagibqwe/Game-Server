package com.gm.project.gmtool.cyAnnounce.service;

import java.util.List;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;

/**
 * Циклическое объявлениеService接口
 * 
 * @author gm
 * @date 2021-10-27
 */
public interface ICyAnnounceService 
{
    /**
     * 查询Циклическое объявление
     * 
     * @param id Циклическое объявлениеID
     * @return Циклическое объявление
     */
    public CyAnnounce selectCyAnnounceById(Integer id);

    /**
     * 查询Циклическое объявление列表
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Циклическое объявление集合
     */
    public List<CyAnnounce> selectCyAnnounceList(CyAnnounce cyAnnounce);

    /**
     * ДобавитьЦиклическое объявление
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Результат
     */
    public int insertCyAnnounce(CyAnnounce cyAnnounce);

    /**
     * ИзменитьЦиклическое объявление
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Результат
     */
    public int updateCyAnnounce(CyAnnounce cyAnnounce);

    /**
     * 批量УдалитьЦиклическое объявление
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteCyAnnounceByIds(String ids);

    /**
     * УдалитьЦиклическое объявлениеИнформация
     * 
     * @param id Циклическое объявлениеID
     * @return Результат
     */
    public int deleteCyAnnounceById(Integer id);

    /**
     * 查询Циклическое объявление列表(禁用列表)
     * @param cyAnnounce
     * @return
     */
    public List<CyAnnounce> selectCyAnnounceDisableList(CyAnnounce cyAnnounce);
}
