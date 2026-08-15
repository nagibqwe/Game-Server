package com.backend.module.activity;

import com.backend.manager.ActivityManager;
import com.backend.struct.activity.ActivityType;
import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.JsonUtils;
import com.backend.utils.TimeUtils;
import com.backend.utils.Toolkit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * 运营活动
 */
@IocBean
@Ok("json")
@At("/activity")
@Fail("http:500")
@Filters
public class ActivityModule {

    private static final Log log = Logs.getLog(ActivityModule.class);

    private Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());

    @Inject
    protected Dao dao;

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String getPage(HttpServletRequest request, int type) {
        request.setAttribute("type", type);
        switch (type) {
            case 0: //活动总览
                return "/WEB-INF/jsp/activity/op/ActivityList.jsp";
            case ActivityType.GetActive: //活跃领取
                return "/WEB-INF/jsp/activity/op/GetActive.jsp";
            case ActivityType.DailyRecharge: //累计每日充值
                return "/WEB-INF/jsp/activity/op/DailyRecharge.jsp";
            case ActivityType.LimitTimeLogin: //限时登陆奖励
                return "/WEB-INF/jsp/activity/op/LimitTimeLogin.jsp";
            case ActivityType.LimitGiftBag: //限够礼包
                return "/WEB-INF/jsp/activity/op/LimitGiftBag.jsp";
            case ActivityType.LimitedTotalRecharge: //限时累充
                return "/WEB-INF/jsp/activity/op/LimitedTotalRecharge.jsp";
            case ActivityType.LimitedTotalConsume: //限时累计消耗
                return "/WEB-INF/jsp/activity/op/LimitedTotalConsume.jsp";
            case ActivityType.GroupBuy://团购
                return "/WEB-INF/jsp/activity/op/GroupBuy.jsp";
            case ActivityType.LuckyCat://招财猫
                return "/WEB-INF/jsp/activity/op/LuckyCat.jsp";
            case ActivityType.CollectGoodsExChange://集物兑换
                return "/WEB-INF/jsp/activity/op/CollectGoodsExChange.jsp";
            case ActivityType.DrawReward://天帝宝库
                return "/WEB-INF/jsp/activity/op/DrawReward.jsp";
            case ActivityType.HolidayBoss://首领狂欢
                return "/WEB-INF/jsp/activity/op/HolidayBoss.jsp";
            case ActivityType.HolidayTask://庆典任务
                return "/WEB-INF/jsp/activity/op/HolidayTask.jsp";
            case ActivityType.HolidayWords://节日集字
                return "/WEB-INF/jsp/activity/op/HolidayWords.jsp";
            case ActivityType.FestivalPreference://节日特惠
                return "/WEB-INF/jsp/activity/op/FestivalPreference.jsp";
            case ActivityType.ContinuousRecharge://连续累充
                return "/WEB-INF/jsp/activity/op/ContinuousRecharge.jsp";
            case ActivityType.LimitShopActivty://限时商城
                return "/WEB-INF/jsp/activity/op/LimitShopActivity.jsp";
            case ActivityType.HolidayDailyGift://节日礼包（金元宝购买）
                return "/WEB-INF/jsp/activity/op/HolidayDailyGift.jsp";
            case ActivityType.HolidayScoreRank://积分排名
                return "/WEB-INF/jsp/activity/op/HolidayScoreRank.jsp";
            case ActivityType.FestivalWish://节日许愿
                return "/WEB-INF/jsp/activity/op/FestivalWish.jsp";
            case ActivityType.FBShare://FB分享(元旦)
                return "/WEB-INF/jsp/activity/op/FBShare.jsp";
            case ActivityType.ContinuousRecharge2://连续累充2(购买礼包)
                return "/WEB-INF/jsp/activity/op/ContinuousRecharge2.jsp";
            case ActivityType.XinNianZhuFu://节日祝福(新春祝福)
                return "/WEB-INF/jsp/activity/op/FestivalSign.jsp";
            case ActivityType.ZhiTouzi://掷骰子
                return "/WEB-INF/jsp/activity/op/JumpGrid.jsp";
            case ActivityType.AppearanceShow://外观展示
                return "/WEB-INF/jsp/activity/op/AppearanceShow.jsp";
            case ActivityType.LoginShow://登录展示
                return "/WEB-INF/jsp/activity/op/LoginShow.jsp";
            case ActivityType.Cornucopia://聚宝盆
                return "/WEB-INF/jsp/activity/op/Cornucopia.jsp";
            case ActivityType.LuckyEgg://幸运砸蛋
                return "/WEB-INF/jsp/activity/op/LuckyEgg.jsp";
        }
        return "";
    }

    /**
     * 添加活动
     */
    @At
    @POST
    public Object addActivity(@Param("..") Activity activity, HttpServletRequest request) {
        boolean flag;
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            flag = ActivityManager.getInstance().addActivity(activity, paramMap);
            BackendLogUtil.getInstance().log(request, "Добавление активности. ID активности: " + activity.getId() +
                    ", название: " + activity.getName());
        } catch (Exception e) {
            log.error(e);
            return Toolkit.outResult(false, e.getMessage());
        }
        return Toolkit.outResult(flag, flag);
    }

    /**
     * 查询活动
     */
    @At
    @POST
    public Object queryActivityById(int id) {
        List<Activity> list = dao.query(Activity.class, Cnd.where("id", "=", id));
        return Toolkit.outResult(true, list.get(0));
    }

    /**
     * 查询指定类型的活动
     */
    @At
    @POST
    public Object queryActivity(int type, Pager pager) {
        Cnd cnd = Cnd.where("type", "=", type).and("isDeleted", "=", "0");
        cnd.orderBy("id", "asc");
        List<Activity> result = new ArrayList<>();
        List<Activity> list = dao.query(Activity.class, cnd, pager);
        for (Activity activity : list) {
//            result.add(manager.getActivityInfo(activity));
            result.add(activity);
        }
        pager.setRecordCount(dao.count(Activity.class, cnd));
        return Toolkit.outResult(true, new QueryResult(result, pager));
    }

    /**
     * 根据筛选条件查询运营活动信息
     */
    @At
    @POST
    public Object queryActivityByServer(int type, int subtype2, int tag, String activityName, Pager pager) {
        Cnd cnd = Cnd.where("isDeleted", "=", "0");
        List<Activity> list = new ArrayList<>();
        if (type != -1){
            cnd.and("type", "=", type);
        }

        if (subtype2 != -1){
            cnd.and("subType","=",subtype2);
        }

        if (tag != -1){
            cnd.and("tag","=",tag);
        }

        if (!activityName.equals("")){
            cnd.and("name","=",activityName);
        }
        cnd.orderBy("id", "asc");
        List<Activity> result = new ArrayList<>();
        for (Activity activity : list) {
//            result.add(manager.getActivityInfo(activity));
            result.add(activity);
        }
        QueryResult qr = new QueryResult();
        qr.setList(dao.query(Activity.class, cnd, pager));
        pager.setRecordCount(dao.count(Activity.class, cnd));
        qr.setPager(pager);
        return Toolkit.outResult(true, qr);
    }

    /**
     * 发布单个活动
     */
    @At
    @POST
    public Object publishActivity(HttpServletRequest request, int actId, String platform, String sids, int operationType, int cover) {
        return batchPublish(request, actId + "", platform, sids, operationType, cover);
    }

    /**
     * 批量发布活动
     */
    @At
    @POST
    public Object batchPublish(HttpServletRequest request, String actIds, String platform, String sids, int operationType, int cover) {
        List<Integer> actIdList = JsonUtils.parseArray("[" + actIds + "]", Integer.class);
        List<Integer> serverIdList = JsonUtils.parseArray("[" + sids + "]", Integer.class);
        HashSet<Integer> actIdSet = new HashSet<>(actIdList);
        HashSet<Integer> serverIdSet = new HashSet<>(serverIdList);
        Map<Integer, List<Integer>> serverSuccessList = new HashMap<>();
        Map<Integer, List<Integer>> serverFailedList = new HashMap<>();
        Map<Integer, List<Integer>> activityFailList = new HashMap<>();

        //取得活动数据
        List<Activity> actList = new ArrayList<>();
        Set<Integer> typeSet = new HashSet<>();
        for (Integer actId : actIdSet) {
            Activity activity = dao.fetch(Activity.class, Cnd.where("id", "=", actId));
            if (activity == null) {
                log.error("Активность не найдена при публикации. ID: " + actId);
                continue;
            }
            //同类型活动无法进行覆盖操作
            if(cover == 1){
                int type = activity.getType()*1000+activity.getSubType();
                if(typeSet.contains(type)){
                    return Toolkit.outResult(false, "Нельзя выполнять массовую перезапись активностей одного типа. Тип: " + type + ", тип активности: " + activity.getType() + ", тип праздника: " + activity.getSubType());
                }
                typeSet.add(type);

                //设置活动类型是否覆盖
                if(activity.getCover()!=cover){
                    activity.setCover(cover);
                }
            }

            actList.add(activity);
        }

        //发布到对应的服务器
        for (Integer serverId : serverIdSet) {
            Server server = dao.fetch(Server.class, Cnd.where("groupName", "=", platform)
                    .and("serverId", "=", serverId).and("isDeleted", "=", 0));
            if (server == null) {
                log.error("Не удалось получить сервер при публикации активности! platform=" + platform + ", sid=" + serverId);
                serverFailedList.put(serverId, actIdList);
                continue;
            }

            NutMap resultMap = GameServerRequestUtil.gmBatchSendActMess(server, actList);
            if (!resultMap.getBoolean("ok")) {
                final List<Integer> failIds = new ArrayList<>();
                if (resultMap.containsKey("data")) {
                    failIds.addAll(JsonUtils.parseArray(resultMap.get("data").toString(), Integer.class));
                } else {
                    actList.forEach(n -> failIds.add(n.getId()));
                }
                serverFailedList.put(serverId, failIds);
                for (Integer n : failIds) {
                    if (!activityFailList.containsKey(n)) {
                        activityFailList.put(n, new ArrayList<>());
                    }
                    activityFailList.get(n).add(serverId);
                }
                log.error("Сервер " + serverId + ": публикация активностей [" + failIds + "] не удалась! Список неудачных активностей: " + JsonUtils.toJSONString(failIds));
            } else {
                serverSuccessList.put(serverId, actIdList);
            }
        }

        //更新活动发布列表，状态，记录日志
        for (Activity activity : actList) {
            HashSet<Integer> toSidList = new HashSet<>();
            HashSet<Integer> okSidList = new HashSet<>();
            if (!activity.getToSidList().isEmpty()) {
                toSidList.addAll(JsonUtils.parseArray(activity.getToSidList(), Integer.class));
            }
            if (!activity.getOkSidList().isEmpty()) {
                okSidList.addAll(JsonUtils.parseArray(activity.getOkSidList(), Integer.class));
            }
            toSidList.addAll(serverIdSet);

            List<Integer> okSids = new ArrayList<>(serverIdSet);
            if (activityFailList.containsKey(activity.getId())) {
                List<Integer> failList = activityFailList.get(activity.getId());
                okSids.removeAll(failList);
            }
            okSidList.addAll(okSids);

            activity.setOkSidList(JsonUtils.toJSONString(okSidList));
            activity.setToSidList(JsonUtils.toJSONString(toSidList));
            if (!activityFailList.containsKey(activity.getId())) {
                activity.setState(operationType);
            }

            BackendLogUtil.getInstance().log(request, "Публикация активности. ID активности: " + activity.getId() +
                    ", название: " + activity.getName() + ", полный список публикации: " + JsonUtils.toJSONString(toSidList) +
                    ", список неудач: " + JsonUtils.toJSONString(activityFailList.get(activity.getId())) +
                    ", список успехов: " + JsonUtils.toJSONString(okSidList));
        }
        dao.update(actList);

        //返回发布结果信息
        StringBuilder promptInfo = new StringBuilder("Результат:\n");
        if (serverFailedList.size() > 0) {
            promptInfo.append("Список неудач:\n");
            promptInfo.append("Количество неудач: ").append(serverFailedList.size())
                .append(", серверы с ошибкой: ").append(JsonUtils.toJSONString(serverFailedList.keySet())).append("\n");
            for (Integer serverId : serverFailedList.keySet()) {
                promptInfo.append("Сервер с ошибкой: ").append(serverId)
                        .append(", неопубликованные активности: ").append(JsonUtils.toJSONString(serverFailedList.get(serverId))).append("\n");
            }
        }
        if (serverSuccessList.size() > 0) {
            promptInfo.append("Список успехов:\n");
            promptInfo.append("Перезапись: ").append(cover == 1 ? "Да\n" : "Нет\n");
            promptInfo.append("Количество успехов: ").append(serverSuccessList.size())
                    .append(", успешные серверы: ").append(JsonUtils.toJSONString(serverSuccessList.keySet())).append("\n");
            for (Integer serverId : serverSuccessList.keySet()) {
                promptInfo.append("Успешный сервер: ").append(serverId)
                        .append(", успешно опубликованные активности: ").append(JsonUtils.toJSONString(serverSuccessList.get(serverId))).append("\n");
            }
        }
        return Toolkit.outResult(true, promptInfo.toString());
    }

    /**
     * 删除单个活动
     */
    @At
    @POST
    public Object deleteActivity(HttpServletRequest request, int actId) {
        return batchDelete(request, actId + "");
    }

    /**
     * 批量删除活动
     */
    @At
    @POST
    public Object batchDelete(HttpServletRequest request, String actIds) {

        List<Integer> actIdList = JsonUtils.parseArray("[" + actIds + "]", Integer.class);
        HashMap<Integer, List<Integer>> serverActIds = new HashMap<>();
        HashMap<Integer, List<Integer>> serverActTypes = new HashMap<>();
        HashSet<Integer> actIdSet = new HashSet<>(actIdList);
        List<Activity> actList = new ArrayList<>();
        Map<Integer, List<Integer>> serverSuccessList = new HashMap<>();
        Map<Integer, List<Integer>> serverFailedList = new HashMap<>();
        Map<Integer, List<Integer>> activityFailList = new HashMap<>();

        //统计游戏服活动发布情况
        for (Integer actId : actIdSet) {
            Activity activity = dao.fetch(Activity.class, Cnd.where("id", "=", actId));
            if (activity == null) {
                continue;
            }
            actList.add(activity);
            if (!Strings.isBlank(activity.getOkSidList())) {
                List<Integer> okList = JsonUtils.parseArray(activity.getOkSidList(), Integer.class);
                for (Integer sid : okList) {
                    if (!serverActIds.containsKey(sid)) {
                        serverActIds.put(sid, new ArrayList<>());
                        serverActTypes.put(sid, new ArrayList<>());
                    }
                    serverActIds.get(sid).add(activity.getId());
                    serverActTypes.get(sid).add(activity.getType()*1000+activity.getSubType());
                }
            }
        }

        //发送指令到游戏服删除活动
        for (Integer serverId : serverActIds.keySet()) {
            Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId).and("isDeleted", "=", 0));
            if (server == null) {
                log.error("Не удалось получить сервер при удалении активности! sid=" + serverId);
                serverFailedList.put(serverId, serverActIds.get(serverId));
                continue;
            }
            NutMap resultMap = GameServerRequestUtil.gmBatchDeleteActMess(server, serverActIds.get(serverId), serverActTypes.get(serverId));
            if (!resultMap.getBoolean("ok")) {
                List<Integer> failIds;
                if (resultMap.containsKey("data")) {
                    failIds = JsonUtils.parseArray(resultMap.get("data").toString(), Integer.class);
                } else {
                    failIds = new ArrayList<>(serverActIds.get(serverId));
                }
                serverFailedList.put(serverId, failIds);
                for (Integer n : failIds) {
                    if (!activityFailList.containsKey(n)) {
                        activityFailList.put(n, new ArrayList<>());
                    }
                    activityFailList.get(n).add(serverId);
                }
                log.error("Сервер " + serverId + ": удаление активностей [" + failIds + "] не удалось! Список неудачных активностей: " + JsonUtils.toJSONString(failIds));
            } else {
                serverSuccessList.put(serverId, serverActIds.get(serverId));
            }
        }

        //更新本地活动数据，记录日志
        List<Integer> successActList = new ArrayList<>();
        for (Activity activity : actList) {
            if (!activityFailList.containsKey(activity.getId())) {
                activity.setIsDeleted((byte) 1);
                activity.setCover((byte) 0);
                successActList.add(activity.getId());

            }
            BackendLogUtil.getInstance().log(request, "Удаление активности. ID активности: " + activity.getId() +
                    ", название: " + activity.getName() + ", список неудач: " + JsonUtils.toJSONString(activityFailList.get(activity.getId())));
        }
        dao.update(actList);

        //返回删除活动结果信息
        StringBuilder promptInfo = new StringBuilder("Результат:\n");
        promptInfo.append("Успешно удалённые активности: ").append(JsonUtils.toJSONString(successActList));
        if (activityFailList.size() > 0) {
            promptInfo.append("Список неудач:\n");
    promptInfo.append("Количество неудач: ").append(activityFailList.size()).append("\n");
            for (Integer serverId : serverFailedList.keySet()) {
                promptInfo.append("Сервер с ошибкой: ").append(serverId)
                        .append(", не удалённые активности: ").append(JsonUtils.toJSONString(serverFailedList.get(serverId))).append("\n");
            }
        }
        if (serverSuccessList.size() > 0) {
            promptInfo.append("Список успехов:\n");
            promptInfo.append("Количество успехов: ").append(serverSuccessList.size())
                    .append(", соответствующие серверы: ").append(JsonUtils.toJSONString(serverSuccessList.keySet())).append("\n");
        }
        BackendLogUtil.getInstance().log(request,promptInfo.toString());
        return Toolkit.outResult(true, promptInfo.toString());
    }

    /**
     * 验证活动
     */
    @At
    @POST
    public Object verifyActivity(int actId) {
        return batchVerify(actId + "");
    }

    /**
     * 批量验证活动
     */
    @At
    @POST
    public Object batchVerify(String actIds) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        List<Integer> actIdList = JsonUtils.parseArray("[" + actIds + "]", Integer.class);
        List<Activity> actList = dao.query(Activity.class, Cnd.where("id", "in", actIdList));
        for (Activity activity : actList) {
            if (activity.getState() != ActivityManager.TESTED) {
                continue;
            }
            activity.setState(ActivityManager.VALID);
            int result = dao.update(activity);
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "Проверка активности " + activity.getId() + " [" + activity.getName() + "] \t Результат: " + (result == 1));
        }
        return Toolkit.outResult(true, msg.get("activity.msg.validatesuccess"));
    }

    /**
     * 一键删除过期活动
     */
    @At
    @POST
    public Object deleteAllExpiredActivity(int type) {
        Cnd cnd = Cnd.where("unix_timestamp(endTime)", "<", System.currentTimeMillis() / 1000)
                .and("type", "=", type).and("isDeleted", "=", 0);
        List<Activity> activities = dao.query(Activity.class, cnd);
        activities.forEach(n -> n.setIsDeleted((byte) 1));
        int num = dao.update(activities);
        return Toolkit.outResult(true, num + "");
    }

    /**
     * 添加活动模板
     */
    @At
    @POST
    public Object addTemplate(@Param("..") ActivityTemplate activity, HttpServletRequest request) {
        boolean flag;
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            flag = ActivityManager.getInstance().addActivityTemplate(activity, paramMap);
        } catch (Exception e) {
            return Toolkit.outResult(false, e.getMessage());
        }
        return Toolkit.outResult(flag);
    }

    /**
     * 根据模板名进行检测
     */
    @At
    @POST
    public Object checkTemplateName(@Param("..") ActivityTemplate activity, HttpServletRequest request) {
        ActivityTemplate template = dao.fetch(ActivityTemplate.class, Cnd.where("templateName", "=", activity.getTemplateName()));
        if (null != template){
            return Toolkit.outResult(false);
        }
        return Toolkit.outResult(true);
    }

    /**
     * 根据模板名进行更新
     */
    @At
    @POST
    public Object updateTemplate(@Param("..") ActivityTemplate activity, HttpServletRequest request) {
        int templateId = Integer.parseInt(request.getParameter("templateId"));
        activity.setId(templateId);
        int result = dao.update(activity);
        if (result < 1){
            return Toolkit.outResult(false);
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "Обновление шаблона активности. ID: " + activity.getId());
        return Toolkit.outResult(true);
    }

    /**
     * 删除活动模板
     */
    @At
    @POST
    public Object deleteTemplate(int id) {
        dao.delete(ActivityTemplate.class, id);
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "Удаление шаблона активности. ID: " + id);
        return Toolkit.outResult(true);
    }

    /**
     * 查取活动模板时间列表
     */
    @At
    @POST
    public Object getTemplateTime(int type) {
        List<ActivityTemplate> templates = dao.query(ActivityTemplate.class, Cnd.where("type", "=", type));
        return Toolkit.outResult(true, templates);
    }

    /**
     * 获取活动模板数据
     */
    @At
    @POST
    public Object getTemplate(int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        ActivityTemplate template = dao.fetch(ActivityTemplate.class, id);
        if (template == null) {
            return Toolkit.outResult(false, msg.get("activity.msg.modelgetfail"));
        }
        return Toolkit.outResult(true, template);
    }

    /**
     * 获取活动boss类型
     * @return
     */
    @At
    public Object getActivityBossType() {
        List<ActivityBossType> list = dao.query(ActivityBossType.class, Cnd.NEW());
        return Toolkit.outResult(true, list);
    }

    /**
     * 获取活动节日类型
     * @return
     */
    @At
    public Object getActivityFestivalType(){
        List<ActivityFestivalType> list = dao.query(ActivityFestivalType.class, Cnd.NEW());
        return Toolkit.outResult(true, list);
    }

    /**
     * 导出运营活动数据(单行)
     */
    @At
    @POST
    @Ok("void")
    public void exportActivityData(int actId, HttpServletResponse response) {
        List<Activity> list = dao.query(Activity.class, Cnd.where("id", "=", actId));
        List<Map<String, String>> listMap = new ArrayList<>();
        Activity activity = list.get(0);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("Название активности", activity.getName());
        map.put("Описание активности", activity.getDescription());
        map.put("Тип активности", String.valueOf(activity.getType()));
        map.put("Тип праздника", String.valueOf(activity.getSubType()));
        map.put("Минимальный уровень", String.valueOf(activity.getMinLv()));
        map.put("Максимальный уровень", String.valueOf(activity.getMaxLv()));
        map.put("Тег активности", String.valueOf(activity.getTag()));
        map.put("Порядок тега", String.valueOf(activity.getSort()));
        map.put("Тип времени", String.valueOf(activity.getTimeType()));
        map.put("Дней с открытия сервера (начало)", String.valueOf(activity.getOpenServerOffsetBegin()));
        map.put("Дней с открытия сервера (продолжительность)", String.valueOf(activity.getOpenServerOffset()));
        map.put("Время начала", activity.getBeginTime());
        map.put("Время окончания", activity.getEndTime());
        map.put("Дней записи (начало)", String.valueOf(activity.getOpenServerRecordOffsetBegin()));
        map.put("Дней записи (продолжительность)", String.valueOf(activity.getOpenServerRecordOffset()));
        map.put("Время начала записи", activity.getStartRecordTime());
        map.put("Время окончания записи", activity.getEndRecordTime());
        map.put("Автопубликация при открытии сервера", String.valueOf(activity.getAutoSend()));
        map.put("Активность для нового сервера", String.valueOf(activity.getIsOpenServer()));
        map.put("Данные", activity.getCustom());
    //  map.put("Статус активности", String.valueOf(activity.getState()));
    //  map.put("Список публикации", activity.getPlatform());
    //  map.put("Список успехов", activity.getOkSidList());
        listMap.add(map);
        List<String> list1 = Arrays.asList("name","description","type","subType","minLv","maxLv","tag","sort","timeType","openServerOffsetBegin","openServerOffset","beginTime","endTime","openServerRecordOffsetBegin","openServerRecordOffset","startRecordTime","endRecordTime","autoSend","isOpenServer","custom");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.addHeader("content-type", "application/shlnd.ms-excel;charset=utf-8");
            Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
            String excelName = msg.get("activity.activityData");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(excelName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xls");
            // 转码防止乱码
            genExcel(listMap,list1,activity,out);
            out.flush();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void genExcel(List<Map<String, String>> dataList,List<String> list1, Activity activity, OutputStream out) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        workbook.setSheetName(0, msg.get("activity.activityData"));

        int ri = 0, ci = 0;
        HSSFRow row = sheet.createRow(ri++);//第一行英文表头
        HSSFCell cell;
        for (String field : list1) {
            cell = row.createCell(ci);
            cell.setCellValue(field);
            ci++;//增加列
        }
        row = sheet.createRow(ri++);//第二行中文表头
        ci=0;//从第一列开始
        for (Map.Entry<String, String> entry : dataList.get(0).entrySet()) {
            cell = row.createCell(ci);
            cell.setCellValue(entry.getKey());
            ci++;
        }
        //数据
        for (Map<String, String> dataMap : dataList){
            row = sheet.createRow(ri++);
            ci = 0;
            for (String key : dataMap.keySet()) {
                cell = row.createCell(ci++);
                cell.setCellValue(dataMap.get(key) == null ? "" : dataMap.get(key));
            }
        }
        workbook.write(out);
    }

    /**
     * 导入运营活动数据
     * @param activityFile
     * @return
     */
    @At
    @POST
    @AdaptBy(type = UploadAdaptor.class,args = {"ioc:upload"})
    public Object importActivityData(@Param("ActivityFile") TempFile activityFile,HttpServletRequest request){
        if (activityFile == null){
            return Toolkit.outResult(false,"file is null!");
        }
        String fileName = activityFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")){
            return Toolkit.outResult(false, "file type error!");
        }
        List<Activity> activities = new ArrayList<>();
        int[] activityDataPos = new int[20];//活动名称~数据(数组长度个数)
        try {
            Workbook wb;

            FileInputStream in = new FileInputStream(activityFile.getFile());
            //根据文件后缀（xls/xlsx）进行判断
            if ( fileName.endsWith(".xlsx")){
                wb = new XSSFWorkbook(in);
            }else if (fileName.endsWith(".xls")) {
                wb = new HSSFWorkbook(in);
            }else {
                return null;
            }
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            int cellNum = row.getLastCellNum();

            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("name")) {
                    activityDataPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("description")) {
                    activityDataPos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("type")) {
                    activityDataPos[2] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("subType")) {
                    activityDataPos[3] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("minLv")) {
                    activityDataPos[4] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("maxLv")) {
                    activityDataPos[5] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("tag")) {
                    activityDataPos[6] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("sort")) {
                    activityDataPos[7] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("timeType")) {
                    activityDataPos[8] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerOffsetBegin")) {
                    activityDataPos[9] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerOffset")) {
                    activityDataPos[10] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("beginTime")) {
                    activityDataPos[11] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("endTime")) {
                    activityDataPos[12] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerRecordOffsetBegin")) {
                    activityDataPos[13] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerRecordOffset")) {
                    activityDataPos[14] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("startRecordTime")) {
                    activityDataPos[15] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("endRecordTime")) {
                    activityDataPos[16] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("autoSend")) {
                    activityDataPos[17] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("isOpenServer")) {
                    activityDataPos[18] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("custom")) {
                    activityDataPos[19] = i;
                }
            }
            for (int j = 2; j <= sheet.getLastRowNum(); j++) {
                Row dataRow = sheet.getRow(j);
                Activity activity = new Activity();
                activity.setName(dataRow.getCell(activityDataPos[0]).toString());
                activity.setDescription(dataRow.getCell(activityDataPos[1]).toString());
                activity.setType((int)Float.parseFloat(dataRow.getCell(activityDataPos[2]).toString()));
                activity.setSubType((int)Float.parseFloat(dataRow.getCell(activityDataPos[3]).toString()));
                activity.setMinLv((int)Float.parseFloat(dataRow.getCell(activityDataPos[4]).toString()));
                activity.setMaxLv((int)Float.parseFloat(dataRow.getCell(activityDataPos[5]).toString()));
                activity.setTag((int)Float.parseFloat(dataRow.getCell(activityDataPos[6]).toString()));
                activity.setSort((int)Float.parseFloat(dataRow.getCell(activityDataPos[7]).toString()));
                activity.setTimeType((int)Float.parseFloat(dataRow.getCell(activityDataPos[8]).toString()));
                activity.setOpenServerOffsetBegin((int)Float.parseFloat(dataRow.getCell(activityDataPos[9]).toString()));
                activity.setOpenServerOffset((int)Float.parseFloat(dataRow.getCell(activityDataPos[10]).toString()));
                activity.setBeginTime(dataRow.getCell(activityDataPos[11]).toString());
                activity.setEndTime(dataRow.getCell(activityDataPos[12]).toString());
                activity.setOpenServerRecordOffsetBegin((int)Float.parseFloat(dataRow.getCell(activityDataPos[13]).toString()));
                activity.setOpenServerRecordOffset((int)Float.parseFloat(dataRow.getCell(activityDataPos[14]).toString()));
                activity.setStartRecordTime(dataRow.getCell(activityDataPos[15]).toString());
                activity.setEndRecordTime(dataRow.getCell(activityDataPos[16]).toString());
                activity.setAutoSend((int)Float.parseFloat(dataRow.getCell(activityDataPos[17]).toString()));
                activity.setIsOpenServer((int)Float.parseFloat(dataRow.getCell(activityDataPos[18]).toString()));
                activity.setCustom(dataRow.getCell(activityDataPos[19]).toString());
                activities.add(activity);
            }
            if (activities.size() > 0){
                List<Activity> result = dao.insert(activities);
                StringBuilder promptInfo = new StringBuilder("Импорт данных операционных активностей:\n");
                for (Activity activity:activities){
                    promptInfo.append("  Название активности: ").append(activity.getName())
                            .append(", тип активности: ").append(JsonUtils.toJSONString(activity.getType())).append("\n");
                }
                BackendLogUtil.getInstance().log(request, String.valueOf(promptInfo));
                return Toolkit.outResult(true, "import activity file, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "import activity file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    /**
     * 发布开服活动到对应游戏服
     */
    @At
    @GET
    @POST
    public Object sendOpenServerActivity(int serverId) throws ParseException {
        Server server = dao.fetch(Server.class, Cnd.where("serverId","=",serverId));
        if(server == null){
            log.error("При отправке активности открытия сервера сервер не найден. serverId=" + serverId);
            return Toolkit.outResult(false, "При отправке активности открытия сервера сервер не найден. serverId=" + serverId);
        }

        Cnd cnd = Cnd.where("isDeleted", "=", 0).
                and("autoSend","=","1");
        List<Activity> activities = dao.query(Activity.class, cnd);
        if(activities.isEmpty()){
            log.error("При отправке активности открытия сервера активность не найдена.");
            return Toolkit.outResult(false, "При отправке активности открытия сервера активность не найдена.");
        }

        Iterator<Activity> iterator = activities.iterator();
        while(iterator.hasNext()){
            Activity activity = iterator.next();
            if(activity.getTimeType() == 0&& TimeUtils.Time()>TimeUtils.getDateByString2(activity.getEndTime()).getTime()) {
                iterator.remove();
            }
        }

        try {
            NutMap resultMap = GameServerRequestUtil.gmBatchSendActMess(server, activities);
            if (!resultMap.getBoolean("ok")) {
                log.error("Сервер " + serverId + ": публикация активности открытия сервера не удалась! Результат: " + resultMap.get("data").toString());
            } else {
                return Toolkit.outResult(true, "Сервер " + serverId + ": активность открытия сервера опубликована успешно!");
            }
        }catch (Exception e){
            log.error("Сервер " + serverId + ": ошибка публикации активности! Ошибка: " + e.getMessage());
            return Toolkit.outResult(false, "Сервер " + serverId + ": ошибка публикации активности! Ошибка: " + e.getMessage());
        }
        return Toolkit.outResult(false, "Сервер " + serverId + ": публикация активности открытия сервера не удалась!");
    }
}
