package com.backend.admin;

import com.backend.bean.BackendLog;
import com.backend.bean.Item;
import com.backend.manager.*;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@IocBean
@At("/admin")
@Ok("json")
@Fail("http:500")
public class AdminModule {

    private static final Logger log = Logger.getLogger(AdminModule.class);
    private DateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    protected Dao dao;

    @At
    public Object getBackendLog(@Param("id") int id, @Param("content") String content, @Param("..") Pager pager,
                                @Param("startDate") String startDate, @Param("endDate") String endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(ymdhms.parse(startDate));
            end.setTime(ymdhms.parse(endDate));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        Cnd cnd = Cnd.where("time", ">", start.getTimeInMillis()).and("time", "<", end.getTimeInMillis());
        if (id != 0) {
            cnd.and("userId", "=", id);
        }

        if (!Strings.isBlank(content)) {
            cnd.and("content", "like", "%" + content + "%");
        }

        QueryResult qr = new QueryResult();
        qr.setList(dao.query(BackendLog.class, cnd, pager));
        pager.setRecordCount(dao.count(BackendLog.class, cnd));
        qr.setPager(pager);
        return qr;
    }

    @At
    public Object reloadAllServer(HttpServletRequest request) {
        LoginServerManager.getInstance().loadAll();
        DbLogListManager.getInstance().reloadAll();
        ServerListManager.getInstance().load();
        BackendLogUtil.getInstance().log(request, "reloadAllServer");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadLoginServer(HttpServletRequest request) {
        LoginServerManager.getInstance().loadAll();
        BackendLogUtil.getInstance().log(request, "reloadLoginServer");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadPlatFormServerInfo(HttpServletRequest request) {
        DbLogListManager.getInstance().reloadAll();
        BackendLogUtil.getInstance().log(request, "reloadGameLog");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadServerList(HttpServletRequest request) {
        ServerListManager.getInstance().load();
        BackendLogUtil.getInstance().log(request, "reloadGameWorld");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadBlackList(HttpServletRequest request) {
        BlackListManager.getInstance().loadData();
        BackendLogUtil.getInstance().log(request, "reloadBlackList");
        return Toolkit.outResult(true);
    }

    @At
    @POST
    public Object getAllItem() {
        List<Item> list = new ArrayList<>(ItemManager.getInstance().getItemList().values());
        return Toolkit.outResult(true, list);
    }

}
