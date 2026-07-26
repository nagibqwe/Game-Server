package com.gm.project.gmtool.evaluate.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.evaluate.mapper.EvaluateMapper;
import com.gm.project.gmtool.evaluate.domain.Evaluate;
import com.gm.project.gmtool.evaluate.service.IEvaluateService;
import com.gm.common.utils.text.Convert;

/**
 * Оценки включеныService业务层处理
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
     * 查询Оценки включены
     * 
     * @param id Оценки включеныID
     * @return Оценки включены
     */
    @Override
    public Evaluate selectEvaluateById(Integer id)
    {
        return evaluateMapper.selectEvaluateById(id);
    }

    /**
     * 查询Оценки включены列表
     * 
     * @param evaluate Оценки включены
     * @return Оценки включены
     */
    @Override
    public List<Evaluate> selectEvaluateList(Evaluate evaluate)
    {
        return evaluateMapper.selectEvaluateList(evaluate);
    }

    /**
     * ДобавитьОценки включены
     * 
     * @param evaluate Оценки включены
     * @return Результат
     */
    @Override
    public int insertEvaluate(Evaluate evaluate)
    {
        return evaluateMapper.insertEvaluate(evaluate);
    }

    /**
     * ИзменитьОценки включены
     * 
     * @param evaluate Оценки включены
     * @return Результат
     */
    @Override
    public int updateEvaluate(Evaluate evaluate)
    {
        return evaluateMapper.updateEvaluate(evaluate);
    }

    /**
     * УдалитьОценки включены对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteEvaluateByIds(String ids)
    {
        return evaluateMapper.deleteEvaluateByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьОценки включеныИнформация
     * 
     * @param id Оценки включеныID
     * @return Результат
     */
    @Override
    public int deleteEvaluateById(Integer id)
    {
        return evaluateMapper.deleteEvaluateById(id);
    }
}
