package com.gm.project.gmtool.banChat.controller;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.common.utils.text.Convert;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.activity.domain.TagGrid;
import com.gm.project.gmtool.banChat.domain.BanChat;
import com.gm.project.gmtool.banChat.service.IBanChatService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 聊День封禁Controller
 * 
 * @author gm
 * @date 2021-11-20
 */
@Controller
@RequestMapping("/gmtool/banChat")
public class BanChatController extends BaseController {
    private String prefix = "gmtool/banChat";

    private static final Logger log = LoggerFactory.getLogger(BanChatController.class);

    @Autowired
    private IBanChatService banChatService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:banChat:view")
    @GetMapping()
    public String banChat() {
        return prefix + "/banChat";
    }

    @RequiresPermissions("gmtool:unBanChat:view")
    @GetMapping("/unBanChat")
    public String unBanChat() {
        return prefix + "/unBanChat";
    }

    @RequiresPermissions("gmtool:shieldWord:view")
    @GetMapping("/shieldWord")
    public String shieldWord() {
        return prefix + "/shieldWord";
    }

    @RequiresPermissions("gmtool:replaceWord:view")
    @GetMapping("/replaceWord")
    public String replaceWord() {
        return prefix + "/replaceWord";
    }

    @RequiresPermissions("gmtool:blackList:view")
    @GetMapping("/blackList")
    public String blackList() {
        return prefix + "/blackList";
    }

    /**
     * 禁言
     */
//    @RequiresPermissions("gmtool:banChat:ban")
    @PostMapping("/ban")
    @ResponseBody
    public AjaxResult ban(String serverIdStr, Integer crimeType, Integer banType, String userIds, Integer endTime, String reason)
    {
        if (StringUtils.isEmpty(serverIdStr) || StringUtils.isEmpty(userIds) || StringUtils.isEmpty(reason)) {
            return AjaxResult.info("参数错误").put("ok",false);
        }

        for (String userId:userIds.split(",")) {
            BanChat banChat = new BanChat();
            banChat.setUserId(userId);

            List<BanChat> banChats = banChatService.selectBanChatList(banChat);
            if(!banChats.isEmpty()){
                banChat = banChats.get(0);
            }
            boolean isUpdate = banChat.getId()!=null;
            banChat.setCrimeType(crimeType);
            banChat.setBanType(banType);
            banChat.setUserId(userId);
            banChat.setReason(reason);

            long time = TimeUtils.Time() + endTime * 1000L;
            banChat.setEndTime(endTime > 0 ? TimeUtils.format2string(time) : "-1");

            if (endTime <= 0) {
                time = -1;
            }
            log.info("userId:" + userId + ",禁言Время окончания：" + banChat.getEndTime());

            //检查Сервер входа禁言表
            String sqlStr = "select count(*) as num from forbidspeeking where `userId`=" + userId + ";";
            DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
            List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
            if (null != resultMap && resultMap.size() > 0 && resultMap.get(0).get("num")!=null && (long)resultMap.get(0).get("num")>0){
                sqlStr = "update forbidspeeking set `endTime`= " + time + ", `forbidType`=" + banType + " where userId = " + userId + ";";
            }else{
                sqlStr = "insert into forbidspeeking(userId, forbidType, `endTime`, createTime) values(" + userId + ","+ banType +"," + time + ",'" + TimeUtils.format2string(TimeUtils.Time()) + "')";
            }
            int exeNum = 0;
            try {
                exeNum = loginDao.executeUpdate(sqlStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            log.info("Обновитьforbidspeeking禁言表的РезультатДа：" + (exeNum>0));

            StringBuilder sb = new StringBuilder();
            int[] serverIds = StringUtils.stringArrTointArr(Convert.toStrArray(serverIdStr));
            for (int serverId : serverIds) {
                TServer ser = tServerService.selectTServerByServerId(serverId);
                if (ser == null) {
                    continue;
//                return AjaxResult.error("Выбранный ID сервера не существует");
                }
                if (ser.getIsHeFu() == 1) {
                    continue;
                }

                AjaxResult map = GameServerRequestUtil.gmBanChat(ser, 5000);
                if (Boolean.valueOf(map.get("ok").toString())) {
                    GMLogUtil.log("禁言Успешно,serverId:" + serverId + "userId:" + userId);
                }
                sb.append(map);
            }

            banChat.setServerIds(serverIdStr);
            banChat.setState(0);
            if (isUpdate) {
                banChatService.updateBanChat(banChat);
            }else{
                banChatService.insertBanChat(banChat);
            }
        }

        return AjaxResult.info("聊День禁言完成").put("ok", true);
    }

    /**
     * 聊День解封
     */
    //    @RequiresPermissions("gmtool:unBanChat:unBan")
    @PostMapping("/unBan")
    @ResponseBody
    public Object unBan(Integer id) {
        BanChat banChat = banChatService.selectBanChatById(id.longValue());
        if (banChat == null) {
            return AjaxResult.info("禁言Информация未找到").put("ok", false);
        }

        //更新Сервер входа表
        String sqlStr = "update forbidspeeking set `endTime`= 0 where userId=" + banChat.getUserId() + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int excNUm = 0;
        try {
            excNUm = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.error("Обновитьforbidspeeking禁言表的РезультатДа：" + (excNUm > 0));

        StringBuilder sb = new StringBuilder();
        int[] serverIds = StringUtils.stringArrTointArr(Convert.toStrArray(banChat.getServerIds()));
        for (int serverId : serverIds) {
            TServer ser = tServerService.selectTServerByServerId(serverId);
            if (ser == null) {
                continue;
            }
            if (ser.getIsHeFu() == 1) {
                continue;
            }

            AjaxResult map = GameServerRequestUtil.gmBanChat(ser, 5000);
            if (Boolean.valueOf(map.get("ok").toString())) {
                GMLogUtil.log("聊День解封Успешно,serverId:" + serverId + "userId:" + banChat.getUserId());
            }
            sb.append(map);
        }

        banChat.setState(1);
        banChatService.updateBanChat(banChat);

        return AjaxResult.info("聊День解封完成").put("ok", true);
    }

    /**
     * 查询聊День封禁列表
     */
    @RequiresPermissions("gmtool:banChat:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BanChat banChat)
    {
        startPage();
        banChat.setState(0);
        List<BanChat> list = banChatService.selectBanChatList(banChat);
        return getDataTable(list);
    }

    /**
     * Экспорт聊День封禁列表
     */
    @RequiresPermissions("gmtool:banChat:export")
    @Log(title = "聊День封禁", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BanChat banChat)
    {
        List<BanChat> list = banChatService.selectBanChatList(banChat);
        ExcelUtil<BanChat> util = new ExcelUtil<BanChat>(BanChat.class);
        return util.exportExcel(list, "聊День封禁Данные");
    }

    /**
     * 查询屏蔽字
     */
//    @RequiresPermissions("gmtool:banChat:searchShieldWord")
    @PostMapping("/searchShieldWord")
    @ResponseBody
    public TableDataInfo searchShieldWord(String serverId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return getDataTableErrorMsg("param error");
        }

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return getDataTableErrorMsg("Не удалось получить данные сервера");
        }

        AjaxResult resultMap = GameServerRequestUtil.gmOrderSendMess(server, "gmGetKeyWords", "",8000);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字列表获取Успешно,serverId:" + serverId);
//            return AjaxResult.info(resultMap.get("msg").toString()).put("ok", true);
            HashMap<String, Object> data = (HashMap<String,Object>)resultMap.get("data");
            HashMap<Integer, HashMap<String, Object>> msg = JsonUtils.parseObject(data.get("msg").toString(), new TypeReference<HashMap<Integer, HashMap<String, Object>>>(){});
            List<HashMap<String, Object>> result = new ArrayList<>();
            result.addAll(msg.values());
            return getDataTable(result);
        }else{
            GMLogUtil.log("屏蔽字列表获取Ошибка,serverId:" + serverId);
            log.error("СерверНомер" + serverId + "反馈Информация:被屏蔽的关键字列表获取Ошибка！");
            return getDataTableErrorMsg("列表获取Ошибка");
        }
    }

    /**
     * Добавить屏蔽字
     */
    @GetMapping("/addShieldWord")
    public String addShieldWord()
    {
        return prefix + "/addShieldWord";
    }

    /**
     * Добавить屏蔽字
     */
//    @RequiresPermissions("gmtool:banChat:addSheildWord")
    @PostMapping("/addShieldWord")
    @ResponseBody
    public AjaxResult addShieldWord(String serverId, String shieldType, String shieldWord)
    {
        if (StringUtils.isEmpty(serverId) || StringUtils.isEmpty(shieldWord)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmAddShieldWord(server, shieldType, shieldWord);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字ДобавитьУспешно,serverId:" + serverId);
//            return AjaxResult.info("屏蔽字ДобавитьУспешно").put("ok", true);
            return toAjax(true);
        }else{
            GMLogUtil.log("屏蔽字УдалитьОшибка,serverId:" + serverId);
            return AjaxResult.info("屏蔽字ДобавитьОшибка").put("ok", false);
        }
    }

    /**
     * Удалить屏蔽字
     */
//    @RequiresPermissions("gmtool:banChat:removeShieldWord")
    @PostMapping("/removeShieldWord")
    @ResponseBody
    public AjaxResult removeShieldWord(String serverId, Integer id)
    {
        if (StringUtils.isEmpty(serverId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmDeleteShieldWord(server, id);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字УдалитьУспешно,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("屏蔽字УдалитьОшибка,serverId:" + serverId);
            return AjaxResult.info("屏蔽字УдалитьОшибка").put("ok", false);
        }
    }

    /**
     * 查询替换字
     */
//    @RequiresPermissions("gmtool:banChat:searchReplaceWord")
    @PostMapping("/searchReplaceWord")
    @ResponseBody
    public TableDataInfo searchReplaceWord(String serverId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return getDataTableErrorMsg("param error");
        }
        //更新Сервер входа表
        String sqlStr = "SELECT id,serverId,word,`replace` as replaceWord,`type` as replaceType FROM chatword where serverId="+serverId;
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if(resultMap == null){
            return getDataTableErrorMsg("获取Данные错误");
        }
        return getDataTable(resultMap);
    }

    /**
     * Добавить替换字
     */
    @GetMapping("/addReplaceWord")
    public String addReplaceWord()
    {
        return prefix + "/addReplaceWord";
    }

    /**
     * Добавить替换字
     */
//    @RequiresPermissions("gmtool:banChat:addReplaceWord")
    @PostMapping("/addReplaceWord")
    @ResponseBody
    public AjaxResult addReplaceWord(String serverId, String replaceType, String word, String replaceWord)
    {
        if (StringUtils.isEmpty(serverId) || StringUtils.isEmpty(word) || StringUtils.isEmpty(replaceWord)) {
            return AjaxResult.info("param error").put("ok", false);
        }
        //更新Сервер входа聊День替换字表
        String sqlStr = "select count(*) as num from chatword where `serverId`=" + serverId + " and word='" + word +"';";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if (null != resultMap && resultMap.size() > 0 && resultMap.get(0).get("num")!=null && (long)resultMap.get(0).get("num")>0){
            sqlStr = "update chatword set `replace`= " + replaceWord + " where serverId = " + serverId + " and word=" + word +";";
        }else{
            sqlStr = "insert into chatword(serverId, type, `word`, `replace`) values(" + serverId + "," + replaceType + ",'" + word + "','" + replaceWord + "')";
        }
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("更新聊День替换字表Ошибка").put("ok", false);
        }
        log.info("Добавить聊День替换字表的РезультатДа：" + (exeNum > 0));

        //通知游戏服更新聊День替换字
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult result = GameServerRequestUtil.gmLoadReplaceWord(server);
        if (Boolean.valueOf(result.get("ok").toString())) {
            GMLogUtil.log("替换字表ОбновитьУспешно,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("替换字表ОбновитьОшибка,serverId:" + serverId);
            return AjaxResult.info("替换字表ОбновитьОшибка").put("ok", false);
        }
    }

    /**
     * Удалить替换字
     */
//    @RequiresPermissions("gmtool:banChat:removeReplaceWord")
    @PostMapping("/removeReplaceWord")
    @ResponseBody
    public AjaxResult removeReplaceWord(String serverId, Integer id)
    {
        if (StringUtils.isEmpty(serverId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新Сервер входа聊День替换字表
        String sqlStr = "DELETE from chatword where id=" + id + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("更新聊День替换字表Ошибка").put("ok", false);
        }
        log.info("УдалитьchatWord替换关键字表的РезультатДа：" + (exeNum > 0));

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmDeleteShieldWord(server, id);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("替换字УдалитьУспешно,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("替换字УдалитьОшибка,serverId:" + serverId);
            return AjaxResult.info("替换字УдалитьОшибка").put("ok", false);
        }
    }

    /**
     * 查询Чёрный список
     */
//    @RequiresPermissions("gmtool:banChat:searchBlackList")
    @PostMapping("/searchBlackList")
    @ResponseBody
    public TableDataInfo searchBlackList(String serverId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return getDataTableErrorMsg("param error");
        }

        //查询Сервер входа表
        String sqlStr = "SELECT userId,serverId FROM chatblacklist where serverId="+serverId;
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if(resultMap == null){
            return getDataTableErrorMsg("获取Данные错误");
        }
        return getDataTable(resultMap);
    }

    /**
     * ДобавитьЧёрный список
     */
    @GetMapping("/addBlackList")
    public String addBlackList()
    {
        return prefix + "/addBlackList";
    }

    /**
     * ДобавитьЧёрный список
     */
//    @RequiresPermissions("gmtool:banChat:addBlackList")
    @PostMapping("/addBlackList")
    @ResponseBody
    public AjaxResult addBlackList(String serverId, String userId)
    {
        if (StringUtils.isEmpty(serverId) || StringUtils.isEmpty(userId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新Сервер входа聊День替换字表
        String sqlStr = "select count(*) from chatblacklist where `serverId`=" + serverId + " and userId='" + userId +"';";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if (null != resultMap && resultMap.size() > 0 && resultMap.get(0).get("num")!=null && (long)resultMap.get(0).get("num")>0){
//            sqlStr = "update chatblacklist set `replace`= " + replace + " where serverId = " + serverId + " and word=" + word +";";
            return AjaxResult.info("Добавить聊ДеньЧёрный список已存在").put("ok", false);
        }else{
            sqlStr = "insert into chatblacklist(userId,serverId) values(" + userId + "," + serverId + ")";
        }
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("Добавить聊ДеньЧёрный списокОшибка").put("ok", false);
        }
        log.info("Добавить聊ДеньЧёрный список表的РезультатДа：" + (exeNum > 0));

        //通知游戏服更新聊День替换字
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult result = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (Boolean.valueOf(result.get("ok").toString())) {
            GMLogUtil.log("替换字表ОбновитьУспешно,serverId:" + serverId);
            return AjaxResult.info("替换字表ОбновитьУспешно").put("ok", true);
        }else{
            GMLogUtil.log("替换字表ОбновитьОшибка,serverId:" + serverId);
            return AjaxResult.info("替换字表ОбновитьОшибка").put("ok", false);
        }
    }

    /**
     * УдалитьЧёрный список
     */
//    @RequiresPermissions("gmtool:banChat:removeBlackList")
    @PostMapping("/removeBlackList")
    @ResponseBody
    public AjaxResult removeBlackList(String serverId, Integer userId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新Сервер входа聊День替换字表
        String sqlStr = "DELETE from chatblacklist where userId="+userId+" and serverId=" + serverId + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("Удалить聊ДеньЧёрный список表Ошибка").put("ok", false);
        }
        log.info("Удалить聊ДеньЧёрный список表的РезультатДа：" + (exeNum > 0));

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("Не удалось получить данные сервера").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("聊ДеньЧёрный списокУдалитьУспешно,serverId:" + serverId);
            return AjaxResult.info("聊ДеньЧёрный списокУдалитьУспешно").put("ok", true);
        }else{
            GMLogUtil.log("聊ДеньЧёрный списокУдалитьОшибка,serverId:" + serverId);
            return AjaxResult.info("聊ДеньЧёрный списокУдалитьОшибка").put("ok", false);
        }
    }
}
