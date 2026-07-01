package com.backend.module.api;

import com.backend.bean.BackendLog;
import com.backend.manager.RechargeItemManager;
import com.backend.struct.RechargeItemInfo;
import com.backend.utils.*;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import java.util.TreeMap;

/**
 * 处理充值商城请求
 */
@IocBean
@At("/rechargeItem")
@Ok("json")
@Filters
public class RechargeItemModule {

    private static final Log logger = Logs.get();

    @Inject
    private Dao dao;

    /**
     * 通知游戏服刷新充值商城商品列表
     * GM后台>>>APIServer
     */
    @At
    @POST
    public Object refreshRechargeInfos(String rechargeStr) {
        //解析配置
        if(rechargeStr==null||rechargeStr.equals("")){
            return Toolkit.outResult(false, "解析刷新的数据为空");
        }

        TreeMap<Integer, RechargeItemInfo> rechargeInfoMap = JsonUtils.parseObject(rechargeStr, new TypeReference<TreeMap<Integer, RechargeItemInfo>>(){});
        if (rechargeInfoMap ==null || rechargeInfoMap.isEmpty()){
            return Toolkit.outResult(false, "刷新的数据为空");
        }

        RechargeItemManager.getInstance().getRechargeItemInfoMap().clear();
        RechargeItemManager.getInstance().getRechargeItemInfoMap().putAll(rechargeInfoMap);
//        logger.info("refreshRechargeInfos==============rechargeStr:"+rechargeStr);
//        logger.info("refreshRechargeInfos==============md5:"+MD5Util.MD5(rechargeStr));
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "充值配置数据同步成功，MD5:"+ MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr()));
        return Toolkit.outResult(true, "API服务器刷新配置成功,商品数量："+rechargeInfoMap.size());
    }

    /**
     * 通知游戏服更新充值商城某条商品
     */
    @At
    @POST
    public Object updateRechargeItem(String rechargeStr) {
        RechargeItemInfo rechargeItemInfo = JsonUtils.toJavaObject(rechargeStr, RechargeItemInfo.class);
        if(rechargeItemInfo == null){
            return Toolkit.outResult(false, "更新的数据为空");
        }

        RechargeItemManager.getInstance().getRechargeItemInfoMap().put(rechargeItemInfo.getGoods_id(), rechargeItemInfo);
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "充值配置数据修改成功，ID:"+ rechargeItemInfo.getGoods_id());
        return Toolkit.outResult(true, "API服务器更新配置成功，ID："+rechargeItemInfo.getGoods_id());
    }

    @At
    @POST
    public Object deleteRechargeItem(String id) {
        if(!RechargeItemManager.getInstance().getRechargeItemInfoMap().containsKey(Integer.parseInt(id))){
            return Toolkit.outResult(false, "没有找到的相关配置");
        }

        RechargeItemManager.getInstance().getRechargeItemInfoMap().remove(Integer.parseInt(id));
        return Toolkit.outResult(true, "API服务器删除配置成功，ID："+id);
    }
}
