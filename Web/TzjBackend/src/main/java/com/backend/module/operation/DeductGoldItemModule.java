package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.struct.RoleState;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.DbConfigUtil;
import com.backend.utils.QueryUtil;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@IocBean
@Ok("json")
@At("/deductgolditem")
@Fail("http:500")
public class DeductGoldItemModule {

    private static final Logger log = Logger.getLogger(DeductGoldItemModule.class);

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.operation.decItem")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void item() {}

    @At
    public Object deductItemByRoleId(DeductItem deductItem, String serverId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        int sid = QueryUtil.getInstance().getHeFuId(deductItem.getServerId());

        Dblog dbLog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", sid));
        if (dbLog == null) {
            return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверной БД");
        }

        // Проверка ID персонажа
        String sqlStr = "SELECT roleId,userId FROM $table WHERE roleId = @roleId";
        List<RoleState> roles = queryRoleState(dbLog, sqlStr, deductItem.getRoleId());
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, "ID персонажа не найден");
        }
        if (roles.get(0).getIsDelete() != 0) {
            return Toolkit.outResult(false, "Этот персонаж уже удалён");
        }
        try {
            deductItem.setDedTime(new Date());
            deductItem.setSendUser(user.getName());
            deductItem.setIsDelete(0);
            // Отправка сообщения на GameServer
            Server server = ServerListManager.getInstance().getServer(serverId);
            if (server == null) {
                return Toolkit.outResult(false, "Не удалось получить информацию о подключении к серверу");
            }
            NutMap resultMap = GameServerRequestUtil.gmDeductItemopt(server, deductItem);
            // Получение фактического количества списанных предметов
            int realCount = Integer.parseInt(resultMap.get("data").toString());
            deductItem.setRealCount(realCount);
            String prompt;
            if (resultMap.getBoolean("ok")) {
                prompt = "Операция выполнена успешно!";
                dao.insert(deductItem);
            } else {
                prompt = "Ошибка операции!";
            }
            log.error("Списание предметов: sid=" + serverId + ", roleId=" + deductItem.getRoleId() + ", результат: " + resultMap.getString("msg"));
            BackendLogUtil.getInstance().log(request, "Удаление персонажа (roleIds=" + deductItem.getRoleId() + ")" + deductItem.getRealCount() + " предметов");
            return Toolkit.outResult(resultMap.getBoolean("ok"), prompt);
        } catch (Exception e) {
            log.error(e);
            return Toolkit.outResult(false, "Ошибка списания");
        }
    }

    @At
    public Object getDeductItemList(int serverId) {
        Condition cnd = Cnd.where("serverId", "=", serverId).and("isDelete", "=", 0).desc("id");
        List<DeductItem> deItemList = dao.query(DeductItem.class, cnd);
        return Toolkit.outResult(true, deItemList);
    }

    @At
    public Object delDeductItem(int id) {
        DeductItem deductItem = dao.fetch(DeductItem.class, id);
        if (deductItem != null) {
            deductItem.setIsDelete(1);
            int num = dao.update(deductItem);
            return Toolkit.outResult(num == 1);
        }
        return Toolkit.outResult(false);
    }

    private List<RoleState> queryRoleState(Dblog dblog, String sqlStr, Object paramValue) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "rolestate");
        sql.params().set("roleId", paramValue);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(RoleState.class);
    }
}