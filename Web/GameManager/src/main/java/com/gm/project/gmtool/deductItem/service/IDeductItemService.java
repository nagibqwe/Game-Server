package com.gm.project.gmtool.deductItem.service;

import java.util.List;
import com.gm.project.gmtool.deductItem.domain.DeductItem;

/**
 * Списание предметовService接口
 * 
 * @author gm
 * @date 2021-10-30
 */
public interface IDeductItemService 
{
    /**
     * 查询Списание предметов
     * 
     * @param id Списание предметовID
     * @return Списание предметов
     */
    public DeductItem selectDeductItemById(Integer id);

    /**
     * 查询Списание предметов列表
     * 
     * @param deductItem Списание предметов
     * @return Списание предметов集合
     */
    public List<DeductItem> selectDeductItemList(DeductItem deductItem);

    /**
     * ДобавитьСписание предметов
     * 
     * @param deductItem Списание предметов
     * @return Результат
     */
    public int insertDeductItem(DeductItem deductItem);

    /**
     * ИзменитьСписание предметов
     * 
     * @param deductItem Списание предметов
     * @return Результат
     */
    public int updateDeductItem(DeductItem deductItem);

    /**
     * 批量УдалитьСписание предметов
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteDeductItemByIds(String ids);

    /**
     * УдалитьСписание предметовИнформация
     * 
     * @param id Списание предметовID
     * @return Результат
     */
    public int deleteDeductItemById(Integer id);
}
