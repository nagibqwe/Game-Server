package com.backend.module.operation;

import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.manager.ServerListManager;
import com.backend.struct.ServerGroupGrid;
import com.backend.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Обработка запросов магазина пополнения
 */
@IocBean
@At("/serverGroup")
@Ok("json")
@Filters
@Fail("http:500")
public class ServerGruopModule {
    private static final Log logger = Logs.get();

    @Inject
    private Dao dao;

    @At
    @Ok("jsp:jsp.operation.serverGroup")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void serverGroup() {

    }

    /**
     * Получение информации о кросс-серверных группах с публичного сервера
     * @param request
     * @param publicServerId
     * @return
     * @throws UnsupportedEncodingException
     */
    @At
    public Object getServerGroup(HttpServletRequest request, int publicServerId) throws UnsupportedEncodingException {
        if (publicServerId <= 0) {
            return Toolkit.outResult(false, "Неверный ID публичного сервера");
        }
        HashMap<Integer, HashMap<Integer, List<String>>> serverGroupList = new HashMap<>();
        try {
            serverGroupList = (HashMap<Integer, HashMap<Integer, List<String>>>) sendPS(request, publicServerId, "getServerGroupList", "");
        } catch (Exception e) {
            return Toolkit.outResult(false).setv("total", 0).setv("rows", 0);
        }
        if (serverGroupList == null || serverGroupList.isEmpty()) {
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        List<ServerGroupGrid> serverGroupGrids = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<Integer, List<String>>> en : serverGroupList.entrySet()) {
            ServerGroupGrid serverGroupGrid = new ServerGroupGrid();

            int bigGroupId = en.getKey();
            serverGroupGrid.setBigGroupId(bigGroupId);

            HashMap<Integer, List<String>> serverGroup = en.getValue();
            for (Map.Entry<Integer, List<String>> entry : serverGroup.entrySet()) {
                int groupId = entry.getKey();
                serverGroupGrid.setGroupId(groupId);
                List<String> serverIds = entry.getValue();

                String area = getArea(serverIds.get(0)); // Все переданные данные из одного региона
                serverGroupGrid.setArea(area);
                int count = 0;
                for (String serverIdStr : serverIds) { // Соглашение: передаётся хотя бы один serverId
                    int serverId = getServerId(serverIdStr);
                    String serverName = getServerName(serverId);
                    count++;
                    if (count == 1) {
                        serverGroupGrid.setServerId1(serverId);
                        serverGroupGrid.setServerName1(serverName);
                    }
                    if (count == 2) {
                        serverGroupGrid.setServerId2(serverId);
                        serverGroupGrid.setServerName2(serverName);
                    }
                    if (count == 3) {
                        serverGroupGrid.setServerId3(serverId);
                        serverGroupGrid.setServerName3(serverName);
                    }
                    if (count == 4) {
                        serverGroupGrid.setServerId4(serverId);
                        serverGroupGrid.setServerName4(serverName);
                    }
                    if (count == 5) {
                        serverGroupGrid.setServerId5(serverId);
                        serverGroupGrid.setServerName5(serverName);
                    }
                    if (count == 6) {
                        serverGroupGrid.setServerId6(serverId);
                        serverGroupGrid.setServerName6(serverName);
                    }
                    if (count == 7) {
                        serverGroupGrid.setServerId7(serverId);
                        serverGroupGrid.setServerName7(serverName);
                    }
                    if (count == 8) {
                        serverGroupGrid.setServerId8(serverId);
                        serverGroupGrid.setServerName8(serverName);
                    }
                }
            }
            serverGroupGrids.add(serverGroupGrid);
        }
        return Toolkit.outResult(true).setv("total", serverGroupGrids.size()).setv("rows", serverGroupGrids);
    }

    // Тестовые данные
    public Object getData() {
        List<ServerGroupGrid> serverGroupGrids = new ArrayList<>();
        ServerGroupGrid groupGrid = new ServerGroupGrid(1, 10000, "cn", 20003, "Ян Ланьфэй", 20004, "Чэнь Лунчуань", 20005, "Чжоу Мэнъюй", 20006, "Чжан Сань", 20007, "Ли Сы", 20008, "Ван У", 20009, "Хэй Хэй", 0, "");
        ServerGroupGrid groupGrid2 = new ServerGroupGrid(2, 20000, "cn", 20010, "2222", 20011, "3333", 20011, "4444", 20012, "5555", 20013, "6666", 20014, "77777", 20015, "8888", 0, "");
        ServerGroupGrid groupGrid3 = new ServerGroupGrid(3, 30000, "cn", 20016, "2222", 20017, "3333", 20018, "4444", 20019, "5555", 20020, "6666", 20021, "77777", 20022, "8888", 0, "");
        serverGroupGrids.add(groupGrid);
        serverGroupGrids.add(groupGrid2);
        serverGroupGrids.add(groupGrid3);
        return Toolkit.outResult(true).setv("total", 3).setv("rows", serverGroupGrids);
    }

    /**
     * Отправка команды на публичный сервер
     * @param request
     * @param serverId
     * @param action
     * @return
     * @throws UnsupportedEncodingException
     */
    public Object sendPS(HttpServletRequest request, int serverId, String action, String params) throws UnsupportedEncodingException {
        if (Strings.isBlank(action)) {
            return Toolkit.outResult(false, "Ошибка команды");
        }
        if (serverId <= 0) {
            return Toolkit.outResult(false, "Неверный ID сервера");
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("secret_key", ServerKeyUtil.GetPSRuquestKey());
        if (!"".equals(params)) {
            paramMap.put("serverGroup", params);
        }
        String url;
        StringBuilder result = new StringBuilder();
        boolean isOk = true;
        Server ser = ServerListManager.getInstance().getServer(serverId);
        if (ser == null) {
            return Toolkit.outResult(false, "Выбранный ID сервера не существует");
        }

        url = "http://" + ser.getWorldIP() + ":" + ser.getWorldPort() + "/" + action;
        String re = HttpConnectionUtils.get(url, null, paramMap);
        result.append(re);
        if (Strings.isBlank(re)) {
            isOk = false;
            result.append("Данные запроса пусты!");
        }
        BackendLogUtil.getInstance().log(request, "Выполнение GM-команды, результат: " + result);
        Object object = new Object();
        if (!"".equals(params)) {
            object = Toolkit.outResult(isOk, result.toString());
        } else {
            object = JsonUtils.parseObject(re, new TypeReference<HashMap<Integer, HashMap<Integer, List<String>>>>() {});
        }
        return object;
    }


    /**
     * Получение ID сервера
     * @param severIdStr
     * @return
     */
    public int getServerId(String severIdStr) {
        String[] severIdArr = severIdStr.split("_");
        int serverId = Integer.parseInt(severIdArr[1]);
        return serverId;
    }

    /**
     * Получение кода региона
     * @param severIdStr
     * @return
     */
    public String getArea(String severIdStr) {
        String[] severIdArr = severIdStr.split("_");
        String area = severIdArr[0];
        return area;
    }

    /**
     * Получение имени сервера
     * @param serverId
     * @return
     */
    public String getServerName(int serverId) {
        Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId));
        String serverName = "";
        if (server != null) {
            serverName = "(" + serverId + ")" + server.getServerName();
        } else {
            serverName = "(" + serverId + ")";
        }
        return serverName;
    }

    /**
     * Установка кросс-серверных групп
     * @param request
     * @param serverGroupList
     * @return
     */
    @At
    public Object setServerGroupList(HttpServletRequest request, String serverGroupList, int publicServerId) throws UnsupportedEncodingException {

        HashMap<Integer, HashMap<Integer, List<String>>> bIgGroupMap = new HashMap<>();

        List<ServerGroupGrid> serverGroupGridList = (List<ServerGroupGrid>) JSONArray.toList(JSONArray.fromObject(serverGroupList), ServerGroupGrid.class);

        int bigGroupId = 0;
        for (int i = 0; i < serverGroupGridList.size(); i++) {
            ServerGroupGrid serverGroupGrid = serverGroupGridList.get(i);
            bigGroupId = serverGroupGrid.getBigGroupId();
            int groupId = serverGroupGrid.getGroupId();
            List<String> serverIdList = getServerIdList(serverGroupGrid);
            HashMap<Integer, List<String>> groupMap = new HashMap<>();

            groupMap.put(groupId, serverIdList);
            bIgGroupMap.put(bigGroupId, groupMap);
        }
        String serverGroup = JsonUtils.toJSONString(bIgGroupMap);
        Object result = sendPS(request, publicServerId, "setServerGroupList", serverGroup);
        BackendLogUtil.getInstance().log(request, "Выполнение GM-команды setServerGroupList, результат: " + result.toString());
        return result;
    }

    /**
     * Формирование списка ID серверов в формате регион_серверID
     * @param serverGroupGrid
     * @return
     */
    private List<String> getServerIdList(ServerGroupGrid serverGroupGrid) {
        List<String> serverIds = new ArrayList<>();
        String area = serverGroupGrid.getArea();
        if (serverGroupGrid.getServerId1() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId1());
        }
        if (serverGroupGrid.getServerId2() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId2());
        }
        if (serverGroupGrid.getServerId3() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId3());
        }
        if (serverGroupGrid.getServerId4() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId4());
        }
        if (serverGroupGrid.getServerId5() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId5());
        }
        if (serverGroupGrid.getServerId6() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId6());
        }
        if (serverGroupGrid.getServerId7() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId7());
        }
        if (serverGroupGrid.getServerId8() != 0) {
            serverIds.add(area + "_" + serverGroupGrid.getServerId8());
        }

        return serverIds;
    }
}