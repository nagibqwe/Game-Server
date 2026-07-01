package com.gm.project.gmtool.deductItem.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.deductItem.mapper.DeductItemMapper;
import com.gm.project.gmtool.deductItem.domain.DeductItem;
import com.gm.project.gmtool.deductItem.service.IDeductItemService;
import com.gm.common.utils.text.Convert;

/**
 * 道具扣除Service业务层处理
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
     * 查询道具扣除
     * 
     * @param id 道具扣除ID
     * @return 道具扣除
     */
    @Override
    public DeductItem selectDeductItemById(Integer id)
    {
        return deductItemMapper.selectDeductItemById(id);
    }

    /**
     * 查询道具扣除列表
     * 
     * @param deductItem 道具扣除
     * @return 道具扣除
     */
    @Override
    public List<DeductItem> selectDeductItemList(DeductItem deductItem)
    {
        return deductItemMapper.selectDeductItemList(deductItem);
    }

    /**
     * 新增道具扣除
     * 
     * @param deductItem 道具扣除
     * @return 结果
     */
    @Override
    public int insertDeductItem(DeductItem deductItem)
    {
        return deductItemMapper.insertDeductItem(deductItem);
    }

    /**
     * 修改道具扣除
     * 
     * @param deductItem 道具扣除
     * @return 结果
     */
    @Override
    public int updateDeductItem(DeductItem deductItem)
    {
        return deductItemMapper.updateDeductItem(deductItem);
    }

    /**
     * 删除道具扣除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDeductItemByIds(String ids)
    {
        return deductItemMapper.deleteDeductItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除道具扣除信息
     * 
     * @param id 道具扣除ID
     * @return 结果
     */
    @Override
    public int deleteDeductItemById(Integer id)
    {
        return deductItemMapper.deleteDeductItemById(id);
    }
}
