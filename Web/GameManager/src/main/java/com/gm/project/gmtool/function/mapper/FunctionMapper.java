package com.gm.project.gmtool.function.mapper;

import java.util.List;
import com.gm.project.gmtool.function.domain.Function;

/**
 * 游戏功能列表Mapper接口
 * 
 * @author gm
 * @date 2021-10-26
 */
public interface FunctionMapper 
{
    /**
     * 查询游戏功能列表
     * 
     * @param funcId 游戏功能列表ID
     * @return 游戏功能列表
     */
    public Function selectFunctionById(Integer funcId);

    /**
     * 查询游戏功能列表列表
     * 
     * @param function 游戏功能列表
     * @return 游戏功能列表集合
     */
    public List<Function> selectFunctionList(Function function);

    /**
     * 新增游戏功能列表
     * 
     * @param function 游戏功能列表
     * @return 结果
     */
    public int insertFunction(Function function);

    /**
     * 修改游戏功能列表
     * 
     * @param function 游戏功能列表
     * @return 结果
     */
    public int updateFunction(Function function);

    /**
     * 删除游戏功能列表
     * 
     * @param funcId 游戏功能列表ID
     * @return 结果
     */
    public int deleteFunctionById(Integer funcId);

    /**
     * 批量删除游戏功能列表
     * 
     * @param funcIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFunctionByIds(String[] funcIds);

    /**
     * 删除全部的游戏功能
     * @return
     */
    public int deleteAllFunctions();
}
