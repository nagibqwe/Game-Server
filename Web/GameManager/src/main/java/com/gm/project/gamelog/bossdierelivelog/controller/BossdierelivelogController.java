package com.gm.project.gamelog.bossdierelivelog.controller;

import com.gm.common.utils.StringUtils;
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
import com.gm.project.gamelog.bossdierelivelog.domain.Bossdierelivelog;
import com.gm.project.gamelog.bossdierelivelog.service.IBossdierelivelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * Контроллер логов смерти и возрождения боссов
 * 
 * @author gm
 * @date 2021-09-10
 */
@Controller
@RequestMapping("/gamelog/bossdierelivelog")
public class BossdierelivelogController extends BaseController
{
    private String prefix = "gamelog/bossdierelivelog";

    @Autowired
    private IBossdierelivelogService bossdierelivelogService;

    @RequiresPermissions("gamelog:bossdierelivelog:view")
    @GetMapping()
    public String bossdierelivelog()
    {
        return prefix + "/bossdierelivelog";
    }
    /**
     * Получение списка логов смерти и возрождения боссов
     */
    @RequiresPermissions("gamelog:bossdierelivelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Bossdierelivelog bossdierelivelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("Укажите время начала и окончания");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("Выберите сервер из списка");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Bossdierelivelog> list = bossdierelivelogService.selectBossdierelivelogList(bossdierelivelog,param);
        return getDataTable(list);
    }
    /**
     * Экспорт списка логов смерти и возрождения боссов
     */
    @RequiresPermissions("gamelog:bossdierelivelog:export")
    @Log(title = "Логи смерти и возрождения боссов", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Bossdierelivelog bossdierelivelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Bossdierelivelog> list = bossdierelivelogService.selectBossdierelivelogList(bossdierelivelog,param);
        ExcelUtil<Bossdierelivelog> util = new ExcelUtil<Bossdierelivelog>(Bossdierelivelog.class);
        return util.exportExcel(list, "Данные логов смерти и возрождения боссов");
    }
}