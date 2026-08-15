package com.backend.module.operation;

import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.struct.ShopInfo;
import com.backend.utils.JsonUtils;
import com.backend.utils.Toolkit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@At("/shop")
@IocBean
@Ok("json")
@Fail("http:500")
public class ShopModule {

    private static final Log logger = Logs.get();

    @At("/")
    @Ok("jsp:jsp.operation.shoplist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {}

    @At
    public Object list(String serverId, String shopId) {
        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "Сервер не выбран");
        }
        // Отправка сообщения на GameServer
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверу");
        }
        NutMap result = GameServerRequestUtil.gmGetShopInfo(server, "0");
        if (!result.getBoolean("ok")) {
            return result;
        }
        if (Strings.isBlank(shopId) || "0".equals(shopId)) {
            return result;
        }
        JSONArray jsonArray = new JSONArray();
        JSONArray data = (JSONArray) result.get("data");
        for (Object datum : data) {
            JSONObject shopInfo = (JSONObject) datum;
            String shopid = shopInfo.getOrDefault("shopid", 0).toString();
            if (shopId.equals(shopid)) {
                jsonArray.add(shopInfo);
            }
        }
        return Toolkit.outResult(true, "", jsonArray);
    }

    @At
    @Ok("jsp:jsp.operation.shopform")
    public void shopForm(HttpServletRequest request, String groupName, String serverId, String id) {
        Object data = "null";
        if (!"0".equals(id)) {
            if (Strings.isBlank(serverId)) {
                logger.error("ID сервера не получен");
                return;
            }
            // Отправка сообщения на GameServer
            Server server = ServerListManager.getInstance().getServer(serverId);
            if (server == null) {
                logger.error("Сервер с указанным ID не найден: " + serverId);
                return;
            }
            NutMap result = GameServerRequestUtil.gmGetShopInfo(server, id);
            data = result.get("data");
        }
        request.setAttribute("id", id);
        request.setAttribute("date", new Date());
        request.setAttribute("shopInfo", data);
        request.setAttribute("groupName", groupName);
        request.setAttribute("serverId", serverId);
    }

    @At
    public Object updateShop(String serverId, ShopInfo info, int type) {
        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "Сервер не выбран");
        }
        // Отправка сообщения на GameServer
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверу");
        }
        if (info.getId() > 110000 || info.getId() < 100000) {
            return Toolkit.outResult(false, "Ошибка ID товара, допустимый диапазон: 100000~110000");
        }
        if (type == 0) {
            NutMap result = GameServerRequestUtil.gmGetShopInfo(server, "0");
            JSONArray data = (JSONArray) result.get("data");
            for (Object datum : data) {
                JSONObject shopInfo = (JSONObject) datum;
                String id = shopInfo.getOrDefault("id", 0).toString();
                if (info.getId().toString().equals(id)) {
                    return Toolkit.outResult(false, "Ошибка добавления товара: дублирующийся ID");
                }
            }
        }
        String shopInfo = JsonUtils.toJSONString(info);
        return GameServerRequestUtil.gmSetShopInfo(server, shopInfo);
    }

    @At
    public Object deleteShop(String serverId, String shopId) {
        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "Сервер не выбран");
        }
        // Отправка сообщения на GameServer
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверу");
        }
        return GameServerRequestUtil.gmDeleteShopInfo(server, shopId);
    }

    @At
    public Object refreshShop(String serverId) {
        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "Сервер не выбран");
        }
        // Отправка сообщения на GameServer
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверу");
        }
        return GameServerRequestUtil.gmOrderSendMess(server, "gmRefreshShop", "");
    }
}