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
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Обработка запросов магазина пополнения
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
     * Уведомление игрового сервера об обновлении списка товаров магазина пополнения
     */
    @At
    public Object sendRechargeInfos() {
        return RechargeItemManager.getInstance().sendRechargeInfos();
    }

    /**
     * Уведомление игрового сервера об обновлении товара магазина пополнения
     */
    @At
    public String updateRechargeItem(RechargeItem rechargeItem) {
        // Обновление данных в локальной БД GM-бэкенда
        int count = dao.update(rechargeItem);
        if (count <= 0) {
            return "Ошибка обновления конфигурации магазина пополнения в GM-бэкенде";
        }

        RechargeItemManager.getInstance().getRechargeItemMap().put(rechargeItem.getGoods_id(), rechargeItem);
        RechargeItemManager.getInstance().getRechargeItemInfoMap().put(rechargeItem.getGoods_id(), RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));

        StringBuilder sb = new StringBuilder();
        // Уведомление API-сервера об обновлении информации о пополнении
        String rechargeStr = JsonUtils.toJSONString(RechargeItemManager.getInstance().convertRechargeItem(rechargeItem));
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("rechargeStr", rechargeStr);
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl") + "/rechargeItem/updateRechargeItem", paramMap);

        sb.append("Результат ответа API-сервера: ").append(httpResult).append("\n");

        // Уведомление игровых серверов об обновлении всей информации о пополнении
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("isHeFu", "=", 0).and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();

        String allRechargeStr = RechargeItemManager.getInstance().getRechargeStr();
        String md5 = MD5Util.MD5(allRechargeStr);

        for (Server server : servers) {
            int serverId = server.getServerId();
            try {
                NutMap resultMap = GameServerRequestUtil.gmRefreshRechargeItemInfos(server, allRechargeStr, md5, 15 * 1000);
                if (!resultMap.getBoolean("ok")) {
                    serverFailedList.add(serverId);
                    logger.error("Сервер " + serverId + ": ошибка обновления конфигурации пополнения [" + rechargeItem.getGoods_id() + "]! Результат: " + resultMap.get("data").toString());
                } else {
                    serverSuccessList.add(serverId);
                }
            } catch (Exception e) {
                logger.error("Сервер " + serverId + ": ошибка обновления конфигурации пополнения! Ошибка: " + e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("Список успешно синхронизированных игровых серверов: ").append(serverSuccessList).append("\n");
        sb.append("Список серверов с ошибкой синхронизации: ").append(serverFailedList).append("\n");
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "Изменение конфигурации пополнения, ID: " + rechargeItem.getGoods_id());
        return sb.toString();
    }

    /**
     * Уведомление игрового сервера об удалении товара магазина пополнения
     */
    @At
    public Object deleteRechargeItem(String id) {
        // Удаление данных из локальной БД GM-бэкенда
        int count = dao.delete(RechargeItem.class, Integer.parseInt(id));
        if (count <= 0) {
            return Toolkit.outResult(false, "Ошибка удаления конфигурации магазина пополнения в GM-бэкенде");
        }

        RechargeItemManager.getInstance().getRechargeItemMap().remove(Integer.parseInt(id));
        RechargeItemManager.getInstance().getRechargeItemInfoMap().remove(Integer.parseInt(id));

        StringBuilder sb = new StringBuilder();
        // Уведомление API-сервера об удалении информации о пополнении
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl") + "/rechargeItem/deleteRechargeItem", paramMap);

        sb.append("Результат ответа API-сервера: ").append(httpResult).append("\n");

        // Уведомление игровых серверов об удалении информации о пополнении
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("isHeFu", "=", 0).and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for (Server server : servers) {
            int serverId = server.getServerId();
            NutMap resultMap = GameServerRequestUtil.gmDeleteRechargeInfo(server, id);
            if (!resultMap.getBoolean("ok")) {
                serverFailedList.add(serverId);
                logger.error("Сервер " + serverId + ": ошибка удаления конфигурации пополнения [" + id + "]! Результат: " + resultMap.get("data").toString());
            } else {
                serverSuccessList.add(serverId);
            }
        }
        sb.append("Список успешно синхронизированных игровых серверов: ").append(serverSuccessList).append("\n");
        sb.append("Список серверов с ошибкой синхронизации: ").append(serverFailedList).append("\n");
        return Toolkit.outResult(true, sb.toString());
    }

    /**
     * Запрос обычных пополнений
     */
    @At
    @POST
    public Object queryRechargeItem(@Param("page") int page, @Param("rows") int rows) {
        Cnd cnd = Cnd.where("goods_pay_channel", "=", "").or(Exps.isNull("goods_pay_channel"));
        List<RechargeItem> list = dao.query(RechargeItem.class, cnd);
        if (list == null) {
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex)).setv("md5", md5);
    }

    /**
     * Запрос конфигурации пополнения для сторонних платформ
     */
    @At
    @POST
    public Object queryRechargeItem3(@Param("page") int page, @Param("rows") int rows) {
        Cnd cnd = Cnd.where("goods_pay_channel", "<>", "");
        List<RechargeItem> list = dao.query(RechargeItem.class, cnd);
        if (list == null) {
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex)).setv("md5", md5);
    }

    @At
    @POST
    public Object saveRechargeItem(HttpServletRequest request) {
        RechargeItem rechargeItem = new RechargeItem();

        String rechargeTableName = request.getParameter("rechargeTableName");
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        rechargeItem.setGoods_id(goods_id);

        if (!request.getParameter("goods_system_cfg_id").equals("")) {
            rechargeItem.setGoods_system_cfg_id(Integer.parseInt(request.getParameter("goods_system_cfg_id")));
        }

        String goods_name = request.getParameter("goods_name");
        rechargeItem.setGoods_name(goods_name);

        String goods_pay_channel = request.getParameter("goods_pay_channel");
        rechargeItem.setGoods_pay_channel(goods_pay_channel);

        if (!request.getParameter("goods_pay_type").equals("")) {
            rechargeItem.setGoods_pay_type(Integer.parseInt(request.getParameter("goods_pay_type")));
        }

        if (!request.getParameter("goods_type").equals("")) {
            rechargeItem.setGoods_type(Integer.parseInt(request.getParameter("goods_type")));
        }

        if (!request.getParameter("goods_subtype").equals("")) {
            rechargeItem.setGoods_subtype(Integer.parseInt(request.getParameter("goods_subtype")));
        }

        if (!request.getParameter("goods_limit").equals("")) {
            rechargeItem.setGoods_limit(Integer.parseInt(request.getParameter("goods_limit")));
        }

        if (!request.getParameter("goods_icon").equals("")) {
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

        if (!request.getParameter("goods_extra_reward_limit").equals("")) {
            rechargeItem.setGoods_extra_reward_limit(Integer.parseInt(request.getParameter("goods_extra_reward_limit")));
        }
        if (!request.getParameter("isTotalRecharge").equals("")) {
            rechargeItem.setIsTotalRecharge(Integer.parseInt(request.getParameter("isTotalRecharge")));
        }
        if (!request.getParameter("totalVipPower").equals("")) {
            rechargeItem.setTotalVipPower(Integer.parseInt(request.getParameter("totalVipPower")));
        }
        // Получение старого MD5-кода таблицы пополнений
        String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

        // Обновление данных (база данных и APIServer)
        String updateResultStr = updateRechargeItem(rechargeItem);
        // Перезагрузка данных в памяти
        RechargeItemManager.getInstance().load();

        // Запись лога действий пользователя
        BackendLogUtil.getInstance().log(request, rechargeItem.toString());

        // Обновление MD5-кода
        String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        String content = "";
        if (null != oldMd5 && !oldMd5.equals(md5)) { // MD5 изменился
            if (rechargeTableName.equals("rechargeItem")) { // Обычное пополнение
                Cnd cnd = Cnd.where("goods_pay_channel", "=", "").or(Exps.isNull("goods_pay_channel"));
                rechargeItemList = dao.query(RechargeItem.class, cnd);
                content = JsonUtils.toJSONString(rechargeItemList);
                BackendLogUtil.getInstance().rechargeItemlog(request, content, rechargeTableName);
            } else { // Пополнение через сторонние платформы
                Cnd cnd = Cnd.where("goods_pay_channel", "<>", "");
                rechargeItemList = dao.query(RechargeItem.class, cnd);
                content = JsonUtils.toJSONString(rechargeItemList);
                BackendLogUtil.getInstance().rechargeItemlog(request, content, rechargeTableName);
            }
        }

        return Toolkit.outResult(true).setv("msg", updateResultStr).setv("md5", md5);
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadRechargeItem(HttpServletRequest request, @Param("rechargeItemFile") TempFile rechargeItemFile) {
        if (rechargeItemFile == null) {
            return Toolkit.outResult(false, "Файл не найден!");
        }
        String fileName = rechargeItemFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "Неверный тип файла!");
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[19];
        try {
            FileInputStream in = new FileInputStream(rechargeItemFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("rechargeItem")) {
                return Toolkit.outResult(false, "Это не конфигурационный файл rechargeItem");
            }

            // Получение старого MD5-кода таблицы пополнений
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());

            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size() > 0) { // Обычное пополнение заменяет только обычные уровни пополнения
                // Удаление данных обычного пополнения из БД
                int delCount = dao.clear(RechargeItem.class,
                        Cnd.wrap("goods_pay_channel is NULL or goods_pay_channel = ''"));

                List<RechargeItem> result = dao.insert(items);
                if (result.size() > 0) {
                    RechargeItemManager.getInstance().load();
                    Object resultStr = RechargeItemManager.getInstance().sendRechargeInfos();
                    // Обновление MD5-кода
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    BackendLogUtil.getInstance().log(Mvcs.getReq(), "Импорт конфигурации обычного пополнения успешен, MD5:" + md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    if (null != oldMd5 && !oldMd5.equals(md5)) { // MD5 изменился
                        Cnd cnd = Cnd.where("goods_pay_channel", "=", "").or(Exps.isNull("goods_pay_channel"));
                        rechargeItemList = dao.query(RechargeItem.class, cnd);
                        content = JsonUtils.toJSONString(rechargeItemList);
                        BackendLogUtil.getInstance().rechargeItemlog(request, content, "rechargeItem");
                    }
                    return Toolkit.outResult(true, "Перезагрузка выполнена, сохранено " + result.size() + " записей!\n" + resultStr);
                }
            }
            return Toolkit.outResult(false, "Ошибка перезагрузки файла rechargeItem!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadOtherRechargeItem(HttpServletRequest request, @Param("otherRechargeItemFile") TempFile otherRechargeItemFile) {
        if (otherRechargeItemFile == null) {
            return Toolkit.outResult(false, "Файл не найден!");
        }
        String fileName = otherRechargeItemFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "Неверный тип файла!");
        }
        List<RechargeItem> items = new ArrayList<>();
        int[] itemInfoPos = new int[19];
        try {
            FileInputStream in = new FileInputStream(otherRechargeItemFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("otherRechargeItem")) {
                return Toolkit.outResult(false, "Это не конфигурационный файл rechargeItem");
            }
            // Получение старого MD5-кода таблицы пополнений
            String oldMd5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
            parseRechargeExcel(items, itemInfoPos, sheet);

            if (items.size() > 0) {
                // Удаление данных пополнения через сторонние платформы из БД
                int delCount = dao.clear(RechargeItem.class,
                        Cnd.wrap("goods_pay_channel is NOT NULL and goods_pay_channel != ''"));

                List<RechargeItem> result = dao.insert(items);
                if (result.size() > 0) {
                    RechargeItemManager.getInstance().load();
                    Object resultStr = RechargeItemManager.getInstance().sendRechargeInfos();
                    // Обновление MD5-кода
                    String md5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
                    BackendLogUtil.getInstance().log(Mvcs.getReq(), "Импорт конфигурации пополнения через сторонние платформы успешен, MD5:" + md5);
                    List<RechargeItem> rechargeItemList = new ArrayList<>();
                    String content = "";
                    if (null != oldMd5 && !oldMd5.equals(md5)) { // MD5 изменился
                        Cnd cnd = Cnd.where("goods_pay_channel", "<>", "");
                        rechargeItemList = dao.query(RechargeItem.class, cnd);
                        content = JsonUtils.toJSONString(rechargeItemList);
                        BackendLogUtil.getInstance().rechargeItemlog(request, content, "otherRechargeItem");
                    }
                    return Toolkit.outResult(true, "Перезагрузка выполнена, сохранено " + result.size() + " записей!\n" + resultStr);
                }
            }
            return Toolkit.outResult(false, "Ошибка перезагрузки!");
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
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException("Ошибка парсинга Excel с пополнениями. Строка с ошибкой: " + rowCount + ", столбец: " + columnCount);
        }
    }

    @At
    @POST
    @Ok("void")
    public void exportRechargeExcel(int type, HttpServletResponse response) {
        Cnd cnd = Cnd.NEW();
        String sheetName = "";
        if (type == 0) { // Обычная таблица пополнений
            cnd = Cnd.where("goods_pay_channel", "=", "").or(Exps.isNull("goods_pay_channel"));
            sheetName = "rechargeItem";
        } else {
            cnd = Cnd.where("goods_pay_channel", "<>", "");
            sheetName = "otherRechargeItem";
        }
        List<RechargeItem> rechargeItemList = dao.query(RechargeItem.class, cnd);
        exportExcel(rechargeItemList, sheetName, response);
    }

    public void exportExcel(List<RechargeItem> rechargeItemList, String sheetName, HttpServletResponse response) {
        LinkedList<Map<String, String>> listMap = new LinkedList<>();
        for (RechargeItem rechargeItem : rechargeItemList) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("ID уровня пополнения, используется для отправки сторонним платформам", String.valueOf(rechargeItem.getGoods_id()));
            map.put("Используется внутри игрового сервера, в основном для вызова других функций", String.valueOf(rechargeItem.getGoods_system_cfg_id()));
            map.put("Название товара (в основном для BI-бэкенда)", rechargeItem.getGoods_name());
            map.put("Название канала (если не заполнено или пусто — обычное пополнение в игре, иначе — пополнение через сторонний канал)", rechargeItem.getGoods_pay_channel());
            map.put("Тип оплаты (фильтрация товаров по полю paytype из стороннего SDK)", String.valueOf(rechargeItem.getGoods_pay_type()));
            map.put("Тип пополнения\n1: обычное, 2: ежедневный подарок, 3: месячная карта, 4: премиум-карта, 5: пожизненная карта, 6: фонд роста, 7: тайный магазин, 8: покупка за 0, 9: прямая покупка (супер-скидка), 10: неделя карнавала, 11: операционные активности (бэкенд), 12: продвинутый пропуск Tianjinling, 99: сторонние уровни", String.valueOf(rechargeItem.getGoods_type()));
            map.put("Используется только для Type=1 (обычное пополнение), другие типы не используют\n1=обычное пополнение\n2=подарок новичка (один раз)\n3=недельный подарок (обновляется раз в неделю)\n4=ежедневный подарок (обновляется раз в день)", String.valueOf(rechargeItem.getGoods_subtype()));
            map.put("Количество пополнений (текущий уровень)\n-1=без ограничений", String.valueOf(rechargeItem.getGoods_limit()));
            map.put("ID отображаемой иконки (скрыто)", String.valueOf(rechargeItem.getGoods_icon()));
            map.put("Реальная валюта для уровня пополнения (в копейках)\n1: android\n2: ios\n(регистр не важен)\nTHB тайский бат, MYR малайзийский ринггит, SGD сингапурский доллар, VND вьетнамский донг, EUR евро, GBP фунт, HKD гонконгский доллар, IDR индонезийская рупия, KRW вона, TWD новый тайваньский доллар, USD доллар США, JPY иена, CNY юань\nПример: android_THB,1200_CNY,1200;ios_THB,1200_CNY,120", rechargeItem.getGoods_price());
            map.put("Точка оплаты платформы\n(требуется настройка оператором)\nandroid:tzj_oo6;ios:tzj_oo6 (канал_ID товара платформы)", rechargeItem.getGoods_price_point());
            map.put("Валюта по умолчанию (для отображения в поле money)", rechargeItem.getGoods_show_price());
            map.put("Соответствующая награда\nтип предмета_количество_привязка_класс\nпривязка: 0 — нет, 1 — да\nкласс: 0 — мужской меч, 1 — женское ружьё, 2 — Дидзан, 3 — Ракшаса, 9 — общий", rechargeItem.getGoods_reward());
            map.put("Множитель пополнения\nмножитель_количество (3_2 означает, что первые 2 пополнения дают тройную награду)\n-1=без ограничений", rechargeItem.getGoods_multiple());
            map.put("Дополнительный бонус\nтип предмета_количество_привязка_класс\nпривязка: 0 — нет, 1 — да\nкласс: 0 — мужской меч, 1 — женское ружьё, 2 — Дидзан, 3 — Ракшаса, 9 — общий", rechargeItem.getGoods_extra_reward());
            map.put("Количество получений дополнительной награды\n-1=без ограничений", String.valueOf(rechargeItem.getGoods_extra_reward_limit()));
            map.put("Учитывается ли в накопительной активности пополнения\n0 — не учитывается, >0 — учитывается с указанной суммой", String.valueOf(rechargeItem.getIsTotalRecharge()));
            map.put("Добавляет ли VIP-опыт\n0 — не добавляет, >0 — добавляет указанное количество", String.valueOf(rechargeItem.getTotalVipPower()));
            listMap.add(map);
        }
        List<String> list1 = Arrays.asList("goods_id", "goods_system_cfg_id", "goods_name", "goods_pay_channel", "goods_pay_type", "goods_type", "goods_subtype", "goods_limit", "goods_icon", "goods_price", "goods_price_point", "goods_show_price", "goods_reward", "goods_multiple", "goods_extra_reward", "goods_extra_reward_limit", "isTotalRecharge", "totalVipPower");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.addHeader("content-type", "application/shlnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(sheetName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xls");
            genExcel(listMap, list1, sheetName, out);
            out.flush();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void genExcel(LinkedList<Map<String, String>> dataList, List<String> list1, String sheetName, OutputStream out) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, sheetName);

        int ri = 1, ci = 0;
        HSSFRow row = sheet.createRow(ri++); // Строка 2: заголовки на английском
        HSSFCell cell;
        for (String field : list1) {
            cell = row.createCell(ci);
            cell.setCellValue(field);
            ci++;
        }
        ri += 2;
        row = sheet.createRow(ri++); // Строка 5: заголовки на русском
        ci = 0;
        for (Map.Entry<String, String> entry : dataList.get(0).entrySet()) {
            cell = row.createCell(ci);
            cell.setCellValue(entry.getKey());
            ci++;
        }
        // Данные
        for (Map<String, String> dataMap : dataList) { // Данные начиная со строки 6
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
     * Получение данных таблицы пополнений с goods_type = 11
     */
    @At
    public Object getRechargeByType() {
        Cnd cnd = Cnd.where("goods_type", "=", 11);
        List<RechargeItem> list = dao.query(RechargeItem.class, cnd);
        return Toolkit.outResult(true, list);
    }

    /**
     * Запрос истории изменения конфигурации пополнений
     */
    @At
    public Object queryRechargeItemLog(@Param("page") int page, @Param("rows") int rows) {
        List<RechargeItemLog> list = dao.query(RechargeItemLog.class, Cnd.wrap("order by time desc limit 20"));
        List<RechargeItemLogGrid> rechargeItemLogGrids = new ArrayList<>();
        for (RechargeItemLog rechargeItemLog : list) {
            RechargeItemLogGrid rechargeItemLogGrid = new RechargeItemLogGrid();
            rechargeItemLogGrid.setId(rechargeItemLog.getId());
            rechargeItemLogGrid.setUserName(rechargeItemLog.getUserName());
            rechargeItemLogGrid.setTime(TimeUtils.format2string(rechargeItemLog.getTime()));
            rechargeItemLogGrid.setTableName(rechargeItemLog.getTableName());
            rechargeItemLogGrid.setContent(rechargeItemLog.getContent());
            rechargeItemLogGrids.add(rechargeItemLogGrid);
        }
        if (rechargeItemLogGrids.size() == 0) {
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= rechargeItemLogGrids.size() ? rechargeItemLogGrids.size() : rows * page;
        return Toolkit.outResult(true).setv("total", rechargeItemLogGrids.size()).setv("rows", rechargeItemLogGrids.subList(fromIndex, toIndex));
    }

    /**
     * Получение деталей записи по индексу, переданному со страницы
     */
    @At
    public Object queryRechargeItemContent(@Param("page") int page, @Param("rows") int rows, int index) {
        List<RechargeItemLog> list = dao.query(RechargeItemLog.class, Cnd.wrap("order by time desc limit 20"));
        List<JSONArray> jsonArrayList = new ArrayList<>();
        for (RechargeItemLog rechargeItemLog : list) {
            JSONArray jsonarray = new JSONArray();
            jsonarray = JSONArray.fromObject(rechargeItemLog.getContent());
            jsonArrayList.add(jsonarray);
        }
        if (jsonArrayList.size() == 0) {
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= jsonArrayList.get(index).size() ? jsonArrayList.get(index).size() : rows * page;
        return Toolkit.outResult(true).setv("total", jsonArrayList.get(index).size()).setv("rows", jsonArrayList.get(index).subList(fromIndex, toIndex));
    }

    /**
     * Экспорт истории изменения конфигурации пополнений
     */
    @At
    @POST
    @Ok("void")
    public void exportRechargeItemLog(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("hId"));
        String tableName = request.getParameter("tableName");
        RechargeItemLog rechargeItemLog = dao.fetch(RechargeItemLog.class, Cnd.where("id", "=", id));
        String content = rechargeItemLog.getContent();
        JSONArray jsonarray = JSONArray.fromObject(content);
        List<RechargeItem> rechargeItemList = new ArrayList<>();
        for (int i = 0; i < jsonarray.size(); i++) {
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
        exportExcel(rechargeItemList, tableName, response);
    }

    /**
     * Очистка данных обычного пополнения и пополнения через сторонние платформы
     */
    @At
    @POST
    public Object clearRechargeItem(HttpServletRequest request, HttpServletResponse response) {
        dao.truncate(RechargeItem.class);
        BackendLogUtil.getInstance().log(request, "Очистка данных обычного пополнения и пополнения через сторонние платформы");
        return Toolkit.outResult(true);
    }
}