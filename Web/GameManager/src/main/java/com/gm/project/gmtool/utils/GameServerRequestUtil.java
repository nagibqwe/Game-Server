package com.gm.project.gmtool.utils;

import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.allMail.domain.AllMailData;
import com.gm.project.gmtool.deductItem.domain.DeductItem;
import com.gm.project.gmtool.evaluate.domain.Evaluate;
import com.gm.project.gmtool.mail.domain.MailData;
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;
import com.gm.project.gmtool.server.result.ResultNutMapHandler;
import com.gm.project.gmtool.server.send.BackendCommand;
import com.gm.project.gmtool.server.domain.TServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServerRequestUtil {

    /**
     * Тестовое пополнение через GM
     * @param roleId        ID персонажа
     * @param rechargeNum   Пополнение数量
     */
    public static AjaxResult gmRecharge(TServer server, long roleId, int rechargeNum, int rechargeTotalGold, int rechargeVipExp) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmRecharge");
        map.put("roleId", roleId);
        map.put("rechargeNum", rechargeNum);
        map.put("rechargeTotalGold", rechargeTotalGold);
        map.put("rechargeVipExp", rechargeVipExp);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 发布公告
     * @param type      公告Тип：0滚动公告，1聊День框
     * @param content   Содержимое
     */
    public static AjaxResult gmPublishAnnounce(TServer server, int type, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmPublishAnnounce");
        map.put("type", type);
        map.put("content", content);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

//    /**
//     * 发布单个活动
//     */
//    public static NutMap gmActivitySendMess(Server server, Activity activity) {
//        Map<String, Object> map = Lang.obj2map(activity);
//        map.put("cmd", "gmActivitySendMess");
//        BackendCommand command = new BackendCommand(map);
//        return command.execute(server, new ResultNutMapHandler());
//    }

    /**
     * 批量发送活动
     */
    public static AjaxResult gmBatchSendActMess(TServer server, List<Activity> actDatas) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", actDatas);
        map.put("cmd", "gmBatchSendActMess");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

//    /**
//     * Удалить活动
//     */
//    public static NutMap gmActivityDeleteMess(Server server, int actId) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("cmd", "gmActivityDeleteMess");
//        map.put("id", actId);
//        BackendCommand command = new BackendCommand(map);
//        return command.execute(server, new ResultNutMapHandler());
//    }

    /**
     * 批量Удалить活动
     */
    public static AjaxResult gmBatchDeleteActMess(TServer server, List<Integer> actIds, List<Integer> actTypes) {
        Map<String, Object> map = new HashMap<>();
        map.put("actIds", actIds);
        map.put("actTypes", actTypes);
        map.put("cmd", "gmBatchDeleteActMess");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 处理Игровые события
     */
    public static AjaxResult activityDeal(TServer server, String params) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "activityDeal");
        map.put("params", params.trim());
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 踢人下线
     */
    public static AjaxResult gmKickPlayer(TServer server, String roleId, int timeout) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmKickPlayer");
        map.put("roleId", roleId);
        BackendCommand command = new BackendCommand(map, timeout);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 通知游戏Обновить禁言Информация
     */
    public static AjaxResult gmBanChat(TServer server, int timeout) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmForbidChat");
        BackendCommand command = new BackendCommand(map, timeout);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * Добавить屏蔽字
     */
    public static AjaxResult gmAddShieldWord(TServer server, String shieldType, String shieldWord) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmAddKeyWord");
        if ("1".equals(shieldType)) {
            shieldWord = "<t=0>,," + shieldWord + "</t>";
        }
        map.put("type", shieldType);
        map.put("keyword", shieldWord);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * Удалить屏蔽字
     */
    public static AjaxResult gmDeleteShieldWord(TServer server, int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteKeyWords");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 聊День替换字
     */
    public static AjaxResult gmLoadReplaceWord(TServer server) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmLoadChatWord");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 聊ДеньЧёрный список
     */
    public static AjaxResult gmLoadChatBlackList(TServer server) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmLoadChatBlackList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 扣除道具
     */
    public static AjaxResult gmDeductItemopt(TServer server, DeductItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", item.getRoleId());
        map.put("itemId", item.getItemId());
        map.put("itemNum", item.getDedCount());
        map.put("isBind", item.getIsBind());
        map.put("isMail", item.getIsMail());
        map.put("mailTitle", item.getMailTitle());
        map.put("mailContent", item.getMailContent());
        map.put("sendUser", item.getSendUser());
        map.put("cmd", "gmDeductItemOpt");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置角色属性
     */
    public static AjaxResult gmSetRoleAttrOpt(TServer server, RoleAttr roleAttr) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleAttr.getRoleId());
        map.put("attrType", roleAttr.getAttrType());
        map.put("attrValue", roleAttr.getAttrValue());
        map.put("cmd", "gmSetRoleAttrOpt");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置Оценки включены
     */
    public static AjaxResult gmSetEvaluate(TServer server, Evaluate evaluate) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", evaluate.geteType());
        map.put("state", evaluate.getState() == 1?"true":"false");
        map.put("cmd", "gmEvaluateState");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * РейтингДанные获取
     */
    public static AjaxResult gmQueryRankList(TServer server, int rankType) {
        Map<String, Object> map = new HashMap<>();
        map.put("rankKind", rankType);
        map.put("cmd", "gmQueryRankList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 请求请求开关游戏功能
     */
    public static AjaxResult gmGetFuncOpenList(TServer server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmGetFuncOpenList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 请求开关游戏功能
     */
    public static AjaxResult gmSwitchFunction(TServer server, String funcSwitch) {
        String[] funcSwitchArr = funcSwitch.split(",");
        List<Map<String, Integer>> funcList = new ArrayList<>();
        for (String s : funcSwitchArr) {
            Map<String, Integer> map = new HashMap<>();
            String[] funcs = s.split("@");
            map.put(funcs[0], Integer.parseInt(funcs[1]));
            funcList.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", funcList);
        map.put("cmd", "gmSwitchFunction");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * gm指令
     */
    public static AjaxResult gmOrderSendMess(TServer server, String action, String params,int timeout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", action.trim());
        map.put("params", params.trim());
        BackendCommand command = new BackendCommand(map,timeout);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * gm发送Письмо
     */
    public static AjaxResult gmSendMail(TServer server, MailData mailData) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmSendMail");
        map.put("roleIds", mailData.getRoleIds());
        map.put("title", mailData.getTitle());
        map.put("content",mailData.getContent());
        map.put("items", mailData.getItems());
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * gm发送多个СерверПисьмо всем серверам
     */
    public static AjaxResult gmSendAllMail(TServer server, AllMailData mailData, String serverIdList) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmSendAllMail");
        map.put("minLv", mailData.getMinLv());
        map.put("maxLv", mailData.getMaxLv());
        map.put("career", mailData.getCareer());
        map.put("title", mailData.getTitle());
        map.put("content",mailData.getContent());
        map.put("items", mailData.getItems());
        map.put("serverIdList", serverIdList);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 查询Время открытия сервера
     */
    public static AjaxResult gmQueryOpsTime(TServer server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "dealOpsTime");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置Время открытия сервера
     */
    public static AjaxResult gmSetOpsTime(TServer server, String time) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "dealOpsTime");
        map.put("time", time);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

//    /**
//     * 查询Зарегистрироваться限制Количество
//     */
//    public static AjaxResult gmQueryRegisterLimitNum(TServer server) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("cmd", "setRegisterLimitNum");
//        BackendCommand command = new BackendCommand(map);
//        return command.execute(server, new ResultNutMapHandler());
//    }

    /**
     * 设置Зарегистрироваться限制Количество
     */
    public static AjaxResult gmSetRegisterLimitNum(TServer server, int num) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "setRegisterLimitNum");
        map.put("num", num);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取МагазинИнформация
     */
    public static AjaxResult gmGetShopInfo(TServer server, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmGetShopInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置商品Информация
     */
    public static AjaxResult gmSetShopInfo(TServer server, String shopInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmSetShopInfo");
        map.put("shopInfo", shopInfo);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }
    /**
     * Удалить商品Информация
     */
    public static AjaxResult gmDeleteShopInfo(TServer server, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteShopInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * Перенос персонажа
     */
    public static AjaxResult gmTranRole(TServer server, String tranRoleId, String tranUserId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "tranRole");
        map.put("tranRoleId", tranRoleId);
        map.put("tranUserId", tranUserId);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 发送Объявление об обновлении
     */
    public static AjaxResult gmSendUpdateNotice(TServer server, String content, String items, String resetReceives) {
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("items", items);
        map.put("resetReceives", resetReceives);
        map.put("cmd", "gmSendUpdateNotice");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取发送ПополнениеМагазинИнформация
     */
    public static AjaxResult gmRefreshRechargeItemInfos(TServer server, String datas, String md5, int timeout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmRefreshRechargeItemInfos");
        map.put("rechargeInfos", datas);
        map.put("md5", md5);
        BackendCommand command = new BackendCommand(map, timeout);
        return command.execute(server, new ResultNutMapHandler());
    }
//
//    /**
//     * 设置某一条Пополнение商品Информация
//     */
//    public static NutMap gmUpdateRechargeInfo(Server server, String rechargeInfo) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("cmd", "gmUpdateRechargeInfo");
//        map.put("rechargeInfo", rechargeInfo);
//        BackendCommand command = new BackendCommand(map);
//        return command.execute(server, new ResultNutMapHandler());
//    }

    /**
     * Удалить某一条ПополнениеМагазинИнформация
     */
    public static AjaxResult gmDeleteRechargeInfo(TServer server, String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteRechargeInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 总幸运值Данные
     * @param server
     * @param totalLuckyValue
     * @return
     */
    public static AjaxResult gmPublishLuckyValue(TServer server, int totalLuckyValue) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmPublishLuckyValue");
        map.put("totalLuckyValue", totalLuckyValue);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 更新Сервер的标签库Информация
     * @param server
     * @return
     */
    public static AjaxResult gmUpdateTagInfo(TServer server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmUpdateTagInfo");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }
}
