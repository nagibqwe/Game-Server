package com.gm.project.gamelog.changerolenamelog.controller;

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
import com.gm.project.gamelog.changerolenamelog.domain.Changerolenamelog;
import com.gm.project.gamelog.changerolenamelog.service.IChangerolenamelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 改名ЖурналController
 * 
 * @author gm
 * @date 2021-09-09
 */
@Controller
@RequestMapping("/gamelog/changerolenamelog")
public class ChangerolenamelogController extends BaseController
{
    private String prefix = "gamelog/changerolenamelog";

    @Autowired
    private IChangerolenamelogService changerolenamelogService;

    @RequiresPermissions("gamelog:changerolenamelog:view")
    @GetMapping()
    public String changerolenamelog()
    {
        return prefix + "/changerolenamelog";
    }
    /**
     * 查询改名Журнал列表
     */
    @RequiresPermissions("gamelog:changerolenamelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Changerolenamelog changerolenamelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("Укажите время начала и окончания");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("Выберите сервер из списка");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Changerolenamelog> list = changerolenamelogService.selectChangerolenamelogList(changerolenamelog,param);
        return getDataTable(list);
    }
    /**
     * Экспорт改名Журнал列表
     */
    @RequiresPermissions("gamelog:changerolenamelog:export")
    @Log(title = "改名Журнал", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Changerolenamelog changerolenamelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Changerolenamelog> list = changerolenamelogService.selectChangerolenamelogList(changerolenamelog,param);
        ExcelUtil<Changerolenamelog> util = new ExcelUtil<Changerolenamelog>(Changerolenamelog.class);
        return util.exportExcel(list, "改名ЖурналДанные");
    }
}
