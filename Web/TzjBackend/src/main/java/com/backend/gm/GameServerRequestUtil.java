package com.backend.gm;

import com.backend.bean.*;
import com.backend.gm.result.ResultNutMapHandler;
import com.backend.gm.send.BackendCommand;
import com.backend.struct.RechargeItemInfo;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServerRequestUtil {

    /**
     * 后台模拟充值 九 零 一起玩 www.9  017 5.com
     * @param roleId        角色id
     * @param rechargeNum   充值数量
     */
    public static NutMap gmRecharge(Server server, long roleId, int rechargeNum, int rechargeTotalGold, int rechargeVipExp) {
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
     * @param type      公告类型：0滚动公告，1聊天框
     * @param content   内容
     */
    public static NutMap gmPublishAnnounce(Server server, int type, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmPublishAnnounce");
        map.put("type", type);
        map.put("content", content);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 发布单个活动
     */
    public static NutMap gmActivitySendMess(Server server, Activity activity) {
        Map<String, Object> map = Lang.obj2map(activity);
        map.put("cmd", "gmActivitySendMess");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 批量发送活动
     */
    public static NutMap gmBatchSendActMess(Server server, List<Activity> actDatas) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", actDatas);
        map.put("cmd", "gmBatchSendActMess");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 删除活动
     */
    public static NutMap gmActivityDeleteMess(Server server, int actId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmActivityDeleteMess");
        map.put("id", actId);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 批量删除活动
     */
    public static NutMap gmBatchDeleteActMess(Server server, List<Integer> actIds, List<Integer> actTypes) {
        Map<String, Object> map = new HashMap<>();
        map.put("actIds", actIds);
        map.put("actTypes", actTypes);
        map.put("cmd", "gmBatchDeleteActMess");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 踢人下线
     */
    public static NutMap gmKickPlayer(Server server, String roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmKickPlayer");
        map.put("roleId", roleId);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 玩家禁言
     */
    public static NutMap gmForbidChat(Server server) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmForbidChat");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 添加屏蔽字
     */
    public static NutMap gmAddKeyWord(Server server, String type, String keyword) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmAddKeyWord");
        if ("1".equals(type)) {
            keyword = "<t=0>,," + keyword + "</t>";
        }
        map.put("type", type);
        map.put("keyword", keyword);

        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 删除屏蔽字
     */
    public static NutMap gmDeleteKeyWord(Server server, int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteKeyWords");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 禁言黑名单
     */
    public static NutMap gmLoadChatBlackList(Server server) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmLoadChatBlackList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 禁言替换库
     */
    public static NutMap gmLoadChatWord(Server server) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "gmLoadChatWord");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 扣除道具
     */
    public static NutMap gmDeductItemopt(Server server, DeductItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", item.getRoleId());
        map.put("itemId", item.getItemId());
        map.put("itemNum", item.getDedCount());
        map.put("isBind", item.isIsBind());
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
    public static NutMap gmSetRoleAttrOpt(Server server, RoleAttr roleAttr) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleAttr.getRoleId());
        map.put("attrType", roleAttr.getAttrType());
        map.put("attrValue", roleAttr.getAttrValue());
        map.put("cmd", "gmSetRoleAttrOpt");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置评价开关
     */
    public static NutMap gmSetEvaluate(Server server, Evaluate evaluate) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", evaluate.geteType());
        map.put("state", evaluate.isState());
        map.put("cmd", "gmEvaluateState");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 排行榜数据获取
     */
    public static NutMap gmQueryRankList(Server server, int rankType) {
        Map<String, Object> map = new HashMap<>();
        map.put("rankKind", rankType);
        map.put("cmd", "gmQueryRankList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 请求请求开关游戏功能
     */
    public static NutMap gmGetFuncOpenList(Server server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmGetFuncOpenList");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 请求开关游戏功能
     */
    public static NutMap gmSwitchFunction(Server server, String funcSwitch) {
        String[] funcSwitchArr = funcSwitch.split(",");
        List<Map<String, Integer>> funcList = new ArrayList<>();
        for (String s : funcSwitchArr) {
            Map<String, Integer> map = new HashMap<>();
            map.put(s.split("@")[0], Integer.parseInt(s.split("@")[1]));
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
    public static NutMap gmOrderSendMess(Server server, String action, String params) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", action.trim());
        map.put("params", params.trim());
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * gm发送邮件
     */
    public static NutMap gmSendMail(Server server, MailData mailData) {
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
     * gm发送多个服务器全服邮件
     */
    public static NutMap gmSendAllMail(Server server, AllMailData mailData,String serverIdList) {
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
     * 查询开服时间
     */
    public static NutMap gmQueryOpsTime(Server server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "dealOpsTime");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置开服时间
     */
    public static NutMap gmSetOpsTime(Server server, String time) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "dealOpsTime");
        map.put("time", time);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 查询注册限制人数
     */
    public static NutMap gmQueryRegisterLimitNum(Server server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "setRegisterLimitNum");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置注册限制人数
     */
    public static NutMap gmSetRegisterLimitNum(Server server, int num) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "setRegisterLimitNum");
        map.put("num", num);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取商城信息
     */
    public static NutMap gmGetShopInfo(Server server, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmGetShopInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置商品信息
     */
    public static NutMap gmSetShopInfo(Server server, String shopInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmSetShopInfo");
        map.put("shopInfo", shopInfo);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }
    /**
     * 删除商品信息
     */
    public static NutMap gmDeleteShopInfo(Server server, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteShopInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 角色转移
     */
    public static NutMap gmTranRole(Server server, String tranRoleId, String tranUserId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "tranRole");
        map.put("tranRoleId", tranRoleId);
        map.put("tranUserId", tranUserId);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 发送更新公告
     */
    public static NutMap gmSendUpdateNotice(Server server, String content, String items, String resetReceives) {
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("items", items);
        map.put("resetReceives", resetReceives);
        map.put("cmd", "gmSendUpdateNotice");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取发送充值商城信息
     */
    public static NutMap gmRefreshRechargeItemInfos(Server server, String datas, String md5, int timeout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmRefreshRechargeItemInfos");
        map.put("rechargeInfos", datas);
        map.put("md5", md5);
        BackendCommand command = new BackendCommand(map, timeout);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置某一条充值商品信息
     */
    public static NutMap gmUpdateRechargeInfo(Server server, String rechargeInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmUpdateRechargeInfo");
        map.put("rechargeInfo", rechargeInfo);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 删除某一条充值商城信息
     */
    public static NutMap gmDeleteRechargeInfo(Server server, String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmDeleteRechargeInfo");
        map.put("id", id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 总幸运值数据
     * @param server
     * @param totalLuckyValue
     * @return
     */
    public static NutMap gmPublishLuckyValue(Server server, int totalLuckyValue) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmPublishLuckyValue");
        map.put("totalLuckyValue", totalLuckyValue);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 更新服务器的标签库信息
     * @param server
     * @return
     */
    public static NutMap gmUpdateTagInfo(Server server) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmUpdateTagInfo");
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }
}
