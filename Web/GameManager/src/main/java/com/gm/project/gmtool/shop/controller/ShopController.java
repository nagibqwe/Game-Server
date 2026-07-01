package com.gm.project.gmtool.shop.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.util.CodedUtil;
import com.gm.common.utils.StringUtils;
import com.gm.framework.web.page.PageDomain;

import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.shop.domain.ShopInfo;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.shop.domain.Shop;
import com.gm.project.gmtool.shop.service.IShopService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;


/**
 * 商城Controller
 * 
 * @author gm
 * @date 2021-09-23
 */
@Controller
@RequestMapping("/gmtool/shop")
public class ShopController extends BaseController
{
    private String prefix = "gmtool/shop";

    @Autowired
    private IShopService shopService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:shop:view")
    @GetMapping()
    public String shop()
    {
        return prefix + "/shop";
    }

    /**
     * 导出商城列表
     */
    @RequiresPermissions("gmtool:shop:export")
    @Log(title = "商城", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Shop shop)
    {
        List<Shop> list = shopService.selectShopList(shop);
        ExcelUtil<Shop> util = new ExcelUtil<Shop>(Shop.class);
        return util.exportExcel(list, "商城数据");
    }

    /**
     * 新增商城
     */
    @GetMapping("/add")
    public String add(String groupName, String serverId, ModelMap mmap)
    {
        mmap.put("groupName", groupName);
        mmap.put("serverId", serverId);
        return prefix + "/add";
    }

    /**
     * 新增保存商城
     */
    @Log(title = "商城", businessType = BusinessType.INSERT)
    @PostMapping("/addShop")
    @ResponseBody
    public AjaxResult addShop(String serverId,ShopInfo info)
    {
        if (StringUtils.isBlank(serverId)) {
            return AjaxResult.info("未选择服务器").put("ok",false);
        }
        //发送消息到GameServer
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
        }
        if (info.getId() > 110000 || info.getId() < 100000) {
            return AjaxResult.info("商品id配置错误，范围100000~1100000").put("ok",false);
        }

        HashMap result = GameServerRequestUtil.gmGetShopInfo(server, "0");
        HashMap dataMap = (HashMap) result.get("data");
        List<HashMap<String,Object>> shops = (List<HashMap<String, Object>>) dataMap.get("data");
        HashMap<String, Object> map = new HashMap<>();
        for (int i=0;i<shops.size();i++){
            map = shops.get(i);
            if (Integer.parseInt(map.get("id").toString()) == info.getId()){
                return AjaxResult.info("增加商品配置错误，重复的商品id").put("ok",false);
            }
        }

        String shopInfo = JsonUtils.toJSONString(info);
        return GameServerRequestUtil.gmSetShopInfo(server, shopInfo);
    }

    /**
     * 修改商城
     */
    @GetMapping("/editShop")
    public String editShop(String groupName, String serverId, String shopId, ModelMap mmap) throws InvocationTargetException, IllegalAccessException, JsonProcessingException {
        HashMap data = new HashMap();
        if (!"0".equals(shopId)) {
            if (StringUtils.isBlank(serverId)) {
                logger.error("未获取到服务器id");
                return "";
            }
            //发送消息到GameServer
            TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
            if (server == null) {
                logger.error("对应id的服务器不存在：" + serverId);
                return "";
            }
            HashMap result = GameServerRequestUtil.gmGetShopInfo(server, shopId);
            data  = (HashMap) result.get("data");
        }
        ShopInfo shopInfo = new ShopInfo();
        BeanUtils.populate(shopInfo, (Map<String, ? extends Object>) data.get("data"));//将hashMap集合转成java对象
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(shopInfo);//将java对象转成json字符串
        String base= CodedUtil.encodeBase64(json);//将json字符串用Base64编码，防止到页面将特殊字符转义了
        mmap.put("id", shopId);
        mmap.put("date", new Date());
        mmap.put("shopInfo", base);
        mmap.put("groupName", groupName);
        mmap.put("serverId", serverId);
        return prefix + "/edit";
    }

    /**
     * 修改保存商城
     */
    @Log(title = "商城", businessType = BusinessType.UPDATE)
    @PostMapping("/updateShop")
    @ResponseBody
    public AjaxResult editSave(String serverId,ShopInfo info)
    {

        if (StringUtils.isBlank(serverId)) {
            return AjaxResult.info("未选择服务器").put("ok",false);
        }
        //发送消息到GameServer
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
        }
        String shopInfo = JsonUtils.toJSONString(info);
        return GameServerRequestUtil.gmSetShopInfo(server, shopInfo);
    }


    /**
     * 查询商城列表
     */
    @PostMapping("/list")
    @ResponseBody
    public Object list(PageDomain pageDomain,String serverId, String shopId)
    {
        int page = pageDomain.getPageNum();
        int rows = pageDomain.getPageSize();
        if (StringUtils.isBlank(serverId)) {
            return getDataTableErrorMsg("未选择服务器");
        }
        //发送消息到GameServer
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return getDataTableErrorMsg("服务器连接信息获取失败");
        }
        HashMap result = GameServerRequestUtil.gmGetShopInfo(server, "0");
        if (!Boolean.valueOf(result.get("ok").toString())) {
            return getDataTableErrorMsg(result.get("msg").toString());
        }
        HashMap dataMap = (HashMap) result.get("data");
        List<HashMap<String,Object>> shops = (List<HashMap<String, Object>>) dataMap.get("data");
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        if (Integer.parseInt(shopId) == 0){
            toIndex = rows*page >= shops.size() ? shops.size() : rows*page;
            return AjaxResult.success().put("total",shops.size()).put("rows",shops.subList(fromIndex,toIndex));
        }else {
            List<HashMap<String,Object>> shopList = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            for (int i=0;i<shops.size();i++){
                map = shops.get(i);
                if (Integer.parseInt(map.get("shopid").toString()) == Integer.parseInt(shopId)){
                    shopList.add(map);
                }
            }
            toIndex = rows*page >= shopList.size() ? shopList.size() : rows*page;
            return AjaxResult.success().put("total",shopList.size()).put("rows",shopList.subList(fromIndex,toIndex));
        }
    }

    /**
     * 删除商城配置
     * @param serverId
     * @param shopId
     * @return
     */
    @PostMapping("/deleteShop")
    @ResponseBody
    public Object deleteShop(String serverId, String shopId) {
        if (StringUtils.isBlank(serverId)) {
            return AjaxResult.info("未选择服务器").put("ok",false);
        }
        //发送消息到GameServer
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
        }
        return GameServerRequestUtil.gmDeleteShopInfo(server, shopId);
    }
}
