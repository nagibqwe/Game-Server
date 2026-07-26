package com.gm.project.gmtool.function.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.function.mapper.FunctionMapper;
import com.gm.project.gmtool.function.domain.Function;
import com.gm.project.gmtool.function.service.IFunctionService;
import com.gm.common.utils.text.Convert;

/**
 * Функции игрыService业务层处理
 * 
 * @author gm
 * @date 2021-10-26
 */
@Service
public class FunctionServiceImpl implements IFunctionService 
{
    @Autowired
    private FunctionMapper functionMapper;

    /**
     * 查询Функции игры
     * 
     * @param funcId Функции игрыID
     * @return Функции игры
     */
    @Override
    public Function selectFunctionById(Integer funcId)
    {
        return functionMapper.selectFunctionById(funcId);
    }

    /**
     * 查询Функции игры列表
     * 
     * @param function Функции игры
     * @return Функции игры
     */
    @Override
    public List<Function> selectFunctionList(Function function)
    {
        return functionMapper.selectFunctionList(function);
    }

    /**
     * ДобавитьФункции игры
     * 
     * @param function Функции игры
     * @return Результат
     */
    @Override
    public int insertFunction(Function function)
    {
        return functionMapper.insertFunction(function);
    }

    /**
     * ИзменитьФункции игры
     * 
     * @param function Функции игры
     * @return Результат
     */
    @Override
    public int updateFunction(Function function)
    {
        return functionMapper.updateFunction(function);
    }

    /**
     * УдалитьФункции игры对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteFunctionByIds(String ids)
    {
        return functionMapper.deleteFunctionByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьФункции игрыИнформация
     * 
     * @param funcId Функции игрыID
     * @return Результат
     */
    @Override
    public int deleteFunctionById(Integer funcId)
    {
        return functionMapper.deleteFunctionById(funcId);
    }

    @Override
    public int deleteAllFunctions() {
        return functionMapper.deleteAllFunctions();
    }
}
