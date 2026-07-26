package com.gm.project.gmtool.changereason.service;

import java.util.List;
import com.gm.project.gmtool.changereason.domain.TChangereason;

/**
 * Код причиныService接口
 * 
 * @author gm
 * @date 2021-12-21
 */
public interface ITChangereasonService 
{

    public void loadData();
    /**
     * 查询Код причины
     * 
     * @param id Код причиныID
     * @return Код причины
     */
    public TChangereason selectTChangereasonById(Long id);

    /**
     * 查询Код причины列表
     * 
     * @param tChangereason Код причины
     * @return Код причины集合
     */
    public List<TChangereason> selectTChangereasonList(TChangereason tChangereason);

    /**
     * ДобавитьКод причины
     * 
     * @param tChangereason Код причины
     * @return Результат
     */
    public int insertTChangereason(TChangereason tChangereason);

    /**
     * ИзменитьКод причины
     * 
     * @param tChangereason Код причины
     * @return Результат
     */
    public int updateTChangereason(TChangereason tChangereason);

    /**
     * 批量УдалитьКод причины
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteTChangereasonByIds(String ids);

    /**
     * УдалитьКод причиныИнформация
     * 
     * @param id Код причиныID
     * @return Результат
     */
    public int deleteTChangereasonById(Long id);

    /**
     * Удалить全部
     * @return
     */
    public int deleteAllTChangereason();

}
