package common.activity;

import com.game.activity.log.ActivityDonateLog;
import com.game.activity.log.ActivityGetLog;
import com.game.activity.manager.ActivityManager;
import com.game.activity.script.IActivityManageScript;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.backgrand.manager.BackGrandServer;
import com.game.backgrand.script.IBackCommandScript;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.db.bean.ActivityConfigBean;
import com.game.db.bean.ActivityDataBean;
import com.game.db.bean.RoleActivityDataBean;
import com.game.db.bean.TagInfoBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.ActivityMessage;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运营活动管理脚本类(活动具体数据由运营在GM后台配置)
 */
public class ActivityManageScript implements IActivityManageScript {

    private static final Logger log = LogManager.getLogger(ActivityManageScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ActivityScriptBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqActivityDeal(Player player, int actType, String dataStr) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            log.error("script is null. scriptId：" + ScriptEnum.getActivityScriptId(actType / 1000));
            return;
        }
        if (!checkOpen(player, actType)) {
            return;
        }

        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actType);
        if (actCfg == null) {
            log.error("ActivityConfig is null. type：" + actType);
            return;
        }
        try{
            as.onReqActivityDeal(player, dataStr, actCfg);
        }catch (Exception e){
            log.error(e+",请求操作运营活动时异常",e);
        }
    }

    @Override
    public void sendActivityDealMessage(Player player, int actType, String dataStr) {
        ActivityMessage.ResActivityDeal.Builder msg = ActivityMessage.ResActivityDeal.newBuilder();
        msg.setType(actType);
        msg.setData(dataStr);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqActivity(Player player, int actType) {
        if (!checkOpen(player, actType)) {
            return;
        }
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = getActivityDataBuilder(player.getId(), actType);
        msg.setAct(actMess);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void playerOnline(Player player) {

        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            as.playerOnline(player, actCfg);
        }

        ActivityMessage.ResActivityList.Builder msg = ActivityMessage.ResActivityList.newBuilder();
        for (ActivityConfig actCfg : Manager.activityManager.getActCfgMap().values()) {
            ActivityMessage.Activity.Builder actMess = getActivityBuilder(player.getId(), actCfg);
            msg.addActList(actMess);
//            log.info("=========actId:"+actCfg.getId()+",actType:"+actCfg.getType());
        }
//        log.info("活动数量:"+msg.getActListCount()+",活动信息:"+msg.build().toString());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private ActivityMessage.Activity.Builder getActivityBuilder(long roleId, ActivityConfig actCfg) {

        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actCfg.getType());
        actMess.setActConfig(getActivityConfigStr(roleId, actCfg));

        String actData = getActivityDataStr(roleId, actCfg.getType());
        if (actData == null || actData.equals("")) {
            actMess.setActData("{}");
        } else {
            actMess.setActData(actData);
        }
        return actMess;
    }

    private ActivityMessage.Activity.Builder getActivityDataBuilder(long roleId, int actType) {
        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actType);

        String actData = getActivityDataStr(roleId, actType);
        if (actData == null || actData.equals("")) {
            actMess.setActData("{}");
        } else {
            actMess.setActData(actData);
        }
        return actMess;
    }

    @Override
    public ConcurrentHashMap<String, Object> getRoleActivityData(long roleId, int actType) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> roleActDataMap = Manager.activityManager.getRoleActDatas().get(roleId);
        if (roleActDataMap == null) {
            roleActDataMap = new ConcurrentHashMap<>();
            Manager.activityManager.getRoleActDatas().put(roleId, roleActDataMap);
        }
        ConcurrentHashMap<String, Object> roleActData = roleActDataMap.get(actType);
        if (roleActData == null) {
            roleActData = new ConcurrentHashMap<>();
            roleActDataMap.put(actType, roleActData);
        }
        return roleActData;
    }

    @Override
    public ConcurrentHashMap<String, Object> getActivityData(int actType) {
        ConcurrentHashMap<String, Object> ActDataMap = Manager.activityManager.getActDatas().get(actType);
        if (ActDataMap == null) {
            ActDataMap = new ConcurrentHashMap<>();
            Manager.activityManager.getActDatas().put(actType, ActDataMap);
        }
        return ActDataMap;
    }

    @Override
    public boolean checkLevel(int level, ActivityConfig actCfg) {
        return level >= actCfg.getMinLv() && actCfg.getMaxLv() >= level;
    }

    /**
     * 检测活动是否开启
     * @param player
     * @param type
     * @return
     */
    public boolean checkOpen(Player player, int type) {
        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
        if (actCfg == null) {
            return false;
        }
        if (!actCfg.isActiviting()) {
            return false;
        }
        /// if (player != null) {
        ///     if (!checkLevel(player.getLevel(), actCfg)) {
        ///         return false;
        ///     }
        /// }
        return true;
    }

    private String getActivityConfigStr(long roleId, ActivityConfig actCfg) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("type", actCfg.getType());
        resultMap.put("minLv", actCfg.getMinLv());
        resultMap.put("maxLv", actCfg.getMaxLv());
        resultMap.put("tag", actCfg.getTag());
        resultMap.put("sort", actCfg.getSort());
        resultMap.put("name", actCfg.getName());

        long bTime = actCfg.getBeginTime();
        long eTime = actCfg.getEndTime();

        resultMap.put("beginTime", bTime);
        resultMap.put("endTime", eTime);
        resultMap.put("isDelete", actCfg.getIsDelete());

        //客户端需要的自定义配置数据
        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(roleId);
        String custom = "client";
        if (actCfg.getCustomCfgMap().containsKey(custom)) {
            resultMap.put("custom", actCfg.getCustomCfgMap().get(custom));
        }
        if (actCfg.getCustomCfgMap().containsKey(custom + player.getCareer())) {
            resultMap.put("custom", actCfg.getCustomCfgMap().get(custom + player.getCareer()));
        }
        return JsonUtils.toJSONString(resultMap);
    }

    private String getActivityDataStr(long roleId, int actType) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            return null;
        }
        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actType);
        if (actCfg == null) {
            log.error("ActivityConfig is null, type=" + actType);
            return null;
        }
        String dataStr = "";
        try{
            dataStr = as.getActivityDataStr(actCfg, roleId);
        }catch (Exception e){
            log.error("获取运营活动数据时报错，actId="+actCfg.getId(),e);
        }

        return dataStr;
    }

    @Override
    public boolean registerActivityBean(ActivityConfigBean actBean) {
        try {
            if (actBean == null) {
                return false;
            }

            if (actBean.getType() <= 0) {
                return false;
            }
            Manager.countManager.setCount(ActivityManager.getInstance(), BaseCountType.Activity, actBean.getId(), Count.RefreshType.CountType_Day, 0);

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actBean.getType());
            if (actCfg != null) {//已经注册了,更新活动配置
                //已经有加载的同类型活动，排除活动时间不匹配的
                if (!isActiviting(actBean, TimeUtils.Time())) {
                    actBean.setState((byte) 0);
                    addPreMap(actBean);
                    return true;
                }

                IActivityScript as = getScript(actBean.getType());
                if (as == null) {
                    return false;
                }

                ConcurrentHashMap<String, Object> oldCustomMap = actCfg.getCustomCfgMap();

                actCfg.setCustomCfgMap(new ConcurrentHashMap<String, Object>());
                //检查解析自定义配置字段是否正确
                if (!(as.parseCustomConfig(actCfg, actBean.getCustom()))) {
                    log.error("自定义运营活动数据解析错误，actId=" + actCfg.getId());
                    actCfg.setCustomCfgMap(oldCustomMap);
                    return false;
                }

                //结束之前的活动处理
                as.activityEndDeal(actCfg);

                //删除之前的活动(修改标记)
                Manager.activityManager.deal().delActConfig(actCfg.getId());

                //清理之前的活动数据
                cleanActData(actCfg.getType());

                //更新活动基础信息
                actCfg.beanToActivityBaseConfig(actBean);
                actCfg.setIsDelete((byte) 0);

                //活动配置变更后活动处理
                sendActivityOpen(actCfg);
                return true;
            }

            //检查活动是否可以加入进行的活动
            if (!isActiviting(actBean, TimeUtils.Time())) {
                actBean.setState((byte) 0);
                addPreMap(actBean);
                return true;
            }

            //注册新活动
            actCfg = new ActivityConfig();
            //注册自定义活动信息
            IActivityScript as = getScript(actBean.getType());
            if (as == null) {
                return false;
            }

            //设置活动基础信息
            actCfg.beanToActivityBaseConfig(actBean);

            //解析自定义配置字段
            if (!as.parseCustomConfig(actCfg, actBean.getCustom())) {
                log.error("自定义运营活动数据解析错误");
                return false;
            }

            Manager.activityManager.getActCfgMap().put(actBean.getType(), actCfg);

            sendActivityOpen(actCfg);
            return true;
        } catch (Exception e) {
            log.error(actBean, e);
        }
        return false;
    }

    private boolean loadActConfig(ActivityConfigBean actBean) {
        try {
            int actType = actBean.getType();
            if (actBean == null) {
                return false;
            }

            if (actType <= 0) {
                return false;
            }

            if (actBean.getIsDelete() == 1) {
                return false;
            }

            long nowTime = TimeUtils.Time();
            //活动已过期，删除活动
            if (nowTime >= actBean.getEndTime()) {
                delActConfig(actBean.getId());
                return false;
            }

            if (actBean.getState() == 0) {//预发布
                addPreMap(actBean);
            } else if (actBean.getState() == 1) {//进行活动
                //如果已经加载了同类型活动则排除不在活动时间内的。
                if (Manager.activityManager.getActCfgMap().containsKey(actType)) {
                    if (!isActiviting(actBean, nowTime)) {
                        addPreMap(actBean);
                        actBean.setState((byte) 0);
                        Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
                        return false;
                    }
                }

                IActivityScript as = getScript(actType);
                if (as == null) {
                    return false;
                }

                //注册新活动
                ActivityConfig actCfg = new ActivityConfig();

                //设置活动基础信息
                actCfg.beanToActivityBaseConfig(actBean);

                //检查解析自定义配置字段是否正确
                if (!(as.parseCustomConfig(actCfg, actBean.getCustom()))) {
                    log.error("自定义运营活动数据解析错误，actId=" + actCfg.getId());
                    return false;
                }

                Manager.activityManager.getActCfgMap().put(actType, actCfg);
            }
            return true;
        } catch (Exception e) {
            log.error(actBean, e);
        }
        return false;
    }

    private boolean isActiviting(ActivityConfigBean actBean, long nowTime) {
        //增加新服活动条件 开服7天内只有新服活动才会生效
        if(TimeUtils.getOpenServerDay() <= 7){
            if(actBean.getIsOpenServer() != 1){
                return false;
            }
        }
        return nowTime >= actBean.getBeginTime() && nowTime <= actBean.getEndTime();
    }

    private void addPreMap(ActivityConfigBean actBean) {
        Map<Integer, ActivityConfigBean> preMap = Manager.activityManager.getPreCfgMap().get(actBean.getType());
        if (preMap == null) {
            preMap = new HashMap<>();
            Manager.activityManager.getPreCfgMap().put(actBean.getType(), preMap);
        }
        preMap.put(actBean.getId(), actBean);
    }

    private void sendActivityOpen(ActivityConfig actCfg) {
        if (Manager.countManager.getCount(ActivityManager.getInstance(), BaseCountType.Activity, actCfg.getId()) <= 0) {

            Manager.countManager.setCount(ActivityManager.getInstance(), BaseCountType.Activity, actCfg.getId(), Count.RefreshType.CountType_Day, 1);

            ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
            for (Player player : Manager.playerManager.getOnLines()) {
                ActivityMessage.Activity.Builder actMess = getActivityBuilder(player.getId(), actCfg);
                msg.setAct(actMess);
                MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public IActivityScript getScript(int actType) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.getActivityScriptId(actType / 1000));
        if (is == null) {
            return null;
        }
        if (is instanceof IActivityScript) {
            return (IActivityScript) is;
        }
        return null;
    }

    @Override
    /**
     * 通过活动逻辑类型ID,获取关联的活动配置信息列表
     * @param actLogicID
     * @return
     */
    public List<ActivityConfig> getActCfgListByActLogicID(int actLogicID) {
        List<ActivityConfig> result = new ArrayList<>();
        for (Integer typeID : Manager.activityManager.getActCfgMap().keySet()) {
            if (toActLogicID(typeID) == actLogicID) {
                result.add(Manager.activityManager.getActCfgMap().get(typeID));
            }
        }
        return result;
    }

    @Override
    public int toActLogicID(int actType) {
        return actType / 1000;
    }

    @Override
    public int toActType(int logicID, int festivalID) {
        return logicID * 1000 + festivalID;
    }

    @Override
    public void sendActivityConfigChange(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void sendActivityDataChange(Player player, int actType) {
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = getActivityDataBuilder(player.getId(), actType);
        msg.setAct(actMess);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void saveActConfig(ActivityConfig actCfg, int state) {
        ActivityConfigBean bean = makeActivityBean(actCfg);
        bean.setState((byte) state);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
    }

    @Override
    public void delActConfig(int id) {
        ActivityConfigBean bean = new ActivityConfigBean();
        bean.setId(id);
        bean.setWhere(id);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYCONFIG_DELETE, SaveServer.DELETE);
    }

    @Override
    public void saveActData(int type, ConcurrentHashMap<String, Object> actDataMap) {
        ActivityDataBean bean = new ActivityDataBean();
        bean.setType(type);
        String str = JsonUtils.toJSONString(actDataMap);
        bean.setActData(str);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_UPDATE, SaveServer.MERGE);
    }

    @Override
    public void delActData(int type) {
        ActivityDataBean bean = new ActivityDataBean();
        bean.setType(type);
        bean.setWhere(type);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);
    }

    @Override
    public void saveRoleActData(long roleId, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> actDataMap) {
        RoleActivityDataBean bean = new RoleActivityDataBean();
        bean.setRoleId(roleId);
        String str = JsonUtils.toJSONString(actDataMap);
        bean.setActData(str);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ROLEACTIVITYDATA_UPDATE, SaveServer.MERGE);
    }

    /**
     * 检查活动开启和活动关闭
     */
    @Override
    public void checkAllActivity() {
        long now = TimeUtils.Time();
        //检查关闭
        Iterator<Entry<Integer, ActivityConfig>> iterator1 = Manager.activityManager.getActCfgMap().entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<Integer, ActivityConfig> entry = iterator1.next();
            int actType = entry.getKey();
            ActivityConfig actCfg = entry.getValue();
            //活动已过期，移除掉
            if (actCfg.getEndTime() < now) {
                IActivityScript as = getScript(actType);
                if (as == null) {
                    log.error("活动脚本错误：actType=" + actType);
                    continue;
                }

                try {
                    //结束活动处理
                    as.activityEndDeal(actCfg);
                }catch (Exception e){
                    log.error(e+",结束活动处理时出错,actId="+actCfg.getId(), e);
                }
                //清理活动配置
                iterator1.remove();
                Manager.activityManager.deal().delActConfig(actCfg.getId());

                //清理活动相关数据
                cleanActData(actType);
                continue;
            }
            if (actCfg.isActiviting()) {
                sendActivityOpen(actCfg);
            }
        }


        //TODO 检查预备活动是否有满足开启条件的
        Iterator<Entry<Integer, Map<Integer, ActivityConfigBean>>> its = Manager.activityManager.getPreCfgMap().entrySet().iterator();
        while (its.hasNext()) {
            Entry<Integer, Map<Integer, ActivityConfigBean>> entry = its.next();
            int actType = entry.getKey();

            if (Manager.activityManager.getActCfgMap().containsKey(actType)) {
                //已经有进行中的该类型活动
                continue;
            }

            //预备列表中，同类型活动可能会有多种
            Iterator<Entry<Integer, ActivityConfigBean>> preIt = entry.getValue().entrySet().iterator();
            while (preIt.hasNext()) {
                Entry<Integer, ActivityConfigBean> pre = preIt.next();
                ActivityConfigBean actBean = pre.getValue();
                //增加新服活动条件 开服7天内只有新服活动才会生效
                if(TimeUtils.getOpenServerDay() <= 7){
                    if(actBean.getIsOpenServer() != 1){
                        continue;
                    }
                }
                //活动满足开放
                if (actBean.getBeginTime() < now && now < actBean.getEndTime()) {
                    IActivityScript as = getScript(actType);
                    if (as == null) {
                        continue;
                    }
                    ActivityConfig actCfg = new ActivityConfig();
                    actCfg.beanToActivityBaseConfig(actBean);
                    //解析自定义配置字段
                    try{
                        if (!as.parseCustomConfig(actCfg, actBean.getCustom())) {
                            log.error("自定义运营活动数据解析错误");
                            continue;
                        }
                    }catch (Exception e){
                        log.error(e+",检查活动开启时出错, actId="+actCfg.getId(), e);
                        continue;
                    }
                    Manager.activityManager.getActCfgMap().put(actType, actCfg);
                    //开放活动成功则预备活动缓存列表中移除
                    preIt.remove();

                    sendActivityOpen(actCfg);

                    actBean.setState((byte) 1);
                    Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
                    break;
                }
            }
        }
    }

    @Override
    public void load() {
        Manager.activityManager.getActCfgMap().clear();
        List<ActivityConfigBean> configList = Manager.activityManager.getConfigDao().selectAll();
        if (configList == null) {
            return;
        }

        for (ActivityConfigBean bean : configList) {
            loadActConfig(bean);
        }

        //加载活动数据
        Manager.activityManager.getActDatas().clear();
        List<ActivityDataBean> dataList = Manager.activityManager.getDataDao().selectAll();
        if (dataList == null) {
            return;
        }

        for (ActivityDataBean bean : dataList) {
            Manager.activityManager.getActDatas().put(bean.getType(), JsonUtils.parseObject(bean.getActData(), new TypeReference<ConcurrentHashMap<String, Object>>() {
            }));
        }

        //加载玩家活动数据
        Manager.activityManager.getRoleActDatas().clear();
        List<RoleActivityDataBean> roleDataList = Manager.activityManager.getRoleDataDao().selectAll();
        if (roleDataList == null) {
            return;
        }

        for (RoleActivityDataBean bean : roleDataList) {
            Manager.activityManager.getRoleActDatas().put(bean.getRoleId(), JsonUtils.parseObject(bean.getActData(),
                    new TypeReference<ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>>() {
                    }));
        }

        log.info("共加载了" + configList.size() + "条活动配置数据");
    }

    @Override
    public int loadTagInfo() {
        Manager.activityManager.getTagInfoList().clear();
        List<TagInfoBean> tagList = Manager.activityManager.getTagDao().selectAll();
        if (tagList != null && !tagList.isEmpty()) {
            Manager.activityManager.getTagInfoList().addAll(tagList);
            return Manager.activityManager.getTagInfoList().size();
        }
        return 0;
    }

    /**
     * 活动零点处理
     *
     * @param player 玩家
     */
    @Override
    public void zeroClockPlayerDeal(Player player) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            as.zeroClockPlayerDeal(player, actCfg);
        }
    }

    @Override
    public void fiveClockPlayerDeal(Player player) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            as.fiveClockPlayerDeal(player, actCfg);
        }
    }

    @Override
    public void zeroClockDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.zeroClockDeal(actCfg);
        }
    }

    @Override
    public void fiveClockDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.fiveClockDeal(actCfg);
        }
    }

    @Override
    public void everyHourDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.everyHourDeal(actCfg);
        }
    }

    @Override
    public void cleanActivity(int actType) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            return;
        }

        as.activityEndDeal(Manager.activityManager.getActCfgMap().get(actType));

        //清理活动配置
        Manager.activityManager.getActCfgMap().remove(actType);
        Manager.activityManager.deal().delActConfig(actType);

        //清理活动相关数据
        cleanActData(actType);
    }

    @Override
    public void sendActivityTagInfo(Player player) {
        ActivityMessage.ResTagInfoList.Builder builder = ActivityMessage.ResTagInfoList.newBuilder();
        builder.setTag(JsonUtils.toJSONString(Manager.activityManager.getTagInfoList()));
        MessageUtils.send_to_player(player, ActivityMessage.ResTagInfoList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void sendActivityTagInfo(Player player, String data) {
        ActivityMessage.ResTagInfoList.Builder builder = ActivityMessage.ResTagInfoList.newBuilder();
        builder.setTag(data);
        MessageUtils.send_to_player(player, ActivityMessage.ResTagInfoList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void cleanActData(int actType) {
        //清理活动数据
        if (Manager.activityManager.getActDatas().containsKey(actType)) {
            Manager.activityManager.getActDatas().remove(actType);
            Manager.activityManager.deal().delActData(actType);
        }

        //清理玩家活动数据
        for (Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : Manager.activityManager.getRoleActDatas().entrySet()) {
            long roleId = entry.getKey();
            if (entry.getValue().containsKey(actType)) {
                entry.getValue().remove(actType);
                Manager.activityManager.deal().saveRoleActData(roleId, entry.getValue());
            }
        }
    }

    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (!actCfg.isRecordTime()){
                continue;
            }
            as.rechargeDeal(player, getGoodsCfgId, rechargeNum, actCfg);
        }
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (!actCfg.isRecordTime()){
                continue;
            }
            as.consumeDeal(player, coinType, consumeNum, actCfg);
        }
    }

    @Override
    public List<Long> getRoleIdList(int actType) {
        List<Long> roleIds = new ArrayList<>();
        for (Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : Manager.activityManager.getRoleActDatas().entrySet()) {
            if (entry.getValue().containsKey(actType)) {
                roleIds.add(entry.getKey());
            }
        }
        return roleIds;
    }

    /**
     * 获取活动掉落
     *
     * @param player
     * @return
     */
    @Override
    public void bossDrop(Player player, int bossId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            as.bossDrop(player, bossId, actCfg);
        }
    }

    public boolean boxDrop(Player player, int boxId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (as.boxDrop(player, boxId, actCfg)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 副本活动掉落
     *
     * @param player
     * @param cloneId
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig is null. type：" + type);
                continue;
            }

            if (as.cloneDrop(player, cloneId, actCfg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查登录
     */
    private boolean checkLogin(Player player, String[] ss) {
        try {
            Date d = TimeUtils.getSdfNoSecond().parse(ss[1]);
            long beginDay = TimeUtils.GetCurTimeInMin(4, d.getTime());
            d = TimeUtils.getSdfNoSecond().parse(ss[2]);
            long endDay = TimeUtils.GetCurTimeInMin(4, d.getTime());

            for (Integer last : player.getLoginDays()) {
                if (beginDay <= last && last < endDay) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e, e);
            log.error("ss参数内容如下:");
            for (String s : ss) {
                log.error("s=" + s);
            }
            return false;
        }
        return false;
    }

    private void writeActivityDonateLog(long sendId, long receiveId, int itemModelId, int num, long actionId) {
        try {
            ActivityDonateLog donateLog = new ActivityDonateLog();
            donateLog.setActionId(actionId);
            donateLog.setItemModelid(itemModelId);
            donateLog.setNum(num);
            donateLog.setReceiveId(receiveId);
            donateLog.setSendId(sendId);
            donateLog.setSid(Manager.playerManager.getCreateServeId(sendId));
            LogService.getInstance().execute(donateLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeGetActivityLog(Player player, long activityId, String parms, long actionId) {
        try {
            ActivityGetLog getLog = new ActivityGetLog();
            getLog.setActionId(actionId);
            getLog.setActivityId(activityId);
            getLog.setParms(parms);
            getLog.setRoleId(player.getId());
            getLog.setSid(player.getCreateServerId());
            getLog.setPlatformName(player.getPlatformName());
            LogService.getInstance().execute(getLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 检查活动是否是最早的活动，只选择最早的活动
     */
    private ActivityConfig checkActivity(Map<Long, ActivityConfig> activityMap) {
        long now = TimeUtils.Time();
        ActivityConfig activity = new ActivityConfig();
        ActivityConfigBean bean;
        long minStartTime = 9999999999999L;
        long minEndTime = 0L;
        //活动检查
        Iterator<Entry<Long, ActivityConfig>> iterator1 = activityMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<Long, ActivityConfig> entry = iterator1.next();
            if (entry.getValue().getEndTime() < (now - 5 * 1000)) {
                //检查活动是否已经结束，云购活动未开启幸运大奖的需要开启幸运大奖
                //活动已过期，移除掉
                bean = new ActivityConfigBean();
                bean.setWhere(entry.getKey());
                Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);
                iterator1.remove();
            }
            if (entry.getValue().getBeginTime() < minStartTime) {
                minStartTime = entry.getValue().getBeginTime();
                minEndTime = entry.getValue().getEndTime();
                activity = entry.getValue();
            } else if (entry.getValue().getBeginTime() == minStartTime) {
                if (entry.getValue().getEndTime() < minEndTime) {
                    minStartTime = entry.getValue().getBeginTime();
                    minEndTime = entry.getValue().getEndTime();
                    activity = entry.getValue();
                }
            }
        }
        return activity;
    }

    /**
     * 后台设置活动
     *
     * @param actBean    后台活动信息
     * @param b2wSession 会话
     */
    public void w2gSyncActivity(ActivityConfigBean actBean, Channel b2wSession) {
        boolean result = w2gSyncActivity(actBean);
        Map<String, Object> map = new HashMap<>(16);
        map.put("ok", result);
        map.put("msg", result ? "发布成功！" : "发布失败！");

//		BackGrandServer.Send(b2wSession, JSON.toJSONString(map, true));
        BackGrandServer.Send(b2wSession, JsonUtils.toJSONString(map));
    }

    /**
     * 后台设置活动
     *
     * @param actBean 后台活动信息
     */
    @Override
    public boolean w2gSyncActivity(ActivityConfigBean actBean) {
        log.error("收到后台发布活动 activityId:" + actBean.getId() + ",活动类型：" + actBean.getType() + ",活动名称：" + actBean.getName());
        actBean.setWhere(actBean.getId());
        //注册活动数据
        boolean isSuccess = false;
        if (actBean.getState() == 1) {//覆盖掉正在进行的活动
            isSuccess = Manager.activityManager.deal().registerActivityBean(actBean);
        } else {
            addPreMap(actBean);
            isSuccess = true;
        }
        if (isSuccess) {
//            Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);
        }
        return isSuccess;
    }

    /**
     * 后台批量发布活动
     *
     * @param activityBeans 活动信息列表
     */
    @Override
    public List<Integer> w2gBatchSyncActivity(List<ActivityConfigBean> activityBeans) {
        log.error("收到后台批量发布活动");
        List<Integer> faultList = new ArrayList<>();
        for (ActivityConfigBean actConfigBean : activityBeans) {
            actConfigBean.setWhere(actConfigBean.getId());
            boolean isSuccess = false;
            if (actConfigBean.getState() == 1) {
                isSuccess = Manager.activityManager.deal().registerActivityBean(actConfigBean);
            } else {
                addPreMap(actConfigBean);
                isSuccess = true;
            }

//            if(actConfigBean.getState()==1){
//                log.info("===========ActivityConfigBean:"+actConfigBean);
//            }

            if (isSuccess) {
//                Manager.saveThreadManager.getOtherServerSave().deal(actConfigBean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);

            } else {
                faultList.add(actConfigBean.getId());
            }
        }
        return faultList;
    }

    /**
     * 后台批量发布活动
     *
     * @param activityConfigBeans 活动信息列表
     * @param session             会话
     */
    public void w2gBatchSyncActivity(List<ActivityConfigBean> activityConfigBeans, Channel session) {
        List<Integer> faultList = w2gBatchSyncActivity(activityConfigBeans);
        Map<String, Object> map = new HashMap<>(16);
        if (faultList.isEmpty()) {
            map.put("ok", true);
            map.put("msg", "批量发布成功！");
        } else {
            map.put("ok", false);
            map.put("data", faultList);
        }
//		BackGrandServer.Send(session, JSON.toJSONString(map, true));
        BackGrandServer.Send(session, JsonUtils.toJSONString(map));
    }

    /**
     * 后台删除设置活动
     *
     * @param b2wSession 会话
     */
    @Override
    public void w2gSyncDeleteActivity(int actType, Channel b2wSession) {
        log.error("世界服来的删除活动 activityId:" + actType);
        //Channel b2wSession = BackGrandServer.getBackSession().get(sessionId); //就没有做加入缓存操作，用sessionId取不到
        Map<String, Object> map = new HashMap<>(16);
        map.put("ok", true);
        map.put("msg", actType + "活动删除成功！");
        Manager.activityManager.getActCfgMap().remove(actType);
//		BackGrandServer.Send(b2wSession, JSON.toJSONString(map, true));
        BackGrandServer.Send(b2wSession, JsonUtils.toJSONString(map));
    }

    /**
     * 后台删除设置活动
     */
    public void w2gSyncDeleteActivity(int actType) {
        log.error("世界服来的删除活动 actType:" + actType);
        Manager.activityManager.getActCfgMap().remove(actType);
    }

    public void batchDelActivity(int actType) {
        log.error("批量删除活动 actType:" + actType);

        IActivityScript as = getScript(actType);
        if (as == null) {
            return;
        }
        //活动已经删除
        if (!Manager.activityManager.getActCfgMap().containsKey(actType)) {
            return;
        }

        as.activityEndDeal(Manager.activityManager.getActCfgMap().get(actType));

        Manager.activityManager.getActCfgMap().remove(actType);

        cleanActData(actType);

        noticeDelActivityMessage(actType);
    }

    private void noticeDelActivityMessage(int actType) {
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actType);

        HashMap<String, Object> actMap = new HashMap<>();
        actMap.put("isDelete", 1);

        actMess.setActConfig(JsonUtils.toJSONString(actMap));
        msg.setAct(actMess);

        for (Player player : Manager.playerManager.getOnLines()) {
            MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void gmSetActivity(Map<String, Object> cmdMap) {
        IBackCommandScript script = (IBackCommandScript) Manager.scriptManager.GetScriptClass(ScriptEnum.BackCommandBaseScript);
        script.gmActivitySendMess(cmdMap);
    }

    private ActivityConfigBean makeActivityBean(ActivityConfig actCfg) {
        ActivityConfigBean actBean = new ActivityConfigBean();
        actBean.setId(actCfg.getId());
        actBean.setType(actCfg.getType());
        actBean.setMinLv(actCfg.getMinLv());
        actBean.setMaxLv(actCfg.getMaxLv());
        actBean.setTag(actCfg.getTag());
        actBean.setSort(actCfg.getSort());
        actBean.setName(actCfg.getName());
        actBean.setBeginTime(actCfg.getBeginTime());
        actBean.setEndTime(actCfg.getEndTime());
        actBean.setIsDelete(actCfg.getIsDelete());
        actBean.setCustom(actCfg.getCustom());
        return actBean;
    }

    @Override
    public void reload() {
        if (Manager.activityManager == null) {
            return;
        }
    }
}
