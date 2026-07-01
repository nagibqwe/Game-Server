package com.backend.gm;

import com.backend.bean.RechargeItem;
import com.backend.bean.Server;
import com.backend.gm.result.ResultNutMapHandler;
import com.backend.gm.send.BackendCommand;
import org.nutz.lang.util.NutMap;

import java.util.HashMap;
import java.util.List;

public class GameServerRequestUtil {

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

    public static NutMap gmGetRoleRechargeItem(Server server, long roleId, String goods_id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmGetRoleRechargeItem");
        map.put("roleId", String.valueOf(roleId));
        map.put("goods_id", goods_id);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取发送充值商城信息
     */
    public static NutMap gmRefreshRechargeItemInfos(Server server, String datas, String md5) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmRefreshRechargeItemInfos");
        map.put("rechargeInfos", datas);
        map.put("md5", md5);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 获取发送充值商城信息
     */
    public static NutMap gmSendRechargeInfos(Server server, List<RechargeItem> datas) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmSendRechargeInfos");
        map.put("data", datas);
        BackendCommand command = new BackendCommand(map);
        return command.execute(server, new ResultNutMapHandler());
    }

    /**
     * 设置某一条充值商品信息
     */
    public static NutMap gmUpdateRechargeInfo(Server server, String rechargeInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmUpdateRechargeInfo");
        map.put("shopInfo", rechargeInfo);
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
     * 通知游戏服开服
     * @param server
     * @return
     */
    public static NutMap gmOpenServer(Server server, int force, int timeout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", "gmOpenServer");
        map.put("force", String.valueOf(force));
        BackendCommand command = new BackendCommand(map, timeout);
        return command.execute(server, new ResultNutMapHandler());
    }
}
