package com.gm.project.gmtool.shop.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.shop.mapper.ShopMapper;
import com.gm.project.gmtool.shop.domain.Shop;
import com.gm.project.gmtool.shop.service.IShopService;
import com.gm.common.utils.text.Convert;

/**
 * 商城Service业务层处理
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
     * 查询商城
     * 
     * @param ID 商城ID
     * @return 商城
     */
    @Override
    public Shop selectShopById(Integer ID)
    {
        return shopMapper.selectShopById(ID);
    }

    /**
     * 查询商城列表
     * 
     * @param shop 商城
     * @return 商城
     */
    @Override
    public List<Shop> selectShopList(Shop shop)
    {
        return shopMapper.selectShopList(shop);
    }

    /**
     * 新增商城
     * 
     * @param shop 商城
     * @return 结果
     */
    @Override
    public int insertShop(Shop shop)
    {
        return shopMapper.insertShop(shop);
    }

    /**
     * 修改商城
     * 
     * @param shop 商城
     * @return 结果
     */
    @Override
    public int updateShop(Shop shop)
    {
        return shopMapper.updateShop(shop);
    }

    /**
     * 删除商城对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteShopByIds(String ids)
    {
        return shopMapper.deleteShopByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商城信息
     * 
     * @param ID 商城ID
     * @return 结果
     */
    @Override
    public int deleteShopById(Integer ID)
    {
        return shopMapper.deleteShopById(ID);
    }
}
