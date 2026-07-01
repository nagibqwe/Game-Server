package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.utils.*;
import net.sf.json.JSON;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/mail")
@Fail("http:500")
public class GameMailModule {

    private static final Log log = Logs.get();

    private static HashMap<Integer, String> stateMap = new HashMap<>();
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static int allServerMailState = 6;//标识多个服务器发送全服邮件的状态信息

    @Inject
    protected Dao dao;

    static {
        //1：待批准，2：发送未成功，3：已发送成功，4：不允许发送，5已删除
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        stateMap.put(1, msg.get("mail.list.state1"));
        stateMap.put(2, msg.get("mail.list.state2"));
        stateMap.put(3, msg.get("mail.list.state3"));
        stateMap.put(4, msg.get("mail.list.state4"));
        stateMap.put(5, msg.get("mail.list.state5"));
    }

    @At
    @Ok("jsp:jsp.mail.maillist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void mailList(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    @Ok("jsp:jsp.mail.sendmail")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void sendMail() {}

    @At
    @Ok("jsp:jsp.mail.sendsupermail")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void sendSuperMail() {}

    @At
    @Ok("jsp:jsp.mail.sendAllMail")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void sendAllMail() {
        HttpServletRequest request = Mvcs.getReq();
        HttpSession session = request.getSession();
        List<String> groupList = ServerListManager.getInstance().getGroupList();
        JSON groupServer = ServerListManager.getInstance().getGroupServer(groupList,1);
        session.setAttribute("groupServer",groupServer);
    }

    @At
    @Ok("jsp:jsp.mail.allMailList")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void allMailList() {}

    @At
    @POST
    public Object queryRoleIds(@Param("roleIds") String roleIds, @Param("serverId") int serverId) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (Strings.isBlank(roleIds) && !roleIds.equals("all")) {
            return Toolkit.outResult(false, msg.get("mail.query.roleidsError"));
        }

        Dblog dblog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", serverId));
        if (dblog == null) {
            return Toolkit.outResult(false, msg.get("mail.server.notexist"));
        }

        SimpleDataSource sds = DbConfigUtil.getInstance().getSDS(dblog);
        try (Connection conn = sds.getConnection();
            Statement stmt = conn.createStatement()) {
            String sql = "select roleId from rolestate where roleId in (" + roleIds + ");";
            ResultSet rs = stmt.executeQuery(sql);
            String[] ids = roleIds.split(",");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(ids));
            while (rs.next()) {
                String roleId = rs.getLong(1) + "";
                list.remove(roleId);
            }
            if (list.size() > 0) {
                return Toolkit.outResult(false, list.toString() + msg.get("mail.query.begin") + serverId + msg.get("mail.query.end"));
            }
            return Toolkit.outResult(true, msg.get("mail.query.roleidsCorrect"));
        } catch (SQLException e) {
            log.error(e);
            return Toolkit.outResult(false, "查询的角色错误！");
        }
    }

    @At
    @POST
    public Object saveMail(HttpServletRequest request, MailData mailData) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }
        if (StringUtils.isEmpty(mailData.getRoleIds()) || StringUtils.isEmpty(mailData.getTitle())
                || StringUtils.isEmpty(mailData.getContent()) || StringUtils.isEmpty(mailData.getReason())) {
            return Toolkit.outResult(false, language.get("mail.save.paramError"));
        }
        if (mailData.getServerId() < 1) {
            return Toolkit.outResult(false, language.get("mail.server.notexist"));
        }

        //保存邮件
        mailData.setCreateDate(format.format(new Date()));
        mailData.setCreateUser(user.getName());
        if (mailData.getItems().trim().length() > 0) {
            mailData.setAdminState(1);
        }
        MailData da = dao.insert(mailData);
        if (da == null) {
            return Toolkit.outResult(false, language.get("mail.save.updateError"));
        }
        BackendLogUtil.getInstance().log(request, "保存邮件\t理由：" + da.getReason() + "\t 服务器ID：" + da.getServerId() + ",角色列表：" + da.getRoleIds() + "\t标题：" + da.getTitle());

        // 小于则直接发送邮件
        if (mailData.getItems().length() < 1) {
            boolean bn = sendMail(da);
            BackendLogUtil.getInstance().log(request, "发送邮件\t邮件ID：" + mailData.getId() + "\t 服务器ID：" + da.getServerId() + ",结果:" + bn + "\t信息：" + da.getSendErrorMess());
            return Toolkit.outResult(true, language.get("mail.list.state3"));
        }
        return Toolkit.outResult(true, language.get("mail.save.success"));
    }

    @At
    @POST
    public Object query(Pager pager, int type, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }

        Cnd cnd = cnd(type, request, user);
        List<MailData> list = dao.query(MailData.class, cnd, pager);
        QueryResult qr = new QueryResult(list, pager);
        pager.setRecordCount(dao.count(MailData.class, cnd));

        for (MailData dat : list) {
			dat.setItemStr("");
			dat.setStateStr(stateMap.get(dat.getAdminState()));
        }
        return qr;
    }
    @At
    @POST
    public Object queryAll(@Param("page") int page, @Param("rows") int rows,int type, HttpServletRequest request) {
        if (type == 0){
            return Toolkit.outResult(true).setv("total",0).setv("rows",0);
        }
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }

        Cnd cnd = cnd(type, request, user);
        List<AllMailData> list = dao.query(AllMailData.class, cnd);
        for (AllMailData dat : list) {
            dat.setItemStr("");
            if (dat.getAdminState() != allServerMailState){
                dat.setStateStr(stateMap.get(dat.getAdminState()));
            }else {
                dat.setStateStr(dat.getSendErrorMess());//多个服务器发送全服邮件的状态信息
            }
        }
        int fromIndex = 0;
        int toIndex = 0;
        if (null != list){
            fromIndex = rows*(page - 1);
            toIndex = rows*page >= list.size() ? list.size() : rows*page;
        }
        return Toolkit.outResult(true).setv("total",list.size()).setv("rows",list.subList(fromIndex,toIndex));
    }
    private Cnd cnd(int type, HttpServletRequest request, User user){
        Cnd cnd = Cnd.where("isDelete", "=", 0);
        String date = ParamUtil.getString(request, "queryDate");
        switch (type) {
            case 1:
                cnd.and("adminState", "=", 1);
                break;
            case 2:
                cnd.and("createUser", "=", user.getName());
                cnd.and("createDate", "<=", date + " 23:59:59");
                cnd.and("adminState", ">", 2);
                cnd.orderBy("id", "desc");
                break;
            case 3:
                cnd.and("adminState", ">=", 2);
                cnd.orderBy("id", "desc");
                break;
        }
        return cnd;
    }
    @At
    @POST
    public Object queryMailList(@Param("..") Pager pager) {
        Cnd cnd = Cnd.where("isDelete", "=", 0);
        cnd.orderBy("id", "desc");
        List<MailData> mailList = dao.query(MailData.class, cnd, pager);
        pager.setRecordCount(dao.count(MailData.class, cnd));
        return new QueryResult(mailList, pager);
    }

    /**
     * 多选服务器发送邮件(全服邮件发送)查询邮件列表
     * @param pager
     * @return
     */
    @At
    @POST
    public Object queryAllMailList(@Param("..") Pager pager) {
        Cnd cnd = Cnd.where("isDelete", "=", 0);
        cnd.orderBy("id", "desc");
        List<AllMailData> mailList = dao.query(AllMailData.class, cnd, pager);
        pager.setRecordCount(dao.count(AllMailData.class, cnd));
        return new QueryResult(mailList, pager);
    }


    @At
    @POST
    public Object queryById(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        MailData data = dao.fetch(MailData.class, id);
        if (data != null) {
            return Toolkit.outResult(true, data);
        }
        return Toolkit.outResult(false, msg.get("mail.admin.updateNoRecord"));
    }

    /**
     * 多选服务器发送邮件(全服邮件发送)根据邮件id查询邮件信息
     * @param id
     * @return
     */
    @At
    @POST
    public Object queryAllById(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        AllMailData data = dao.fetch(AllMailData.class, id);
        if (data != null) {
            return Toolkit.outResult(true, data);
        }
        return Toolkit.outResult(false, msg.get("mail.admin.updateNoRecord"));
    }

    @At
    @POST
    public Object adminMail(int id, int type, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        MailData da = dao.fetch(MailData.class, id);
        if (da == null) {
            return Toolkit.outResult(false, msg.get("mail.admin.updateNoRecord"));
        }
        da.setAdminUser(user.getName());
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            boolean isOk = sendMail(da);
            BackendLogUtil.getInstance().log(request, "发送邮件\t邮件ID：" + da.getId() + "\t 服务器ID：" + da.getServerId() + ",结果:" + isOk + "\t信息：" + da.getSendErrorMess());
            if (!isOk) {
                return Toolkit.outResult(false, da.getSendErrorMess());
            }
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(user.getName() + msg.get("mail.admin.notallow"));
            if (dao.update(da) < 1) {
                return Toolkit.outResult(false, msg.get("mail.admin.updateError"));
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(user.getName() + "删除了本邮件");
            int num = dao.update(da);
            BackendLogUtil.getInstance().log(request, "删除邮件\t邮件ID：" + da.getId() + ",结果:" + num + "\t信息：" + da.getTitle());
            if (num < 1) {
                return Toolkit.outResult(false, msg.get("mail.admin.updateError"));
            }
        }
        return Toolkit.outResult(true, msg.get("mail.admin.updateSuccess"));
    }

    /**
     * 一键发送所有的未发送的邮件
     */
    @At
    @POST
    public Object onekeySend(HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        // 检查权限是否足够
        Cnd cnd = Cnd.where("isDelete", "=", 0).and("adminState", "<", 3);
        List<MailData> list = dao.query(MailData.class, cnd);
        String ctime = format.format(new Date());
        for (MailData da : list) {
            da.setAdminUser(user.getName());
            da.setAdminDate(ctime);
            boolean bn = sendMail(da);
            int num = dao.update(da);
            BackendLogUtil.getInstance().log(request, "发送邮件\t邮件ID：" + da.getId() + "\t " +
                    "服务器ID：" + da.getServerId() + ",结果:" + bn + " , 更新记录数=" + num + "\t信息：" + da.getSendErrorMess());
        }
        return Toolkit.outResult(true, msg.get("mail.onekey.success"));
    }

    //向服务器发送邮件
    private boolean sendMail(MailData mailData) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        if (mailData.getSended() > 0 || mailData.getIsDelete() > 0) {
            log.error("邮件(" + mailData.getId() + ")已发送或被删除");
            return false;
        }

        if (mailData.getAdminState() > 1) {
            log.error("邮件(" + mailData.getId() + ")状态为" + mailData.getAdminState() + "不予发送");
            return false;
        }

        int sid = QueryUtil.getInstance().getHeFuId(mailData.getServerId());
        Server server = ServerListManager.getInstance().getServer(sid);
        if (server == null) {
            mailData.setAdminState(2);
            mailData.setSendErrorMess(language.get("mail.send.serverNull"));
            dao.update(mailData);
            log.error("t_server中未找到serverId=" + mailData.getServerId());
            return false;
        }

        NutMap result = GameServerRequestUtil.gmSendMail(server, mailData);
        long st = System.currentTimeMillis();
        mailData.setSended(1);
        mailData.setSendErrorMess(result.getString("msg"));
        if (result.getBoolean("ok")) {
            mailData.setAdminState(3);
        } else {
            mailData.setAdminState(2);
        }
        long et = System.currentTimeMillis();
        log.error("邮件(id:" + mailData.getId() + ")发送至(sid:" + sid + ",roleId:" + mailData.getRoleIds() + "),结果:" + result.getString("msg") + ",耗时:" + (et - st));
        dao.update(mailData);
        return true;
    }

    /**
     * 发送多个服务器的全服邮件
     * @param request
     * @param serverids
     * @param minLevel
     * @param maxLevel
     * @param career
     * @param mailTitle
     * @param mailContent
     * @param reason
     * @param items
     * @return
     */
    @At
    @POST
    public Object saveAllMail(HttpServletRequest request,@Param("groupName") String groupName,
                              @Param("serverids") String serverids,@Param("minLevel") int minLevel,
                              @Param("maxLevel") int maxLevel,@Param("career") int career,
                              @Param("mailTitle") String mailTitle,@Param("mailContent") String mailContent,
                              @Param("reason") String reason,@Param("items") String items) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }
        if (StringUtils.isEmpty(serverids) || StringUtils.isEmpty(mailTitle) || StringUtils.isEmpty(mailContent) || StringUtils.isEmpty(reason)) {
            return Toolkit.outResult(false, language.get("mail.save.paramError"));
        }
        AllMailData mailData = new AllMailData();
        //保存邮件
        mailData.setCreateDate(format.format(new Date()));
        mailData.setCreateUser(user.getName());
        mailData.setGroupName(groupName);
        mailData.setServerIdList(serverids);
        mailData.setMinLv(minLevel);
        mailData.setMaxLv(maxLevel);
        mailData.setCareer(career);
        mailData.setTitle(mailTitle);
        mailData.setContent(mailContent);
        mailData.setReason(reason);
        mailData.setItems(items);
        if (mailData.getItems().trim().length() > 0) {
            mailData.setAdminState(1);
        }
        AllMailData da = dao.insert(mailData);
        if (da == null) {
            return Toolkit.outResult(false, language.get("mail.save.updateError"));
        }
        BackendLogUtil.getInstance().log(request, "保存全服邮件\t理由：" + da.getReason() + "\t 服务器列表ID：" + da.getServerIdList() + "\t标题：" + da.getTitle());

        // 小于则直接发送邮件
        if (mailData.getItems().length() < 1) {
            StringBuilder sb = sendAllServerMail(da);
            BackendLogUtil.getInstance().log(request, "发送全服邮件\t邮件ID：" + mailData.getId() + "\t 服务器列表ID：" + da.getServerIdList() + ",结果:" + sb.toString() + "\t信息：" + da.getSendErrorMess());
            return Toolkit.outResult(true, sb.toString());
        }
        return Toolkit.outResult(true, language.get("mail.save.success"));
    }

    /**
     * 向服务器发送多个服务器的全服邮件
     * @param mailData
     * @return
     */
    private StringBuilder sendAllServerMail(AllMailData mailData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbSendErrorMess = new StringBuilder();
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        if (mailData.getSended() > 0 || mailData.getIsDelete() > 0) {
            log.error("邮件(" + mailData.getId() + ")已发送或被删除");
            return sb.append("邮件(" + mailData.getId() + ")已发送或被删除");
        }

        if (mailData.getAdminState() > 1) {
            log.error("邮件(" + mailData.getId() + ")状态为" + mailData.getAdminState() + "不予发送");
            return sb.append("邮件(" + mailData.getId() + ")状态为" + mailData.getAdminState() + "不予发送");
        }
        String serverIdStr = mailData.getServerIdList();
        if (serverIdStr.contains("[")){
            serverIdStr = serverIdStr.replace("[","").replace("]","");
        }
        String[] serverIdList = serverIdStr.split(",");
        NutMap result = new NutMap();
        Set<Integer> sids = new HashSet<>();//存放不重复的合服后的服务器id
        for (String serverId:serverIdList) {
            int sid = QueryUtil.getInstance().getHeFuId(Integer.parseInt(serverId));
            sids.add(sid);
        }
        sb.append("邮件(id:" + mailData.getId()+")");
        for (Integer sid:sids){
            Server server = ServerListManager.getInstance().getServer(sid);
            if (server == null) {
                mailData.setAdminState(allServerMailState);
                sbSendErrorMess.append(sid+language.get("mail.send.serverNull")).append("\n");
//                mailData.setSendErrorMess(language.get("mail.send.serverNull"));
                dao.update(mailData);
                sb.append("发送至服务器id:"+sid+",失败未找到serverId="+sid).append("\n");
                log.error("t_server中未找到serverId=" + sid);
            }else {
                result = GameServerRequestUtil.gmSendAllMail(server, mailData, mailData.getServerIdList());//向游戏服务器发送命令
                mailData.setSended(1);
                sbSendErrorMess.append(result.getString("msg")).append("\n");
//                mailData.setSendErrorMess(result.getString("msg"));
                if (result.getBoolean("ok")) {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至服务器id:"+sid+"成功").append("\n");
                    sb.append("发送至服务器id:"+sid+"成功").append("\n");
                } else {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至服务器id:"+sid+"失败").append("\n");
                    sb.append("发送至服务器id:"+sid+"失败").append("\n");
                }
                mailData.setSendErrorMess(sbSendErrorMess.toString());
                log.error("邮件(id:" + mailData.getId() + ")发送至(sid:" + sid + "),结果:" + result.getString("msg") + ")");
                dao.update(mailData);
            }
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(),sb.toString());//记录日志
        return sb;
    }

    /**
     * 向服务器发送多个服务器的全服邮件(全服邮件列表的操作)
     * @param id
     * @param type
     * @param request
     * @return
     */
    @At
    @POST
    public Object adminAllMail(int id, int type, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        AllMailData da = dao.fetch(AllMailData.class, id);
        if (da == null) {
            return Toolkit.outResult(false, msg.get("mail.admin.updateNoRecord"));
        }
        da.setAdminUser(user.getName());
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            StringBuilder sb = sendAllServerMail(da);
            BackendLogUtil.getInstance().log(request, "发送全服邮件\t邮件ID：" + da.getId() + "\t 服务器ID列表：" + da.getServerIdList() + ",结果:" + sb.toString() + "\t信息：" + da.getSendErrorMess());
            return Toolkit.outResult(false, sb.toString());
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(user.getName() + msg.get("mail.admin.notallow"));
            if (dao.update(da) < 1) {
                return Toolkit.outResult(false, msg.get("mail.admin.updateError"));
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(user.getName() + "删除了本邮件");
            int num = dao.update(da);
            BackendLogUtil.getInstance().log(request, "删除全服邮件\t邮件ID：" + da.getId() + ",结果:" + num + "\t信息：" + da.getTitle());
            if (num < 1) {
                return Toolkit.outResult(false, msg.get("mail.admin.updateError"));
            }
        }
        return Toolkit.outResult(true, msg.get("mail.admin.updateSuccess"));
    }

    /**
     * 向服务器发送多个服务器的全服邮件(全服邮件列表一键发送的操作)
     * @param request
     * @return
     */
    @At
    @POST
    public Object onekeySendAll(HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        // 检查权限是否足够
        Cnd cnd = Cnd.where("isDelete", "=", 0).and("adminState", "=", 1);//一键发送待批准的邮件
        List<AllMailData> list = dao.query(AllMailData.class, cnd);
        String ctime = format.format(new Date());
        StringBuilder sb = new StringBuilder();
        StringBuilder sbAllMail = new StringBuilder();
        for (AllMailData da : list) {
            da.setAdminUser(user.getName());
            da.setAdminDate(ctime);
            sb = sendAllServerMail(da);
            int num = dao.update(da);
            BackendLogUtil.getInstance().log(request, "发送全服邮件\t邮件ID：" + da.getId() + "\t 服务器ID列表：" + da.getServerIdList() + ",结果:" + sb.toString() + " , 更新记录数=" + num);
            sbAllMail.append(sb.toString());
        }
        return Toolkit.outResult(true, sbAllMail.toString());
    }
}
