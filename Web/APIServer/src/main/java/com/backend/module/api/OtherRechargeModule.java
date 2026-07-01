package com.backend.module.api;

import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.GameInfoManager;
import com.backend.manager.RechargeItemManager;
import com.backend.struct.RechargeItemInfo;
import com.backend.struct.RoleState;
import com.backend.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Desc 绿岸第三方充值对外接口
 */
@IocBean
@At("/otherRecharge")
@Ok("json")
@Filters
public class OtherRechargeModule {

    private static final Log logger = Logs.get();

    @At("/")
    public void index() {
    }

    @Inject
    private Dao dao;

    /**
     * 请求获取商品列表API
     */
    @At
    @POST
    @GET
    public Object getRechargeItemList(HttpServletRequest request) {
        TreeMap<Integer, RechargeItemInfo> allRechargeItemMap = RechargeItemManager.getInstance().getRechargeItemInfoMap();
        if (allRechargeItemMap == null && allRechargeItemMap.size() == 0) {
            this.outResultFailure(11, "配置不存在");
        }
        String rechargeItemLogByGoodIdMapMd5 = RechargeItemManager.getInstance().getRechargeStr(allRechargeItemMap);
//        TreeMap<String, Object> jsonObj = new TreeMap<>();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("game_id", GameInfoManager.getInstance().getGameInfo().getGameId());  //游戏id
        jsonObj.put("version", "1.0.0.1"); //商品版本号
        jsonObj.put("goods_md5", MD5Util.MD5(rechargeItemLogByGoodIdMapMd5)); //商品配置md5
//        jsonObj.put("goods_currency",GameInfoManager.getInstance().getGameInfo().getRechargeCurrency()); //商品币种
//        List<TreeMap<String, Object>> goodsJsonArray = new ArrayList<>();
//        TreeMap<String, Object> goodJsonObj = null;
        JSONArray goodsJsonArray = new JSONArray();
        JSONObject goodJsonObj = null;
        for (RechargeItemInfo rechargeItemInfo : allRechargeItemMap.values()) {
//            goodJsonObj = new TreeMap<>();
            goodJsonObj = new JSONObject();
            goodJsonObj.put("goods_id", rechargeItemInfo.getGoods_id()); //商品id
            goodJsonObj.put("goods_system_cfg_id", rechargeItemInfo.getGoods_system_cfg_id()); //游戏系统使用配置ID
            goodJsonObj.put("goods_name", rechargeItemInfo.getGoods_name()); //商品名称
            goodJsonObj.put("goods_pay_channel", rechargeItemInfo.getGoods_pay_channel());//商品交易类型
            goodJsonObj.put("goods_type", rechargeItemInfo.getGoods_type());  //充值类型
            goodJsonObj.put("goods_subtype", rechargeItemInfo.getGoods_subtype()); //商品类型
            goodJsonObj.put("goods_desc", rechargeItemInfo.getGoods_name()); //商品描述
            goodJsonObj.put("goods_price", rechargeItemInfo.getGoods_price());
            goodJsonObj.put("goods_price_point", rechargeItemInfo.getGoods_price_point());
            goodJsonObj.put("goods_show_price", rechargeItemInfo.getGoods_show_price());
            goodJsonObj.put("goods_limit", rechargeItemInfo.getGoods_limit());//充值限制次数（用于在网页上显示最大购买次数)
            goodJsonObj.put("goods_reward", rechargeItemInfo.getGoods_reward()); //--充值奖励,格式：物品类型_数量_绑定_职业（用于在网页上显示购买奖励）
            goodJsonObj.put("goods_multiple", rechargeItemInfo.getGoods_multiple()); //充值奖励倍数，格式：倍数_次数（3_2表示前2次充值都是3倍奖励）（用于在网页上显示奖励倍数）
            goodJsonObj.put("goods_extra_reward", rechargeItemInfo.getGoods_extra_reward()); //
            goodJsonObj.put("goods_extra_reward_limit", rechargeItemInfo.getGoods_extra_reward_limit()); //额外奖励可领取次数,-1代表无限次（用于在网页上显示赠送奖励可领取次数）
            goodJsonObj.put("goods_icon", rechargeItemInfo.getGoods_icon()); //
            goodJsonObj.put("goods_url",rechargeItemInfo.getGoodsurl());
            goodJsonObj.put("goods_ext", ""); //商品扩展字段（用于传递以后业务参数，且不能及 时更新的情况）
            goodsJsonArray.add(goodJsonObj);
        }
        jsonObj.put("goods_infos", goodsJsonArray);
        return this.outResultSuccess(jsonObj);
    }

    /**
     * 请求获取角色可购买的商品信息
     */
    @At
    @POST
    @GET
    public Object getRechargeItem(HttpServletRequest request) {
        String server_id = request.getParameter("server_id");
        String role_id = request.getParameter("role_id");
        String goods_md5 = request.getParameter("goods_md5");
        String payTypeStr = request.getParameter("pay_type");
        //支付类型:1:点卡渠道,2:非点卡渠道
        int pay_type = 0;
        if (payTypeStr == null || payTypeStr.equals("")) {
            logger.error("支付类型错误，pay_type=" + payTypeStr);
            return this.outResultFailure(1, "支付类型错误，pay_type=" + payTypeStr);
        } else {
            pay_type = Integer.parseInt(payTypeStr);
        }

        String pay_channel = request.getParameter("pay_channel");
        String sign = request.getParameter("sign");

        if (StringUtils.isEmpty(server_id)) {
            logger.error("服务器id为空 查询失败");
            return this.outResultFailure(7, "服务器id为空 查询失败");
        }

        if (StringUtils.isEmpty(sign)) {
            logger.error("校验码为空");
            return this.outResultFailure(8, "校验码为空");
        }

        String newSignString = RechargeUtil.buildMd5String("server_id", server_id, "role_id", role_id, "goods_md5", goods_md5, "pay_type", payTypeStr, "pay_channel", pay_channel);
        if (!RechargeUtil.isSignOK(newSignString, sign)) {
            logger.error("md5校验失败");
            return this.outResultFailure(9, "md5校验失败");
        }

        int serverId = Integer.parseInt(server_id);
        int finalServerId = QueryUtil.getInstance().getHeFuId(serverId);
        Server dblog = QueryUtil.getInstance().getFinalHeFuDB(serverId);
        if (dblog == null) {
            return this.outResultFailure(12, "server not found");
        }
        if (StringUtils.isEmpty(role_id)) {
            logger.error("角色id为空 查询失败");
            return this.outResultFailure(11, "角色id为空 查询失败");
        }
        long playerId;
        try {
            playerId = role_id.matches("[0-9]+") ? Long.parseLong(role_id) : Long.parseLong(role_id, 36);
        } catch (Exception e) {
            return this.outResultFailure(11, "输入查询参数错误");
        }

        String roleSqlStr = "SELECT * FROM $table WHERE roleId = @roleId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "roleId", playerId);
        } catch (Exception e) {
            e.printStackTrace();
            return this.outResultFailure(11, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return this.outResultFailure(11, "无角色");
        }

        TreeMap<Integer, RechargeItemInfo> rechargemMap = RechargeItemManager.getInstance().getRechargeItemInfoMap();
        if (rechargemMap.isEmpty()) {
            logger.error("充值配置不存在");
            return this.outResultFailure(11, "充值配置不存在");
        }

        //判断md5是否修改
        String cfgMD5 = MD5Util.MD5(RechargeItemManager.getInstance().getRechargeStr());
        int is_update = 0;
        if (!cfgMD5.toLowerCase().equals(goods_md5.toLowerCase())) {
            is_update = 1;
        }

        TreeMap<Integer, RechargeItemInfo> resultMap = new TreeMap<>();
        //服务器获取的充值商品列表 <充值ID, <已购买次数, 剩余时间>>
        HashMap<Integer, HashMap<Integer, Integer>> serverRechargeMap;

        try {
            Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", finalServerId));
            if (server == null) {
                logger.error(serverId + "服获取连接失败！");
                return this.outResultFailure(11, "服获取连接失败！");
            }
            //向游戏服请求角色可购买商品列表（游戏服只会返回玩家达到可购买条件的充值商品，包括次数上限也会检查）
            NutMap httpResultMap = GameServerRequestUtil.gmGetRoleRechargeItem(server, playerId, "0");
            if (!httpResultMap.getBoolean("ok")) {
                logger.error(serverId + "服,获取角色可购买商品失败！操作结果：" + httpResultMap.get("data").toString());
                return this.outResultFailure(11, "无法从游戏服获取角色可购买商品");
            }
            serverRechargeMap = JsonUtils.parseObject(httpResultMap.get("data").toString(), new TypeReference<HashMap<Integer, HashMap<Integer, Integer>>>() {
            });

            for (Map.Entry<Integer, HashMap<Integer, Integer>> entries : serverRechargeMap.entrySet()) {
                int rechargeItemId = entries.getKey();
                //不在APIServer充值缓存中
                RechargeItemInfo rii = rechargemMap.get(rechargeItemId);
                if (rii == null) {
                    logger.error("APIServer缓存中不存在充值ID:" + rechargeItemId);
                    continue;
                }

                //过滤paytype
                if(pay_type!=rii.getGoods_pay_type()){
                    continue;
                }

                //过滤支付渠道
                if (!StringUtils.isEmpty(rii.getGoods_pay_channel())
                        &&!rii.getGoods_pay_channel().equalsIgnoreCase(pay_channel)) {
                    continue;
                }

                resultMap.put(rechargeItemId, rechargemMap.get(rechargeItemId));
            }
        } catch (Exception e) {
            logger.error(serverId + "服获取角色可购买商品失败！" , e);
            return this.outResultFailure(11, "无法从游戏服获取角色可购买商品");
        }

        //购买日志
//        Map<Integer,List<Map<String, Object>>> rechargeLogMap = this.getRechargeItemLogByGoodIdMap(dblog,playerId);
        String clientOS = roles.get(0).getClientOS();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("map_game_id", GameInfoManager.getInstance().getGameInfo().getGameId());  //游戏id
        jsonObj.put("server_id", server_id); //服务器id
        jsonObj.put("role_id", role_id); //角色id
        jsonObj.put("os", clientOS); //操作系统
        jsonObj.put("version", "1.0.0.1"); //商品版本号
        jsonObj.put("is_update", is_update); //是否更新所有商品信息，0：不更新，1：更新

        JSONArray goodsJsonArray = new JSONArray();
        if (resultMap != null && resultMap.size() > 0) {
            TreeMap<String, Object> goodJsonObj;
            for (RechargeItemInfo rechargeItem : resultMap.values()) {
                goodJsonObj = new TreeMap<>();
                goodJsonObj.put("goods_id", rechargeItem.getGoods_id()); //商品id
//                goodJsonObj.put("goods_pay_num",this.getBuyNumberRechargeItem(rechargeItem,rechargeLogMap.get(rechargeItem.getGoods_id()))); //已购买次数（用于界面显示）
                HashMap<Integer, Integer> recharge = serverRechargeMap.get(rechargeItem.getGoods_id());
                for (Map.Entry<Integer, Integer> entry : recharge.entrySet()) {
                    goodJsonObj.put("goods_pay_num", entry.getKey()); //已购买次数（用于界面显示）
                    goodJsonObj.put("goods_over_time", entry.getValue()); //到期时间，时间字符串（用于显示倒计时）
                }
                goodsJsonArray.add(goodJsonObj);
            }
        }
        jsonObj.put("goods", goodsJsonArray);
        return this.outResultSuccess(jsonObj);
    }

    /**
     * 检查某个商品是否可以购买
     */
    @At
    @POST
    @GET
    public Object checkBuy(HttpServletRequest request) {
        String server_id = request.getParameter("server_id");
        String role_id = request.getParameter("role_id");
        String goods_id = request.getParameter("goods_id");
        String sign = request.getParameter("sign");
        String newSignString = RechargeUtil.buildMd5String("server_id", server_id, "role_id", role_id, "goods_id", goods_id);
        if (!RechargeUtil.isSignOK(newSignString, sign)) {
            logger.error("md5校验失败");
            return this.outResultFailure(9, "md5校验失败");
        }

        if (StringUtils.isEmpty(server_id)) {
            logger.error("服务器id为空 查询失败");
            return this.outResultFailure(11, "服务器id为空 查询失败");
        }
        int serverId = Integer.parseInt(server_id);
        Server dblog = QueryUtil.getInstance().getFinalHeFuDB(serverId);
        if (dblog == null) {
            logger.error("server not found");
            return this.outResultFailure(12, "server not found");
        }
        if (StringUtils.isEmpty(role_id)) {
            logger.error("角色id为空 查询失败");
            return this.outResultFailure(13, "角色id为空 查询失败");
        }
        if (StringUtils.isEmpty(goods_id)) {
            logger.error("商品id为空 查询失败");
            return this.outResultFailure(14, "商品id为空 查询失败");
        }
        long playerId;
        try {
            playerId = role_id.matches("[0-9]+") ? Long.parseLong(role_id) : Long.parseLong(role_id, 36);
        } catch (Exception e) {
            logger.error("输入查询参数错误");
            return this.outResultFailure(15, "输入查询参数错误");
        }
//        String version = request.getParameter("version");
//        if(StringUtils.isEmpty(version)){
//            logger.error("版本号数据为空");
//            return Toolkit.outResult(false, "版本号数据为空");
//        }
        String roleSqlStr = "SELECT * FROM $table WHERE roleId = @roleId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "roleId", playerId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("连接数据库失败");
            return this.outResultFailure(16, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            logger.error("无角色");
            return this.outResultFailure(17, "无角色");
        }
        Map<Integer, RechargeItemInfo> allRechargeItemMap = RechargeItemManager.getInstance().getRechargeItemInfoMap();
        if (allRechargeItemMap == null || allRechargeItemMap.size() == 0) {
            logger.error("充值配置不存在");
            return this.outResultFailure(18, "充值配置不存在");
        }
        int goodsId = Integer.parseInt(goods_id);
        if (!allRechargeItemMap.containsKey(goodsId)) {
            logger.error("充值id不存在");
            return this.outResultFailure(18, "充值id不存在");
        }
        //当前商品配置
        RechargeItemInfo rechargeItem = allRechargeItemMap.get(goodsId);
        //不在APIServer充值缓存中
        if (rechargeItem == null) {
            logger.error("APIServer缓存中不存在充值ID:" + goodsId);
            return this.outResultFailure(18, "APIServer缓存中不存在充值ID:" + goodsId);
        }
        //所有购买日志集合 (按照商品id 分类)
//        Map<Integer, List<Map<String, Object>>> rechargeItemLogByGoodIdMap = this.getRechargeItemLogByGoodIdMap(dblog, playerId);

        boolean canBuy = false;
        try {
            int finalServerId = QueryUtil.getInstance().getHeFuId(serverId);
            Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", finalServerId));
            if (server == null) {
                logger.error(serverId + "服获取连接失败！");
                return this.outResultFailure(11, "服获取连接失败！");
            }
            //向游戏服请求角色可购买商品列表（游戏服只会返回玩家达到可购买条件的充值商品，包括次数上限也会检查）
            NutMap httpResultMap = GameServerRequestUtil.gmGetRoleRechargeItem(server, playerId, goods_id);
            if (!httpResultMap.getBoolean("ok")) {
                logger.error(serverId + "服,检查是否可购买失败！操作结果：" + httpResultMap.get("data").toString());
                return this.outResultFailure(11, "无法从游戏服检查是否可购买");
            }
            canBuy = true;
        } catch (Exception e) {
            logger.error(serverId + "服检查是否可购买失败！error：" + e.getMessage());
            return this.outResultFailure(11, "无法从游戏服检查是否可购买");
        }

        //获取物品购买次数
//        int buyNumber = this.getBuyNumberRechargeItem(rechargeItem, rechargeItemLogByGoodIdMap.get(goodsId));
//        int canBuy = 0;
//        if (rechargeItem.getGoods_limit() < buyNumber) {
//            canBuy = 1;
//        }

        //返回结果数据
//        TreeMap<String, Object> data = new TreeMap<>();
        JSONObject data = new JSONObject();
        data.put("game_id", 1);
        data.put("server_id", server_id);
        data.put("role_id", role_id);
        data.put("goods_id", goods_id);
        data.put("result", canBuy?1:0);
        return this.outResultSuccess(data);
    }

    /**
     * 获取物品购买次数
     *
     * @param rechargeItem
     * @param buyLogList
     * @return
     */
    public int getBuyNumberRechargeItem(RechargeItemInfo rechargeItem, List<Map<String, Object>> buyLogList) {
        int buyNumber = 0;
        //终身限购次数
        if (rechargeItem.getGoods_subtype() == RechargeSubType.EnLimitOne) {
            buyNumber = this.getBuyNumberByOne(rechargeItem, buyLogList);
            if (rechargeItem.getGoods_limit() >= buyNumber) {
                return buyNumber;
            }
        }
        //判断每周
        else if (rechargeItem.getGoods_subtype() == RechargeSubType.EnLimitWeekly) {
            buyNumber = this.getBuyNumberByWeekly(rechargeItem, buyLogList);
            if (rechargeItem.getGoods_limit() >= buyNumber) {
                return buyNumber;
            }
        }
        //判断每天购买次数
        else if (rechargeItem.getGoods_subtype() == RechargeSubType.EnLimitDay) {
            buyNumber = this.getBuyNumberByDay(rechargeItem, buyLogList);
            if (rechargeItem.getGoods_limit() >= buyNumber) {
                return buyNumber;
            }
        }
        return 0;
    }


    /**
     * 获取改商品所有购买次数
     *
     * @param rechargeItem
     * @param buyLogList
     * @return
     */
    public int getBuyNumberByOne(RechargeItemInfo rechargeItem, List<Map<String, Object>> buyLogList) {
        //没有购买记录
        if (buyLogList == null || buyLogList.size() == 0) {
            return 0;
        }
        return buyLogList.size();
    }

    /**
     * 获取改商品本周购买次数
     *
     * @param rechargeItem
     * @param buyLogList
     * @return
     */
    public int getBuyNumberByWeekly(RechargeItemInfo rechargeItem, List<Map<String, Object>> buyLogList) {
        //没有购买记录
        if (buyLogList == null || buyLogList.size() == 0) {
            return 0;
        }
        //获取当前周开始时间
        long startSec = DateUtil.getWeekStartSec(System.currentTimeMillis());
        //获取当前周结束时间
        long endSec = DateUtil.getWeekEndSec(System.currentTimeMillis());
        int buyNumber = 0;
        for (int i = 0; i < buyLogList.size(); i++) {
            Map<String, Object> rechargeLogBean = buyLogList.get(i);
            //购买时间
            long addTime = Long.parseLong(rechargeLogBean.get("addTime").toString());
            //本周购买次数累计
            if (startSec < addTime && addTime < endSec) {
                buyNumber++;
            }
        }
        return buyNumber;
    }

    /**
     * 获取该商品本天购买次数
     *
     * @param rechargeItem
     * @param buyLogList
     * @return
     */
    public int getBuyNumberByDay(RechargeItemInfo rechargeItem, List<Map<String, Object>> buyLogList) {
        //没有购买记录
        if (buyLogList == null || buyLogList.size() == 0) {
            return 0;
        }
        int buyNumber = 0;
        for (int i = 0; i < buyLogList.size(); i++) {
            Map<String, Object> rechargeLogBean = buyLogList.get(i);
            //购买时间
            long addTime = Long.parseLong(rechargeLogBean.get("addTime").toString());
            //当前天次数累计
            if (!DateUtil.isDifferentDay(addTime, System.currentTimeMillis())) {
                buyNumber++;
            }
        }
        return buyNumber;
    }

    private List<RoleState> queryRoleState(Server server, String sqlStr, String paramKey, Object paramValue) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(server);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "rolestate");
        sql.params().set(paramKey, paramValue);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(RoleState.class);
    }

    /**
     * 所有玩家购买商品记录列表
     *
     * @param dblog
     * @param role_id
     * @return
     */
    public Map<Integer, List<Map<String, Object>>> getRechargeItemLogByGoodIdMap(Server dblog, long role_id) {
//        int dbCount = QueryUtil.getInstance().queryCount(dblog, String.format("SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='%s'",dblog.getDbname()));
        Map<Integer, List<Map<String, Object>>> rechargreItemLogByGoodIdMap = new HashMap<>();

        int tableCount = QueryUtil.getInstance().queryCount(dblog, String.format("SELECT COUNT(*) FROM information_schema.TABLES t, information_schema.SCHEMATA n WHERE t.table_name = '%s' AND n.SCHEMA_NAME = '%s'", "rechargelog", dblog.getDblogName()));
        if (tableCount <= 0) {
            return rechargreItemLogByGoodIdMap;
        }
        //所有玩家购买商品记录列表
        List<Map<String, Object>> rechargeLogList = QueryUtil.getInstance().query(dblog, "select *  FROM rechargelog where roleId = " + role_id);
        //按照商品id 分类

        if (rechargeLogList != null && rechargeLogList.size() > 0) {
            for (int i = 0; i < rechargeLogList.size(); i++) {
                Map<String, Object> rechargeBeanMap = rechargeLogList.get(i);
                List<Map<String, Object>> rechargeList = null;
                int goodsId = Integer.parseInt(rechargeBeanMap.get("goodsId").toString());
                if (rechargreItemLogByGoodIdMap.containsKey(goodsId)) {
                    rechargeList = rechargreItemLogByGoodIdMap.get(goodsId);
                } else {
                    rechargeList = new ArrayList<>();
                }
                rechargeList.add(rechargeBeanMap);
            }
        }
        return rechargreItemLogByGoodIdMap;
    }

    /**
     * 通用结构体成功返回
     *
     * @param data
     * @return
     */
    public Object outResultSuccess(Object data) {
        JSONObject commonData = new JSONObject();
        commonData.put("success", true);
        commonData.put("result_code", 200);
        commonData.put("data", data);
        commonData.put("msg", "成功");
        commonData.put("time", DateUtil.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        String md5Sign = RechargeUtil.buildMd5String(commonData);
        String md5 = MD5Util.MD5(md5Sign).toLowerCase();
        commonData.put("sign", md5);
//        logger.info("加密字符串:"+md5Sign+",加密结果："+ md5);
//        logger.info("返回数据:"+commonData);
        return commonData;
    }

    /**
     * 通用结构体失败返回
     *
     * @param result_code
     * @param msg
     * @return
     */
    public Object outResultFailure(int result_code, String msg) {
        JSONObject commonData = new JSONObject();
        commonData.put("success", false);
        commonData.put("result_code", result_code);
        commonData.put("data", "");
        commonData.put("msg", msg);
        commonData.put("time", DateUtil.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        String md5Sign = RechargeUtil.buildMd5String(commonData);
        String md5 = MD5Util.MD5(md5Sign).toLowerCase();
        commonData.put("sign", md5);
//        logger.info("返回错误结果时，加密字符串:"+md5Sign+",加密结果："+ md5);
//        logger.info("返回数据:"+commonData);
        return commonData;
    }
}
