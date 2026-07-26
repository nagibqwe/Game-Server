package com.gm.project.gmtool.deductItem.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.deductItem.mapper.DeductItemMapper;
import com.gm.project.gmtool.deductItem.domain.DeductItem;
import com.gm.project.gmtool.deductItem.service.IDeductItemService;
import com.gm.common.utils.text.Convert;

/**
 * Списание предметовService业务层处理
 * 
 * @author gm
 * @date 2021-10-30
 */
@Service
public class DeductItemServiceImpl implements IDeductItemService 
{
    @Autowired
    private DeductItemMapper deductItemMapper;

    /**
     * 查询Списание предметов
     * 
     * @param id Списание предметовID
     * @return Списание предметов
     */
    @Override
    public DeductItem selectDeductItemById(Integer id)
    {
        return deductItemMapper.selectDeductItemById(id);
    }

    /**
     * 查询Списание предметов列表
     * 
     * @param deductItem Списание предметов
     * @return Списание предметов
     */
    @Override
    public List<DeductItem> selectDeductItemList(DeductItem deductItem)
    {
        return deductItemMapper.selectDeductItemList(deductItem);
    }

    /**
     * ДобавитьСписание предметов
     * 
     * @param deductItem Списание предметов
     * @return Результат
     */
    @Override
    public int insertDeductItem(DeductItem deductItem)
    {
        return deductItemMapper.insertDeductItem(deductItem);
    }

    /**
     * ИзменитьСписание предметов
     * 
     * @param deductItem Списание предметов
     * @return Результат
     */
    @Override
    public int updateDeductItem(DeductItem deductItem)
    {
        return deductItemMapper.updateDeductItem(deductItem);
    }

    /**
     * УдалитьСписание предметов对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteDeductItemByIds(String ids)
    {
        return deductItemMapper.deleteDeductItemByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьСписание предметовИнформация
     * 
     * @param id Списание предметовID
     * @return Результат
     */
    @Override
    public int deleteDeductItemById(Integer id)
    {
        return deductItemMapper.deleteDeductItemById(id);
    }
}
