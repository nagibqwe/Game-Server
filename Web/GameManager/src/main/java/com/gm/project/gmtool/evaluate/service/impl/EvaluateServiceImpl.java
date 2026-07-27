package com.gm.project.gmtool.evaluate.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.evaluate.mapper.EvaluateMapper;
import com.gm.project.gmtool.evaluate.domain.Evaluate;
import com.gm.project.gmtool.evaluate.service.IEvaluateService;
import com.gm.common.utils.text.Convert;

/**
 * 评价开关Service业务层处理
 * 
 * @author gm
 * @date 2021-11-04
 */
@Service
public class EvaluateServiceImpl implements IEvaluateService 
{
    @Autowired
    private EvaluateMapper evaluateMapper;

    /**
     * 查询评价开关
     * 
     * @param id 评价开关ID
     * @return 评价开关
     */
    @Override
    public Evaluate selectEvaluateById(Integer id)
    {
        return evaluateMapper.selectEvaluateById(id);
    }

    /**
     * 查询评价开关列表
     * 
     * @param evaluate 评价开关
     * @return 评价开关
     */
    @Override
    public List<Evaluate> selectEvaluateList(Evaluate evaluate)
    {
        return evaluateMapper.selectEvaluateList(evaluate);
    }

    /**
     * 新增评价开关
     * 
     * @param evaluate 评价开关
     * @return 结果
     */
    @Override
    public int insertEvaluate(Evaluate evaluate)
    {
        return evaluateMapper.insertEvaluate(evaluate);
    }

    /**
     * 修改评价开关
     * 
     * @param evaluate 评价开关
     * @return 结果
     */
    @Override
    public int updateEvaluate(Evaluate evaluate)
    {
        return evaluateMapper.updateEvaluate(evaluate);
    }

    /**
     * 删除评价开关对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEvaluateByIds(String ids)
    {
        return evaluateMapper.deleteEvaluateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除评价开关信息
     * 
     * @param id 评价开关ID
     * @return 结果
     */
    @Override
    public int deleteEvaluateById(Integer id)
    {
        return evaluateMapper.deleteEvaluateById(id);
    }
}
