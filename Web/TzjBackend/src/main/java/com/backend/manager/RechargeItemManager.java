package com.backend.manager;

import com.backend.bean.RechargeItem;
import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.struct.RechargeItemInfo;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 充值商城商品管理
 */
public class RechargeItemManager {

    private static final Log logger = Logs.get();

    private enum Singleton {

        INSTANCE;
        RechargeItemManager manager;

        Singleton() {
            this.manager = new RechargeItemManager();
        }

        RechargeItemManager getProcessor() {
            return manager;
        }
    }

    public static RechargeItemManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    public TreeMap<Integer, RechargeItem> rechargeItemMap = new TreeMap<>();

    public TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap = new TreeMap<>();


    public TreeMap<Integer, RechargeItem> getRechargeItemMap() {
        return rechargeItemMap;
    }

    public void setRechargeItemMap(TreeMap<Integer, RechargeItem> rechargeItemMap) {
        this.rechargeItemMap = rechargeItemMap;
    }

    public TreeMap<Integer, RechargeItemInfo> getRechargeItemInfoMap() {
        return rechargeItemInfoMap;
    }

    public void setRechargeItemInfoMap(TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap) {
        this.rechargeItemInfoMap = rechargeItemInfoMap;
    }

    public void init(Dao dao) {
        this.dao = dao;
        //启动时从数据库加载一次充值配置信息
        load();
    }

    public void load() {
        rechargeItemMap.clear();
        rechargeItemInfoMap.clear();
        List<RechargeItem> rechargeItemList = this.dao.query(RechargeItem.class, null);
        for (RechargeItem rechargeItem : rechargeItemList) {
            rechargeItemMap.put(rechargeItem.getGoods_id(), rechargeItem);
            rechargeItemInfoMap.put(rechargeItem.getGoods_id(), convertRechargeItem(rechargeItem));
        }
    }

    public Object sendRechargeInfos() {
        TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap = RechargeItemManager.getInstance().getRechargeItemInfoMap();
        if(rechargeItemInfoMap.isEmpty()){
            logger.error("充值数据为空");
            return Toolkit.outResult(false, "数据获取失败");
        }

        StringBuilder sb = new StringBuilder();
        //通知API服务器更新某一条的充值信息
        String rechargeStr = RechargeItemManager.getInstance().getRechargeStr(rechargeItemInfoMap);
        String md5 = MD5Util.MD5(rechargeStr);

        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("rechargeStr",rechargeStr);
//        logger.info("==============rechargeStr:"+rechargeStr);
//        logger.info("==============md5:"+md5);
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl")+"/rechargeItem/refreshRechargeInfos", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("\n");

        //通知游戏服更新某一条的充值信息
        Cnd cnd = Cnd.where("isDeleted", "=", 0).
                and("isHeFu","=", 0).
                and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(Server server : servers){
            int serverId = server.getServerId();
            try {
                NutMap resultMap = GameServerRequestUtil.gmRefreshRechargeItemInfos(server, rechargeStr, md5, 15*1000);
                if (!resultMap.getBoolean("ok")) {
                    serverFailedList.add(serverId);
                    logger.error(serverId + "服,充值配置刷新失败！操作结果：" + resultMap.get("data").toString());
                } else {
                    serverSuccessList.add(serverId);
                }
            }catch (Exception e){
                logger.error(serverId + "服充值配置同步失败！error："+e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("\n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("\n");
        return Toolkit.outResult(true, sb.toString());
    }

    public RechargeItemInfo convertRechargeItem(RechargeItem rechargeItem){
        RechargeItemInfo rechargeItemInfo = new RechargeItemInfo();
        rechargeItemInfo.setGoods_id(rechargeItem.getGoods_id());
        rechargeItemInfo.setGoods_system_cfg_id(rechargeItem.getGoods_system_cfg_id());
        rechargeItemInfo.setGoods_name(rechargeItem.getGoods_name());
        rechargeItemInfo.setGoods_pay_channel(rechargeItem.getGoods_pay_channel());
        rechargeItemInfo.setGoods_pay_type(rechargeItem.getGoods_pay_type());
        rechargeItemInfo.setGoods_type(rechargeItem.getGoods_type());
        rechargeItemInfo.setGoods_subtype(rechargeItem.getGoods_subtype());
        rechargeItemInfo.setGoods_limit(rechargeItem.getGoods_limit());
        rechargeItemInfo.setGoods_icon(rechargeItem.getGoods_icon());

        rechargeItemInfo.setGoods_show_price(rechargeItem.getGoods_show_price());
        rechargeItemInfo.setGoods_reward(rechargeItem.getGoods_reward());
        rechargeItemInfo.setGoods_multiple(rechargeItem.getGoods_multiple());
        rechargeItemInfo.setGoods_extra_reward(rechargeItem.getGoods_extra_reward());
        rechargeItemInfo.setGoods_extra_reward_limit(rechargeItem.getGoods_extra_reward_limit());
        rechargeItemInfo.setIsTotalRecharge(rechargeItem.getIsTotalRecharge());
        rechargeItemInfo.setTotalVipPower(rechargeItem.getTotalVipPower());
        try {
            //解析档位
            String[] moneyStrs = rechargeItem.getGoods_price().split(";");
            TreeMap<String, String> moneyMap;
            for (int i = 0; i < moneyStrs.length; i++) {
                String[] platformArr = moneyStrs[i].split("_");
                moneyMap = new TreeMap<>();
                for (int j = 1; j < platformArr.length; j++) {
                    String[] moneyArr = platformArr[j].split(",");
                    if(moneyArr.length!=2){
                        logger.error("配置解析错误，id："+rechargeItem.getGoods_id());
                        continue;
                    }
                    moneyMap.put(moneyArr[0], moneyArr[1]);
                }
                rechargeItemInfo.getGoods_price().put(platformArr[0], moneyMap);
            }

            //解析计费点
            if(!rechargeItem.getGoods_price_point().equals("")){
                String[] moneyPointStrs = rechargeItem.getGoods_price_point().split(";");
                for (int i = 0; i < moneyPointStrs.length; i++) {
                    String[] moneyPointArr = moneyPointStrs[i].split(":");
                    rechargeItemInfo.getGoods_price_point().put(moneyPointArr[0], moneyPointArr[1]);
                }
            }
        }catch (Exception e){
            logger.error("充值配置解析错误，ID:"+rechargeItem.getGoods_id(),e);
            return rechargeItemInfo;
        }

        return rechargeItemInfo;
    }

    public String getRechargeStr() {
        return JsonUtils.toJSONString(this.getRechargeItemInfoMap());
    }

    public String getRechargeStr(TreeMap<Integer, RechargeItemInfo> rechargeInfoMap) {
        return JsonUtils.toJSONString(rechargeInfoMap);
    }
}
