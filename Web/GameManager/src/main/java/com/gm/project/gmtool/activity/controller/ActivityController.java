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
 * Контроллер игровых событий (операционных активностей)
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
     * Универсальный переход на страницу игровых событий
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
            case 0: //Обзор событий
                return prefix + "/ActivityList";
            case ActivityType.GetActive: //Получение активности
                return prefix + "/GetActive";
            case ActivityType.DailyRecharge: //Ежедневный пополнение
                return prefix + "/DailyRecharge";
            case ActivityType.LimitTimeLogin: //Лимитированная награда за вход
                return prefix + "/LimitTimeLogin";
            case ActivityType.LimitGiftBag: //Лимитированный набор
                return prefix + "/LimitGiftBag";
            case ActivityType.LimitedTotalRecharge: //Лимитированное общее пополнение
                return prefix + "/LimitedTotalRecharge";
            case ActivityType.LimitedTotalConsume: //Лимитированное общее потребление
                return prefix + "/LimitedTotalConsume";
            case ActivityType.GroupBuy://Совместная покупка
                return prefix + "/GroupBuy";
            case ActivityType.LuckyCat://Кот удачи
                return prefix + "/LuckyCat";
            case ActivityType.CollectGoodsExChange://Обмен предметов
                return prefix + "/CollectGoodsExChange";
            case ActivityType.DrawReward://Сокровищница
                return prefix + "/DrawReward";
            case ActivityType.HolidayBoss://Боссы праздника
                return prefix + "/HolidayBoss";
            case ActivityType.HolidayTask://Задания праздника
                return prefix + "/HolidayTask";
            case ActivityType.HolidayWords://Сбор слов праздника
                return prefix + "/HolidayWords";
            case ActivityType.FestivalPreference://Праздничные предложения
                return prefix + "/FestivalPreference";
            case ActivityType.ContinuousRecharge://Непрерывное пополнение
                return prefix + "/ContinuousRecharge";
            case ActivityType.LimitShopActivty://Лимитированный магазин
                return prefix + "/LimitShopActivity";
            case ActivityType.HolidayDailyGift://Праздничный подарок (покупка за золотые юани)
                return prefix + "/HolidayDailyGift";
            case ActivityType.HolidayScoreRank://Рейтинг по очкам
                return prefix + "/HolidayScoreRank";
            case ActivityType.FestivalWish://Праздничное желание
                return prefix + "/FestivalWish";
            case ActivityType.FBShare://Публикация в FB (Новый год)
                return prefix + "/FBShare";
            case ActivityType.ContinuousRecharge2://Непрерывное пополнение 2 (покупка набора)
                return prefix + "/ContinuousRecharge2";
            case ActivityType.XinNianZhuFu://Праздничное благословение (Новогоднее)
                return prefix + "/FestivalSign";
            case ActivityType.ZhiTouzi://Бросок кубика
                return prefix + "/JumpGrid";
            case ActivityType.AppearanceShow://Показ внешности
                return prefix + "/AppearanceShow";
            case ActivityType.LoginShow://Показ входа
                return prefix + "/LoginShow";
            case ActivityType.Cornucopia://Рог изобилия
                return prefix + "/Cornucopia";
            case ActivityType.LuckyEgg://Счастливое яйцо
                return prefix + "/LuckyEgg";
            case ActivityType.LuckyGem://Счастливый самоцвет
                return prefix + "/LuckyGem";
            case ActivityType.FangZeTreasureHunt://Охота за сокровищами Фанцзэ
                return prefix + "/FangZeTreasureHunt";
            case ActivityType.xianJingTreasureHunt://Охота за сокровищами в Стране Бессмертных
                return prefix + "/xianJingTreasureHunt";
        }
        return "";
    }

    /**
     * Разделение по searchType: общий обзор или запрос по типу
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
     * Экспорт списка игровых событий
     */
    @RequiresPermissions("gmtool:activity:export")
    @Log(title = "Игровые события", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Activity activity)
    {
        List<Activity> list = activityService.selectActivityList(activity);
        ExcelUtil<Activity> util = new ExcelUtil<Activity>(Activity.class);
        return util.exportExcel(list, "Данные игровых событий");
    }

    /**
     * Добавление игрового события
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Сохранение нового игрового события
     */
    @RequiresPermissions("gmtool:activity:add")
    @Log(title = "Игровые события", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Activity activity)
    {
        return toAjax(activityService.insertActivity(activity));
    }

    /**
     * Редактирование игрового события
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Activity activity = activityService.selectActivityById(id);
        mmap.put("activity", activity);
        return prefix + "/edit";
    }

    /**
     * Сохранение изменений игрового события
     */
    @RequiresPermissions("gmtool:activity:edit")
    @Log(title = "Игровые события", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Activity activity)
    {
        return toAjax(activityService.updateActivity(activity));
    }

    /**
     * Удаление игрового события
     */
    @RequiresPermissions("gmtool:activity:remove")
    @Log(title = "Игровые события", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityService.deleteActivityByIds(ids));
    }

    /**
     * Получение типов праздников для игровых событий
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
     * Получение списка шаблонов игровых событий
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
     * Получение данных шаблона
     * @param id
     * @return
     */
    @PostMapping( "/getTemplate")
    @ResponseBody
    public Object getTemplate(int id) {
        ActivityTemplate template = activityTemplateService.selectActivityTemplateById(id);
        if (template == null) {
            return AjaxResult.info("Ошибка получения данных шаблона!").put("ok",false);
        }
        return AjaxResult.info("",template).put("ok",true);
    }

    /**
     * Удаление шаблона
     * @param id
     * @return
     */
    @PostMapping( "/deleteTemplate")
    @ResponseBody
    public Object deleteTemplate(int id) {
        activityTemplateService.deleteActivityTemplateById(id);
        GMLogUtil.log("Удаление шаблона события id:" + id);
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * Проверка уникальности имени шаблона для типа события
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
     * Добавление шаблона
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
     * Обновление шаблона по имени
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
        GMLogUtil.log("Обновление шаблона события id:" + activity.getId());
        return AjaxResult.info("").put("ok",true);
    }


    /**
     * Добавление события
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
                    activity.setState(4);//Изменение после публикации
                }
            }
            flag = ActivityManager.getInstance().addActivity(activity, paramMap);
            GMLogUtil.log("Добавление события, ID:" + activity.getId() +
                    ", название:" + activity.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
        return AjaxResult.info("").put("ok",flag);
    }

    /**
     * Импорт данных игровых событий из Excel
     * @param activityFile
     * @param request
     * @return
     */
    @PostMapping( "/importActivityData")
    @ResponseBody
    public Object importActivityData(MultipartFile activityFile, HttpServletRequest request){
        if (activityFile == null){
            return AjaxResult.info("Файл отсутствует!").put("ok",false);
        }
        String fileName = activityFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")){
            return AjaxResult.info("Неверный тип файла!").put("ok",false);
        }
        List<Activity> activities = new ArrayList<>();
        int[] activityDataPos = new int[20];//Позиции полей
        try {
            Workbook wb;

            InputStream in = activityFile.getInputStream();
            //Определение по расширению
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
                StringBuilder promptInfo = new StringBuilder("Импорт данных событий: \n");
                for (Activity activity:activities){
                    promptInfo.append("   Название: ").append(activity.getName())
                            .append(", Тип: ").append(JsonUtils.toJSONString(activity.getType())).append("\n");
                }
                GMLogUtil.log(String.valueOf(promptInfo));
                return AjaxResult.info("Импорт выполнен, сохранено " + result.size() + " записей!").put("ok",true);
            }
            return AjaxResult.info("Ошибка импорта!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * Получение события по ID
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
     * Экспорт данных события (одна строка)
     * @param actId
     * @param response
     */
    @PostMapping( "/exportActivityData")
    @ResponseBody
    public void exportActivityData(int actId, HttpServletResponse response) {
        Activity activity = activityService.selectActivityById(actId);
        List<Map<String, String>> listMap = new ArrayList<>();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("Название",activity.getName());
        map.put("Описание",activity.getDescription());
        map.put("Тип",String.valueOf(activity.getType()));
        map.put("Тип праздника",String.valueOf(activity.getSubType()));
        map.put("Мин. уровень",String.valueOf(activity.getMinLv()));
        map.put("Макс. уровень",String.valueOf(activity.getMaxLv()));
        map.put("Тег",String.valueOf(activity.getTag()));
        map.put("Сортировка",String.valueOf(activity.getSort()));
        map.put("Тип времени",String.valueOf(activity.getTimeType()));
        map.put("Дней с открытия",String.valueOf(activity.getOpenServerOffsetBegin()));
        map.put("Дней длительности",String.valueOf(activity.getOpenServerOffset()));
        map.put("Время начала",activity.getBeginTime());
        map.put("Время окончания",activity.getEndTime());
        map.put("Дней записи с",String.valueOf(activity.getOpenServerRecordOffsetBegin()));
        map.put("Дней записи длит.",String.valueOf(activity.getOpenServerRecordOffset()));
        map.put("Время записи с",activity.getStartRecordTime());
        map.put("Время записи по",activity.getEndRecordTime());
        map.put("Авто-публикация",String.valueOf(activity.getAutoSend()));
        map.put("Новый сервер",String.valueOf(activity.getIsOpenServer()));
        map.put("Данные",activity.getCustom());
//        map.put("Состояние", String.valueOf(activity.getState()));
//        map.put("Список публикации", activity.getPlatform());
//        map.put("Успешные серверы", activity.getOkSidList());
        listMap.add(map);
        List<String> list1 = Arrays.asList("name","description","type","subType","minLv","maxLv","tag","sort","timeType","openServerOffsetBegin","openServerOffset","beginTime","endTime","openServerRecordOffsetBegin","openServerRecordOffset","startRecordTime","endRecordTime","autoSend","isOpenServer","custom");
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelName = "Данные события";
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(excelName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xlsx");
            genExcel(listMap,list1,activity,out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void genExcel(List<Map<String, String>> dataList,List<String> list1, Activity activity, OutputStream out) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Данные события");

        int ri = 0, ci = 0;
        XSSFRow row = sheet.createRow(ri++);//Первая строка - заголовки на английском
        XSSFCell cell;
        for (String field : list1) {
            cell = row.createCell(ci);
            cell.setCellValue(field);
            ci++;
        }
        row = sheet.createRow(ri++);//Вторая строка - заголовки на русском
        ci=0;
        for (Map.Entry<String, String> entry : dataList.get(0).entrySet()) {
            cell = row.createCell(ci);
            cell.setCellValue(entry.getKey());
            ci++;
        }
        //Данные
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
     * Массовая публикация игровых событий
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

        //Получение данных событий
        List<Activity> actList = new ArrayList<>();
        Set<Integer> typeSet = new HashSet<>();
        for (Integer actId : actIdSet) {
            Activity activity = activityService.selectActivityById(actId);
            if (activity == null) {
                log.error("Событие не найдено при публикации, id：" + actId);
                continue;
            }
            //События одного типа нельзя перезаписывать массово
            if(cover == 1){
                int type = activity.getType()*1000+activity.getSubType();
                if(typeSet.contains(type)){
                    return AjaxResult.info("События одного типа нельзя перезаписывать массово, тип: "+type+", activityType: "+activity.getType()+", festivalType: "+activity.getSubType()).put("ok",false);
                }
                typeSet.add(type);

                if(activity.getCover()!=cover){
                    activity.setCover(cover);
                }
            }

            actList.add(activity);
        }

        //Публикация на серверы
        for (Integer serverId : serverIdSet) {
            TServer server = new TServer();
            server.setGroupName(platform);
            server.setServerId(serverId);
            List<TServer> servers = tServerService.selectTServerList(server);
            if (servers.size() < 1) {
                log.error("Сервер не найден при публикации! platform=" + platform + ", sid=" + serverId);
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
                log.error(serverId + " сервер, публикация [" + failIds + "] не удалась!");
            } else {
                serverSuccessList.put(serverId, actIdList);
            }
        }

        //Обновление данных публикации, статуса, запись лога
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

            GMLogUtil.log("Публикация события, ID: " + activity.getId() +
                    ", название: " + activity.getName() + ", список публикации:" + JsonUtils.toJSONString(toSidList) +
                    ", неудачно:" + JsonUtils.toJSONString(activityFailList.get(activity.getId())) +
                    ", успешно:" + JsonUtils.toJSONString(okSidList));
            activityService.updateActivity(activity);
        }

        //Формирование результата
        StringBuilder promptInfo = new StringBuilder("Результат: \n");
        if (serverFailedList.size() > 0) {
            promptInfo.append("Неудачные серверы:\n");
            promptInfo.append("Количество: ").append(serverFailedList.size())
                    .append(", серверы: ").append(JsonUtils.toJSONString(serverFailedList.keySet())).append("\n");
            for (Integer serverId : serverFailedList.keySet()) {
                promptInfo.append("Сервер: ").append(serverId)
                        .append(", неопубликованные события: ").append(JsonUtils.toJSONString(serverFailedList.get(serverId))).append("\n");
            }
        }
        if (serverSuccessList.size() > 0) {
            promptInfo.append("Успешные серверы:\n");
            promptInfo.append("Перезапись: ").append(cover==1?"Да\n":"Нет\n");
            promptInfo.append("Количество: ").append(serverSuccessList.size())
                    .append(", серверы: ").append(JsonUtils.toJSONString(serverSuccessList.keySet())).append("\n");
            for (Integer serverId : serverSuccessList.keySet()) {
                promptInfo.append("Сервер: ").append(serverId)
                        .append(", опубликованные события: ").append(JsonUtils.toJSONString(serverSuccessList.get(serverId))).append("\n");
            }
        }
        return AjaxResult.info(promptInfo.toString()).put("ok",true);
    }

    /**
     * Массовое удаление игровых событий
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

        //Статистика публи