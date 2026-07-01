package com.gm.project.gmtool.activity.controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activityBossType.service.IActivityBossTypeService;
import com.gm.project.gmtool.activity.domain.ActivityType;
import com.gm.project.gmtool.activityFestivalRelation.service.IActivityFestivalRelationService;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;
import com.gm.project.gmtool.activityFestivalType.service.IActivityFestivalTypeService;
import com.gm.project.gmtool.activityTemplate.domain.ActivityTemplate;
import com.gm.project.gmtool.activityTemplate.service.IActivityTemplateService;
import com.gm.project.gmtool.manager.ActivityManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TimeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.service.IActivityService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 运营活动Controller
 * 
 * @author gm
 * @date 2021-09-07
 */
@Controller
@RequestMapping("/gmtool/activity")
public class ActivityController extends BaseController
{
    private String prefix = "gmtool/activity";

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IActivityFestivalTypeService activityFestivalTypeService;

    @Autowired
    private IActivityTemplateService activityTemplateService;

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private IActivityBossTypeService activityBossTypeService;

    @Autowired
    private IActivityFestivalRelationService activityFestivalRelationService;

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    /**
     * 运营活动页面统一跳转逻辑
     * @param type
     * @param mmap
     * @return
     */
    @RequiresPermissions("gmtool:activity:view")
    @GetMapping("/getPage")
    public String activity(int type,ModelMap mmap)
    {
        mmap.put("type",type);
        switch (type) {
            case 0: //活动总览
                return prefix + "/ActivityList";
            case ActivityType.GetActive: //活跃领取
                return prefix + "/GetActive";
            case ActivityType.DailyRecharge: //累计每日充值
                return prefix + "/DailyRecharge";
            case ActivityType.LimitTimeLogin: //限时登陆奖励
                return prefix + "/LimitTimeLogin";
            case ActivityType.LimitGiftBag: //限够礼包
                return prefix + "/LimitGiftBag";
            case ActivityType.LimitedTotalRecharge: //限时累充
                return prefix + "/LimitedTotalRecharge";
            case ActivityType.LimitedTotalConsume: //限时累计消耗
                return prefix + "/LimitedTotalConsume";
            case ActivityType.GroupBuy://团购
                return prefix + "/GroupBuy";
            case ActivityType.LuckyCat://招财猫
                return prefix + "/LuckyCat";
            case ActivityType.CollectGoodsExChange://集物兑换
                return prefix + "/CollectGoodsExChange";
            case ActivityType.DrawReward://天帝宝库
                return prefix + "/DrawReward";
            case ActivityType.HolidayBoss://首领狂欢
                return prefix + "/HolidayBoss";
            case ActivityType.HolidayTask://庆典任务
                return prefix + "/HolidayTask";
            case ActivityType.HolidayWords://节日集字
                return prefix + "/HolidayWords";
            case ActivityType.FestivalPreference://节日特惠
                return prefix + "/FestivalPreference";
            case ActivityType.ContinuousRecharge://连续累充
                return prefix + "/ContinuousRecharge";
            case ActivityType.LimitShopActivty://限时商城
                return prefix + "/LimitShopActivity";
            case ActivityType.HolidayDailyGift://节日礼包（金元宝购买）
                return prefix + "/HolidayDailyGift";
            case ActivityType.HolidayScoreRank://积分排名
                return prefix + "/HolidayScoreRank";
            case ActivityType.FestivalWish://节日许愿
                return prefix + "/FestivalWish";
            case ActivityType.FBShare://FB分享(元旦)
                return prefix + "/FBShare";
            case ActivityType.ContinuousRecharge2://连续累充2(购买礼包)
                return prefix + "/ContinuousRecharge2";
            case ActivityType.XinNianZhuFu://节日祝福(新春祝福)
                return prefix + "/FestivalSign";
            case ActivityType.ZhiTouzi://掷骰子
                return prefix + "/JumpGrid";
            case ActivityType.AppearanceShow://外观展示
                return prefix + "/AppearanceShow";
            case ActivityType.LoginShow://登录展示
                return prefix + "/LoginShow";
            case ActivityType.Cornucopia://聚宝盆
                return prefix + "/Cornucopia";
            case ActivityType.LuckyEgg://幸运砸蛋
                return prefix + "/LuckyEgg";
            case ActivityType.LuckyGem://幸运宝玉
                return prefix + "/LuckyGem";
            case ActivityType.FangZeTreasureHunt://方泽探宝
                return prefix + "/FangZeTreasureHunt";
            case ActivityType.xianJingTreasureHunt://仙境探宝
                return prefix + "/xianJingTreasureHunt";
        }
        return "";
    }

    /**
     * 根据searchType区分是活动总览还是单类型活动查询
     * @param request
     * @param searchType
     * @param type
     * @param subtype2
     * @param tag2
     * @param activityName
     * @return
     */
//    @RequiresPermissions("gmtool:activity:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HttpServletRequest request,Integer searchType,Integer type,Integer subtype2,Integer tag2,Integer autoSend2,Integer isOpenServer2,String activityName)
    {
        startPage();
        Activity activity = new Activity();
        List<Activity> list = new ArrayList<>();
        if (searchType != null){
            activity.setType(searchType);
            list = activityService.selectActivityList(activity);
        }else {
            if (type != -1){
                activity.setType(type);
            }
            if (subtype2 != -1){
                activity.setSubType(subtype2);
            }
            if (tag2 != -1){
                activity.setTag(tag2);
            }
            if (autoSend2 != -1){
                activity.setAutoSend(autoSend2);
            }
            if (isOpenServer2 != -1){
                activity.setIsOpenServer(isOpenServer2);
            }
            if (!activityName.equals("")){
                activity.setName(activityName);
            }
            list = activityService.selectActivityList(activity);
        }

        return getDataTable(list);
    }

    /**
     * 导出运营活动列表
     */
    @RequiresPermissions("gmtool:activity:export")
    @Log(title = "运营活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Activity activity)
    {
        List<Activity> list = activityService.selectActivityList(activity);
        ExcelUtil<Activity> util = new ExcelUtil<Activity>(Activity.class);
        return util.exportExcel(list, "运营活动数据");
    }

    /**
     * 新增运营活动
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存运营活动
     */
    @RequiresPermissions("gmtool:activity:add")
    @Log(title = "运营活动", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Activity activity)
    {
        return toAjax(activityService.insertActivity(activity));
    }

    /**
     * 修改运营活动
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Activity activity = activityService.selectActivityById(id);
        mmap.put("activity", activity);
        return prefix + "/edit";
    }

    /**
     * 修改保存运营活动
     */
    @RequiresPermissions("gmtool:activity:edit")
    @Log(title = "运营活动", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Activity activity)
    {
        return toAjax(activityService.updateActivity(activity));
    }

    /**
     * 删除运营活动
     */
    @RequiresPermissions("gmtool:activity:remove")
    @Log(title = "运营活动", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityService.deleteActivityByIds(ids));
    }

    /**
     * 获取运营活动的节日类型数据
     * @return
     */
    @PostMapping( "/getActivityFestivalType")
    @ResponseBody
    public Object getActivityFestivalType(Integer type){
        List<ActivityFestivalType> list = new ArrayList<>();
        if (type == null){
            list = activityFestivalTypeService.selectActivityFestivalTypeList(new ActivityFestivalType());
        }else {
            List<Integer> ids = activityFestivalRelationService.selectActFestivalRelationByLogicId(type);
            list = activityFestivalTypeService.selectActivityFestivalTypeByIds(ids);
        }
        return AjaxResult.info("", list).put("ok",true);
    }

    /**
     * 获取运营活动模板列表
     * @param type
     * @return
     */
    @PostMapping( "/getTemplateTime")
    @ResponseBody
    public Object getTemplateTime(int type) {
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setType(type);
        List<ActivityTemplate> templates = activityTemplateService.selectActivityTemplateList(activityTemplate);
        return AjaxResult.info("",templates).put("ok",true);
    }

    /**
     * 获取活动模板数据
     * @param id
     * @return
     */
    @PostMapping( "/getTemplate")
    @ResponseBody
    public Object getTemplate(int id) {
        ActivityTemplate template = activityTemplateService.selectActivityTemplateById(id);
        if (template == null) {
            return AjaxResult.info("模板数据获取失败！").put("ok",false);
        }
        return AjaxResult.info("",template).put("ok",true);
    }

    /**
     * 删除活动模板
     * @param id
     * @return
     */
    @PostMapping( "/deleteTemplate")
    @ResponseBody
    public Object deleteTemplate(int id) {
        activityTemplateService.deleteActivityTemplateById(id);
        GMLogUtil.log("删除活动模板id:" + id);
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * 根据模板名和活动类型进行检测(同一活动类型只能有唯一的模板名)
     * @param activity
     * @param request
     * @return
     */
    @PostMapping( "/checkTemplateName")
    @ResponseBody
    public Object checkTemplateName(ActivityTemplate activity, HttpServletRequest request) {

        ActivityTemplate template = new ActivityTemplate();
        template.setTemplateName(activity.getTemplateName());
        template.setType(activity.getType());
        List<ActivityTemplate> templates = activityTemplateService.selectActivityTemplateList(template);
        if (null != templates && templates.size() > 0){
            return AjaxResult.info("").put("ok",false);
        }
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * 添加活动模板
     * @param activity
     * @param request
     * @return
     */
    @PostMapping( "/addTemplate")
    @ResponseBody
    public Object addTemplate(ActivityTemplate activity, HttpServletRequest request) {
        boolean flag;
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            flag = ActivityManager.getInstance().addActivityTemplate(activity, paramMap);
        } catch (Exception e) {
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
        return AjaxResult.info("").put("ok",flag);
    }

    /**
     * 根据模板名进行更新
     * @param activity
     * @param request
     * @return
     */
    @PostMapping( "/updateTemplate")
    @ResponseBody
    public Object updateTemplate(ActivityTemplate activity, HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        int templateId = Integer.parseInt(request.getParameter("templateId"));
        activity.setId(templateId);
        activity.setCustom(JsonUtils.toJSONString(paramMap));
        int result = activityTemplateService.updateActivityTemplate(activity);
        if (result < 1){
            return AjaxResult.info("").put("ok",false);
        }
        GMLogUtil.log("更新活动模板id:" + activity.getId());
        return AjaxResult.info("").put("ok",true);
    }


    /**
     * 提交活动操作
     * @param activity
     * @param request
     * @return
     */
    @PostMapping( "/addActivity")
    @ResponseBody
    public Object addActivity(Activity activity, HttpServletRequest request) {
        boolean flag;
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            if (activity.getId() > 0){
                Activity activityOld = activityService.selectActivityById(activity.getId());
                if (!activityOld.getToSidList().equals("") && !activityOld.getOkSidList().equals("")){
                    activity.setState(4);//发布之后有修改的提交操作
                }
            }
            flag = ActivityManager.getInstance().addActivity(activity, paramMap);
            GMLogUtil.log("添加活动,活动ID:" + activity.getId() +
                    ",活动名字:" + activity.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
        return AjaxResult.info("").put("ok",flag);
    }

    /**
     * 导入运营活动数据
     * @param activityFile
     * @param request
     * @return
     */
    @PostMapping( "/importActivityData")
    @ResponseBody
    public Object importActivityData(MultipartFile activityFile, HttpServletRequest request){
        if (activityFile == null){
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = activityFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")){
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<Activity> activities = new ArrayList<>();
        int[] activityDataPos = new int[20];//活动名称~数据(数组长度个数)
        try {
            Workbook wb;

            InputStream in = activityFile.getInputStream();
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
                List<Activity> result = new ArrayList<>();
                for (Activity activity:activities){
                    activityService.insertActivity(activity);
                    result.add(activity);
                }
                StringBuilder promptInfo = new StringBuilder("导入运营活动数据: \n");
                for (Activity activity:activities){
                    promptInfo.append("  活动名：").append(activity.getName())
                            .append("，活动类型：").append(JsonUtils.toJSONString(activity.getType())).append("\n");
                }
                GMLogUtil.log(String.valueOf(promptInfo));
                return AjaxResult.info("import activity file, saved" + result.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("import activity file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 根据ID查询活动
     * @param id
     * @return
     */
    @PostMapping( "/queryActivityById")
    @ResponseBody
    public Object queryActivityById(int id) {
        Activity activity = activityService.selectActivityById(id);
        return AjaxResult.info("",activity).put("ok",true);
    }

    /**
     * 导出运营活动数据(单行)
     * @param actId
     * @param response
     */
    @PostMapping( "/exportActivityData")
    @ResponseBody
    public void exportActivityData(int actId, HttpServletResponse response) {
        Activity activity = activityService.selectActivityById(actId);
        List<Map<String, String>> listMap = new ArrayList<>();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("活动名称",activity.getName());
        map.put("活动备注",activity.getDescription());
        map.put("活动类型",String.valueOf(activity.getType()));
        map.put("节日类型",String.valueOf(activity.getSubType()));
        map.put("最小等级",String.valueOf(activity.getMinLv()));
        map.put("最大等级",String.valueOf(activity.getMaxLv()));
        map.put("活动标签",String.valueOf(activity.getTag()));
        map.put("标签排序",String.valueOf(activity.getSort()));
        map.put("时间类型",String.valueOf(activity.getTimeType()));
        map.put("开服天数",String.valueOf(activity.getOpenServerOffsetBegin()));
        map.put("持续天数",String.valueOf(activity.getOpenServerOffset()));
        map.put("开始时间",activity.getBeginTime());
        map.put("结束时间",activity.getEndTime());
        map.put("记录开始天数",String.valueOf(activity.getOpenServerRecordOffsetBegin()));
        map.put("记录持续天数",String.valueOf(activity.getOpenServerRecordOffset()));
        map.put("记录开始时间",activity.getStartRecordTime());
        map.put("记录结束时间",activity.getEndRecordTime());
        map.put("开服自动发布",String.valueOf(activity.getAutoSend()));
        map.put("是否是新服活动",String.valueOf(activity.getIsOpenServer()));
        map.put("数据",activity.getCustom());
//        map.put("活动状态", String.valueOf(activity.getState()));
//        map.put("发布列表", activity.getPlatform());
//        map.put("成功列表", activity.getOkSidList());
        listMap.add(map);
        List<String> list1 = Arrays.asList("name","description","type","subType","minLv","maxLv","tag","sort","timeType","openServerOffsetBegin","openServerOffset","beginTime","endTime","openServerRecordOffsetBegin","openServerRecordOffset","startRecordTime","endRecordTime","autoSend","isOpenServer","custom");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelName = "活动数据";
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(excelName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xlsx");
            // 转码防止乱码
            genExcel(listMap,list1,activity,out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void genExcel(List<Map<String, String>> dataList,List<String> list1, Activity activity, OutputStream out) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "活动数据");

        int ri = 0, ci = 0;
        XSSFRow row = sheet.createRow(ri++);//第一行英文表头
        XSSFCell cell;
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
     * 批量发布运营活动
     * @param request
     * @param actIds
     * @param platform
     * @param sids
     * @param operationType
     * @param cover
     * @return
     */
    @PostMapping( "/publishActivity")
    @ResponseBody
    public Object publishActivity(HttpServletRequest request, String actIds, String platform, String sids, int operationType, int cover) {
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
            Activity activity = activityService.selectActivityById(actId);
            if (activity == null) {
                log.error("发布活动时未找到活动数据,id：" + actId);
                continue;
            }
            //同类型活动无法进行覆盖操作
            if(cover == 1){
                int type = activity.getType()*1000+activity.getSubType();
                if(typeSet.contains(type)){
                    return AjaxResult.info("同类型活动无法进行批量的覆盖操作，相同类型："+type+",活动类型："+activity.getType()+",节日类型："+activity.getSubType()).put("ok",false);
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
            TServer server = new TServer();
            server.setGroupName(platform);
            server.setServerId(serverId);
            List<TServer> servers = tServerService.selectTServerList(server);
            if (servers.size() < 1) {
                log.error("发布活动时服务器获取失败！platform=" + platform + ", sid=" + serverId);
                serverFailedList.put(serverId, actIdList);
                continue;
            }
            server = servers.get(0);
            HashMap resultMap = GameServerRequestUtil.gmBatchSendActMess(server, actList);
            if (!Boolean.valueOf(resultMap.get("ok").toString())) {
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
                log.error(serverId + "服,活动[" + failIds + "]发布失败！,失败的活动：" + JsonUtils.toJSONString(failIds));
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

            GMLogUtil.log("发布活动，活动编号：" + activity.getId() +
                    "，活动名字：" + activity.getName() + ",发布全部列表:" + JsonUtils.toJSONString(toSidList) +
                    "失败列表：" + JsonUtils.toJSONString(activityFailList.get(activity.getId())) +
                    "成功列表：" + JsonUtils.toJSONString(okSidList));
            activityService.updateActivity(activity);
        }

        //返回发布结果信息
        StringBuilder promptInfo = new StringBuilder("返回结果: \n");
        if (serverFailedList.size() > 0) {
            promptInfo.append("失败列表:\n");
            promptInfo.append("失败个数：").append(serverFailedList.size())
                    .append("，失败区服：").append(JsonUtils.toJSONString(serverFailedList.keySet())).append("\n");
            for (Integer serverId : serverFailedList.keySet()) {
                promptInfo.append("失败区服：").append(serverId)
                        .append("，未成功发布活动：").append(JsonUtils.toJSONString(serverFailedList.get(serverId))).append("\n");
            }
        }
        if (serverSuccessList.size() > 0) {
            promptInfo.append("成功列表:\n");
            promptInfo.append("是否覆盖: ").append(cover==1?"是\n":"否\n");
            promptInfo.append("成功个数：").append(serverSuccessList.size())
                    .append("，成功区服：").append(JsonUtils.toJSONString(serverSuccessList.keySet())).append("\n");
            for (Integer serverId : serverSuccessList.keySet()) {
                promptInfo.append("成功区服：").append(serverId)
                        .append("，成功发布活动：").append(JsonUtils.toJSONString(serverSuccessList.get(serverId))).append("\n");
            }
        }
        return AjaxResult.info(promptInfo.toString()).put("ok",true);
    }

    /**
     * 批量删除运营活动
     * @param request
     * @param actIds
     * @return
     */
    @PostMapping( "/deleteActivity")
    @ResponseBody
    public Object deleteActivity(HttpServletRequest request, String actIds) {

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
            Activity activity = activityService.selectActivityById(actId);
            if (activity == null) {
                continue;
            }
            actList.add(activity);
            if (!StringUtils.isBlank(activity.getOkSidList())) {
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
            TServer server = tServerService.selectTServerByServerId(serverId);
            if (server == null) {
                log.error("删除活动时服务器获取失败！sid=" + serverId);
                serverFailedList.put(serverId, serverActIds.get(serverId));
                continue;
            }
            HashMap resultMap = GameServerRequestUtil.gmBatchDeleteActMess(server, serverActIds.get(serverId), serverActTypes.get(serverId));
            if (!Boolean.valueOf(resultMap.get("ok").toString())) {
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
                log.error(serverId + "服,活动[" + failIds + "]删除失败！,失败的活动：" + JsonUtils.toJSONString(failIds));
            } else {
                serverSuccessList.put(serverId, serverActIds.get(serverId));
            }
        }

        //更新本地活动数据，记录日志
        List<Integer> successActList = new ArrayList<>();
        for (Activity activity : actList) {
            if (!activityFailList.containsKey(activity.getId())) {
                activity.setIsDeleted(1);
                activity.setCover(0);
                successActList.add(activity.getId());

            }
            GMLogUtil.log("删除活动，活动编号：" + activity.getId() +
                    "，活动名字：" + activity.getName() + " 失败列表：" + JsonUtils.toJSONString(activityFailList.get(activity.getId())));
            activityService.updateActivity(activity);
        }

        //返回删除活动结果信息
        StringBuilder promptInfo = new StringBuilder("结果: \n");
        promptInfo.append("成功删除活动：").append(JsonUtils.toJSONString(successActList));
        if (activityFailList.size() > 0) {
            promptInfo.append("失败列表:\n");
            promptInfo.append("失败个数：").append(activityFailList.size()).append("\n");
            for (Integer serverId : serverFailedList.keySet()) {
                promptInfo.append("失败区服：").append(serverId)
                        .append("，未删除活动：").append(JsonUtils.toJSONString(serverFailedList.get(serverId))).append("\n");
            }
        }
        if (serverSuccessList.size() > 0) {
            promptInfo.append("成功列表:\n");
            promptInfo.append("成功个数：").append(serverSuccessList.size())
                    .append("，对应区服：").append(JsonUtils.toJSONString(serverSuccessList.keySet())).append("\n");
        }
        GMLogUtil.log(promptInfo.toString());
        return AjaxResult.info(promptInfo.toString()).put("ok",true);
    }

    /**
     * 获取活动boss类型
     * @return
     */
    @PostMapping( "/getActivityBossType")
    @ResponseBody
    public Object getActivityBossType() {
        List<ActivityBossType> list = activityBossTypeService.selectActivityBossTypeList(new ActivityBossType());
        return AjaxResult.info("",list).put("ok",true);
    }

    /**
     * 发布开服活动到对应游戏服
     * @return
     */
    @PostMapping( "/sendOpenServerActivity")
    @ResponseBody
    public Object sendOpenServerActivity(int serverId) throws ParseException {
        TServer server = tServerService.selectTServerByServerId(serverId);
        if(server == null){
            log.error("发送开服活动时，服务器未找到，serverId="+serverId);
            return AjaxResult.info("发送开服活动时，服务器未找到，serverId="+serverId).put("ok",false);
        }

        Activity act = new Activity();
        act.setAutoSend(1);
        act.setIsDeleted(0);
        List<Activity> activities = activityService.selectActivityList(act);
        if(activities.isEmpty()){
            log.error("发送开服活动时，没有找到开服活动");
            return AjaxResult.info("发送开服活动时，没有找到开服活动").put("ok", false);
        }

        Iterator<Activity> iterator = activities.iterator();
        while(iterator.hasNext()){
            Activity activity = iterator.next();
            if(activity.getTimeType() == 0&& TimeUtils.Time()>TimeUtils.getDateByString2(activity.getEndTime()).getTime()) {
                iterator.remove();
            }
        }

        try {
            HashMap resultMap = GameServerRequestUtil.gmBatchSendActMess(server, activities);
            if (!Boolean.valueOf(resultMap.get("ok").toString())) {
                log.error(serverId + "服,发布开服活动失败！操作结果：" + resultMap.get("data").toString());
            } else {
                return AjaxResult.info(serverId + "服,发布开服活动成功！").put("ok", true);
            }
        }catch (Exception e){
            log.error(serverId + "服,发布活动失败！error："+e.getMessage());
            return AjaxResult.info(serverId + "服,发布活动失败！error："+e.getMessage());
        }
        return AjaxResult.info(serverId + "服,发布开服活动失败！").put("ok", false);
    }
}

