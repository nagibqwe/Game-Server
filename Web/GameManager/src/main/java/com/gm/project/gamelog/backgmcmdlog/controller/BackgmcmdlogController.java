package com.gm.project.gamelog.backgmcmdlog.controller;

import com.gm.common.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.project.common.utils.GameLogUtil;
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
import com.gm.project.gamelog.backgmcmdlog.domain.Backgmcmdlog;
import com.gm.project.gamelog.backgmcmdlog.service.IBackgmcmdlogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * Контроллер логов команд бэкенда
 * 
 * @author gm
 * @date 2021-09-10
 */
@Controller
@RequestMapping("/gamelog/backgmcmdlog")
public class BackgmcmdlogController extends BaseController
{
    private String prefix = "gamelog/backgmcmdlog";

    @Autowired
    private IBackgmcmdlogService backgmcmdlogService;

    @RequiresPermissions("gamelog:backgmcmdlog:view")
    @GetMapping()
    public String backgmcmdlog()
    {
        return prefix + "/backgmcmdlog";
    }
    /**
     * Получение списка логов команд бэкенда
     */
    @RequiresPermissions("gamelog:backgmcmdlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Backgmcmdlog backgmcmdlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("Укажите время начала и окончания");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("Выберите сервер из списка");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Backgmcmdlog> list = backgmcmdlogService.selectBackgmcmdlogList(backgmcmdlog,param);
        return getDataTable(list);
    }
    /**
     * Экспорт списка логов команд бэкенда
     */
    @RequiresPermissions("gamelog:backgmcmdlog:export")
    @Log(title = "Логи команд бэкенда", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Backgmcmdlog backgmcmdlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {

        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Backgmcmdlog> list = backgmcmdlogService.selectBackgmcmdlogList(backgmcmdlog,param);
        ExcelUtil<Backgmcmdlog> util = new ExcelUtil<Backgmcmdlog>(Backgmcmdlog.class);
        return util.exportExcel(list, "Данные логов команд бэкенда");
    }
}