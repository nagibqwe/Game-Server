package com.gm.project.gmtool.item.service;

import java.util.List;
import com.gm.project.gmtool.item.domain.Item;

/**
 * 道具装备Service接口
 * 
 * @author gm
 * @date 2021-08-31
 */
public interface IItemService 
{
    /**
     * 查询道具装备
     * 
     * @param itemId 道具装备ID
     * @return 道具装备
     */
    public Item selectItemById(Integer itemId);

    /**
     * 查询道具装备列表
     * 
     * @param item 道具装备
     * @return 道具装备集合
     */
    public List<Item> selectItemList(Item item);

    /**
     * 新增道具装备
     * 
     * @param item 道具装备
     * @return 结果
     */
    public int insertItem(Item item);

    /**
     * 修改道具装备
     * 
     * @param item 道具装备
     * @return 结果
     */
    public int updateItem(Item item);

    /**
     * 批量删除道具装备
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteItemByIds(String ids);

    /**
     * 删除道具装备信息
     * 
     * @param itemId 道具装备ID
     * @return 结果
     */
    public int deleteItemById(Integer itemId);

    /**
     * 清空道具装备数据
     * @param tableName
     */
    public void clearItem(String tableName);
}
