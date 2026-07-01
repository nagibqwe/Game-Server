package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.CyAnnounceManager;
import com.backend.manager.ServerListManager;
import com.backend.service.TaskTimerService;
import com.backend.struct.RoleState;
import com.backend.utils.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@IocBean
@Ok("json")
@At("/announce")
@Fail("http:500")
public class AnnounceModule {

    private static final Logger log = Logger.getLogger(AnnounceModule.class);

    private static HashMap<Integer, String> stateMap = new HashMap<>();
    private static SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Inject
    protected Dao dao;

    @Inject
    private TaskTimerService taskTimer;

    static {
        //0-可用  1-停用  2-过期  3-次数耗尽  4-删除
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        stateMap.put(0, msg.get("announce.qiyong"));
        stateMap.put(1, msg.get("announce.stop"));
        stateMap.put(2, msg.get("announce.overtime"));
        stateMap.put(3, msg.get("announce.timesOver"));
        stateMap.put(4, msg.get("announce.isDelete"));
    }

    @At("/immediate")
    @Ok("jsp:jsp.announce.immediate")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void immediate() {}

    @At("/cycleAnnounce")
    @Ok("jsp:jsp.announce.cycleAnnounce")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void cycleAnnounce(HttpServletRequest request) {
        request.setAttribute("nowDate", sdfhm.format(new Date()));
    }

    @At("/updateNotice")
    @Ok("jsp:jsp.announce.updateNotice")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void updateNotice(HttpServletRequest request) {
        request.setAttribute("nowDate", sdfhm.format(new Date()));
    }


    @At
    @POST
    public Object queryByType(@Param("..") Pager pager, @Param("type") int type) {
        Cnd cnd = Cnd.where("type", " = ", type);
        QueryResult qr = new QueryResult();
        qr.setList(dao.query(Announce.class, cnd, pager));
        pager.setRecordCount(dao.count(Announce.class, cnd));
        qr.setPager(pager);
        return Toolkit.outResult(true).setv("qr", qr);
    }

    @At
    @POST
    public Object addImmediateAnnounce(HttpServletRequest request,String groupName, String[] serverId, int type, String content, String reason) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (serverId == null || serverId.length <= 0) {
            return Toolkit.outResult(false, msg.get("announce.imedia.serverNull"));
        }
        List<Integer> serverIdList = new ArrayList<>();
        for (String sid : serverId) {
            if (StringUtils.isNumber(sid)) {
                int id = Integer.parseInt(sid);
                serverIdList.add(id);
            }
        }

        if (Strings.isBlank(reason)) {
            return Toolkit.outResult(false, msg.get("announce.imedia.reasonNull"));
        }
        if (serverIdList.size() < 1) {
            return Toolkit.outResult(false, msg.get("announce.imedia.serverNull"));
        }

        Cnd gp = Cnd.where("isDeleted", "=", 0).and("serverId", "in", serverIdList);
        List<Server> serverList = dao.query(Server.class, gp);

        StringBuilder sb = new StringBuilder();
        for (Server server : serverList) {
            NutMap ret = GameServerRequestUtil.gmPublishAnnounce(server, type, content);
            if (ret.getBoolean("ok")) {
                sb.append(server.getServerName()).append(ret.get("msg")).append("\n");
            } else {
                sb.append(server.getServerName()).append("失败，原因：").append(ret.get("msg")).append("\n");
            }
        }

        Announce a = new Announce();
        a.setUserId(user.getId());
        a.setUserName(user.getName());
        a.setCreateTime(System.currentTimeMillis());
        a.setCreateDate(sdfhm.format(new Date()));
        a.setGroupName(groupName);
        a.setServerIds(JsonUtils.toJSONString(serverId));
        a.setType(type);
        a.setContent(content);
        a.setReason(reason);
        dao.insert(a);
        //保存公告内容
        BackendLogUtil.getInstance().log(request, "添加即时公告\t理由：" + reason + "\t" + JsonUtils.toJSONString(serverIdList) + "\t" + content + "\t结果：" + sb.toString());
        return Toolkit.outResult(true, sb.toString());
    }

    @At
    @POST
    public Object addAnnounceTemplate(HttpServletRequest request, String content) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        Announce a = new Announce();
        a.setContent(content);
        a.setUserId(user.getId());
        a.setUserName(user.getName());
        a.setCreateTime(System.currentTimeMillis());
        a.setCreateDate(sdfhm.format(new Date()));
        a.setType(0);
        Announce b = dao.insert(a);
        if (b != null) {
            return Toolkit.outResult(true, msg.get("announce.imedia.saveOk"));
        }
        return Toolkit.outResult(false, msg.get("announce.imedia.saveFailure"));
    }

    @At
    @POST
    public Object parseAnnounce(int announceId, int type) {
        Cnd cnd = Cnd.where("type", " = ", type).and("id", "=", announceId);
        return dao.fetch(Announce.class, cnd);
    }

    @At
    @POST
    public Object deleteImmediateAnnounce(@Param("announceId") int announceId, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        int num = dao.delete(Announce.class, announceId);
        boolean b = num > 0;
        BackendLogUtil.getInstance().log(request, "删除即时公告，结果：" + b);
        return Toolkit.outResult(b, msg.get("announce.do.success"));
    }

    @At
    public Object cycleAnnouncelist(@Param("..") Pager pager, @Param("page") int page, @Param("rows") int rows) {
        Map<String, Object> cycleList = new HashMap<>();
        Cnd cnd = Cnd.where("state", "=", 0);
        cnd.orderBy("createTime", "desc");
        List<CyAnnounce> enableList = dao.query(CyAnnounce.class, cnd,pager);
        for (CyAnnounce cya : enableList) {
            cya.setStateStr(stateMap.getOrDefault(cya.getState(), "未知"));
        }
        QueryResult qr = new QueryResult();
        qr.setList(enableList);
        pager.setRecordCount(dao.count(CyAnnounce.class,cnd));
        qr.setPager(pager);

        cnd = Cnd.where("state", ">", 0).and("state", "<", 4);
        cnd.orderBy("createTime", "desc");
        List<CyAnnounce> disableList = dao.query(CyAnnounce.class, cnd);
        for (CyAnnounce cya : disableList) {
            cya.setStateStr(stateMap.getOrDefault(cya.getState(), "未知"));
        }

        int fromIndex = 0;
        int toIndex = 0;
        if (null != disableList){
            fromIndex = rows*(page - 1);
            toIndex = rows*page >= disableList.size() ? disableList.size() : rows*page;
        }
        return Toolkit.outResult(true).setv("qr", qr).setv("total",disableList.size()).setv("rows",disableList.subList(fromIndex,toIndex));
    }

    @At
    @POST
    public Object addAnnounce(HttpServletRequest request, CyAnnounce announce, String[] serverId) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "不存在该用户");
        }
        if (Strings.isBlank(announce.getGroupName()) || serverId == null || serverId.length < 1) {
            return Toolkit.outResult(false, msg.get("announce.imedia.serverNull"));
        }

        if (announce.getTotalTimes() < 0 || announce.getState() < 0 ||
                announce.getState() > 1 || announce.getCycleInterval() < 1) {
            return Toolkit.outResult(false, msg.get("announce.add.timesError"));
        }

        if (Strings.isBlank(announce.getContent()) || announce.getType() <= 0) {
            return Toolkit.outResult(false, msg.get("announce.add.contentError"));
        }
        if (!(Pattern.matches("(\\d{1,4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2})", announce.getFromDate())
                && Pattern.matches("(\\d{1,4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2})", announce.getToDate()))) {
            return Toolkit.outResult(false, msg.get("announce.add.endTimeError"));
        }

        StringBuilder sb = new StringBuilder();

        for (String sid : serverId) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(sid);
        }

        try {
            long createTime = System.currentTimeMillis();
            String createDate = sdfhm.format(new Date(createTime));
            long fromTime = sdfhm.parse(announce.getFromDate()).getTime();
            long toTime = sdfhm.parse(announce.getToDate()).getTime();
            String batchTag = createTime + String.valueOf(RandomUtil.random(100000, 999999));
            announce.setBatchTag(batchTag);
            announce.setServerIds(sb.toString());
            announce.setCreateUserId(user.getId());
            announce.setCreateUserName(user.getName());
            announce.setCreateTime(createTime);
            announce.setCreateDate(createDate);
            announce.setFromTime(fromTime);
            announce.setToTime(toTime);
            announce.setNowTimes(0);
            announce.setNextTimes(0L);
            announce.setNextDate("");

            CyAnnounce cb = dao.insert(announce);
            if (cb != null) {
                boolean bn = CyAnnounceManager.getInstance().addCyAnnounce(cb);
                if (bn) {
                    //开启一个新的计时器
                    taskTimer.StartAnnounceTask(cb.getCycleInterval());
                }
                BackendLogUtil.getInstance().log(request, "发布即时公告:" + cb.getBatchTag() + "\t" + cb.getContent() + "\t结果：" + cb.getId());
                return Toolkit.outResult(true, msg.get("announce.add.saveSuccess"));
            } else {
                return Toolkit.outResult(false, msg.get("announce.add.savefailure"));
            }
        } catch (ParseException e) {
            log.error(e);
        }

        return Toolkit.outResult(false, msg.get("announce.do.failure"));
    }

    @At
    @POST
    public Object sendUpdateNotice(HttpServletRequest request, String[] serverId, String content, String items, int type) {
        if (serverId==null||serverId.length == 0||Strings.isBlank(content) || Strings.isBlank(items)) {
            return Toolkit.outResult(false, "param error");
        }
        StringBuilder serverIdStr = new StringBuilder();
        boolean sendSuccess = false;
        for (String sid:serverId) {
            Server server = ServerListManager.getInstance().getServer(sid);
            if (server == null) {
                return Toolkit.outResult(false, "服务器连接信息获取失败,serverId="+serverId);
            }
            NutMap resultMap = GameServerRequestUtil.gmSendUpdateNotice(server, content, items, type>0?"1":String.valueOf(type));
            if (resultMap.getBoolean("ok")) {
                serverIdStr.append(sid).append(",");
                sendSuccess = true;
            }
        }

        if(sendSuccess){
            UpdateNotice bean = new UpdateNotice();
            bean.setServerIds(serverIdStr.toString());
            bean.setContent(content);
            bean.setReward(items);
            bean.setType(type);
            bean = dao.insert(bean);
            BackendLogUtil.getInstance().log(request, "设置更新公告成功,ID="+bean.getId());
        }
        return Toolkit.outResult(sendSuccess,"更新公告成功");
    }

    @At
    @POST
    public Object queryNoticeList(@Param("..") Pager pager) {
        Cnd cnd = Cnd.NEW();
        cnd.orderBy("id", "desc");
        List<UpdateNotice> noticeList = dao.query(UpdateNotice.class, cnd, pager);
        pager.setRecordCount(dao.count(UpdateNotice.class, cnd));
        return new QueryResult(noticeList, pager);
    }

    @At
    @POST
    public Object queryById(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        UpdateNotice data = dao.fetch(UpdateNotice.class, id);
        if (data != null) {
            return Toolkit.outResult(true, data);
        }
        return Toolkit.outResult(false, msg.get("announce.query.noRecord"));
    }


    @At
    @POST
    public Object jYCyAnnounce(@Param("announceId") int announceId, @Param("state") int state, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        CyAnnounce ca = dao.fetch(CyAnnounce.class, Cnd.where("id", "=", announceId));
        if (ca == null) {
            return Toolkit.outResult(false, msg.get("announce.query.noRecord"));
        }

        ca.setState(state);
        if (state == 0) {
            boolean bn = CyAnnounceManager.getInstance().addCyAnnounce(ca);
            if (bn) {
                taskTimer.StartAnnounceTask(ca.getCycleInterval());
            }
        }
        int num = CyAnnounceManager.getInstance().updateSave(ca);
        boolean b = num > 0;
        User user = (User) request.getSession().getAttribute("USER");
        if (user != null) {
            BackendLogUtil.getInstance().log(request, String.format("更新公告状态，id: %s, state: %s, 结果 %s：", announceId, state, b));
        }
        return Toolkit.outResult(b, msg.get("announce.do.success"));
    }

    @At
    @POST
    public Object deleteCyAnnounce(@Param("announceId") int announceId, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        CyAnnounce ca = dao.fetch(CyAnnounce.class, Cnd.where("id", "=", announceId));
        if (ca == null) {
            return Toolkit.outResult(false, msg.get("announce.query.noRecord"));
        }
        CyAnnounceManager.getInstance().remove(ca);
        BackendLogUtil.getInstance().log(request, "删除发送公告\t" + announceId);
        return Toolkit.outResult(true, msg.get("announce.do.success"));
    }

}
