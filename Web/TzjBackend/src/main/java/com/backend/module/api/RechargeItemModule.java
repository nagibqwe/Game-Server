package com.backend.module.api;

import com.backend.bean.BackendLog;
import com.backend.bean.RechargeItem;
import com.backend.bean.RechargeItemLog;
import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.RechargeItemManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.RechargeItemInfo;
import com.backend.struct.RechargeItemLogGrid;
import com.backend.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.Exps;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 处理充值商城请求
 */
@IocBean
@At("/rechargeItem")
@Ok("json")
@Filters
@Fail("http:500")
public class RechargeItemModule {

    private static final Log logger = Logs.get();

    @Inject
    private Dao dao;

    @At
    @Ok("jsp:jsp.operation.rechargeItem")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void rechargeItem() {

    }

    /**
     * 通知游戏服刷新充值商城商品列表
     */
    @At
    public Object sendRechargeInfos() {
        return RechargeItemManager.getInstance().sendRechargeInfos();
    }

    /**
     * 通知游戏服更新充值商城某条商品
     */
    @At
    public String updateRechargeItem(RechargeItem rechargeItem) {
        //更新GM后台本地数据库数据
        int count = dao.update(rechargeItem);
        if(count<=0){
            return "更新GM后台充值商城配置失败";
        }

        RechargeItemManager.getInstance().getRechargeItemMap().put(rechargeItem.getGoods_id(), rechargeItem);
        RechargeItemManager.getInstance().getRechargeItemInfoMap().put(rechargeItem.getGoods_id(), RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));

        StringBuilder sb = new StringBuilder();
        //通知API服务器更新某一条的充值信息
        String rechargeStr = JsonUtils.toJSONString(RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("rechargeStr", rechargeStr);
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl")+"/rechargeItem/updateRechargeItem", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("/n");

        //通知游戏服更新全部的充值信息
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("isHeFu","=", 0).and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();

        String allRechargeStr = RechargeItemManager.getInstance().getRechargeStr();
        String md5 = MD5Util.MD5(allRechargeStr);

        for(Server server : servers){
            int serverId = server.getServerId();
            try {
                NutMap resultMap = GameServerRequestUtil.gmRefreshRechargeItemInfos(server, allRechargeStr, md5, 15*1000);
                if (!resultMap.getBoolean("ok")) {
                    serverFailedList.add(serverId);
                    logger.error(serverId + "服,充值配置[" + rechargeItem.getGoods_id() + "]更新失败！操作结果：" + resultMap.get("data").toString());
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
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "修改充值配置，修改的ID："+rechargeItem.getGoods_id());
        return sb.toString();
    }

    /**
     * 通知游戏服删除充值商城某条商品
     */
    @At
    public Object deleteRechargeItem(String id) {
        //删除GM后台本地数据库数据
        int count = dao.delete(RechargeItem.class, Integer.parseInt(id));
        if(count<=0){
            return Toolkit.outResult(false, "删除GM后台充值商城配置失败");
        }

        RechargeItemManager.getInstance().getRechargeItemMap().remove(Integer.parseInt(id));
        RechargeItemManager.getInstance().getRechargeItemInfoMap().remove(Integer.parseInt(id));

        StringBuilder sb = new StringBuilder();
        //通知API服务器删除某一条的充值信息
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl")+"/rechargeItem/deleteRechargeItem", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("/n");

        //通知游戏服删除某一条的充值信息
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("isHeFu","=", 0).and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(Server server : servers){
            int serverId = server.getServerId();
            NutMap resultMap = GameServerRequestUtil.gmDeleteRechargeInfo(server, id);
            if (!resultMap.getBoolean("ok")) {
                serverFailedList.add(serverId);
                logger.error(serverId + "服,充值配置[" + id + "]删除失败！操作结果：" + resultMap.get("data").toString());
            } else {
                serverSuccessList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("/n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("/n");
        return Toolkit.outResult(true, sb.toString());
    }

    /**
     *普通充值查询
     * @param page
     * @param rows
     * @return
     */
    @At
    @POST
    public Object queryRechargeItem(@Param("page") int page, @Param("rows") int rows) {
        Cnd cnd = Cnd.where("goods_pay_channel","=","").or(Exps.isNull("goods_pay_channel"));
        List<RechargeItem> list = dao.query(RechargeItem.class,cnd);
        if (list == null){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex)).setv("md5",md5);
    }

    /**
     * 第三方充值配置查询
     * @param page
     * @param rows
     * @return
     */
    @At
    @POST
    public Object queryRechargeItem3(@Param("page") int page, @Param("rows") int rows) {
        Cnd cnd = Cnd.where("goods_pay_channel","<>","");
        List<RechargeItem> list = dao.query(RechargeItem.class,cnd);
        if (list == null){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex)).setv("md5",md5);
    }

    @At
    @POST
    public Object saveRechargeItem(HttpServletRequest request) {
        RechargeItem rechargeItem = new RechargeItem();

        String rechargeTableName = request.getParameter("rechargeTableName");
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        rechargeItem.setGoods_id(goods_id);

        if (!request.getParameter("goods_system_cfg_id").equals("")){
            rechargeItem.setGoods_system_cfg_id(Integer.parseInt(request.getParameter("goods_system_cfg_id")));
        }

        String goods_name = request.getParameter("goods_name");
        rechargeItem.setGoods_name(goods_name);

        String goods_pay_channel = request.getParameter("goods_pay_channel");
        rechargeItem.setGoods_pay_channel(goods_pay_channel);

        if (!request.getParameter("goods_pay_type").equals("")){
            rechargeItem.setGoods_pay_type(Integer.parseInt(request.getParameter("goods_pay_type")));
        }

        if (!request.getParameter("goods_type").equals("")){
            rechargeItem.setGoods_type(Integer.parseInt(request.getParameter("goods_type")));
        }

        if (!request.getParameter("goods_subtype").equals("")){
            rechargeItem.setGoods_subtype(Integer.parseInt(request.getParameter("goods_subtype")));
        }

        if (!request.getParameter("goods_limit").equals("")){
            rechargeItem.setGoods_limit(Integer.parseInt(request.getParameter("goods_limit")));
        }

        if (!request.getParameter("goods_icon").equals("")){
            rechargeItem.setGoods_icon(Integer.parseInt(request.getParameter("goods_icon")));
        }
        String goods_price = request.getParameter("goods_price");
        rechargeItem.setGoods_price(goods_price);
        String goods_price_point = request.getParameter("goods_price_point");
        rechargeItem.setGoods_price_point(goods_price_point);
        String goods_show_price = request.getParameter("goods_show_price");
        rechargeItem.setGoods_show_price(goods_show_price);
        String goods_reward = request.getParameter("goods_reward");
        rechargeItem.setGoods_reward(goods_reward);
        String goods_multiple = request.getParameter("goods_multiple");
        rechargeItem.setGoods_multiple(goods_multiple);
        String goods_extra_reward = request.getParameter("goods_extra_reward");
        rechargeItem.setGoods_extra_reward(goods_extra_reward);

        if (!request.getParameter("goods_extra_reward_limit").equals("")){
            rechargeItem.setGoods_extra_reward_limit(Integer.parseInt(request.getParameter("goods_extra_reward_limit")));
        }
        if (!request.getParameter("isTotalRecharge").equals("")){
            rechargeItem.setIsTotalRecharge(Integer.parseInt(request.getParameter("isTotalRecharge")));
        }
        if (!request.getParameter("totalVipPower").equals("")){
            rechargeItem.setTotalVipPower(Integer.parseInt(request.getParameter("totalVipPower")));
        }
        //获取之前旧充值表的MD5码
        String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

        //更新数据(数据库和APIServer)
        String updateResultStr = updateRechargeItem(rechargeItem);
        //重新加载内存数据
        RechargeItemManager.getInstance().load();

        //记录用户操作日志
        BackendLogUtil.getInstance().log(request,rechargeItem.toString());

        //MD5码更新
        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        String content = "";
        if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
            if (rechargeTableName.equals("rechargeItem")){//普通充值
                Cnd cnd = Cnd.where("goods_pay_channel","=","").or(Exps.isNull("goods_pay_channel"));
                rechargeItemList = dao.query(RechargeItem.class,cnd);
                content = JsonUtils.toJSONString(rechargeItemList);
                BackendLogUtil.getInstance().rechargeItemlog(request,content,rechargeTableName);

            }else {//第三方充值
                Cnd cnd = Cnd.where("goods_pay_channel","<>","");
                rechargeItemList = dao.query(RechargeItem.class,cnd);
                content = JsonUtils.toJSONString(rechargeItemList);
                BackendLogUtil.getInstance().rechargeItemlog(request,content,rechargeTableName);
            }
        }

        return Toolkit.outResult(true).setv("msg",updateResultStr).setv("md5",md5);
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadRechargeItem(HttpServletRequest request, @Param("rechargeItemFile") TempFile rechargeItemFile){
        if (rechargeItemFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = rechargeItemFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[19];
        try {
            FileInputStream in = new FileInputStream(rechargeItemFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("rechargeItem")) {
                return Toolkit.outResult(false, "not rechargeItem config file");
            }

            //获取之前旧充值表的MD5码
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size()>0) {//普通充值只会替换普通充值档位
                //先删除数据库中普通充值数据
                int delCount = dao.clear(RechargeItem.class,
                        Cnd.wrap("goods_pay_channel is NULL or goods_pay_channel = ''"));

                List<RechargeItem> result = dao.insert(items);
                if(result.size()>0){
                    RechargeItemManager.getInstance().load();
                    Object resultStr=RechargeItemManager.getInstance().sendRechargeInfos();
                    //MD5码更新
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    BackendLogUtil.getInstance().log(Mvcs.getReq(), "导入普通充值配置数据成功，MD5:"+md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
                        Cnd cnd = Cnd.where("goods_pay_channel","=","").or(Exps.isNull("goods_pay_channel"));
                        rechargeItemList = dao.query(RechargeItem.class,cnd);
                        content = JsonUtils.toJSONString(rechargeItemList);
                        BackendLogUtil.getInstance().rechargeItemlog(request,content,"rechargeItem");
                    }
                    return Toolkit.outResult(true, "Reload Ok, saved " + result.size() + " record!\n"+resultStr);
                }
            }
            return Toolkit.outResult(false, "Reload rechargeItem file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadOtherRechargeItem(HttpServletRequest request,@Param("otherRechargeItemFile") TempFile otherRechargeItemFile){
        if (otherRechargeItemFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = otherRechargeItemFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[19];
        try {
            FileInputStream in = new FileInputStream(otherRechargeItemFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("otherRechargeItem")) {
                return Toolkit.outResult(false, "not rechargeItem config file");
            }
            //获取之前旧充值表的MD5码
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size()>0) {
                //先删除数据库中第三方充值数据
                int delCount = dao.clear(RechargeItem.class,
                        Cnd.wrap("goods_pay_channel is NOT NULL and goods_pay_channel != ''"));

                List<RechargeItem> result = dao.insert(items);
                if(result.size()>0){
                    RechargeItemManager.getInstance().load();
                    Object resultStr=RechargeItemManager.getInstance().sendRechargeInfos();
                    //MD5码更新
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    BackendLogUtil.getInstance().log(Mvcs.getReq(), "导入第三方充值配置数据成功，MD5:"+md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    if (null != oldMd5  && !oldMd5.equals(md5)){//md5码不一样则进行了改变
                        Cnd cnd = Cnd.where("goods_pay_channel","<>","");
                        rechargeItemList = dao.query(RechargeItem.class,cnd);
                        content = JsonUtils.toJSONString(rechargeItemList);
                        BackendLogUtil.getInstance().rechargeItemlog(request,content,"otherRechargeItem");
                    }
                    return Toolkit.outResult(true, "Reload Ok, saved " + result.size() + " record!\n"+resultStr);
                }
            }
            return Toolkit.outResult(false, "Reload failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
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
                if (row.getCell(i).toString().equalsIgnoreCase("goods_price")) {
                    itemInfoPos[10] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_price_point")) {
                    itemInfoPos[11] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_show_price")) {
                    itemInfoPos[12] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_reward")) {
                    itemInfoPos[13] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_multiple")) {
                    itemInfoPos[14] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_extra_reward")) {
                    itemInfoPos[15] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("goods_extra_reward_limit")) {
                    itemInfoPos[16] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("isTotalRecharge")) {
                    itemInfoPos[17] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("totalVipPower")) {
                    itemInfoPos[18] = i;
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
                item.setGoods_id((int) Float.parseFloat(dataRow.getCell(itemInfoPos[0]).toString()));
                item.setGoods_system_cfg_id((int) Float.parseFloat(dataRow.getCell(itemInfoPos[1]).toString()));
                item.setGoods_name(dataRow.getCell(itemInfoPos[3]).toString());
                if (dataRow.getCell(itemInfoPos[4]) == null) {
                    item.setGoods_pay_channel("");
                } else {
                    item.setGoods_pay_channel(dataRow.getCell(itemInfoPos[4]).toString());
                }
                if (dataRow.getCell(itemInfoPos[5]) == null || dataRow.getCell(itemInfoPos[5]).toString().equals("")) {
                    item.setGoods_pay_type(0);
                } else {
                    item.setGoods_pay_type((int) Float.parseFloat(dataRow.getCell(itemInfoPos[5]).toString()));
                }
                item.setGoods_type((int) Float.parseFloat(dataRow.getCell(itemInfoPos[6]).toString()));
                if (dataRow.getCell(itemInfoPos[7]) == null || dataRow.getCell(itemInfoPos[7]).toString().equals("")) {
                    item.setGoods_subtype(0);
                } else {
                    item.setGoods_subtype((int) dataRow.getCell(itemInfoPos[7]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[8]) == null || dataRow.getCell(itemInfoPos[8]).toString().equals("")) {
                    item.setGoods_limit(0);
                } else {
                    item.setGoods_limit((int) dataRow.getCell(itemInfoPos[8]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[9]) == null || dataRow.getCell(itemInfoPos[9]).toString().equals("")) {
                    item.setGoods_icon(0);
                } else {
                    item.setGoods_icon((int) dataRow.getCell(itemInfoPos[9]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[10]) == null) {
                    item.setGoods_price("");
                } else {
                    item.setGoods_price(dataRow.getCell(itemInfoPos[10]).toString());
                }
                if (dataRow.getCell(itemInfoPos[11]) == null) {
                    item.setGoods_price_point("");
                } else {
                    item.setGoods_price_point(dataRow.getCell(itemInfoPos[11]).toString());
                }
                if (dataRow.getCell(itemInfoPos[12]) == null) {
                    item.setGoods_show_price("");
                } else {
                    item.setGoods_show_price(dataRow.getCell(itemInfoPos[12]).toString());
                }
                if (dataRow.getCell(itemInfoPos[13]) == null) {
                    item.setGoods_reward("");
                } else {
                    item.setGoods_reward(dataRow.getCell(itemInfoPos[13]).toString());
                }
                if (dataRow.getCell(itemInfoPos[14]) == null) {
                    item.setGoods_multiple("");
                } else {
                    item.setGoods_multiple(dataRow.getCell(itemInfoPos[14]).toString());
                }
                if (dataRow.getCell(itemInfoPos[15]) == null) {
                    item.setGoods_extra_reward("");
                } else {
                    item.setGoods_extra_reward(dataRow.getCell(itemInfoPos[15]).toString());
                }
                if (dataRow.getCell(itemInfoPos[16]) == null || dataRow.getCell(itemInfoPos[16]).toString().equals("")) {
                    item.setGoods_extra_reward_limit(0);
                } else {
                    item.setGoods_extra_reward_limit((int) dataRow.getCell(itemInfoPos[16]).getNumericCellValue());
                }
                if (dataRow.getCell(itemInfoPos[17]) == null || dataRow.getCell(itemInfoPos[17]).toString().equals("")) {
                    item.setIsTotalRecharge(0);
                } else {
                    item.setIsTotalRecharge((int) Float.parseFloat(dataRow.getCell(itemInfoPos[17]).toString()));
                }
                if (dataRow.getCell(itemInfoPos[18]) == null || dataRow.getCell(itemInfoPos[18]).toString().equals("")) {
                    item.setTotalVipPower(0);
                } else {
                    item.setTotalVipPower((int) Float.parseFloat(dataRow.getCell(itemInfoPos[18]).toString()));
                }

                items.add(item);
            }
        }catch (Exception e){
            logger.error(e,e);
            throw  new RuntimeException("解析充值Excel出错，出错行数:"+rowCount+",列数:"+columnCount);
        }
    }

    @At
    @POST
    @Ok("void")
    public void exportRechargeExcel(int type, HttpServletResponse response){
        Cnd cnd = Cnd.NEW();
        String sheetName = "";
        if (type == 0){//普通充值表
            cnd = Cnd.where("goods_pay_channel","=","").or(Exps.isNull("goods_pay_channel"));
            sheetName="rechargeItem";
        }else {
            cnd = Cnd.where("goods_pay_channel","<>","");
            sheetName="otherRechargeItem";
        }
        List<RechargeItem> rechargeItemList = dao.query(RechargeItem.class,cnd);
        exportExcel(rechargeItemList,sheetName,response);
    }

    public void exportExcel(List<RechargeItem> rechargeItemList,String sheetName,HttpServletResponse response){
        LinkedList<Map<String, String>> listMap = new LinkedList<>();
        for(RechargeItem rechargeItem:rechargeItemList) {
            Map<String,String> map = new LinkedHashMap<>();
            map.put("充值档位ID，用于发送给第三方充值使用", String.valueOf(rechargeItem.getGoods_id()));
            map.put("用于游戏服系统内使用，主要是其他功能调用", String.valueOf(rechargeItem.getGoods_system_cfg_id()));
            map.put("商品名字描述（主要用于BI后台数据）", rechargeItem.getGoods_name());
            map.put("渠道名称(不填或留空为游戏内普通充值，否则为第三方渠道充值)", rechargeItem.getGoods_pay_channel());
            map.put("支付类型(根据第三方SDK传过来的paytype字段在配置表筛选对应的商品数据)", String.valueOf(rechargeItem.getGoods_pay_type()));
            map.put("充值类型\n" +
                    "1：正常充值 2：每日礼包充值 3：畅游月卡 4：尊享月卡 5：终身卡 6：成长基金 7：神秘商店 8：0元购 9：直购礼包（超值折扣） 10：狂欢周 11：运营活动类（后台配置） 12：天禁令高级令牌 99:第三方档位信息\"", String.valueOf(rechargeItem.getGoods_type()));
            map.put("只针对Type=1（正常充值）的情况使用，其他类型不能使用\n" +
                    "1=正常充值\n" +
                    "2=新手礼包（一生一次）\n" +
                    "3=周礼包（一周一刷新）\n" +
                    "4=日礼包（一日一刷新）", String.valueOf(rechargeItem.getGoods_subtype()));
            map.put("充值次数（当前轮每个挡位对应充值的次数）\n" +
                    "-1=无次数限制", String.valueOf(rechargeItem.getGoods_limit()));
            map.put("显示的图标的ID（hide）", String.valueOf(rechargeItem.getGoods_icon()));
            map.put("充值档位对应消耗的真实货币(单位:分)\n" +
                    "1：android\n" +
                    "2：ios\n" +
                    "（不需要区分大小写）\n" +
                    "THB  泰铢   MYR  马来西亚令吉   SGD  新加坡元   VND  越南盾   EUR  欧元   GBP  英镑   HKD  港币   IDR  印尼盾   KRW  韩元   TWD  新台币   USD  美金   JPY  日元\n" +
                    "CNY人民币\n" +
                    "示例：android_THB,1200_CNY,1200;ios_THB,1200_CNY,120", rechargeItem.getGoods_price());
            map.put("充值平台计费点\n" +
                    "（需要运营配置）\n" +
                    "android:tzj_oo6;ios:tzj_oo6（渠道_平台商品ID）", rechargeItem.getGoods_price_point());
            map.put("默认的金额币种（用于运营方读取money字段里需要显示的种类） ", rechargeItem.getGoods_show_price());
            map.put("对应奖励\n" +
                    "物品类型_数量_绑定_职业\n" +
                    "绑定 0未绑定 1绑定\n" +
                    "也只 0男剑 1女枪 2地藏 3罗刹 9通用", rechargeItem.getGoods_reward());
            map.put("充值倍数\n" +
                    "倍数_次数（3_2表示前2次充值都是3倍奖励）\n" +
                    "-1代表无限次", rechargeItem.getGoods_multiple());
            map.put("额外赠送\n" +
                    "物品类型_数量_绑定_职业\n" +
                    "绑定 0未绑定 1绑定\n" +
                    "也只 0男剑 1女枪 2地藏 3罗刹 9通用", rechargeItem.getGoods_extra_reward());
            map.put("额外奖励可领取次数\n" +
                    "-1代表无限次", String.valueOf(rechargeItem.getGoods_extra_reward_limit()));
            map.put("是否计入到游戏累充活动\n" +
                    "为0则代表不计入累充，大于0则代表计入累充的数额", String.valueOf(rechargeItem.getIsTotalRecharge()));
            map.put("是否增加VIP经验\n" +
                    "为0代表不增加VIP经验，大于0代表增加对应的VIP经验", String.valueOf(rechargeItem.getTotalVipPower()));
            listMap.add(map);
        }
        List<String> list1 = Arrays.asList("goods_id","goods_system_cfg_id","goods_name","goods_pay_channel","goods_pay_type","goods_type","goods_subtype","goods_limit","goods_icon","goods_price","goods_price_point","goods_show_price","goods_reward","goods_multiple","goods_extra_reward","goods_extra_reward_limit","isTotalRecharge","totalVipPower");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.addHeader("content-type", "application/shlnd.ms-excel;charset=utf-8");
//                Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
//                String excelName = msg.get("activity.activityData");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(sheetName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xls");
            // 转码防止乱码
            genExcel(listMap,list1,sheetName,out);
            out.flush();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void genExcel(LinkedList<Map<String, String>> dataList,List<String> list1,String sheetName,OutputStream out) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, sheetName);

        int ri = 1, ci = 0;
        HSSFRow row = sheet.createRow(ri++);//第2行英文表头
        HSSFCell cell;
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
    @At
    public Object getRechargeByType() {
        Cnd cnd = Cnd.where("goods_type","=",11);
        List<RechargeItem> list = dao.query(RechargeItem.class,cnd);
        return Toolkit.outResult(true, list);
    }

    /**
     * 查询充值配置修改的历史记录
     * @return
     */
    @At
    public Object queryRechargeItemLog(@Param("page") int page, @Param("rows") int rows) {
        List<RechargeItemLog> list = dao.query(RechargeItemLog.class, Cnd.wrap("order by time desc limit 20"));
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
        if (rechargeItemLogGrids.size() == 0){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= rechargeItemLogGrids.size() ? rechargeItemLogGrids.size() : rows * page;
        return Toolkit.outResult(true).setv("total", rechargeItemLogGrids.size()).setv("rows", rechargeItemLogGrids.subList(fromIndex, toIndex));
    }

    /**
     * 根据页面传过来的index查询对应的记录详情
     * @param page
     * @param rows
     * @param index
     * @return
     */
    @At
    public Object queryRechargeItemContent(@Param("page") int page, @Param("rows") int rows,int index) {
        List<RechargeItemLog> list = dao.query(RechargeItemLog.class, Cnd.wrap("order by time desc limit 20"));
        List<JSONArray> jsonArrayList = new ArrayList<>();
        for (RechargeItemLog rechargeItemLog:list){
            JSONArray jsonarray = new JSONArray();
            jsonarray = JSONArray.fromObject(rechargeItemLog.getContent());
            jsonArrayList.add(jsonarray);
        }
        if (jsonArrayList.size() == 0){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= jsonArrayList.get(index).size() ? jsonArrayList.get(index).size() : rows * page;
        return Toolkit.outResult(true).setv("total", jsonArrayList.get(index).size()).setv("rows", jsonArrayList.get(index).subList(fromIndex, toIndex));
    }

    /**
     * 导出充值配置修改的历史记录
     * @param request
     * @param response
     */
    @At
    @POST
    @Ok("void")
    public void exportRechargeItemLog(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("hId"));
        String tableName = request.getParameter("tableName");
        RechargeItemLog rechargeItemLog = dao.fetch(RechargeItemLog.class,Cnd.where("id","=",id));
        String content = rechargeItemLog.getContent();
        JSONArray jsonarray = JSONArray.fromObject(content);
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        for (int i = 0; i < jsonarray.size(); i++){
            RechargeItem rechargeItem = new RechargeItem();
            rechargeItem.setGoods_id(jsonarray.getJSONObject(i).getInt("goods_id"));
            rechargeItem.setGoods_system_cfg_id(jsonarray.getJSONObject(i).getInt("goods_system_cfg_id"));
            rechargeItem.setGoods_name(jsonarray.getJSONObject(i).getString("goods_name"));
            rechargeItem.setGoods_pay_channel(jsonarray.getJSONObject(i).getString("goods_pay_channel"));
            rechargeItem.setGoods_pay_type(jsonarray.getJSONObject(i).getInt("goods_pay_type"));
            rechargeItem.setGoods_type(jsonarray.getJSONObject(i).getInt("goods_type"));
            rechargeItem.setGoods_subtype(jsonarray.getJSONObject(i).getInt("goods_subtype"));
            rechargeItem.setGoods_limit(jsonarray.getJSONObject(i).getInt("goods_limit"));
            rechargeItem.setGoods_icon(jsonarray.getJSONObject(i).getInt("goods_icon"));
            rechargeItem.setGoods_price(jsonarray.getJSONObject(i).getString("goods_price"));
            rechargeItem.setGoods_price_point(jsonarray.getJSONObject(i).getString("goods_price_point"));
            rechargeItem.setGoods_show_price(jsonarray.getJSONObject(i).getString("goods_show_price"));
            rechargeItem.setGoods_reward(jsonarray.getJSONObject(i).getString("goods_reward"));
            rechargeItem.setGoods_multiple(jsonarray.getJSONObject(i).getString("goods_multiple"));
            rechargeItem.setGoods_extra_reward(jsonarray.getJSONObject(i).getString("goods_extra_reward"));
            rechargeItem.setGoods_extra_reward_limit(jsonarray.getJSONObject(i).getInt("goods_extra_reward_limit"));
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
    @At
    @POST
    public Object clearRechargeItem(HttpServletRequest request, HttpServletResponse response){
        dao.truncate(RechargeItem.class);
        BackendLogUtil.getInstance().log(request,"普通充值及第三方充值数据清除");
        return Toolkit.outResult(true);
    }
}
