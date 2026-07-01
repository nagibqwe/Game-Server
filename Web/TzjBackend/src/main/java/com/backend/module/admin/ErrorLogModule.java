package com.backend.module.admin;

import com.backend.bean.ErrorLog;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 游戏日志收集系统
 */
@IocBean
@At("/error")
@Ok("json")
@Filters
public class ErrorLogModule {

    private static final Logger log = Logger.getLogger(ErrorLogModule.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.error.list")
    public void index(HttpServletRequest request) {
        long now = System.currentTimeMillis();
        String create = format.format(new Date(now - 24 * 3600 * 1000));
        String endTime = format.format(new Date());
        request.setAttribute("yesDate", create);
        request.setAttribute("nowDate", endTime);
    }

    @At
    @POST
    public Object query(@Param("..") Pager page, @Param("serverId") int serverId, @Param("type") int type,
                        @Param("reKey") String reKey, @Param("begin") String begin, @Param("end") String end) {

        Cnd cnd = Cnd.where("receTime", ">=", begin).and("state", "=", 0);

        if (end.length() > 0) {
            cnd.and("receTime", "<=", end);
        }

        if (type > 0) {
            cnd.and("type", "=", type);
        }

        if (reKey.length() > 0) {
            cnd.and("mKey", "like", "%" + reKey + "%");
        }

        if (serverId > 0) {
            cnd.and("serverId", "=", serverId);
        }

        cnd.orderBy("receTime", "desc");

        QueryResult qr = new QueryResult();
        List<ErrorLog> list = dao.query(ErrorLog.class, cnd, page);
        qr.setList(list);
        page.setRecordCount(dao.count(ErrorLog.class, cnd));
        qr.setPager(page);
        return qr;
    }

    @At
    public Object addlog(@Param("..") ErrorLog elog) {

        String sb = "serverId=" + elog.getServerId() + "platform" + elog.getPlatform()
                + "type=" + elog.getType() + "mKey=" + elog.getmKey() + "content="
                + elog.getContent() + "lastValue=" + elog.getLastValue() + "lsbGameKey201512121419";
        String md5 = Lang.md5(sb).toLowerCase();

        if (!md5.equalsIgnoreCase(elog.getSign())) {
            return Toolkit.outResult(false, "参数错误！");
        }

        long createTime = System.currentTimeMillis();
        String createDate = format.format(new Date(createTime));
        elog.setReceTime(createDate);

//        try {
//            String result = RobotUtil.pushMessage(Json.toJson(elog));
//            log.info("推送消息：result->" + result);
//        } catch (ApiException e) {
//            log.error("发送推送消息出现异常");
//            e.printStackTrace();
//        }

        Cnd cd = Cnd.where("serverId", "=", elog.getServerId()).and("mKey", "like", elog.getmKey()).and("content", "like", elog.getContent());
        cd.orderBy("receTime", "desc");
        ErrorLog el = dao.fetch(ErrorLog.class, cd);
        if (el != null) {
            //设置最后的接收时间
            el.setReceTime(createDate);
            //设置接收的次数
            el.setLastValue(elog.getLastValue() + el.getLastValue());
            dao.update(el);
        } else {
            dao.insert(elog);
        }
        return Toolkit.outResult(true, "操作成功 ");
    }

    @At
    @POST
    public Object delete(@Param("id") int id) {
        ErrorLog errorLog = dao.fetch(ErrorLog.class, id);

        if (errorLog == null) {
            return Toolkit.outResult(false, "找不到记录！");
        }

        Cnd cnd = Cnd.where("content", "=", errorLog.getContent());
        int num = dao.update(ErrorLog.class, Chain.make("state", 1), cnd);

        return Toolkit.outResult(true, "更新了" + num + "条");
    }

}
