package com.gm.project.gmtool.shop.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.shop.mapper.ShopMapper;
import com.gm.project.gmtool.shop.domain.Shop;
import com.gm.project.gmtool.shop.service.IShopService;
import com.gm.common.utils.text.Convert;

/**
 * МагазинService业务层处理
 * 
 * @author gm
 * @date 2021-09-23
 */
@Service
public class ShopServiceImpl implements IShopService 
{
    @Autowired
    private ShopMapper shopMapper;

    /**
     * 查询Магазин
     * 
     * @param ID МагазинID
     * @return Магазин
     */
    @Override
    public Shop selectShopById(Integer ID)
    {
        return shopMapper.selectShopById(ID);
    }

    /**
     * 查询Магазин列表
     * 
     * @param shop Магазин
     * @return Магазин
     */
    @Override
    public List<Shop> selectShopList(Shop shop)
    {
        return shopMapper.selectShopList(shop);
    }

    /**
     * ДобавитьМагазин
     * 
     * @param shop Магазин
     * @return Результат
     */
    @Override
    public int insertShop(Shop shop)
    {
        return shopMapper.insertShop(shop);
    }

    /**
     * ИзменитьМагазин
     * 
     * @param shop Магазин
     * @return Результат
     */
    @Override
    public int updateShop(Shop shop)
    {
        return shopMapper.updateShop(shop);
    }

    /**
     * УдалитьМагазин对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteShopByIds(String ids)
    {
        return shopMapper.deleteShopByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьМагазинИнформация
     * 
     * @param ID МагазинID
     * @return Результат
     */
    @Override
    public int deleteShopById(Integer ID)
    {
        return shopMapper.deleteShopById(ID);
    }
}
