package com.gm.project.gmtool.function.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.function.mapper.FunctionMapper;
import com.gm.project.gmtool.function.domain.Function;
import com.gm.project.gmtool.function.service.IFunctionService;
import com.gm.common.utils.text.Convert;

/**
 * 游戏功能列表Service业务层处理
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
     * 查询游戏功能列表
     * 
     * @param funcId 游戏功能列表ID
     * @return 游戏功能列表
     */
    @Override
    public Function selectFunctionById(Integer funcId)
    {
        return functionMapper.selectFunctionById(funcId);
    }

    /**
     * 查询游戏功能列表列表
     * 
     * @param function 游戏功能列表
     * @return 游戏功能列表
     */
    @Override
    public List<Function> selectFunctionList(Function function)
    {
        return functionMapper.selectFunctionList(function);
    }

    /**
     * 新增游戏功能列表
     * 
     * @param function 游戏功能列表
     * @return 结果
     */
    @Override
    public int insertFunction(Function function)
    {
        return functionMapper.insertFunction(function);
    }

    /**
     * 修改游戏功能列表
     * 
     * @param function 游戏功能列表
     * @return 结果
     */
    @Override
    public int updateFunction(Function function)
    {
        return functionMapper.updateFunction(function);
    }

    /**
     * 删除游戏功能列表对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFunctionByIds(String ids)
    {
        return functionMapper.deleteFunctionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除游戏功能列表信息
     * 
     * @param funcId 游戏功能列表ID
     * @return 结果
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
