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
 * 游戏功能开关
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
            log.error("服务器获取失败！sid=" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap resultMap = GameServerRequestUtil.gmGetFuncOpenList(server);
        if (!resultMap.getBoolean("ok")) {
            log.error("服务器编号" + serverId + "反馈信息:功能列表获取失败！");
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
            log.error("服务器获取失败！sid=" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap resultMap = GameServerRequestUtil.gmSwitchFunction(server, funcSwitch);
        if (!resultMap.getBoolean("ok")) {
            log.error("功能开关操作失败！");
            return Toolkit.outResult(false, "服务器处理失败或未作处理！");
        }
        return Toolkit.outResult(true, "处理成功！");
    }
}




