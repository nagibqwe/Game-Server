package com.gm.project.gmtool.manager;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItemInfo;
import com.gm.project.gmtool.rechargeItem.service.IRechargeItemService;
import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 充值商城商品管理
 */
@Service
public class RechargeItemManager {

    private static Logger logger = LoggerFactory.getLogger(RechargeItemManager.class);

    @Autowired
    private ITServerService tServerService;
    @Autowired
    private IRechargeItemService rechargeItemService;
    @Autowired
    private ISelectGroupService selectGroupService;
    @Autowired
    private GameManagerConfig gameManagerConfig;

    public static RechargeItemManager getInstance() {
        return   (RechargeItemManager) SpringUtils.getBean("rechargeItemManager");
    }

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
    @PostConstruct
    public void init() {
        //启动时从数据库加载一次充值配置信息
        load();
    }

    public void load() {
        rechargeItemMap.clear();
        rechargeItemInfoMap.clear();
        List<RechargeItem> rechargeItemList = rechargeItemService.selectRechargeItemList(new RechargeItem());
        for (RechargeItem rechargeItem : rechargeItemList) {
            rechargeItemMap.put(rechargeItem.getGoodsId(), rechargeItem);
            rechargeItemInfoMap.put(rechargeItem.getGoodsId(), convertRechargeItem(rechargeItem));
        }
    }

    public Object sendRechargeInfos() {
        TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap = RechargeItemManager.getInstance().getRechargeItemInfoMap();
        if(rechargeItemInfoMap.isEmpty()){
            logger.error("充值数据为空");
            return AjaxResult.info("数据获取失败").put("ok",false);
        }

        StringBuilder sb = new StringBuilder();
        //通知API服务器更新某一条的充值信息
        String rechargeStr = RechargeItemManager.getInstance().getRechargeStr(rechargeItemInfoMap);
        String md5 = MD5Util.MD5(rechargeStr);

        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("rechargeStr",rechargeStr);
//        logger.info("==============rechargeStr:"+rechargeStr);
//        logger.info("==============md5:"+md5);
        String httpResult = HttpConnectionUtils.post(gameManagerConfig.getAPIServerUrl()+"/rechargeItem/refreshRechargeInfos", paramMap);

        sb.append("API服务器返回结果：").append(httpResult).append("\n");

        //通知游戏服更新某一条的充值信息
        List<TServer> servers = selectGroupService.selectServerList("", 1, "0,1");
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(TServer server : servers){
            int serverId = server.getServerId();
            try {
                HashMap resultMap = GameServerRequestUtil.gmRefreshRechargeItemInfos(server, rechargeStr, md5, 15*1000);
                if (!Boolean.valueOf(resultMap.get("ok").toString())) {
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
        return AjaxResult.info(sb.toString()).put("ok",true);
    }

    public RechargeItemInfo convertRechargeItem(RechargeItem rechargeItem){
        RechargeItemInfo rechargeItemInfo = new RechargeItemInfo();
        rechargeItemInfo.setGoods_id(rechargeItem.getGoodsId());
        rechargeItemInfo.setGoods_system_cfg_id(rechargeItem.getGoodsSystemCfgId());
        rechargeItemInfo.setGoods_name(rechargeItem.getGoodsName());
        rechargeItemInfo.setGoods_pay_channel(rechargeItem.getGoodsPayChannel());
        rechargeItemInfo.setGoods_pay_type(rechargeItem.getGoodsPayType());
        rechargeItemInfo.setGoods_type(rechargeItem.getGoodsType());
        rechargeItemInfo.setGoods_subtype(rechargeItem.getGoodsSubtype());
        rechargeItemInfo.setGoods_limit(rechargeItem.getGoodsLimit());
        rechargeItemInfo.setGoods_icon(rechargeItem.getGoodsIcon());
        rechargeItemInfo.setGoodsurl(rechargeItem.getGoodsurl());

        rechargeItemInfo.setGoods_show_price(rechargeItem.getGoodsShowPrice());
        rechargeItemInfo.setGoods_reward(rechargeItem.getGoodsReward());
        rechargeItemInfo.setGoods_multiple(rechargeItem.getGoodsMultiple());
        rechargeItemInfo.setGoods_extra_reward(rechargeItem.getGoodsExtraReward());
        rechargeItemInfo.setGoods_extra_reward_limit(rechargeItem.getGoodsExtraRewardLimit());
        rechargeItemInfo.setIsTotalRecharge(rechargeItem.getIsTotalRecharge());
        rechargeItemInfo.setTotalVipPower(rechargeItem.getTotalVipPower());
        try {
            //解析档位
            String[] moneyStrs = rechargeItem.getGoodsPrice().split(";");
            TreeMap<String, String> moneyMap;
            for (int i = 0; i < moneyStrs.length; i++) {
                String[] platformArr = moneyStrs[i].split("_");
                moneyMap = new TreeMap<>();
                for (int j = 1; j < platformArr.length; j++) {
                    String[] moneyArr = platformArr[j].split(",");
                    if(moneyArr.length!=2){
                        logger.error("配置解析错误，id："+rechargeItem.getGoodsId());
                        continue;
                    }
                    moneyMap.put(moneyArr[0], moneyArr[1]);
                }
                rechargeItemInfo.getGoods_price().put(platformArr[0], moneyMap);
            }

            //解析计费点
            if(!rechargeItem.getGoodsPricePoint().equals("")){
                String[] moneyPointStrs = rechargeItem.getGoodsPricePoint().split(";");
                for (int i = 0; i < moneyPointStrs.length; i++) {
                    String[] moneyPointArr = moneyPointStrs[i].split(":");
                    rechargeItemInfo.getGoods_price_point().put(moneyPointArr[0], moneyPointArr[1]);
                }
            }
        }catch (Exception e){
            logger.error("充值配置解析错误，ID:"+rechargeItem.getGoodsId(),e);
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
