package com.gm.project.gmtool.function.mapper;

import java.util.List;
import com.gm.project.gmtool.function.domain.Function;

/**
 * Функции игрыMapper接口
 * 
 * @author gm
 * @date 2021-10-26
 */
public interface FunctionMapper 
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
     * УдалитьФункции игры
     * 
     * @param funcId Функции игрыID
     * @return Результат
     */
    public int deleteFunctionById(Integer funcId);

    /**
     * 批量УдалитьФункции игры
     * 
     * @param funcIds 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteFunctionByIds(String[] funcIds);

    /**
     * Удалить全部的游戏功能
     * @return
     */
    public int deleteAllFunctions();
}
