package com.gm.project.gmtool.evaluate.service;

import java.util.List;
import com.gm.project.gmtool.evaluate.domain.Evaluate;

/**
 * Оценки включеныService接口
 * 
 * @author gm
 * @date 2021-11-04
 */
public interface IEvaluateService 
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
     * 批量УдалитьОценки включены
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteEvaluateByIds(String ids);

    /**
     * УдалитьОценки включеныИнформация
     * 
     * @param id Оценки включеныID
     * @return Результат
     */
    public int deleteEvaluateById(Integer id);
}
