package com.gm.project.gmtool.mail.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.project.gmtool.allMail.domain.AllMailData;
import com.gm.project.gmtool.allMail.service.IAllMailDataService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.mail.domain.MailData;
import com.gm.project.gmtool.mail.service.IMailDataService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * 邮件列表Controller
 * 
 * @author gm
 * @date 2021-08-30
 */
@Controller
@RequestMapping("/gmtool/mail")
public class MailDataController extends BaseController
{
    private String prefix = "gmtool/mail";

    private static Logger log = LoggerFactory.getLogger(MailDataController.class);
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static int allServerMailState = 6;//标识多个服务器发送全服邮件的状态信息

    @Autowired
    private IMailDataService mailDataService;

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private IAllMailDataService allMailDataService;

    @RequiresPermissions("gmtool:mail:view")
    @GetMapping()
    public String mail()
    {
        return prefix + "/mail";
    }

    /**
     * 查询邮件列表列表
     */
//    @RequiresPermissions("gmtool:mail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HttpServletRequest request,int type)
    {
        startPage();
        String date = ParamUtil.getString(request, "queryDate");
        String createUser = ShiroUtils.getLoginName();
        List<MailData> list = new ArrayList<>();
        if (type == 1){
            list = mailDataService.selectWaitDealMail();
        }else if (type == 2){
            list = mailDataService.selectMineMail(createUser,date  + " 23:59:59");
        }else if (type == 3){
            list = mailDataService.selectHistoryMail();
        }

        return getDataTable(list);
    }

    @RequiresPermissions("gmtool:mail:allMail")
    @GetMapping("/allMail")
    public String allMail()
    {
        return prefix + "/allMail";
    }

    /**
     * 查询全服邮件列表列表
     */
//    @RequiresPermissions("gmtool:mail:queryAll")
    @PostMapping("/queryAll")
    @ResponseBody
    public TableDataInfo queryAll(HttpServletRequest request,int type)
    {
        startPage();
        String date = ParamUtil.getString(request, "queryDate");
        String createUser = ShiroUtils.getLoginName();
        List<AllMailData> list = new ArrayList<>();
        if (type == 1){
            list = allMailDataService.selectWaitDealMail();
        }else if (type == 2){
            list = allMailDataService.selectMineMail(createUser,date  + " 23:59:59");
        }else if (type == 3){
            list = allMailDataService.selectHistoryMail();
        }

        return getDataTable(list);
    }

    @RequiresPermissions("gmtool:mail:sendmail")
    @GetMapping("/sendmail")
    public String mailList()
    {
        return prefix + "/sendmail";
    }

    /**
     * 查询未删除的邮件
     * @return
     */
    @PostMapping("/queryMailList")
    @ResponseBody
    public Object queryMailList() {
        startPage();
        MailData mailData = new MailData();
        mailData.setIsDelete(0);
        List<MailData> mailList = mailDataService.selectMailDataList(mailData);
        return getDataTable(mailList);
    }

    /**
     * 超级邮件发送页面
     * @return
     */
    @RequiresPermissions("gmtool:mail:sendsupermail")
    @GetMapping("/sendsupermail")
    public String sendSuperMail()
    {
        return prefix + "/sendsupermail";
    }

    /**
     * 全服邮件发送页面
     * @return
     */
    @RequiresPermissions("gmtool:mail:sendAllMail")
    @GetMapping("/sendAllMail")
    public String sendAllMail()
    {
        return prefix + "/sendAllMail";
    }

    /**
     * 查询未删除的全服邮件
     * @return
     */
    @PostMapping("/queryAllMailList")
    @ResponseBody
    public Object queryAllMailList() {
        startPage();
        AllMailData allMailData = new AllMailData();
        allMailData.setIsDelete(0);
        List<AllMailData> mailList = allMailDataService.selectAllMailDataList(allMailData);
        return getDataTable(mailList);
    }


    /**
     * 导出邮件列表列表
     */
    @RequiresPermissions("gmtool:mail:export")
    @Log(title = "邮件列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MailData mailData)
    {
        List<MailData> list = mailDataService.selectMailDataList(mailData);
        ExcelUtil<MailData> util = new ExcelUtil<MailData>(MailData.class);
        return util.exportExcel(list, "邮件列表数据");
    }

    /**
     * 新增邮件列表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存邮件列表
     */
    @RequiresPermissions("gmtool:mail:add")
    @Log(title = "邮件列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MailData mailData)
    {
        return toAjax(mailDataService.insertMailData(mailData));
    }

    /**
     * 修改邮件列表
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MailData mailData = mailDataService.selectMailDataById(id);
        mmap.put("mailData", mailData);
        return prefix + "/edit";
    }

    /**
     * 修改保存邮件列表
     */
    @RequiresPermissions("gmtool:mail:edit")
    @Log(title = "邮件列表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MailData mailData)
    {
        return toAjax(mailDataService.updateMailData(mailData));
    }

    /**
     * 删除邮件列表
     */
    @RequiresPermissions("gmtool:mail:remove")
    @Log(title = "邮件列表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mailDataService.deleteMailDataByIds(ids));
    }

    /**
     * 邮件列表中的邮件操作
     * @param id
     * @param type
     * @param request
     * @return
     */
    @PostMapping( "/adminMail")
    @ResponseBody
    public Object adminMail(int id, int type, HttpServletRequest request) {
        String userName = ShiroUtils.getLoginName();
        MailData da = mailDataService.selectMailDataById(Long.valueOf(String.valueOf(id)));
        if (da == null) {
            return AjaxResult.info("邮件记录已经不存在了").put("ok",false);
        }
        da.setAdminUser(userName);
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            boolean isOk = sendMail(da);
            GMLogUtil.log("发送邮件\t邮件ID：" + da.getId() + "\t 服务器ID：" + da.getServerId() + ",结果:" + isOk + "\t信息：" + da.getSendErrorMess());
            if (!isOk) {
                return AjaxResult.info(da.getSendErrorMess()).put("ok",false);
            }
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(userName + "标记此邮件不允许发送！");
            if (mailDataService.updateMailData(da) < 1) {
                return AjaxResult.info("更新邮件记录失败了！").put("ok",false);
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(userName + "删除了本邮件");
            int num = mailDataService.updateMailData(da);
            GMLogUtil.log("删除邮件\t邮件ID：" + da.getId() + ",结果:" + num + "\t信息：" + da.getTitle());
            if (num < 1) {
                return AjaxResult.info("更新邮件记录失败了！").put("ok",false);
            }
        }
        return AjaxResult.info("更新邮件成功！").put("ok",true);
    }

    /**
     * 一键发送所有的未发送的邮件
     * @return
     */
    @PostMapping( "/onekeySend")
    @ResponseBody
    public Object onekeySend() {
        // 检查权限是否足够
        List<MailData> list = mailDataService.selectMailByState();
        String ctime = format.format(new Date());
        for (MailData da : list) {
            da.setAdminUser(ShiroUtils.getLoginName());
            da.setAdminDate(ctime);
            boolean bn = sendMail(da);
            int num = mailDataService.updateMailData(da);
            GMLogUtil.log("发送邮件\t邮件ID：" + da.getId() + "\t " +
                    "服务器ID：" + da.getServerId() + ",结果:" + bn + " , 更新记录数=" + num + "\t信息：" + da.getSendErrorMess());
        }
        return AjaxResult.info("一键发送功能处理成功!").put("ok",true);
    }
    //向服务器发送邮件
    private boolean sendMail(MailData mailData) {
        if (mailData.getSended() > 0 || mailData.getIsDelete() > 0) {
            log.error("邮件(" + mailData.getId() + ")已发送或被删除");
            return false;
        }

        if (mailData.getAdminState() > 1) {
            log.error("邮件(" + mailData.getId() + ")状态为" + mailData.getAdminState() + "不予发送");
            return false;
        }

        int sid = DBServerMgr.getInstance().getHeFuId(mailData.getServerId());
        TServer server = tServerService.selectTServerByServerId(sid);
        if (server == null) {
            mailData.setAdminState(2);
            mailData.setSendErrorMess("不能指定到达具体的游戏服！");
            mailDataService.updateMailData(mailData);
            log.error("t_server中未找到serverId=" + mailData.getServerId());
            return false;
        }

        HashMap result = GameServerRequestUtil.gmSendMail(server, mailData);
        long st = System.currentTimeMillis();
        mailData.setSended(1);
        mailData.setSendErrorMess(result.get("msg").toString());
        if (Boolean.valueOf(result.get("ok").toString())) {
            mailData.setAdminState(3);
        } else {
            mailData.setAdminState(2);
        }
        long et = System.currentTimeMillis();
        log.error("邮件(id:" + mailData.getId() + ")发送至(sid:" + sid + ",roleId:" + mailData.getRoleIds() + "),结果:" + result.get("msg").toString() + ",耗时:" + (et - st));
        mailDataService.updateMailData(mailData);
        return true;
    }

    /**
     *全服邮件列表中的邮件操作
     * @param id
     * @param type
     * @param request
     * @return
     */
    @PostMapping( "/adminAllMail")
    @ResponseBody
    public Object adminAllMail(int id, int type, HttpServletRequest request) {
        String userName = ShiroUtils.getLoginName();
        AllMailData da = allMailDataService.selectAllMailDataById(Long.valueOf(String.valueOf(id)));
        if (da == null) {
            return AjaxResult.info("邮件记录已经不存在了").put("ok",false);
        }
        da.setAdminUser(userName);
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            StringBuilder sb = sendAllServerMail(da);
            GMLogUtil.log("发送全服邮件\t邮件ID：" + da.getId() + "\t 服务器ID列表：" + da.getServerIdList() + ",结果:" + sb.toString() + "\t信息：" + da.getSendErrorMess());
            return AjaxResult.info(sb.toString()).put("ok",false);
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(userName + "标记此邮件不允许发送！");
            if (allMailDataService.updateAllMailData(da) < 1) {
                return AjaxResult.info("更新邮件记录失败了！").put("ok",false);
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(userName + "删除了本邮件");
            int num = allMailDataService.updateAllMailData(da);
            GMLogUtil.log("删除全服邮件\t邮件ID：" + da.getId() + ",结果:" + num + "\t信息：" + da.getTitle());
            if (num < 1) {
                return AjaxResult.info("更新邮件记录失败了！").put("ok",false);
            }
        }
        return AjaxResult.info("更新邮件成功！").put("ok",true);
    }

    /**
     * 向服务器发送多个服务器的全服邮件
     * @param mailData
     * @return
     */
    private StringBuilder sendAllServerMail(AllMailData mailData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbSendErrorMess = new StringBuilder();
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
        HashMap result = new HashMap();
        Set<Integer> sids = new HashSet<>();//存放不重复的合服后的服务器id
        for (String serverId:serverIdList) {
            int sid = DBServerMgr.getInstance().getHeFuId(Integer.parseInt(serverId));
            sids.add(sid);
        }
        sb.append("邮件(id:" + mailData.getId()+")");
        for (Integer sid:sids){
            TServer server = tServerService.selectTServerByServerId(sid);
            if (server == null) {
                mailData.setAdminState(allServerMailState);
                sbSendErrorMess.append(sid+"不能指定到达具体的游戏服！").append("\n");
//                mailData.setSendErrorMess(language.get("mail.send.serverNull"));
                allMailDataService.updateAllMailData(mailData);
                sb.append("发送至服务器id:"+sid+",失败未找到serverId="+sid).append("\n");
                log.error("t_server中未找到serverId=" + sid);
            }else {
                result = GameServerRequestUtil.gmSendAllMail(server, mailData, mailData.getServerIdList());//向游戏服务器发送命令
                mailData.setSended(1);
                sbSendErrorMess.append(result.get("msg").toString()).append("\n");
//                mailData.setSendErrorMess(result.getString("msg"));
                if (Boolean.valueOf(result.get("ok").toString())) {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至服务器id:"+sid+"成功").append("\n");
                    sb.append("发送至服务器id:"+sid+"成功").append("\n");
                } else {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至服务器id:"+sid+"失败").append("\n");
                    sb.append("发送至服务器id:"+sid+"失败").append("\n");
                }
                mailData.setSendErrorMess(sbSendErrorMess.toString());
                log.error("邮件(id:" + mailData.getId() + ")发送至(sid:" + sid + "),结果:" + result.get("msg").toString() + ")");
                allMailDataService.updateAllMailData(mailData);
            }
        }
        GMLogUtil.log(sb.toString());//记录日志
        return sb;
    }

    /**
     * 向服务器发送多个服务器的全服邮件(全服邮件列表一键发送的操作)
     * @param request
     * @return
     */
    @PostMapping("/oneKeySendAll")
    @ResponseBody
    public Object oneKeySendAll(HttpServletRequest request) {
        String userName = ShiroUtils.getLoginName();
        // 检查权限是否足够
        AllMailData allMailData = new AllMailData();
        allMailData.setIsDelete(0);
        allMailData.setAdminState(1);
        //一键发送待批准的邮件
        List<AllMailData> list = allMailDataService.selectAllMailDataList(allMailData);
        String ctime = format.format(new Date());
        StringBuilder sb = new StringBuilder();
        StringBuilder sbAllMail = new StringBuilder();
        for (AllMailData da : list) {
            da.setAdminUser(userName);
            da.setAdminDate(ctime);
            sb = sendAllServerMail(da);
            int num = allMailDataService.updateAllMailData(da);
            GMLogUtil.log("发送全服邮件\t邮件ID：" + da.getId() + "\t 服务器ID列表：" + da.getServerIdList() + ",结果:" + sb.toString() + " , 更新记录数=" + num);
            sbAllMail.append(sb.toString());
        }
        return AjaxResult.info(sbAllMail.toString()).put("ok",true);
    }

    /**
     *验证角色ID
     * @param roleIds
     * @param serverId
     * @return
     */
    @PostMapping("/queryRoleIds")
    @ResponseBody
    public Object queryRoleIds(String roleIds, Integer serverId) {
        if (StringUtils.isBlank(roleIds) && !roleIds.equals("all")) {
            return AjaxResult.info("请填写正确的角色ID值！").put("ok",false);
        }

        TServer dblog = tServerService.selectTServerByServerId(serverId);
        if (dblog == null) {
            return AjaxResult.info("请求查询的服务器并不存在").put("ok",false);
        }

        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(dblog);
        String sql = "select roleId from rolestate where roleId in (" + roleIds + ");";

        List<Long> roleIdList = dbClient.selectList(sql, (Class<Long>) null);
        String[] ids = Convert.toStrArray(roleIds);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(ids));
        if (null == roleIdList || roleIdList.size() == 0){
            return AjaxResult.info("查询的角色错误！").put("ok",false);
        }
        for (Long roleId:roleIdList){
            list.remove(String.valueOf(roleId));
        }
        if (list.size() > 0) {
            return AjaxResult.info(list.toString() + "的角色ID在" + serverId + "服中找不到！").put("ok", false);
        }
        return AjaxResult.info("输入的角色都有效！").put("ok",true);
    }

    /**
     * 邮件发送提交操作
     * @param request
     * @param mailData
     * @return
     */
    @PostMapping("/saveMail")
    @ResponseBody
    public Object saveMail(HttpServletRequest request, MailData mailData) {
        String userName = ShiroUtils.getLoginName();
        if (StringUtils.isEmpty(mailData.getRoleIds()) || StringUtils.isEmpty(mailData.getTitle())
                || StringUtils.isEmpty(mailData.getContent()) || StringUtils.isEmpty(mailData.getReason())) {
            return AjaxResult.info("提交的邮件参数有错").put("ok",false);
        }
        if (mailData.getServerId() < 1) {
            return AjaxResult.info("请求查询的服务器并不存在").put("ok",false);
        }

        //保存邮件
        mailData.setCreateDate(format.format(new Date()));
        mailData.setCreateUser(userName);
//        if (mailData.getItems().trim().length() > 0) {
            mailData.setAdminState(1);
//        }
        int num = mailDataService.insertMailData(mailData);
        MailData da = mailDataService.selectMailDataById(mailData.getId());
        if (num < 1) {
            return AjaxResult.info("保存数据失败了，通知运维检查后台连接！");
        }
        GMLogUtil.log("保存邮件\t理由：" + da.getReason() + "\t 服务器ID：" + da.getServerId() + ",角色列表：" + da.getRoleIds() + "\t标题：" + da.getTitle());

//        // 小于则直接发送邮件
//        if (mailData.getItems().length() < 1) {
//            boolean bn = sendMail(da);
//            GMLogUtil.log("发送邮件\t邮件ID：" + mailData.getId() + "\t 服务器ID：" + da.getServerId() + ",结果:" + bn + "\t信息：" + da.getSendErrorMess());
//            return AjaxResult.info("发送成功").put("ok",true);
//        }
        return AjaxResult.info("保存成功！请审核后发送").put("ok",true);
    }

    /**
     * 邮件发送粘贴内容操作
     * @param id
     * @return
     */
    @PostMapping("/queryById")
    @ResponseBody
    public Object queryById(long id) {
        MailData data = mailDataService.selectMailDataById(id);
        if (data != null) {
            return AjaxResult.info("",data).put("ok",true);
        }
        return AjaxResult.info("邮件记录已经不存在了").put("ok",false);
    }

    /**
     * 全服邮件发送粘贴内容操作
     * @param id
     * @return
     */
    @PostMapping("/queryAllById")
    @ResponseBody
    public Object queryAllById(long id) {
        AllMailData data = allMailDataService.selectAllMailDataById(id);
        if (data != null) {
            return AjaxResult.info("", data).put("ok",true);
        }
        return AjaxResult.info("邮件记录已经不存在了").put("ok",false);
    }

    /**
     * 全服邮件发送点击提交操作
     * @param request
     * @param groupName
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
    @PostMapping("/saveAllMail")
    @ResponseBody
    public Object saveAllMail(HttpServletRequest request,String groupName,
                              String serverids,Integer minLevel,
                              Integer maxLevel,int career,
                              String mailTitle,String mailContent,
                              String reason,String items) {
        String userName = ShiroUtils.getLoginName();
        if (StringUtils.isEmpty(serverids) || StringUtils.isEmpty(mailTitle) || StringUtils.isEmpty(mailContent) || StringUtils.isEmpty(reason)) {
            return AjaxResult.info("提交的邮件参数有错").put("ok",false);
        }
        AllMailData mailData = new AllMailData();
        //保存邮件
        mailData.setCreateDate(format.format(new Date()));
        mailData.setCreateUser(userName);
        mailData.setGroupName(groupName);
        mailData.setServerIdList(serverids);
        mailData.setMinLv(minLevel);
        mailData.setMaxLv(maxLevel);
        mailData.setCareer(career);
        mailData.setTitle(mailTitle);
        mailData.setContent(mailContent);
        mailData.setReason(reason);
        mailData.setItems(items);
//        if (mailData.getItems().trim().length() > 0) {
            mailData.setAdminState(1);
//        }
        int num = allMailDataService.insertAllMailData(mailData);
        AllMailData da = allMailDataService.selectAllMailDataById(mailData.getId());
        if (num < 1) {
            return AjaxResult.info("保存数据失败了，通知运维检查后台连接！");
        }
        GMLogUtil.log("保存全服邮件\t理由：" + da.getReason() + "\t 服务器列表ID：" + da.getServerIdList() + "\t标题：" + da.getTitle());

        // 小于则直接发送邮件
//        if (mailData.getItems().length() < 1) {
//            StringBuilder sb = sendAllServerMail(da);
//            GMLogUtil.log("发送全服邮件\t邮件ID：" + mailData.getId() + "\t 服务器列表ID：" + da.getServerIdList() + ",结果:" + sb.toString() + "\t信息：" + da.getSendErrorMess());
//            return AjaxResult.info(sb.toString()).put("ok",true);
//        }
        return AjaxResult.info("保存成功！请审核后发送").put("ok",true);
    }
}
