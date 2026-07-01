package com.gm.project.stat.stat_shop_item.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_remain.service.StatRemainServiceImpl;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import com.gm.project.stat.stat_shop_item.service.IStatShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 商城购买统计Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/stat_shop_item")
public class StatShopItemController extends BaseController
{
    private String prefix = "stat/stat_shop_item";

    @Autowired
    private IStatShopItemService statShopItemService;

    @GetMapping("to_stat_shop_item")
    public String toStatShopItem()
    {
        return prefix + "/stat_shop_item";
    }
    /**
     * 查询商城购买统计列表
     * 查询商城购买统计列表
     */
    @PostMapping("/stat_shop_item")
    @ResponseBody
    public TableDataInfo statShopItem(String groupName, Integer serverId, String channelNames, Integer FromSrc, Integer moneyType,String startDate, String endDate, Boolean isBlack)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器");
        }
        List<ShopItemBean> shopItemBeanList =  statShopItemService.statShopItem(groupName, serverId,channelNames,FromSrc,moneyType,startDate,endDate,isBlack);
        return  getDataTable(shopItemBeanList);
    }

}
