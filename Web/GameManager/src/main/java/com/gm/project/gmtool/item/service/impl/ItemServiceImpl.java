package com.gm.project.gmtool.item.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.item.mapper.ItemMapper;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.item.service.IItemService;
import com.gm.common.utils.text.Convert;

/**
 * 道具装备Service业务层处理
 * 
 * @author gm
 * @date 2021-08-31
 */
@Service
public class ItemServiceImpl implements IItemService 
{
    @Autowired
    private ItemMapper itemMapper;

    /**
     * 查询道具装备
     * 
     * @param itemId 道具装备ID
     * @return 道具装备
     */
    @Override
    public Item selectItemById(Integer itemId)
    {
        return itemMapper.selectItemById(itemId);
    }

    /**
     * 查询道具装备列表
     * 
     * @param item 道具装备
     * @return 道具装备
     */
    @Override
    public List<Item> selectItemList(Item item)
    {
        return itemMapper.selectItemList(item);
    }

    /**
     * 新增道具装备
     * 
     * @param item 道具装备
     * @return 结果
     */
    @Override
    public int insertItem(Item item)
    {
        return itemMapper.insertItem(item);
    }

    /**
     * 修改道具装备
     * 
     * @param item 道具装备
     * @return 结果
     */
    @Override
    public int updateItem(Item item)
    {
        return itemMapper.updateItem(item);
    }

    /**
     * 删除道具装备对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItemByIds(String ids)
    {
        return itemMapper.deleteItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除道具装备信息
     * 
     * @param itemId 道具装备ID
     * @return 结果
     */
    @Override
    public int deleteItemById(Integer itemId)
    {
        return itemMapper.deleteItemById(itemId);
    }

    /**
     * 清空道具装备数据
     * @param tableName
     */
    @Override
    public void clearItem(String tableName) {
        itemMapper.clearItem(tableName);
    }
}
