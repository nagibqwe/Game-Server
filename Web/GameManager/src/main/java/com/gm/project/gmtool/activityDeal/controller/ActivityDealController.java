package com.gm.project.gmtool.activityDeal.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.service.IActivityService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


/**
 * Игровые события处理Controller
 *
 * @author gm
 * @date 2021-10-26
 */
@Controller
@RequestMapping("/gmtool/activityDeal")
public class ActivityDealController extends BaseController
{
    private String prefix = "gmtool/activityDeal";

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:activityDeal:view")
    @GetMapping()
    public String activityDeal()
    {
        return prefix + "/activityDeal";
    }

    /**
     * 查询Сервер功能
     * @param serverId
     * @return
     */
    @PostMapping("/searchActivity")
    @ResponseBody
    public TableDataInfo searchActivity(Integer serverId, Integer type, Integer subType, Integer isPre){
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return getDataTableErrorMsg("Выбранный ID сервера не существует");
        }

        int opType = 0;
        if(type == null){
            type = 0;
        }
        if(subType == null){
            subType = 0;
        }
        if(isPre == null){
            isPre = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(opType).append("&").append(type>0?type*1000+subType:0).append("&").append(isPre);

        AjaxResult serverResult = GameServerRequestUtil.activityDeal(server, sb.toString());
        if(serverResult.get("data") == null){
            return getDataTableErrorMsg("没有查到Данные");
        }
        HashMap<String, Object> dataMap = (HashMap<String, Object>)serverResult.get("data");
        if(dataMap.get("data") == null){
            return getDataTableErrorMsg("没有查到Данные");
        }

        List<Integer> actIds = (List<Integer>) dataMap.get("data");
        if(actIds.isEmpty()){
            return getDataTableErrorMsg("没有查到Данные");
        }

        //查询的活动Информация
        StringBuilder actIdStr = new StringBuilder();
        for (Integer actId : actIds) {
            actIdStr.append(actId).append(",");
        }
        actIdStr.deleteCharAt(actIdStr.length()-1).toString();

        List<Activity> activityList = activityService.selectActivityByActIds(actIdStr.toString());
//        dataMap.put("data", activityList);
//        serverResult.put("data", dataMap);
        return getDataTable(activityList);
    }

    /**
     * Удалить正在运行的Игровые события
     * @param serverId
     * @return
     */
    @PostMapping("/removeActivity")
    @ResponseBody
    public AjaxResult removeActivity(Integer serverId, Integer actType) {
        if(actType == null || actType <= 0){
            return AjaxResult.error("Тип события错误："+actType);
        }

        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }

        int opType = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(opType).append("&").append(actType);


        AjaxResult serverResult = GameServerRequestUtil.activityDeal(server, sb.toString());
        if(Boolean.valueOf(serverResult.get("ok").toString())){
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.success("Удалить正在运行的Игровые событияУспешно").put("ok", true);
        }else{
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.error("Удалить正在运行的Игровые событияОшибка").put("ok", false);
        }
    }

    /**
     * Удалить预备队列的Игровые события
     * @param serverId
     * @return
     */
    @PostMapping("/removePreActivity")
    @ResponseBody
    public AjaxResult removePreActivity(Integer serverId, Integer actType, Integer actId) {
        if(actType == null || actType <= 0){
            return AjaxResult.error("Тип события错误："+actType);
        }

        if(actId == null || actId <= 0){
            return AjaxResult.error("ID события错误："+actId);
        }

        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }

        int opType = 2;
        StringBuilder sb = new StringBuilder();
        sb.append(opType).append("&").append(actType).append("&").append(actId);

        AjaxResult serverResult = GameServerRequestUtil.activityDeal(server, sb.toString());
        if(Boolean.valueOf(serverResult.get("ok").toString())){
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.success("Удалить预备队列的Игровые событияУспешно").put("ok", true);
        }else{
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.error("Удалить预备队列的Игровые событияОшибка").put("ok", false);
        }
    }

    /**
     * 重新加载某个正在运行的Игровые события
     * @param serverId
     * @return
     */
    @PostMapping("/reloadActivity")
    @ResponseBody
    public AjaxResult reloadActivity(Integer serverId, Integer actId) {
        if(actId == null || actId <= 0){
            return AjaxResult.error("ID события错误："+actId);
        }

        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }

        int opType = 3;
        StringBuilder sb = new StringBuilder();
        sb.append(opType).append("&").append(actId);

        AjaxResult serverResult = GameServerRequestUtil.activityDeal(server, sb.toString());
        if(Boolean.valueOf(serverResult.get("ok").toString())){
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.success("重新加载某个正在运行的Игровые событияУспешно").put("ok", true);
        }else{
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.error("重新加载某个正在运行的Игровые событияОшибка").put("ok", false);
        }
    }

    /**
     * 重新加载所有正在运行的Игровые события
     * @param serverId
     * @return
     */
    @PostMapping("/reloadAllActivity")
    @ResponseBody
    public AjaxResult reloadAllActivity(Integer serverId) {
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }

        int opType = 4;
        StringBuilder sb = new StringBuilder();
        sb.append(opType);

        AjaxResult serverResult = GameServerRequestUtil.activityDeal(server, sb.toString());
        if(Boolean.valueOf(serverResult.get("ok").toString())){
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.success("重新加载所有正在运行的Игровые событияУспешно").put("ok", true);
        }else{
            logger.error(serverResult.get("msg").toString());
            return AjaxResult.error("重新加载所有正在运行的Игровые событияОшибка").put("ok", false);
        }
    }
}
