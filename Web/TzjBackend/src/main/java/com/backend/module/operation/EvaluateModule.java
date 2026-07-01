package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.bean.Evaluate;
import com.backend.bean.Server;
import com.backend.bean.User;
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
@At("/evaluate")
@Fail("http:500")
public class EvaluateModule {

    private static final Logger log = Logger.getLogger(EvaluateModule.class);

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.operation.evaluate")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void setEvaluate() {

    }

    @At
    public Object setEvaluateState(Evaluate evaluate, String serverId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        try {
            evaluate.seteType(evaluate.geteType());
            evaluate.setState(evaluate.isState());
            evaluate.setActionTime(new Date());
            evaluate.setActionUser(user.getName());
            evaluate.setIsDelete(0);
            //发送消息到GameServer
            Server server = ServerListManager.getInstance().getServer(serverId);
            if (server == null) {
                return Toolkit.outResult(false, "服务器连接信息获取失败");
            }
            NutMap resultMap = GameServerRequestUtil.gmSetEvaluate(server, evaluate);
            String prompt;
            if (resultMap.getBoolean("ok")) {
                prompt = "操作成功！";
                dao.insert(evaluate);
            } else {
                prompt = "操作失败！";
            }
            log.error("评价开关：sid=" + serverId + ",操作结果:" + resultMap.getString("msg"));
            BackendLogUtil.getInstance().log(request, "评价开关"+ evaluate.geteType()+",状态："+evaluate.isState());
            return Toolkit.outResult(resultMap.getBoolean("ok"), prompt);
        } catch (Exception e) {
            log.error(e);
            return Toolkit.outResult(false, "操作失败");
        }
    }

    @At
    public Object getEvaluateList(int serverId) {
        Condition cnd = Cnd.where("serverId", "=", serverId).and("isDelete", "=", 0).desc("id");
        List<Evaluate> evaluateList = dao.query(Evaluate.class, cnd);
        return Toolkit.outResult(true, evaluateList);
    }

    @At
    public Object delEvaluate(int id) {
        Evaluate evaluate = dao.fetch(Evaluate.class, id);
        if (evaluate != null) {
            evaluate.setIsDelete(1);
            int num = dao.update(evaluate);
            return Toolkit.outResult(num == 1);
        }
        return Toolkit.outResult(false);
    }
}




