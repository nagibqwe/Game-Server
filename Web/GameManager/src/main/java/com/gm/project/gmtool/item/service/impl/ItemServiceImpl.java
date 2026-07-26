package com.gm.project.gmtool.item.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.item.mapper.ItemMapper;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.item.service.IItemService;
import com.gm.common.utils.text.Convert;

/**
 * Предметы и экипировкаService业务层处理
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
     * 查询Предметы и экипировка
     * 
     * @param itemId Предметы и экипировкаID
     * @return Предметы и экипировка
     */
    @Override
    public Item selectItemById(Integer itemId)
    {
        return itemMapper.selectItemById(itemId);
    }

    /**
     * 查询Предметы и экипировка列表
     * 
     * @param item Предметы и экипировка
     * @return Предметы и экипировка
     */
    @Override
    public List<Item> selectItemList(Item item)
    {
        return itemMapper.selectItemList(item);
    }

    /**
     * ДобавитьПредметы и экипировка
     * 
     * @param item Предметы и экипировка
     * @return Результат
     */
    @Override
    public int insertItem(Item item)
    {
        return itemMapper.insertItem(item);
    }

    /**
     * ИзменитьПредметы и экипировка
     * 
     * @param item Предметы и экипировка
     * @return Результат
     */
    @Override
    public int updateItem(Item item)
    {
        return itemMapper.updateItem(item);
    }

    /**
     * УдалитьПредметы и экипировка对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteItemByIds(String ids)
    {
        return itemMapper.deleteItemByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьПредметы и экипировкаИнформация
     * 
     * @param itemId Предметы и экипировкаID
     * @return Результат
     */
    @Override
    public int deleteItemById(Integer itemId)
    {
        return itemMapper.deleteItemById(itemId);
    }

    /**
     * 清空Предметы и экипировкаДанные
     * @param tableName
     */
    @Override
    public void clearItem(String tableName) {
        itemMapper.clearItem(tableName);
    }
}
