package com.gm.project.gmtool.deductItem.service;

import java.util.List;
import com.gm.project.gmtool.deductItem.domain.DeductItem;

/**
 * 道具扣除Service接口
 * 
 * @author gm
 * @date 2021-10-30
 */
public interface IDeductItemService 
{
    /**
     * 查询道具扣除
     * 
     * @param id 道具扣除ID
     * @return 道具扣除
     */
    public DeductItem selectDeductItemById(Integer id);

    /**
     * 查询道具扣除列表
     * 
     * @param deductItem 道具扣除
     * @return 道具扣除集合
     */
    public List<DeductItem> selectDeductItemList(DeductItem deductItem);

    /**
     * 新增道具扣除
     * 
     * @param deductItem 道具扣除
     * @return 结果
     */
    public int insertDeductItem(DeductItem deductItem);

    /**
     * 修改道具扣除
     * 
     * @param deductItem 道具扣除
     * @return 结果
     */
    public int updateDeductItem(DeductItem deductItem);

    /**
     * 批量删除道具扣除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDeductItemByIds(String ids);

    /**
     * 删除道具扣除信息
     * 
     * @param id 道具扣除ID
     * @return 结果
     */
    public int deleteDeductItemById(Integer id);
}
