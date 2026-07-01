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
 * 聊天封禁Controller
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
            log.info("userId:" + userId + ",禁言结束时间：" + banChat.getEndTime());

            //检查登录服禁言表
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
            log.info("刷新forbidspeeking禁言表的结果是：" + (exeNum>0));

            StringBuilder sb = new StringBuilder();
            int[] serverIds = StringUtils.stringArrTointArr(Convert.toStrArray(serverIdStr));
            for (int serverId : serverIds) {
                TServer ser = tServerService.selectTServerByServerId(serverId);
                if (ser == null) {
                    continue;
//                return AjaxResult.error("选择服务器Id不存在");
                }
                if (ser.getIsHeFu() == 1) {
                    continue;
                }

                AjaxResult map = GameServerRequestUtil.gmBanChat(ser, 5000);
                if (Boolean.valueOf(map.get("ok").toString())) {
                    GMLogUtil.log("禁言成功,serverId:" + serverId + "userId:" + userId);
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

        return AjaxResult.info("聊天禁言完成").put("ok", true);
    }

    /**
     * 聊天解封
     */
    //    @RequiresPermissions("gmtool:unBanChat:unBan")
    @PostMapping("/unBan")
    @ResponseBody
    public Object unBan(Integer id) {
        BanChat banChat = banChatService.selectBanChatById(id.longValue());
        if (banChat == null) {
            return AjaxResult.info("禁言信息未找到").put("ok", false);
        }

        //更新登录服表
        String sqlStr = "update forbidspeeking set `endTime`= 0 where userId=" + banChat.getUserId() + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int excNUm = 0;
        try {
            excNUm = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.error("刷新forbidspeeking禁言表的结果是：" + (excNUm > 0));

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
                GMLogUtil.log("聊天解封成功,serverId:" + serverId + "userId:" + banChat.getUserId());
            }
            sb.append(map);
        }

        banChat.setState(1);
        banChatService.updateBanChat(banChat);

        return AjaxResult.info("聊天解封完成").put("ok", true);
    }

    /**
     * 查询聊天封禁列表
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
     * 导出聊天封禁列表
     */
    @RequiresPermissions("gmtool:banChat:export")
    @Log(title = "聊天封禁", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BanChat banChat)
    {
        List<BanChat> list = banChatService.selectBanChatList(banChat);
        ExcelUtil<BanChat> util = new ExcelUtil<BanChat>(BanChat.class);
        return util.exportExcel(list, "聊天封禁数据");
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
            return getDataTableErrorMsg("获取服务失败");
        }

        AjaxResult resultMap = GameServerRequestUtil.gmOrderSendMess(server, "gmGetKeyWords", "",8000);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字列表获取成功,serverId:" + serverId);
//            return AjaxResult.info(resultMap.get("msg").toString()).put("ok", true);
            HashMap<String, Object> data = (HashMap<String,Object>)resultMap.get("data");
            HashMap<Integer, HashMap<String, Object>> msg = JsonUtils.parseObject(data.get("msg").toString(), new TypeReference<HashMap<Integer, HashMap<String, Object>>>(){});
            List<HashMap<String, Object>> result = new ArrayList<>();
            result.addAll(msg.values());
            return getDataTable(result);
        }else{
            GMLogUtil.log("屏蔽字列表获取失败,serverId:" + serverId);
            log.error("服务器编号" + serverId + "反馈信息:被屏蔽的关键字列表获取失败！");
            return getDataTableErrorMsg("列表获取失败");
        }
    }

    /**
     * 新增屏蔽字
     */
    @GetMapping("/addShieldWord")
    public String addShieldWord()
    {
        return prefix + "/addShieldWord";
    }

    /**
     * 添加屏蔽字
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
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmAddShieldWord(server, shieldType, shieldWord);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字添加成功,serverId:" + serverId);
//            return AjaxResult.info("屏蔽字添加成功").put("ok", true);
            return toAjax(true);
        }else{
            GMLogUtil.log("屏蔽字删除失败,serverId:" + serverId);
            return AjaxResult.info("屏蔽字添加失败").put("ok", false);
        }
    }

    /**
     * 删除屏蔽字
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
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmDeleteShieldWord(server, id);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("屏蔽字删除成功,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("屏蔽字删除失败,serverId:" + serverId);
            return AjaxResult.info("屏蔽字删除失败").put("ok", false);
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
        //更新登录服表
        String sqlStr = "SELECT id,serverId,word,`replace` as replaceWord,`type` as replaceType FROM chatword where serverId="+serverId;
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if(resultMap == null){
            return getDataTableErrorMsg("获取数据错误");
        }
        return getDataTable(resultMap);
    }

    /**
     * 新增替换字
     */
    @GetMapping("/addReplaceWord")
    public String addReplaceWord()
    {
        return prefix + "/addReplaceWord";
    }

    /**
     * 添加替换字
     */
//    @RequiresPermissions("gmtool:banChat:addReplaceWord")
    @PostMapping("/addReplaceWord")
    @ResponseBody
    public AjaxResult addReplaceWord(String serverId, String replaceType, String word, String replaceWord)
    {
        if (StringUtils.isEmpty(serverId) || StringUtils.isEmpty(word) || StringUtils.isEmpty(replaceWord)) {
            return AjaxResult.info("param error").put("ok", false);
        }
        //更新登录服聊天替换字表
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
            return AjaxResult.info("更新聊天替换字表失败").put("ok", false);
        }
        log.info("添加聊天替换字表的结果是：" + (exeNum > 0));

        //通知游戏服更新聊天替换字
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult result = GameServerRequestUtil.gmLoadReplaceWord(server);
        if (Boolean.valueOf(result.get("ok").toString())) {
            GMLogUtil.log("替换字表刷新成功,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("替换字表刷新失败,serverId:" + serverId);
            return AjaxResult.info("替换字表刷新失败").put("ok", false);
        }
    }

    /**
     * 删除替换字
     */
//    @RequiresPermissions("gmtool:banChat:removeReplaceWord")
    @PostMapping("/removeReplaceWord")
    @ResponseBody
    public AjaxResult removeReplaceWord(String serverId, Integer id)
    {
        if (StringUtils.isEmpty(serverId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新登录服聊天替换字表
        String sqlStr = "DELETE from chatword where id=" + id + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("更新聊天替换字表失败").put("ok", false);
        }
        log.info("删除chatWord替换关键字表的结果是：" + (exeNum > 0));

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmDeleteShieldWord(server, id);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("替换字删除成功,serverId:" + serverId);
            return toAjax(true);
        }else{
            GMLogUtil.log("替换字删除失败,serverId:" + serverId);
            return AjaxResult.info("替换字删除失败").put("ok", false);
        }
    }

    /**
     * 查询黑名单
     */
//    @RequiresPermissions("gmtool:banChat:searchBlackList")
    @PostMapping("/searchBlackList")
    @ResponseBody
    public TableDataInfo searchBlackList(String serverId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return getDataTableErrorMsg("param error");
        }

        //查询登录服表
        String sqlStr = "SELECT userId,serverId FROM chatblacklist where serverId="+serverId;
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if(resultMap == null){
            return getDataTableErrorMsg("获取数据错误");
        }
        return getDataTable(resultMap);
    }

    /**
     * 新增黑名单
     */
    @GetMapping("/addBlackList")
    public String addBlackList()
    {
        return prefix + "/addBlackList";
    }

    /**
     * 添加黑名单
     */
//    @RequiresPermissions("gmtool:banChat:addBlackList")
    @PostMapping("/addBlackList")
    @ResponseBody
    public AjaxResult addBlackList(String serverId, String userId)
    {
        if (StringUtils.isEmpty(serverId) || StringUtils.isEmpty(userId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新登录服聊天替换字表
        String sqlStr = "select count(*) from chatblacklist where `serverId`=" + serverId + " and userId='" + userId +"';";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if (null != resultMap && resultMap.size() > 0 && resultMap.get(0).get("num")!=null && (long)resultMap.get(0).get("num")>0){
//            sqlStr = "update chatblacklist set `replace`= " + replace + " where serverId = " + serverId + " and word=" + word +";";
            return AjaxResult.info("添加聊天黑名单已存在").put("ok", false);
        }else{
            sqlStr = "insert into chatblacklist(userId,serverId) values(" + userId + "," + serverId + ")";
        }
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("添加聊天黑名单失败").put("ok", false);
        }
        log.info("添加聊天黑名单表的结果是：" + (exeNum > 0));

        //通知游戏服更新聊天替换字
        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult result = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (Boolean.valueOf(result.get("ok").toString())) {
            GMLogUtil.log("替换字表刷新成功,serverId:" + serverId);
            return AjaxResult.info("替换字表刷新成功").put("ok", true);
        }else{
            GMLogUtil.log("替换字表刷新失败,serverId:" + serverId);
            return AjaxResult.info("替换字表刷新失败").put("ok", false);
        }
    }

    /**
     * 删除黑名单
     */
//    @RequiresPermissions("gmtool:banChat:removeBlackList")
    @PostMapping("/removeBlackList")
    @ResponseBody
    public AjaxResult removeBlackList(String serverId, Integer userId)
    {
        if (StringUtils.isEmpty(serverId)) {
            return AjaxResult.info("param error").put("ok", false);
        }

        //更新登录服聊天替换字表
        String sqlStr = "DELETE from chatblacklist where userId="+userId+" and serverId=" + serverId + ";";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int exeNum = 0;
        try {
            exeNum = loginDao.executeUpdate(sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.info("删除聊天黑名单表失败").put("ok", false);
        }
        log.info("删除聊天黑名单表的结果是：" + (exeNum > 0));

        TServer server = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
        if (server == null) {
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("聊天黑名单删除成功,serverId:" + serverId);
            return AjaxResult.info("聊天黑名单删除成功").put("ok", true);
        }else{
            GMLogUtil.log("聊天黑名单删除失败,serverId:" + serverId);
            return AjaxResult.info("聊天黑名单删除失败").put("ok", false);
        }
    }
}
