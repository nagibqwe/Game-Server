package com.gm.project.gmtool.white.service;

import java.util.List;
import com.gm.project.gmtool.white.domain.White;

/**
 * Белый списокService接口
 * 
 * @author gm
 * @date 2021-11-22
 */
public interface IWhiteService 
{
    /**
     * 查询Белый список
     * 
     * @param id Белый списокID
     * @return Белый список
     */
    public White selectWhiteById(Long id);

    /**
     * 查询Белый список列表
     * 
     * @param white Белый список
     * @return Белый список集合
     */
    public List<White> selectWhiteList(White white);

    /**
     * ДобавитьБелый список
     * 
     * @param white Белый список
     * @return Результат
     */
    public int insertWhite(White white);

    /**
     * ИзменитьБелый список
     * 
     * @param white Белый список
     * @return Результат
     */
    public int updateWhite(White white);

    /**
     * 批量УдалитьБелый список
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteWhiteByIds(String ids);

    /**
     * УдалитьБелый списокИнформация
     * 
     * @param id Белый списокID
     * @return Результат
     */
    public int deleteWhiteById(Long id);
}
