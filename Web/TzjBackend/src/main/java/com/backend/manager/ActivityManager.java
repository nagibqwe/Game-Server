package com.backend.manager;

import com.alibaba.druid.util.Utils;
import com.backend.bean.Activity;
import com.backend.bean.ActivityBossType;
import com.backend.bean.ActivityTemplate;
import com.backend.bean.Model;
import com.backend.struct.activity.*;
import com.backend.utils.JsonUtils;
import com.backend.utils.StringUtils;
import org.nutz.dao.Dao;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActivityManager {

    private static final Log log = Logs.getLog(ActivityManager.class);

    public static final int INIT = 0;       //已提交
    public static final int TESTED = 1;     //已测试
    public static final int VALID = 2;     //已验证
    public static final int PULISHED = 3;   //已发布

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private enum Singleton {
        INSTANCE;
        ActivityManager manager;

        Singleton() {
            this.manager = new ActivityManager();
        }

        ActivityManager getProcessor() {
            return manager;
        }
    }

    public static ActivityManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    public void init(Dao dao) {
        log.info("初始化活动信息...");
        this.dao = dao;
    }

    /**
     * 添加活动，可能添加多个实体bean
     * 分档次的活动，每一档映射为数据库中一个实体bean
     */
    public boolean addActivity(Activity activity, Map<String, String[]> paramMap) {
        if (!checkActivity(activity)) {
            return false;
        }
        for (Activity activityBean : getActivityBeans(activity, paramMap)) {
            if (activityBean == null) {
                return false;
            }
            activityBean.setConfigData(JsonUtils.toJSONString(paramMap));
            if (activityBean.getId() > 0) {
                dao.update(activityBean);
            } else {
                dao.insert(activityBean);
            }
        }
        return true;
    }

    /**
     * 添加活动，可能添加多个实体bean
     * 分档次的活动，每一档映射为数据库中一个实体bean
     */
    public boolean addActivityTemplate(ActivityTemplate activityTemplate, Map<String, String[]> paramMap) {
//        Activity activity = makeActivity(activityTemplate);

        //检查自定义配置正确性
//        List<Activity> activities = getActivityBeans(activity, paramMap);
//        if(activities.isEmpty()){
//            return false;
//        }

        //记录自定义模板数据
        activityTemplate.setCustom(JsonUtils.toJSONString(paramMap));

        return addTemplate(activityTemplate);
    }

    private Activity makeActivity(ActivityTemplate template) {
        Activity activity = new Activity();
        activity.setDescription(template.getDescription());
        activity.setType(template.getType());
        activity.setMinLv(template.getMinLv());
        activity.setMaxLv(template.getMaxLv());
        activity.setTag(template.getTag());
        activity.setSort(template.getSort());
        activity.setName(template.getName());
        activity.setTimeType(template.getTimeType());
        activity.setOpenServerOffsetBegin(template.getOpenServerOffsetBegin());
        activity.setOpenServerOffset(template.getOpenServerOffset());
        activity.setBeginTime(template.getBeginTime());
        activity.setEndTime(template.getEndTime());
        return activity;
    }

    private ActivityTemplate makeTemplate(Activity activity) {
        ActivityTemplate template = new ActivityTemplate();
        template.setDescription(activity.getDescription());
        template.setType(activity.getType());
        template.setMinLv(activity.getMinLv());
        template.setMaxLv(activity.getMaxLv());
        template.setTag(activity.getTag());
        template.setSort(activity.getSort());
        template.setName(activity.getName());
        template.setTimeType(activity.getTimeType());
        template.setOpenServerOffsetBegin(activity.getOpenServerOffsetBegin());
        template.setOpenServerOffset(activity.getOpenServerOffset());
        template.setBeginTime(activity.getBeginTime());
        template.setEndTime(activity.getEndTime());
        return template;
    }

    /**
     * 添加活动模板
     */
    public boolean addTemplate(ActivityTemplate activity) {
        ActivityTemplate temp = new ActivityTemplate();
        temp.setTemplateName(activity.getTemplateName());
        temp.setDescription(activity.getDescription());
        temp.setCreateTime(sdf.format(new Date()));
        temp.setType(activity.getType());
        temp.setSubType(activity.getSubType());
        temp.setMinLv(activity.getMinLv());
        temp.setMaxLv(activity.getMaxLv());
        temp.setTag(activity.getTag());
        temp.setSort(activity.getSort());
        temp.setName(activity.getName());
        temp.setTimeType(activity.getTimeType());
        temp.setOpenServerOffsetBegin(activity.getOpenServerOffsetBegin());
        temp.setOpenServerOffset(activity.getOpenServerOffset());
        temp.setBeginTime(activity.getBeginTime());
        temp.setEndTime(activity.getEndTime());
        temp.setOpenServerRecordOffsetBegin(activity.getOpenServerRecordOffsetBegin());
        temp.setOpenServerRecordOffset(activity.getOpenServerRecordOffset());
        temp.setStartRecordTime(activity.getStartRecordTime());
        temp.setEndRecordTime(activity.getEndRecordTime());
        temp.setAutoSend(activity.getAutoSend());
        temp.setIsOpenServer(activity.getIsOpenServer());
        temp.setCustom(activity.getCustom());
        temp = dao.insert(temp);
        return temp != null;
    }

    public boolean checkActivity(Activity activity) {
        return true;
    }

    /**
     * 根据提交的表单活动，得到活动bean
     */
    public List<Activity> getActivityBeans(Activity activity, Map<String, String[]> paramMap) {
        List<Activity> activities = new ArrayList<>();
        try {
            switch (activity.getType()) {
                case ActivityType.GetActive:
                    activities.add(new GetActive(activity).parseCustom(paramMap));
                    break;
                case ActivityType.DailyRecharge:
                    activities.add(new DailyRecharge(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LimitTimeLogin:
                    activities.add(new LimitTimeLogin(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LimitGiftBag:
                    activities.add(new LimitGiftBag(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LimitedTotalRecharge:
                    activities.add(new LimitedTotalRecharge(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LimitedTotalConsume:
                    activities.add(new LimitedTotalConsume(activity).parseCustom(paramMap));
                    break;
                case ActivityType.CollectGoodsExChange:
                    activities.add(new CollectGoodsExChange(activity).parseCustom(paramMap));
                    break;
                case ActivityType.GroupBuy:
                    activities.add(new GroupBuy(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LuckyCat:
                    activities.add(new LuckyCat(activity).parseCustom(paramMap));
                    break;
                case ActivityType.DrawReward:
                    activities.add(new DrawReward(activity).parseCustom(paramMap));
                    break;
                case ActivityType.HolidayBoss:
                    activities.add(new HolidayBoss(activity).parseCustom(paramMap));
                    break;
                case ActivityType.HolidayTask:
                    activities.add(new HolidayTask(activity).parseCustom(paramMap));
                    break;
                case ActivityType.HolidayWords:
                    activities.add(new HolidayWords(activity).parseCustom(paramMap));
                    break;
                case ActivityType.FestivalPreference:
                    activities.add(new FestivalPreference(activity).parseCustom(paramMap));
                    break;
                case ActivityType.ContinuousRecharge:
                    activities.add(new ContinuousRecharge(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LimitShopActivty:
                    activities.add(new LimitShopActivity(activity).parseCustom(paramMap));
                    break;
                case ActivityType.HolidayDailyGift://节日礼包
                    activities.add(new HolidayDailyGift(activity).parseCustom(paramMap));
                    break;
                case ActivityType.HolidayScoreRank:
                    activities.add(new HolidayScoreRank(activity).parseCustom(paramMap));
                    break;
                case ActivityType.FestivalWish:
                    activities.add(new FestivalWish(activity).parseCustom(paramMap));
                    break;
                case ActivityType.FBShare:
                    activities.add(new FBShare(activity).parseCustom(paramMap));
                    break;
                case ActivityType.ContinuousRecharge2:
                    activities.add(new ContinuousRecharge2(activity).parseCustom(paramMap));
                    break;
                case ActivityType.XinNianZhuFu://节日祝福
                    activities.add(new FestivalSign(activity).parseCustom(paramMap));
                    break;
                case ActivityType.ZhiTouzi://掷骰子
                    activities.add(new JumpGrid(activity).parseCustom(paramMap));
                    break;
                case ActivityType.AppearanceShow://外观展示
                    activities.add(new AppearanceShow(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LoginShow://登录展示
                    activities.add(new LoginShow(activity).parseCustom(paramMap));
                    break;
                case ActivityType.Cornucopia://聚宝盆
                    activities.add(new Cornucopia(activity).parseCustom(paramMap));
                    break;
                case ActivityType.LuckyEgg://幸运砸蛋
                    activities.add(new LuckyEgg(activity).parseCustom(paramMap));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(",活动类型：" + activity.getType() + ",节日类型：" + activity.getSubType() + "\n-------------------------------------------详细错误：" + Utils.getStackTrace(e));
        }
        return activities;
    }

    /**
     * 根据数据库活动bean得到具体活动info
     */
    public Activity getActivityInfo(Activity activity) {
        switch (activity.getType()) {
            case ActivityType.LuckyCat:
                return new LuckyCat(activity);
        }
        return null;
    }

    /**
     * 获取活动BOSS对应的BOSSID列表
     *
     * @param id
     * @return
     */
    public ActivityBossType getActivityBoss(int id) {
        ActivityBossType activityBossType = dao.fetch(ActivityBossType.class, id);
        return activityBossType;
    }

    /**
     * 根据ID获取对应模型库数据
     * @param id
     * @return
     */
    public String getModelData(int id) {
        Model model = dao.fetch(Model.class, id);
        String modelData = model.getModelData();
        return modelData;
    }
}
