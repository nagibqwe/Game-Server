package com.gm.project.gmtool.item.service;

import java.util.List;
import com.gm.project.gmtool.item.domain.Item;

/**
 * Предметы и экипировкаService接口
 * 
 * @author gm
 * @date 2021-08-31
 */
public interface IItemService 
{
    /**
     * 查询Предметы и экипировка
     * 
     * @param itemId Предметы и экипировкаID
     * @return Предметы и экипировка
     */
    public Item selectItemById(Integer itemId);

    /**
     * 查询Предметы и экипировка列表
     * 
     * @param item Предметы и экипировка
     * @return Предметы и экипировка集合
     */
    public List<Item> selectItemList(Item item);

    /**
     * ДобавитьПредметы и экипировка
     * 
     * @param item Предметы и экипировка
     * @return Результат
     */
    public int insertItem(Item item);

    /**
     * ИзменитьПредметы и экипировка
     * 
     * @param item Предметы и экипировка
     * @return Результат
     */
    public int updateItem(Item item);

    /**
     * 批量УдалитьПредметы и экипировка
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteItemByIds(String ids);

    /**
     * УдалитьПредметы и экипировкаИнформация
     * 
     * @param itemId Предметы и экипировкаID
     * @return Результат
     */
    public int deleteItemById(Integer itemId);

    /**
     * 清空Предметы и экипировкаДанные
     * @param tableName
     */
    public void clearItem(String tableName);
}
