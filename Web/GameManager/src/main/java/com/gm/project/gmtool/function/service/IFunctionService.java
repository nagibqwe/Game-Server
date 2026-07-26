package com.gm.project.gmtool.function.service;

import java.util.List;
import com.gm.project.gmtool.function.domain.Function;

/**
 * Функции игрыService接口
 * 
 * @author gm
 * @date 2021-10-26
 */
public interface IFunctionService 
{
    /**
     * 查询Функции игры
     * 
     * @param funcId Функции игрыID
     * @return Функции игры
     */
    public Function selectFunctionById(Integer funcId);

    /**
     * 查询Функции игры列表
     * 
     * @param function Функции игры
     * @return Функции игры集合
     */
    public List<Function> selectFunctionList(Function function);

    /**
     * ДобавитьФункции игры
     * 
     * @param function Функции игры
     * @return Результат
     */
    public int insertFunction(Function function);

    /**
     * ИзменитьФункции игры
     * 
     * @param function Функции игры
     * @return Результат
     */
    public int updateFunction(Function function);

    /**
     * 批量УдалитьФункции игры
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteFunctionByIds(String ids);

    /**
     * УдалитьФункции игрыИнформация
     * 
     * @param funcId Функции игрыID
     * @return Результат
     */
    public int deleteFunctionById(Integer funcId);

    /**
     * Удалить全部的游戏功能
     * @return
     */
    public int deleteAllFunctions();
}
