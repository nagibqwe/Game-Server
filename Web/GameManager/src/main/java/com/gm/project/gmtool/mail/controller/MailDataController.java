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
 * Список писемController
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
    private final static int allServerMailState = 6;//标识多个Сервер发送Письмо всем серверам的СтатусИнформация

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
     * 查询Список писем列表
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
     * 查询全服Список писем列表
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
     * 查询未Удалить的Письмо
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
     * 超级Письмо发送页面
     * @return
     */
    @RequiresPermissions("gmtool:mail:sendsupermail")
    @GetMapping("/sendsupermail")
    public String sendSuperMail()
    {
        return prefix + "/sendsupermail";
    }

    /**
     * Письмо всем серверам发送页面
     * @return
     */
    @RequiresPermissions("gmtool:mail:sendAllMail")
    @GetMapping("/sendAllMail")
    public String sendAllMail()
    {
        return prefix + "/sendAllMail";
    }

    /**
     * 查询未Удалить的Письмо всем серверам
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
     * ЭкспортСписок писем列表
     */
    @RequiresPermissions("gmtool:mail:export")
    @Log(title = "Список писем", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MailData mailData)
    {
        List<MailData> list = mailDataService.selectMailDataList(mailData);
        ExcelUtil<MailData> util = new ExcelUtil<MailData>(MailData.class);
        return util.exportExcel(list, "Список писемДанные");
    }

    /**
     * ДобавитьСписок писем
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьСписок писем
     */
    @RequiresPermissions("gmtool:mail:add")
    @Log(title = "Список писем", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MailData mailData)
    {
        return toAjax(mailDataService.insertMailData(mailData));
    }

    /**
     * ИзменитьСписок писем
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MailData mailData = mailDataService.selectMailDataById(id);
        mmap.put("mailData", mailData);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьСписок писем
     */
    @RequiresPermissions("gmtool:mail:edit")
    @Log(title = "Список писем", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MailData mailData)
    {
        return toAjax(mailDataService.updateMailData(mailData));
    }

    /**
     * УдалитьСписок писем
     */
    @RequiresPermissions("gmtool:mail:remove")
    @Log(title = "Список писем", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mailDataService.deleteMailDataByIds(ids));
    }

    /**
     * Список писем中的ПисьмоДействия
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
            return AjaxResult.info("Запись письма больше не существует").put("ok",false);
        }
        da.setAdminUser(userName);
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            boolean isOk = sendMail(da);
            GMLogUtil.log("发送Письмо\tПисьмоID：" + da.getId() + "\t ID сервера：" + da.getServerId() + ",Результат:" + isOk + "\tИнформация：" + da.getSendErrorMess());
            if (!isOk) {
                return AjaxResult.info(da.getSendErrorMess()).put("ok",false);
            }
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(userName + "标记此Письмо不允许发送！");
            if (mailDataService.updateMailData(da) < 1) {
                return AjaxResult.info("Не удалось обновить запись письма!").put("ok",false);
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(userName + "Удалить了本Письмо");
            int num = mailDataService.updateMailData(da);
            GMLogUtil.log("УдалитьПисьмо\tПисьмоID：" + da.getId() + ",Результат:" + num + "\tИнформация：" + da.getTitle());
            if (num < 1) {
                return AjaxResult.info("Не удалось обновить запись письма!").put("ok",false);
            }
        }
        return AjaxResult.info("更新ПисьмоУспешно！").put("ok",true);
    }

    /**
     * 一键发送所有的未发送的Письмо
     * @return
     */
    @PostMapping( "/onekeySend")
    @ResponseBody
    public Object onekeySend() {
        // 检查权限ДаНет足够
        List<MailData> list = mailDataService.selectMailByState();
        String ctime = format.format(new Date());
        for (MailData da : list) {
            da.setAdminUser(ShiroUtils.getLoginName());
            da.setAdminDate(ctime);
            boolean bn = sendMail(da);
            int num = mailDataService.updateMailData(da);
            GMLogUtil.log("发送Письмо\tПисьмоID：" + da.getId() + "\t " +
                    "ID сервера：" + da.getServerId() + ",Результат:" + bn + " , 更新记录数=" + num + "\tИнформация：" + da.getSendErrorMess());
        }
        return AjaxResult.info("一键发送功能处理Успешно!").put("ok",true);
    }
    //向Сервер发送Письмо
    private boolean sendMail(MailData mailData) {
        if (mailData.getSended() > 0 || mailData.getIsDelete() > 0) {
            log.error("Письмо(" + mailData.getId() + ")已发送或被Удалить");
            return false;
        }

        if (mailData.getAdminState() > 1) {
            log.error("Письмо(" + mailData.getId() + ")Статус为" + mailData.getAdminState() + "Не отправлять");
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
        log.error("Письмо(id:" + mailData.getId() + ")发送至(sid:" + sid + ",roleId:" + mailData.getRoleIds() + "),Результат:" + result.get("msg").toString() + ",耗时:" + (et - st));
        mailDataService.updateMailData(mailData);
        return true;
    }

    /**
     *全服Список писем中的ПисьмоДействия
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
            return AjaxResult.info("Запись письма больше не существует").put("ok",false);
        }
        da.setAdminUser(userName);
        da.setAdminDate(format.format(new Date()));

        if (type == 1) {
            StringBuilder sb = sendAllServerMail(da);
            GMLogUtil.log("发送Письмо всем серверам\tПисьмоID：" + da.getId() + "\t ID сервера列表：" + da.getServerIdList() + ",Результат:" + sb.toString() + "\tИнформация：" + da.getSendErrorMess());
            return AjaxResult.info(sb.toString()).put("ok",false);
        }

        if (type == 2) {
            da.setAdminState(4);
            da.setSendErrorMess(userName + "标记此Письмо不允许发送！");
            if (allMailDataService.updateAllMailData(da) < 1) {
                return AjaxResult.info("Не удалось обновить запись письма!").put("ok",false);
            }
        }

        if (type == 3) {
            da.setIsDelete(1);
            da.setSendErrorMess(userName + "Удалить了本Письмо");
            int num = allMailDataService.updateAllMailData(da);
            GMLogUtil.log("УдалитьПисьмо всем серверам\tПисьмоID：" + da.getId() + ",Результат:" + num + "\tИнформация：" + da.getTitle());
            if (num < 1) {
                return AjaxResult.info("Не удалось обновить запись письма!").put("ok",false);
            }
        }
        return AjaxResult.info("更新ПисьмоУспешно！").put("ok",true);
    }

    /**
     * 向Сервер发送多个Сервер的Письмо всем серверам
     * @param mailData
     * @return
     */
    private StringBuilder sendAllServerMail(AllMailData mailData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbSendErrorMess = new StringBuilder();
        if (mailData.getSended() > 0 || mailData.getIsDelete() > 0) {
            log.error("Письмо(" + mailData.getId() + ")已发送或被Удалить");
            return sb.append("Письмо(" + mailData.getId() + ")已发送或被Удалить");
        }

        if (mailData.getAdminState() > 1) {
            log.error("Письмо(" + mailData.getId() + ")Статус为" + mailData.getAdminState() + "Не отправлять");
            return sb.append("Письмо(" + mailData.getId() + ")Статус为" + mailData.getAdminState() + "Не отправлять");
        }
        String serverIdStr = mailData.getServerIdList();
        if (serverIdStr.contains("[")){
            serverIdStr = serverIdStr.replace("[","").replace("]","");
        }
        String[] serverIdList = serverIdStr.split(",");
        HashMap result = new HashMap();
        Set<Integer> sids = new HashSet<>();//存放不重复的Объединение серверов后的ID сервера
        for (String serverId:serverIdList) {
            int sid = DBServerMgr.getInstance().getHeFuId(Integer.parseInt(serverId));
            sids.add(sid);
        }
        sb.append("Письмо(id:" + mailData.getId()+")");
        for (Integer sid:sids){
            TServer server = tServerService.selectTServerByServerId(sid);
            if (server == null) {
                mailData.setAdminState(allServerMailState);
                sbSendErrorMess.append(sid+"不能指定到达具体的游戏服！").append("\n");
//                mailData.setSendErrorMess(language.get("mail.send.serverNull"));
                allMailDataService.updateAllMailData(mailData);
                sb.append("发送至ID сервера:"+sid+",Ошибка未找到serverId="+sid).append("\n");
                log.error("t_server中未找到serverId=" + sid);
            }else {
                result = GameServerRequestUtil.gmSendAllMail(server, mailData, mailData.getServerIdList());//向游戏Сервер发送命令
                mailData.setSended(1);
                sbSendErrorMess.append(result.get("msg").toString()).append("\n");
//                mailData.setSendErrorMess(result.getString("msg"));
                if (Boolean.valueOf(result.get("ok").toString())) {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至ID сервера:"+sid+"Успешно").append("\n");
                    sb.append("发送至ID сервера:"+sid+"Успешно").append("\n");
                } else {
                    mailData.setAdminState(allServerMailState);
                    sbSendErrorMess.append("发送至ID сервера:"+sid+"Ошибка").append("\n");
                    sb.append("发送至ID сервера:"+sid+"Ошибка").append("\n");
                }
                mailData.setSendErrorMess(sbSendErrorMess.toString());
                log.error("Письмо(id:" + mailData.getId() + ")发送至(sid:" + sid + "),Результат:" + result.get("msg").toString() + ")");
                allMailDataService.updateAllMailData(mailData);
            }
        }
        GMLogUtil.log(sb.toString());//记录Журнал
        return sb;
    }

    /**
     * 向Сервер发送多个Сервер的Письмо всем серверам(全服Список писем一键发送的Действия)
     * @param request
     * @return
     */
    @PostMapping("/oneKeySendAll")
    @ResponseBody
    public Object oneKeySendAll(HttpServletRequest request) {
        String userName = ShiroUtils.getLoginName();
        // 检查权限ДаНет足够
        AllMailData allMailData = new AllMailData();
        allMailData.setIsDelete(0);
        allMailData.setAdminState(1);
        //一键发送待批准的Письмо
        List<AllMailData> list = allMailDataService.selectAllMailDataList(allMailData);
        String ctime = format.format(new Date());
        StringBuilder sb = new StringBuilder();
        StringBuilder sbAllMail = new StringBuilder();
        for (AllMailData da : list) {
            da.setAdminUser(userName);
            da.setAdminDate(ctime);
            sb = sendAllServerMail(da);
            int num = allMailDataService.updateAllMailData(da);
            GMLogUtil.log("发送Письмо всем серверам\tПисьмоID：" + da.getId() + "\t ID сервера列表：" + da.getServerIdList() + ",Результат:" + sb.toString() + " , 更新记录数=" + num);
            sbAllMail.append(sb.toString());
        }
        return AjaxResult.info(sbAllMail.toString()).put("ok",true);
    }

    /**
     *验证ID персонажа
     * @param roleIds
     * @param serverId
     * @return
     */
    @PostMapping("/queryRoleIds")
    @ResponseBody
    public Object queryRoleIds(String roleIds, Integer serverId) {
        if (StringUtils.isBlank(roleIds) && !roleIds.equals("all")) {
            return AjaxResult.info("Введите корректный ID персонажа!").put("ok",false);
        }

        TServer dblog = tServerService.selectTServerByServerId(serverId);
        if (dblog == null) {
            return AjaxResult.info("请求查询的Сервер并不存在").put("ok",false);
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
            return AjaxResult.info(list.toString() + "的ID персонажа在" + serverId + "服中找不到！").put("ok", false);
        }
        return AjaxResult.info("输入的角色都有效！").put("ok",true);
    }

    /**
     * Письмо发送ОтправитьДействия
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
            return AjaxResult.info("Некорректные параметры письма").put("ok",false);
        }
        if (mailData.getServerId() < 1) {
            return AjaxResult.info("请求查询的Сервер并不存在").put("ok",false);
        }

        //СохранитьПисьмо
        mailData.setCreateDate(format.format(new Date()));
        mailData.setCreateUser(userName);
//        if (mailData.getItems().trim().length() > 0) {
            mailData.setAdminState(1);
//        }
        int num = mailDataService.insertMailData(mailData);
        MailData da = mailDataService.selectMailDataById(mailData.getId());
        if (num < 1) {
            return AjaxResult.info("СохранитьДанныеОшибка了，通知运维检查后台连接！");
        }
        GMLogUtil.log("СохранитьПисьмо\t理由：" + da.getReason() + "\t ID сервера：" + da.getServerId() + ",角色列表：" + da.getRoleIds() + "\t标题：" + da.getTitle());

//        // 小于则直接发送Письмо
//        if (mailData.getItems().length() < 1) {
//            boolean bn = sendMail(da);
//            GMLogUtil.log("发送Письмо\tПисьмоID：" + mailData.getId() + "\t ID сервера：" + da.getServerId() + ",Результат:" + bn + "\tИнформация：" + da.getSendErrorMess());
//            return AjaxResult.info("发送Успешно").put("ok",true);
//        }
        return AjaxResult.info("СохранитьУспешно！请审核后发送").put("ok",true);
    }

    /**
     * Письмо发送粘贴СодержимоеДействия
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
        return AjaxResult.info("Запись письма больше не существует").put("ok",false);
    }

    /**
     * Письмо всем серверам发送粘贴СодержимоеДействия
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
        return AjaxResult.info("Запись письма больше не существует").put("ok",false);
    }

    /**
     * Письмо всем серверам发送点击ОтправитьДействия
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
            return AjaxResult.info("Некорректные параметры письма").put("ok",false);
        }
        AllMailData mailData = new AllMailData();
        //СохранитьПисьмо
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
            return AjaxResult.info("СохранитьДанныеОшибка了，通知运维检查后台连接！");
        }
        GMLogUtil.log("СохранитьПисьмо всем серверам\t理由：" + da.getReason() + "\t Список серверовID：" + da.getServerIdList() + "\t标题：" + da.getTitle());

        // 小于则直接发送Письмо
//        if (mailData.getItems().length() < 1) {
//            StringBuilder sb = sendAllServerMail(da);
//            GMLogUtil.log("发送Письмо всем серверам\tПисьмоID：" + mailData.getId() + "\t Список серверовID：" + da.getServerIdList() + ",Результат:" + sb.toString() + "\tИнформация：" + da.getSendErrorMess());
//            return AjaxResult.info(sb.toString()).put("ok",true);
//        }
        return AjaxResult.info("СохранитьУспешно！请审核后发送").put("ok",true);
    }
}
