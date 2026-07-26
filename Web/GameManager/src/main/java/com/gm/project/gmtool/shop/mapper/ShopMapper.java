package com.gm.project.gmtool.shop.mapper;

import java.util.List;
import com.gm.project.gmtool.shop.domain.Shop;

/**
 * МагазинMapper接口
 * 
 * @author gm
 * @date 2021-09-23
 */
public interface ShopMapper 
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
     * УдалитьМагазин
     * 
     * @param ID МагазинID
     * @return Результат
     */
    public int deleteShopById(Integer ID);

    /**
     * 批量УдалитьМагазин
     * 
     * @param IDs 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteShopByIds(String[] IDs);
}
