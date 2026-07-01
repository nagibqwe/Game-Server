package com.gm.project.gmtool.evaluate.mapper;

import java.util.List;
import com.gm.project.gmtool.evaluate.domain.Evaluate;

/**
 * 评价开关Mapper接口
 * 
 * @author gm
 * @date 2021-11-04
 */
public interface EvaluateMapper 
{
    /**
     * 查询评价开关
     * 
     * @param id 评价开关ID
     * @return 评价开关
     */
    public Evaluate selectEvaluateById(Integer id);

    /**
     * 查询评价开关列表
     * 
     * @param evaluate 评价开关
     * @return 评价开关集合
     */
    public List<Evaluate> selectEvaluateList(Evaluate evaluate);

    /**
     * 新增评价开关
     * 
     * @param evaluate 评价开关
     * @return 结果
     */
    public int insertEvaluate(Evaluate evaluate);

    /**
     * 修改评价开关
     * 
     * @param evaluate 评价开关
     * @return 结果
     */
    public int updateEvaluate(Evaluate evaluate);

    /**
     * 删除评价开关
     * 
     * @param id 评价开关ID
     * @return 结果
     */
    public int deleteEvaluateById(Integer id);

    /**
     * 批量删除评价开关
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEvaluateByIds(String[] ids);
}
