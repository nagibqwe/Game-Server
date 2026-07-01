package com.gm.project.gmtool.rechargeItem.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.page.PageDomain;
import com.gm.project.gmtool.manager.RechargeItemManager;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItemLogGrid;
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;
import com.gm.project.gmtool.rechargeItemLog.service.IRechargeItemLogService;
import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.utils.*;
import net.sf.json.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;
import com.gm.project.gmtool.rechargeItem.service.IRechargeItemService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 充值配置Controller
 * 
 * @author gm
 * @date 2021-08-25
 */
@Controller
@RequestMapping("/gmtool/rechargeItem")
public class RechargeItemController extends BaseController
{
    private String prefix = "gmtool/rechargeItem";

    @Autowired
    private IRechargeItemService rechargeItemService;
    @Autowired
    private ISelectGroupService selectGroupService;
    @Autowired
    private IRechargeItemLogService rechargeItemLogService;
    @Autowired
    private GameManagerConfig gameManagerConfig;

    @RequiresPermissions("gmtool:rechargeItem:view")
    @GetMapping()
    public String rechargeItem()
    {
        return prefix + "/rechargeItem";
    }

    /**
     * 查询充值配置列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RechargeItem rechargeItem)
    {
        startPage();
        List<RechargeItem> list = rechargeItemService.selectRechargeItemList(rechargeItem);
        return getDataTable(list);
    }

    /**
     * 导出充值配置列表
     */
    @Log(title = "充值配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RechargeItem rechargeItem)
    {
        List<RechargeItem> list = rechargeItemService.selectRechargeItemList(rechargeItem);
        ExcelUtil<RechargeItem> util = new ExcelUtil<RechargeItem>(RechargeItem.class);
        return util.exportExcel(list, "充值配置数据");
    }

    /**
     * 新增充值配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存充值配置
     */
    @Log(title = "充值配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RechargeItem rechargeItem)
    {
        return toAjax(rechargeItemService.insertRechargeItem(rechargeItem));
    }

    /**
     * 修改充值配置
     */
    @GetMapping("/edit")
    public String edit(Integer goodsId, String tableName,ModelMap mmap)
    {
        RechargeItem rechargeItem = rechargeItemService.selectRechargeItemById(goodsId);
        mmap.put("tableName",tableName);
        mmap.put("rechargeItem", rechargeItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存充值配置
     */
    @Log(title = "充值配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RechargeItem rechargeItem)
    {
        return toAjax(rechargeItemService.updateRechargeItem(rechargeItem));
    }

    /**
     * 删除充值配置
     */
    @Log(title = "充值配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(rechargeItemService.deleteRechargeItemByIds(ids));
    }

    /**
     * 通知游戏服刷新充值商城商品列表
     */
    public Object sendRechargeInfos() {
        return RechargeItemManager.getInstance().sendRechargeInfos();
    }

    /**
     * 通知游戏服更新充值商城某条商品
     */
    @PostMapping( "/updateRechargeItem")
    @ResponseBody
    public String updateRechargeItem(RechargeItem rechargeItem) {
        //更新GM后台本地数据库数据
        int count = rechargeItemService.updateRechargeItem(rechargeItem);
        if(count<=0){
            return "更新GM后台充值商城配置失败";
        }

        RechargeItemManager.getInstance().getRechargeItemMap().put(rechargeItem.getGoodsId(), rechargeItem);
        RechargeItemManager.getInstance().getRechargeItemInfoMap().put(rechargeItem.getGoodsId(), RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));

        StringBuilder sb = new StringBuilder();
        //通知API服务器更新某一条的充值信息
        String rechargeStr = JsonUtils.toJSONString(RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("rechargeStr", rechargeStr);
        String httpResult = HttpConnectionUtils.post(gameManagerConfig.getAPIServerUrl()+"/rechargeItem/updateRechargeItem", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("/n");

        //通知游戏服更新全部的充值信息
        List<TServer> servers = selectGroupService.selectServerList("", 1, "0,1");
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();

        String allRechargeStr = RechargeItemManager.getInstance().getRechargeStr();
        String md5 = MD5Util.MD5(allRechargeStr);

        for(TServer server : servers){
            int serverId = server.getServerId();
            try {
                HashMap resultMap = GameServerRequestUtil.gmRefreshRechargeItemInfos(server, allRechargeStr, md5, 15*1000);
                if (!Boolean.valueOf(resultMap.get("ok").toString())) {
                    serverFailedList.add(serverId);
                    logger.error(serverId + "服,充值配置[" + rechargeItem.getGoodsId() + "]更新失败！操作结果：" + resultMap.get("data").toString());
                } else {
                    serverSuccessList.add(serverId);
                }
            }catch (Exception e){
                logger.error(serverId + "服充值配置更新失败！error："+e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("/n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("/n");
        GMLogUtil.log("修改充值配置，修改的ID："+rechargeItem.getGoodsId());
        return sb.toString();
    }

    /**
     * 通知游戏服删除充值商城某条商品
     */
    @PostMapping( "/deleteRechargeItem")
    @ResponseBody
    public Object deleteRechargeItem(String id) {
        //删除GM后台本地数据库数据
        int count = rechargeItemService.deleteRechargeItemByIds(id);
        if(count<=0){
            return AjaxResult.info("删除GM后台充值商城配置失败").put("ok",false);
        }

        RechargeItemManager.getInstance().getRechargeItemMap().remove(Integer.parseInt(id));
        RechargeItemManager.getInstance().getRechargeItemInfoMap().remove(Integer.parseInt(id));

        StringBuilder sb = new StringBuilder();
        //通知API服务器删除某一条的充值信息
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        String httpResult = HttpConnectionUtils.post(gameManagerConfig.getAPIServerUrl()+"/rechargeItem/deleteRechargeItem", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("/n");

        //通知游戏服删除某一条的充值信息
        List<TServer> servers = selectGroupService.selectServerList("", 1, "0,1");
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(TServer server : servers){
            int serverId = server.getServerId();
            HashMap resultMap = GameServerRequestUtil.gmDeleteRechargeInfo(server, id);
            if (!Boolean.valueOf(resultMap.get("ok").toString())) {
                serverFailedList.add(serverId);
                logger.error(serverId + "服,充值配置[" + id + "]删除失败！操作结果：" + resultMap.get("data").toString());
            } else {
                serverSuccessList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("/n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("/n");
        return AjaxResult.info(sb.toString()).put("ok",true);
    }
    /**
     *普通充值查询
     * @return
     */
    @PostMapping( "/queryRechargeItem")
    @ResponseBody
    public Object queryRechargeItem() {
        startPage();
        List<RechargeItem> list = rechargeItemService.selectRechargeItemBygoodsPayChannel();
        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return getDataTable(list).put("md5",md5).put("ok",true);
    }

    /**
     * 第三方充值配置查询
     * @return
     */
    @PostMapping( "/queryRechargeItem3")
    @ResponseBody
    public Object queryRechargeItem3() {
        startPage();
        List<RechargeItem> list = rechargeItemService.selectRechargeItemBygoodsPayChannel3();
        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return getDataTable(list).put("md5",md5).put("ok",true);
    }

    @PostMapping( "/saveRechargeItem")
    @ResponseBody
    public Object saveRechargeItem(HttpServletRequest request,RechargeItem rechargeItemParam) {
        RechargeItem rechargeItem = new RechargeItem();

        String rechargeTableName = request.getParameter("rechargeTableName");
        int goods_id = rechargeItemParam.getGoodsId();
        rechargeItem.setGoodsId(goods_id);

        if (!(rechargeItemParam.getGoodsSystemCfgId()==null)){
            rechargeItem.setGoodsSystemCfgId(rechargeItemParam.getGoodsSystemCfgId());
        }

        String goods_name = rechargeItemParam.getGoodsName();
        rechargeItem.setGoodsName(goods_name);

        String goods_pay_channel = rechargeItemParam.getGoodsPayChannel();
        rechargeItem.setGoodsPayChannel(goods_pay_channel);

        if (!(rechargeItemParam.getGoodsPayType()==null)){
            rechargeItem.setGoodsPayType(rechargeItemParam.getGoodsPayType());
        }

        if (!(rechargeItemParam.getGoodsType()==null)){
            rechargeItem.setGoodsType(rechargeItemParam.getGoodsType());
        }

        if (!(rechargeItemParam.getGoodsSubtype()==null)){
            rechargeItem.setGoodsSubtype(rechargeItemParam.getGoodsSubtype());
        }

        if (!(rechargeItemParam.getGoodsLimit()==null)){
            rechargeItem.setGoodsLimit(rechargeItemParam.getGoodsLimit());
        }

        if (!(rechargeItemParam.getGoodsIcon()==null)){
            rechargeItem.setGoodsIcon(rechargeItemParam.getGoodsIcon());
        }
        String goods_url = rechargeItemParam.getGoodsurl();
        rechargeItem.setGoodsurl(goods_url);
        String goods_price = rechargeItemParam.getGoodsPrice();
        rechargeItem.setGoodsPrice(goods_price);
        String goods_price_point = rechargeItemParam.getGoodsPricePoint();
        rechargeItem.setGoodsPricePoint(goods_price_point);
        String goods_show_price = rechargeItemParam.getGoodsShowPrice();
        rechargeItem.setGoodsShowPrice(goods_show_price);
        String goods_reward = rechargeItemParam.getGoodsReward();
        rechargeItem.setGoodsReward(goods_reward);
        String goods_multiple = rechargeItemParam.getGoodsMultiple();
        rechargeItem.setGoodsMultiple(goods_multiple);
        String goods_extra_reward = rechargeItemParam.getGoodsExtraReward();
        rechargeItem.setGoodsExtraReward(goods_extra_reward);

        if (!(rechargeItemParam.getGoodsExtraRewardLimit()==null)){
            rechargeItem.setGoodsExtraRewardLimit(rechargeItemParam.getGoodsExtraRewardLimit());
        }
        if (!(rechargeItemParam.getIsTotalRecharge()==null)){
            rechargeItem.setIsTotalRecharge(rechargeItemParam.getIsTotalRecharge());
        }
        if (!(rechargeItemParam.getTotalVipPower()==null)){
            rechargeItem.setTotalVipPower(rechargeItemParam.getTotalVipPower());
        }
        //获取之前旧充值表的MD5码
        String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

        //更新数据(数据库和APIServer)
        String updateResultStr = updateRechargeItem(rechargeItem);
        //重新加载内存数据
        RechargeItemManager.getInstance().load();

        //记录用户操作日志
        GMLogUtil.log(rechargeItem.toString());

        //MD5码更新
        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        String content = "";
        RechargeItemLog rechargeItemLog = new RechargeItemLog();
        if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
            if (rechargeTableName.equals("rechargeItem")){//普通充值
                rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel();
                content = JsonUtils.toJSONString(rechargeItemList);
                writeRechargeItemLog(rechargeItemLog,content,rechargeTableName);

            }else {//第三方充值
                rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel3();
                content = JsonUtils.toJSONString(rechargeItemList);
                writeRechargeItemLog(rechargeItemLog,content,rechargeTableName);
            }
        }

        return AjaxResult.info(updateResultStr).put("ok",true).put("md5",md5);
    }

    private void writeRechargeItemLog(RechargeItemLog rechargeItemLog,String content,String tableName){
        rechargeItemLog.setUserId(Integer.parseInt(ShiroUtils.getUserId().toString()));
        rechargeItemLog.setIp(ShiroUtils.getIp());
        rechargeItemLog.setUserName(ShiroUtils.getLoginName());
        rechargeItemLog.setTime(System.currentTimeMillis());
        rechargeItemLog.setTableName(tableName);
        rechargeItemLog.setContent(content);
        rechargeItemLogService.insertRechargeItemLog(rechargeItemLog);
    }

    @PostMapping( "/loadRechargeItem")
    @ResponseBody
    public Object loadRechargeItem(HttpServletRequest request, MultipartFile rechargeItemFile){
        if (rechargeItemFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = rechargeItemFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[20];
        try {
            InputStream in = rechargeItemFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("rechargeItem")) {
                return AjaxResult.info("not rechargeItem config file").put("ok",false);
            }

            //获取之前旧充值表的MD5码
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size()>0) {//普通充值只会替换普通充值档位
                //先删除数据库中普通充值数据
                int delCount = rechargeItemService.deleteRechargeItemBygoodsPayChannel();
                List<RechargeItem> result = new ArrayList<>();
                for (RechargeItem rechargeItem:items){
                    rechargeItemService.insertRechargeItem(rechargeItem);
                    result.add(rechargeItem);
                }
                if(result.size()>0){
                    RechargeItemManager.getInstance().load();
                    Object resultStr=RechargeItemManager.getInstance().sendRechargeInfos();
                    //MD5码更新
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    GMLogUtil.log("导入普通充值配置数据成功，MD5:"+md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    RechargeItemLog rechargeItemLog = new RechargeItemLog();
                    if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
                        rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel();
                        content = JsonUtils.toJSONString(rechargeItemList);
                        writeRechargeItemLog(rechargeItemLog,content,"rechargeItem");
                    }
                    return AjaxResult.info("Reload Ok, saved " + result.size() + " record!\n"+resultStr).put("ok",true);
                }
            }
            return AjaxResult.info("Reload rechargeItem file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    @PostMapping( "/loadOtherRechargeItem")
    @ResponseBody
    public Object loadOtherRechargeItem(HttpServletRequest request,MultipartFile otherRechargeItemFile){
        if (otherRechargeItemFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = otherRechargeItemFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[20];
        try {
            InputStream in = otherRechargeItemFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("otherRechargeItem")) {
                return AjaxResult.info("not rechargeItem config file").put("ok",false);
            }
            //获取之前旧充值表的MD5码
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size()>0) {
                //先删除数据库中第三方充值数据
                int delCount = rechargeItemService.deleteRechargeItemBygoodsPayChannel3();

                List<RechargeItem> result = new ArrayList<>();
                for (RechargeItem rechargeItem:items){
                    rechargeItemService.insertRechargeItem(rechargeItem);
                    result.add(rechargeItem);
                }
                if(result.size()>0){
                    RechargeItemManager.getInstance().load();
                    Object resultStr=RechargeItemManager.getInstance().sendRechargeInfos();
                    //MD5码更新
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    GMLogUtil.log("导入第三方充值配置数据成功，MD5:"+md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    RechargeItemLog rechargeItemLog = new RechargeItemLog();
                    if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
                        rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel3();
                        content = JsonUtils.toJSONString(rechargeItemList);
                        writeRechargeItemLog(rechargeItemLog,content,"otherRechargeItem");
                    }
                    return AjaxResult.info("Reload Ok, saved " + result.size() + " record!\n"+resultStr).put("ok",true);
                }
            }
            return AjaxResult.info("Reload failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }
    private void parseRechargeExcel(List<RechargeItem> items, int[] itemInfoPos, XSSFSheet sheet) {
        XSSFRow row = sheet.getRow(1);
        int cellNum = row.getLastCellNum();
        int rowCount = 0;
        int columnCount = 0;
        try {
            for (int i = 0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("goods_id")) {
                    itemInfoPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_system_cfg_id")) {
                    itemInfoPos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_name")) {
                    itemInfoPos[3] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_pay_channel")) {
                    itemInfoPos[4] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_pay_type")) {
                    itemInfoPos[5] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_type")) {
                    itemInfoPos[6] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_subtype")) {
                    itemInfoPos[7] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_limit")) {
                    itemInfoPos[8] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_icon")) {
                    itemInfoPos[9] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_url")) {
                    itemInfoPos[10] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_price")) {
                    itemInfoPos[11] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_price_point")) {
                    itemInfoPos[12] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_show_price")) {
                    itemInfoPos[13] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_reward")) {
                    itemInfoPos[14] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_multiple")) {
                    itemInfoPos[15] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_extra_reward")) {
                    itemInfoPos[16] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_extra_reward_limit")) {
                    itemInfoPos[17] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("isTotalRecharge")) {
                    itemInfoPos[18] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("totalVipPower")) {
                    itemInfoPos[19] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                rowCount = j;
                XSSFRow dataRow = sheet.getRow(j);

                if (dataRow.getCell(itemInfoPos[0]) == null || dataRow.getCell(itemInfoPos[0]).toString().equals("")) {
                    break;
                }
                columnCount = 0;
                RechargeItem item = new RechargeItem();
                item.setGoodsId((int) Float.parseFloat(dataRow.getCell(itemInfoPos[0]).toString()));
                item.setGoodsSystemCfgId((int) Float.parseFloat(dataRow.getCell(itemInfoPos[1]).toString()));
                item.setGoodsName(dataRow.getCell(itemInfoPos[3]).toString());
                if (dataRow.getCell(itemInfoPos[4]) == null) {
                    item.setGoodsPayChannel("");
                } else {
                    item.setGoodsPayChannel(dataRow.getCell(itemInfoPos[4]).toString());
                }
                if (dataRow.getCell(itemInfoPos[5]) == null || dataRow.getCell(itemInfoPos[5]).toString().equals("")) {
                    item.setGoodsPayType(0);
                } else {
                    item.setGoodsPayType((int) Float.parseFloat(dataRow.getCell(itemInfoPos[5]).toString()));
                }
                item.setGoodsType((int) Float.parseFloat(dataRow.getCell(itemInfoPos[6]).toString()));
                if (dataRow.getCell(itemInfoPos[7]) == null || dataRow.getCell(itemInfoPos[7]).toString().equals("")) {
                    item.setGoodsSubtype(0);
                } else {
                    item.setGoodsSubtype((int) dataRow.getCell(itemInfoPos[7]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[8]) == null || dataRow.getCell(itemInfoPos[8]).toString().equals("")) {
                    item.setGoodsLimit(0);
                } else {
                    item.setGoodsLimit((int) dataRow.getCell(itemInfoPos[8]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[9]) == null || dataRow.getCell(itemInfoPos[9]).toString().equals("")) {
                    item.setGoodsIcon(0);
                } else {
                    item.setGoodsIcon((int) dataRow.getCell(itemInfoPos[9]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[10]) == null) {
                    item.setGoodsurl("");
                } else {
                    item.setGoodsurl(dataRow.getCell(itemInfoPos[10]).toString());
                }
                if (dataRow.getCell(itemInfoPos[11]) == null) {
                    item.setGoodsPrice("");
                } else {
                    item.setGoodsPrice(dataRow.getCell(itemInfoPos[11]).toString());
                }
                if (dataRow.getCell(itemInfoPos[12]) == null) {
                    item.setGoodsPricePoint("");
                } else {
                    item.setGoodsPricePoint(dataRow.getCell(itemInfoPos[12]).toString());
                }
                if (dataRow.getCell(itemInfoPos[13]) == null) {
                    item.setGoodsShowPrice("");
                } else {
                    item.setGoodsShowPrice(dataRow.getCell(itemInfoPos[13]).toString());
                }
                if (dataRow.getCell(itemInfoPos[14]) == null) {
                    item.setGoodsReward("");
                } else {
                    item.setGoodsReward(dataRow.getCell(itemInfoPos[14]).toString());
                }
                if (dataRow.getCell(itemInfoPos[15]) == null) {
                    item.setGoodsMultiple("");
                } else {
                    item.setGoodsMultiple(dataRow.getCell(itemInfoPos[15]).toString());
                }
                if (dataRow.getCell(itemInfoPos[16]) == null) {
                    item.setGoodsExtraReward("");
                } else {
                    item.setGoodsExtraReward(dataRow.getCell(itemInfoPos[16]).toString());
                }
                if (dataRow.getCell(itemInfoPos[17]) == null || dataRow.getCell(itemInfoPos[17]).toString().equals("")) {
                    item.setGoodsExtraRewardLimit(0);
                } else {
                    item.setGoodsExtraRewardLimit((int) dataRow.getCell(itemInfoPos[17]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[18]) == null || dataRow.getCell(itemInfoPos[18]).toString().equals("")) {
                    item.setIsTotalRecharge(0);
                } else {
                    item.setIsTotalRecharge((int) Float.parseFloat(dataRow.getCell(itemInfoPos[18]).toString()));
                }
                if (dataRow.getCell(itemInfoPos[19]) == null || dataRow.getCell(itemInfoPos[19]).toString().equals("")) {
                    item.setTotalVipPower(0);
                } else {
                    item.setTotalVipPower((int) Float.parseFloat(dataRow.getCell(itemInfoPos[19]).toString()));
                }

                items.add(item);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw  new RuntimeException("解析充值Excel出错，出错行数:"+rowCount+",列数:"+columnCount);
        }
    }
    @PostMapping( "/exportRechargeExcel")
    @ResponseBody
    public void exportRechargeExcel(int type, HttpServletResponse response){
        String sheetName = "";
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        if (type == 0){//普通充值表
            rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel();
            sheetName="rechargeItem";
        }else {
            rechargeItemList = rechargeItemService.selectRechargeItemBygoodsPayChannel3();
            sheetName="otherRechargeItem";
        }
        exportExcel(rechargeItemList,sheetName,response);
    }

    public void exportExcel(List<RechargeItem> rechargeItemList,String sheetName,HttpServletResponse response){
        LinkedList<Map<String, String>> listMap = new LinkedList<>();
        for(RechargeItem rechargeItem:rechargeItemList) {
            Map<String,String> map = new LinkedHashMap<>();
            map.put("充值档位ID，用于发送给第三方充值使用", String.valueOf(rechargeItem.getGoodsId()));
            map.put("用于游戏服系统内使用，主要是其他功能调用", String.valueOf(rechargeItem.getGoodsSystemCfgId()));
            map.put("商品名字描述（主要用于BI后台数据）", rechargeItem.getGoodsName());
            map.put("渠道名称(不填或留空为游戏内普通充值，否则为第三方渠道充值)", rechargeItem.getGoodsPayChannel());
            map.put("支付类型(根据第三方SDK传过来的paytype字段在配置表筛选对应的商品数据)", String.valueOf(rechargeItem.getGoodsPayType()));
            map.put("充值类型\n" +
                    "1：正常充值 2：每日礼包充值 3：畅游月卡 4：尊享月卡 5：终身卡 6：成长基金 7：神秘商店 8：0元购 9：直购礼包（超值折扣） 10：狂欢周 11：运营活动类（后台配置） 12：天禁令高级令牌 99:第三方档位信息\"", String.valueOf(rechargeItem.getGoodsType()));
            map.put("只针对Type=1（正常充值）的情况使用，其他类型不能使用\n" +
                    "1=正常充值\n" +
                    "2=新手礼包（一生一次）\n" +
                    "3=周礼包（一周一刷新）\n" +
                    "4=日礼包（一日一刷新）", String.valueOf(rechargeItem.getGoodsSubtype()));
            map.put("充值次数（当前轮每个挡位对应充值的次数）\n" +
                    "-1=无次数限制", String.valueOf(rechargeItem.getGoodsLimit()));
            map.put("显示的图标的ID（hide）", String.valueOf(rechargeItem.getGoodsIcon()));
            map.put("商品图片地址", String.valueOf(rechargeItem.getGoodsurl()));
            map.put("充值档位对应消耗的真实货币(单位:分)\n" +
                    "1：android\n" +
                    "2：ios\n" +
                    "（不需要区分大小写）\n" +
                    "THB  泰铢   MYR  马来西亚令吉   SGD  新加坡元   VND  越南盾   EUR  欧元   GBP  英镑   HKD  港币   IDR  印尼盾   KRW  韩元   TWD  新台币   USD  美金   JPY  日元\n" +
                    "CNY人民币\n" +
                    "示例：android_THB,1200_CNY,1200;ios_THB,1200_CNY,120", rechargeItem.getGoodsPrice());
            map.put("充值平台计费点\n" +
                    "（需要运营配置）\n" +
                    "android:tzj_oo6;ios:tzj_oo6（渠道_平台商品ID）", rechargeItem.getGoodsPricePoint());
            map.put("默认的金额币种（用于运营方读取money字段里需要显示的种类） ", rechargeItem.getGoodsShowPrice());
            map.put("对应奖励\n" +
                    "物品类型_数量_绑定_职业\n" +
                    "绑定 0未绑定 1绑定\n" +
                    "也只 0男剑 1女枪 2地藏 3罗刹 9通用", rechargeItem.getGoodsReward());
            map.put("充值倍数\n" +
                    "倍数_次数（3_2表示前2次充值都是3倍奖励）\n" +
                    "-1代表无限次", rechargeItem.getGoodsMultiple());
            map.put("额外赠送\n" +
                    "物品类型_数量_绑定_职业\n" +
                    "绑定 0未绑定 1绑定\n" +
                    "也只 0男剑 1女枪 2地藏 3罗刹 9通用", rechargeItem.getGoodsExtraReward());
            map.put("额外奖励可领取次数\n" +
                    "-1代表无限次", String.valueOf(rechargeItem.getGoodsExtraRewardLimit()));
            map.put("是否计入到游戏累充活动\n" +
                    "为0则代表不计入累充，大于0则代表计入累充的数额", String.valueOf(rechargeItem.getIsTotalRecharge()));
            map.put("是否增加VIP经验\n" +
                    "为0代表不增加VIP经验，大于0代表增加对应的VIP经验", String.valueOf(rechargeItem.getTotalVipPower()));
            listMap.add(map);
        }
        List<String> list1 = Arrays.asList("goods_id","goods_system_cfg_id","goods_name","goods_pay_channel","goods_pay_type","goods_type","goods_subtype","goods_limit","goods_icon","goods_url","goods_price","goods_price_point","goods_show_price","goods_reward","goods_multiple","goods_extra_reward","goods_extra_reward_limit","isTotalRecharge","totalVipPower");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//                Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
//                String excelName = msg.get("activity.activityData");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(sheetName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xlsx");
            // 转码防止乱码
            genExcel(listMap,list1,sheetName,out);
            out.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void genExcel(LinkedList<Map<String, String>> dataList,List<String> list1,String sheetName,OutputStream out) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, sheetName);

        int ri = 1, ci = 0;
        XSSFRow row = sheet.createRow(ri++);//第2行英文表头
        XSSFCell cell;
        for (String field : list1) {
            cell = row.createCell(ci);
            cell.setCellValue(field);
            ci++;//增加列
        }
        ri+=2;
        row = sheet.createRow(ri++);//第5行中文表头
        ci=0;//从第一列开始
        for (Map.Entry<String, String> entry : dataList.get(0).entrySet()) {
            cell = row.createCell(ci);
            cell.setCellValue(entry.getKey());
            ci++;
        }
        //数据
        for (Map<String, String> dataMap : dataList){//第6行数据开始
            row = sheet.createRow(ri++);
            ci = 0;
            for (String key : dataMap.keySet()) {
                cell = row.createCell(ci++);
                cell.setCellValue(dataMap.get(key) == null ? "" : dataMap.get(key));
            }
        }
        workbook.write(out);
    }

    /**
     * 获取goods_type类型为11的充值表数据
     * @return
     */
    @PostMapping( "/getRechargeByType")
    @ResponseBody
    public Object getRechargeByType() {
        RechargeItem rechargeItem = new RechargeItem();
        rechargeItem.setGoodsType(11);
        List<RechargeItem> list = rechargeItemService.selectRechargeItemList(rechargeItem);
        return AjaxResult.info("",list).put("ok",true);
    }

    /**
     * 查询充值配置修改的历史记录
     * @return
     */
    @PostMapping( "/queryRechargeItemLog")
    @ResponseBody
    public Object queryRechargeItemLog() {
        startPage();
        List<RechargeItemLog> list = rechargeItemLogService.selectRechargeItemLogList(new RechargeItemLog());
        List<RechargeItemLogGrid> rechargeItemLogGrids = new ArrayList<>();
        for (RechargeItemLog rechargeItemLog:list){
            RechargeItemLogGrid rechargeItemLogGrid = new RechargeItemLogGrid();
            rechargeItemLogGrid.setId(rechargeItemLog.getId());
            rechargeItemLogGrid.setUserName(rechargeItemLog.getUserName());
            rechargeItemLogGrid.setTime(TimeUtils.format2string(rechargeItemLog.getTime()));
            rechargeItemLogGrid.setTableName(rechargeItemLog.getTableName());
            rechargeItemLogGrid.setContent(rechargeItemLog.getContent());
            rechargeItemLogGrids.add(rechargeItemLogGrid);
        }
        return getDataTable(rechargeItemLogGrids);
    }

    @GetMapping("/queryRechargeItemLogPage")
    public String queryRechargeItemLogPage(int index,ModelMap mmap){
         mmap.put("index",index);
         return prefix + "/rechargeItemContent";
    }
    /**
     * 根据页面传过来的index查询对应的记录详情
     * @return
     */
    @PostMapping( "/queryRechargeItemContent")
    @ResponseBody
    public Object queryRechargeItemContent(PageDomain pageDomain, int index) {
        int page = pageDomain.getPageNum();
        int rows = pageDomain.getPageSize();
        List<RechargeItemLog> list = rechargeItemLogService.selectRechargeItemLogList(new RechargeItemLog());
        List<JSONArray> jsonArrayList = new ArrayList<>();
        for (RechargeItemLog rechargeItemLog:list){
            JSONArray jsonarray = new JSONArray();
            jsonarray = JSONArray.fromObject(rechargeItemLog.getContent());
            jsonArrayList.add(jsonarray);
        }
        if (jsonArrayList.size() == 0){
            return AjaxResult.success().put("total", 0).put("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= jsonArrayList.get(index).size() ? jsonArrayList.get(index).size() : rows * page;
        List<RechargeItem> rechargeItems = JSONObject.parseArray(jsonArrayList.get(index).toString(),RechargeItem.class);
        return AjaxResult.success().put("total",rechargeItems.size()).put("rows",rechargeItems.subList(fromIndex,toIndex));
    }

    /**
     * 导出充值配置修改的历史记录
     * @param request
     * @param response
     */
    @PostMapping( "/exportRechargeItemLog")
    @ResponseBody
    public void exportRechargeItemLog(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("hId"));
        String tableName = request.getParameter("tableName");
        RechargeItemLog rechargeItemLog = rechargeItemLogService.selectRechargeItemLogById(id);
        String content = rechargeItemLog.getContent();
        JSONArray jsonarray = JSONArray.fromObject(content);
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        for (int i = 0; i < jsonarray.size(); i++){
            RechargeItem rechargeItem = new RechargeItem();
            rechargeItem.setGoodsId(jsonarray.getJSONObject(i).getInt("goodsId"));
            rechargeItem.setGoodsSystemCfgId(jsonarray.getJSONObject(i).getInt("goodsSystemCfgId"));
            rechargeItem.setGoodsName(jsonarray.getJSONObject(i).getString("goodsName"));
            rechargeItem.setGoodsPayChannel(jsonarray.getJSONObject(i).getString("goodsPayChannel"));
            rechargeItem.setGoodsPayType(jsonarray.getJSONObject(i).getInt("goodsPayType"));
            rechargeItem.setGoodsType(jsonarray.getJSONObject(i).getInt("goodsType"));
            rechargeItem.setGoodsSubtype(jsonarray.getJSONObject(i).getInt("goodsSubtype"));
            rechargeItem.setGoodsLimit(jsonarray.getJSONObject(i).getInt("goodsLimit"));
            rechargeItem.setGoodsIcon(jsonarray.getJSONObject(i).getInt("goodsIcon"));
            rechargeItem.setGoodsurl(jsonarray.getJSONObject(i).getString("goodsurl"));
            rechargeItem.setGoodsPrice(jsonarray.getJSONObject(i).getString("goodsPrice"));
            rechargeItem.setGoodsPricePoint(jsonarray.getJSONObject(i).getString("goodsPricePoint"));
            rechargeItem.setGoodsShowPrice(jsonarray.getJSONObject(i).getString("goodsShowPrice"));
            rechargeItem.setGoodsReward(jsonarray.getJSONObject(i).getString("goodsReward"));
            rechargeItem.setGoodsMultiple(jsonarray.getJSONObject(i).getString("goodsMultiple"));
            rechargeItem.setGoodsExtraReward(jsonarray.getJSONObject(i).getString("goodsExtraReward"));
            rechargeItem.setGoodsExtraRewardLimit(jsonarray.getJSONObject(i).getInt("goodsExtraRewardLimit"));
            rechargeItem.setIsTotalRecharge(jsonarray.getJSONObject(i).getInt("isTotalRecharge"));
            rechargeItem.setTotalVipPower(jsonarray.getJSONObject(i).getInt("totalVipPower"));
            rechargeItemList.add(rechargeItem);
        }
        exportExcel(rechargeItemList,tableName,response);
    }

    /**
     * 普通充值及第三方充值数据清除
     * @param request
     * @param response
     * @return
     */
    @PostMapping( "/clearRechargeItem")
    @ResponseBody
    public Object clearRechargeItem(HttpServletRequest request, HttpServletResponse response){
        rechargeItemService.clearRechargeItem("t_recharge_item");
        GMLogUtil.log("普通充值及第三方充值数据清除");
        return AjaxResult.info().put("ok",true);
    }
}
