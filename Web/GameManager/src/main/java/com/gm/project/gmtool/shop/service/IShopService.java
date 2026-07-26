package com.gm.project.gmtool.shop.service;

import java.util.List;
import com.gm.project.gmtool.shop.domain.Shop;

/**
 * МагазинService接口
 * 
 * @author gm
 * @date 2021-09-23
 */
public interface IShopService 
{
    /**
     * 查询Магазин
     * 
     * @param ID МагазинID
     * @return Магазин
     */
    public Shop selectShopById(Integer ID);

    /**
     * 查询Магазин列表
     * 
     * @param shop Магазин
     * @return Магазин集合
     */
    public List<Shop> selectShopList(Shop shop);

    /**
     * ДобавитьМагазин
     * 
     * @param shop Магазин
     * @return Результат
     */
    public int insertShop(Shop shop);

    /**
     * ИзменитьМагазин
     * 
     * @param shop Магазин
     * @return Результат
     */
    public int updateShop(Shop shop);

    /**
     * 批量УдалитьМагазин
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteShopByIds(String ids);

    /**
     * УдалитьМагазинИнформация
     * 
     * @param ID МагазинID
     * @return Результат
     */
    public int deleteShopById(Integer ID);
}
