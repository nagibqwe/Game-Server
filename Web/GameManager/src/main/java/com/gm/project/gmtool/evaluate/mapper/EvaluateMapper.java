package com.gm.project.gmtool.evaluate.mapper;

import java.util.List;
import com.gm.project.gmtool.evaluate.domain.Evaluate;

/**
 * Оценки включеныMapper接口
 * 
 * @author gm
 * @date 2021-11-04
 */
public interface EvaluateMapper 
{
    /**
     * 查询Оценки включены
     * 
     * @param id Оценки включеныID
     * @return Оценки включены
     */
    public Evaluate selectEvaluateById(Integer id);

    /**
     * 查询Оценки включены列表
     * 
     * @param evaluate Оценки включены
     * @return Оценки включены集合
     */
    public List<Evaluate> selectEvaluateList(Evaluate evaluate);

    /**
     * ДобавитьОценки включены
     * 
     * @param evaluate Оценки включены
     * @return Результат
     */
    public int insertEvaluate(Evaluate evaluate);

    /**
     * ИзменитьОценки включены
     * 
     * @param evaluate Оценки включены
     * @return Результат
     */
    public int updateEvaluate(Evaluate evaluate);

    /**
     * УдалитьОценки включены
     * 
     * @param id Оценки включеныID
     * @return Результат
     */
    public int deleteEvaluateById(Integer id);

    /**
     * 批量УдалитьОценки включены
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteEvaluateByIds(String[] ids);
}
