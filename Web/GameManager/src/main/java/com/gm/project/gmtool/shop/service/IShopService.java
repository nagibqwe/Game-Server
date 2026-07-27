package com.gm.project.gmtool.shop.service;

import java.util.List;
import com.gm.project.gmtool.shop.domain.Shop;

/**
 * 商城Service接口
 * 
 * @author gm
 * @date 2021-09-23
 */
public interface IShopService 
{
    /**
     * 查询商城
     * 
     * @param ID 商城ID
     * @return 商城
     */
    public Shop selectShopById(Integer ID);

    /**
     * 查询商城列表
     * 
     * @param shop 商城
     * @return 商城集合
     */
    public List<Shop> selectShopList(Shop shop);

    /**
     * 新增商城
     * 
     * @param shop 商城
     * @return 结果
     */
    public int insertShop(Shop shop);

    /**
     * 修改商城
     * 
     * @param shop 商城
     * @return 结果
     */
    public int updateShop(Shop shop);

    /**
     * 批量删除商城
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteShopByIds(String ids);

    /**
     * 删除商城信息
     * 
     * @param ID 商城ID
     * @return 结果
     */
    public int deleteShopById(Integer ID);
}
