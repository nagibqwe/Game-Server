package com.backend.module.operation;

import com.backend.module.admin.BackRoleModule;
import com.backend.bean.Function;
import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Переключатели игровых функций
 */
@IocBean
@At("/systemSwitch")
@Ok("json")
@Fail("http:500")
public class SystemSwitchModule {

    private static final Logger log = Logger.getLogger(BackRoleModule.class);
    private static final HashMap<Integer, Function> functions = new HashMap<>();

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.operation.switchlist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {
        functions.clear();
        List<Function> functionList = dao.query(Function.class, null);
        for (Function function : functionList) {
            functions.put(function.getFuncId(), function);
        }
    }

    @At
    public Object getFunctionSwitch(String serverId, String condition) {

        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            log.error("Ошибка получения сервера! sid=" + serverId);
            return Toolkit.outResult(false, "Ошибка получения сервера");
        }
        NutMap resultMap = GameServerRequestUtil.gmGetFuncOpenList(server);
        if (!resultMap.getBoolean("ok")) {
            log.error("Сервер " + serverId + " — ошибка получения списка функций!");
            return Toolkit.outResult(false, resultMap.get("msg").toString());
        }
        List<Function> listMap = new ArrayList<>();
        boolean flag = Strings.isBlank(condition);
        List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) resultMap.get("data");
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> object = data.get(i);
            Function function = functions.get(Integer.parseInt(object.get("id").toString()));
            if (function == null) {
                continue;
            }
            function.setOpenState(Integer.parseInt(object.get("openState").toString()));
            if (flag) {
                listMap.add(function);
                continue;
            }
            if (function.getFuncName().matches(".*?" + condition + ".*?")) {
                listMap.add(function);
            }
        }
        return Toolkit.outResult(true, listMap);
    }

    @At
    public Object switchIs(String serverId, String funcSwitch) {
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            log.error("Ошибка получения сервера! sid=" + serverId);
            return Toolkit.outResult(false, "Ошибка получения сервера");
        }
        NutMap resultMap = GameServerRequestUtil.gmSwitchFunction(server, funcSwitch);
        if (!resultMap.getBoolean("ok")) {
            log.error("Ошибка операции переключения функций!");
            return Toolkit.outResult(false, "Сервер не обработал запрос или произошла ошибка!");
        }
        return Toolkit.outResult(true, "Операция выполнена успешно!");
    }
}