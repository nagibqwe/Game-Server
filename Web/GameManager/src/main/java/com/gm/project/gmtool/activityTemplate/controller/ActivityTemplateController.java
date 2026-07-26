package com.gm.project.gmtool.activityTemplate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.JsonUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.gm.project.gmtool.activityTemplate.domain.ActivityTemplate;
import com.gm.project.gmtool.activityTemplate.service.IActivityTemplateService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * Шаблоны событийController
 * 
 * @author gm
 * @date 2021-09-07
 */
@Controller
@RequestMapping("/gmtool/activityTemplate")
public class ActivityTemplateController extends BaseController
{
    private String prefix = "gmtool/activityTemplate";

    @Autowired
    private IActivityTemplateService activityTemplateService;

    @RequiresPermissions("gmtool:activityTemplate:view")
    @GetMapping()
    public String activityTemplate()
    {
        return prefix + "/activityTemplate";
    }

    /**
     * 查询Шаблоны событий列表
     */
//    @RequiresPermissions("gmtool:activityTemplate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityTemplate activityTemplate)
    {
        startPage();
        List<ActivityTemplate> list = activityTemplateService.selectActivityTemplateList(activityTemplate);
        return getDataTable(list);
    }

    /**
     * ЭкспортШаблоны событий列表
     */
//    @RequiresPermissions("gmtool:activityTemplate:export")
    @Log(title = "Шаблоны событий", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityTemplate activityTemplate,String ids)
    {
        List<ActivityTemplate> list = new ArrayList<>();
        if ("".equals(ids)){
            list = activityTemplateService.selectActivityTemplateList(activityTemplate);//根据表单条件选择ЭкспортДанные
        }else {
            list = activityTemplateService.selectActivityTemplateByIds(ids);//根据用户选择的ids进行ДанныеЭкспорт
        }
        ExcelUtil<ActivityTemplate> util = new ExcelUtil<ActivityTemplate>(ActivityTemplate.class);
        return util.exportExcel(list, "Шаблоны событийДанные");
    }

    /**
     * ДобавитьШаблоны событий
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьШаблоны событий
     */
    @RequiresPermissions("gmtool:activityTemplate:add")
    @Log(title = "Шаблоны событий", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityTemplate activityTemplate)
    {
        return toAjax(activityTemplateService.insertActivityTemplate(activityTemplate));
    }

    /**
     * ИзменитьШаблоны событий
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        ActivityTemplate activityTemplate = activityTemplateService.selectActivityTemplateById(id);
        mmap.put("activityTemplate", activityTemplate);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьШаблоны событий
     */
    @RequiresPermissions("gmtool:activityTemplate:edit")
    @Log(title = "Шаблоны событий", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityTemplate activityTemplate)
    {
        return toAjax(activityTemplateService.updateActivityTemplate(activityTemplate));
    }

    /**
     * УдалитьШаблоны событий
     */
//    @RequiresPermissions("gmtool:activityTemplate:remove")
    @Log(title = "Шаблоны событий", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int row = activityTemplateService.deleteActivityTemplateByIds(ids);
        GMLogUtil.log("УдалитьШаблоны событийИнформация,Id:"+ids);
        return toAjax(row);
    }

    /**
     * ИмпортШаблоны событийДанные
     * @param activityTemplateFile
     * @return
     */
    @PostMapping("/loadActivityTemplate")
    @ResponseBody
    public Object loadActivityTemplate(MultipartFile activityTemplateFile){
        if (activityTemplateFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = activityTemplateFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<ActivityTemplate> activityTemplates = new ArrayList<>();
        int[] ActivityTemplatePos = new int[23];
        try {
            InputStream in = activityTemplateFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
//            if (!sheet.getSheetName().equalsIgnoreCase("activity_yunying")) {
//                return AjaxResult.info("not activity_yunying config file").put("ok",false);
//            }
            XSSFRow row = sheet.getRow(0);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("templateName")) {
                    ActivityTemplatePos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("createTime")) {
                    ActivityTemplatePos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("type")) {
                    ActivityTemplatePos[2] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("subType")) {
                    ActivityTemplatePos[3] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("minLv")) {
                    ActivityTemplatePos[4] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("maxLv")) {
                    ActivityTemplatePos[5] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("tag")) {
                    ActivityTemplatePos[6] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("sort")) {
                    ActivityTemplatePos[7] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("name")) {
                    ActivityTemplatePos[8] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("timeType")) {
                    ActivityTemplatePos[9] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerOffsetBegin")) {
                    ActivityTemplatePos[10] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerOffset")) {
                    ActivityTemplatePos[11] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("beginTime")) {
                    ActivityTemplatePos[12] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("endTime")) {
                    ActivityTemplatePos[13] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerRecordOffsetBegin")) {
                    ActivityTemplatePos[14] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("openServerRecordOffset")) {
                    ActivityTemplatePos[15] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("startRecordTime")) {
                    ActivityTemplatePos[16] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("endRecordTime")) {
                    ActivityTemplatePos[17] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("autoSend")) {
                    ActivityTemplatePos[18] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("isOpenServer")) {
                    ActivityTemplatePos[19] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("custom")) {
                    ActivityTemplatePos[20] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("templateDec")) {
                    ActivityTemplatePos[21] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("description")) {
                    ActivityTemplatePos[22] = i;
                }
            }
            List<ActivityTemplate> updateResult = new ArrayList<>();
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                ActivityTemplate activityTemplate = new ActivityTemplate();
                activityTemplate.setTemplateName(dataRow.getCell(ActivityTemplatePos[0]).toString());
                activityTemplate.setCreateTime(dataRow.getCell(ActivityTemplatePos[1]).toString());
                activityTemplate.setType((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[2]).toString()));
                activityTemplate.setSubType((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[3]).toString()));
                activityTemplate.setMinLv((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[4]).toString()));
                activityTemplate.setMaxLv((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[5]).toString()));
                activityTemplate.setTag((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[6]).toString()));
                activityTemplate.setSort((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[7]).toString()));
                activityTemplate.setName(dataRow.getCell(ActivityTemplatePos[8]).toString());
                activityTemplate.setTimeType((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[9]).toString()));
                activityTemplate.setOpenServerOffsetBegin((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[10]).toString()));
                activityTemplate.setOpenServerOffset((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[11]).toString()));
                activityTemplate.setBeginTime(dataRow.getCell(ActivityTemplatePos[12]).toString());
                activityTemplate.setEndTime(dataRow.getCell(ActivityTemplatePos[13]).toString());
                activityTemplate.setOpenServerRecordOffsetBegin((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[14]).toString()));
                activityTemplate.setOpenServerRecordOffset((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[15]).toString()));
                activityTemplate.setStartRecordTime(dataRow.getCell(ActivityTemplatePos[16]).toString());
                activityTemplate.setEndRecordTime(dataRow.getCell(ActivityTemplatePos[17]).toString());
                activityTemplate.setAutoSend((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[18]).toString()));
                activityTemplate.setIsOpenServer((int) Float.parseFloat(dataRow.getCell(ActivityTemplatePos[19]).toString()));
                activityTemplate.setCustom(dataRow.getCell(ActivityTemplatePos[20]).toString());
                activityTemplate.setTemplateDec(dataRow.getCell(ActivityTemplatePos[21]).toString());
                activityTemplate.setDescription(dataRow.getCell(ActivityTemplatePos[22]).toString());

                ActivityTemplate template = new ActivityTemplate();
                template.setTemplateName(activityTemplate.getTemplateName());
                template.setType(activityTemplate.getType());
                List<ActivityTemplate> templates = activityTemplateService.selectActivityTemplateList(template);
                if (null != templates && templates.size() > 0){
                    ActivityTemplate actTemplate = templates.get(0);//有Данные  有且只有一组Данные
                    activityTemplate.setId(actTemplate.getId());
                    activityTemplateService.updateActivityTemplate(activityTemplate);//同种type活动有相同模板名的则为更新Действия
                    updateResult.add(activityTemplate);
                    continue;
                }
                activityTemplates.add(activityTemplate);
            }
            if (activityTemplates.size()>0) {
                List<ActivityTemplate> addResult = new ArrayList<>();
                for (ActivityTemplate activityTemplate:activityTemplates){
                    activityTemplateService.insertActivityTemplate(activityTemplate);
                    addResult.add(activityTemplate);
                }
                StringBuilder promptInfo = new StringBuilder("ИмпортШаблоны событийДанные: \n");
                for (ActivityTemplate activityTemplate:activityTemplates){//Добавить
                    promptInfo.append(" 模版名：").append(activityTemplate.getTemplateName())
                            .append("  Название события：").append(activityTemplate.getName())
                            .append("，Тип события：").append(JsonUtils.toJSONString(activityTemplate.getType())).append("\n");
                }

                for (ActivityTemplate activityTemplate:updateResult){//更新
                    promptInfo.append(" 更新Шаблоны событийДанные: \n")
                            .append(" 模版名：").append(activityTemplate.getTemplateName())
                            .append("  Название события：").append(activityTemplate.getName())
                            .append("，Тип события：").append(JsonUtils.toJSONString(activityTemplate.getType())).append("\n");
                }
                GMLogUtil.log(String.valueOf(promptInfo));

                return AjaxResult.info("Reload file OK, add " + addResult.size() + " record! update " + updateResult.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("Reload file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }
}
