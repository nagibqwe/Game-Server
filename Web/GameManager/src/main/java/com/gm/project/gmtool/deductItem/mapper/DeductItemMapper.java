package com.gm.project.gmtool.deductItem.mapper;

import java.util.List;
import com.gm.project.gmtool.deductItem.domain.DeductItem;

/**
 * Списание предметовMapper接口
 * 
 * @author gm
 * @date 2021-10-30
 */
public interface DeductItemMapper 
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
     * УдалитьСписание предметов
     * 
     * @param id Списание предметовID
     * @return Результат
     */
    public int deleteDeductItemById(Integer id);

    /**
     * 批量УдалитьСписание предметов
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteDeductItemByIds(String[] ids);
}
