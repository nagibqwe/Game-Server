package com.gm.project.gmtool.dbbak.service;

import java.util.List;
import com.gm.project.gmtool.dbbak.domain.Dbbak;

/**
 * Резервные копии БДService接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IDbbakService 
{
    /**
     * 查询Резервные копии БД
     * 
     * @param id Резервные копии БДID
     * @return Резервные копии БД
     */
    public Dbbak selectDbbakById(Long id);

    public Dbbak selectLatestDbbak(Dbbak dbbak);

    /**
     * 查询Список резервных копий БД
     * 
     * @param dbbak Резервные копии БД
     * @return Резервные копии БД集合
     */
    public List<Dbbak> selectDbbakList(Dbbak dbbak);

    /**
     * ДобавитьРезервные копии БД
     * 
     * @param dbbak Резервные копии БД
     * @return Результат
     */
    public int insertDbbak(Dbbak dbbak);

    /**
     * ИзменитьРезервные копии БД
     * 
     * @param dbbak Резервные копии БД
     * @return Результат
     */
    public int updateDbbak(Dbbak dbbak);

    /**
     * 批量УдалитьРезервные копии БД
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteDbbakByIds(String ids);

    /**
     * УдалитьРезервные копии БДИнформация
     * 
     * @param id Резервные копии БДID
     * @return Результат
     */
    public int deleteDbbakById(Long id);
}
