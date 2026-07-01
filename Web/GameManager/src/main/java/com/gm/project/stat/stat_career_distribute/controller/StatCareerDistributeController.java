package com.gm.project.stat.stat_career_distribute.controller;

import java.util.List;
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
import com.gm.project.stat.stat_career_distribute.domain.StatCareerDistribute;
import com.gm.project.stat.stat_career_distribute.service.IStatCareerDistributeService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 职业分布Controller
 * 
 * @author gm
 * @date 2021-09-07
 */
@Controller
@RequestMapping("/stat/stat_career_distribute")
public class StatCareerDistributeController extends BaseController
{
    private String prefix = "stat/stat_career_distribute";

    @Autowired
    private IStatCareerDistributeService statCareerDistributeService;

    @RequiresPermissions("stat:stat_career_distribute:view")
    @GetMapping()
    public String stat_career_distribute()
    {

        return prefix + "/stat_career_distribute";
    }

    /**
     * 查询职业分布列表
     */
    @RequiresPermissions("stat:stat_career_distribute:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StatCareerDistribute statCareerDistribute,String channelNames,Integer serverId)
    {
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }

        startPage();
        List<StatCareerDistribute> list = statCareerDistributeService.selectStatCareerDistributeList(statCareerDistribute,channelNames,serverId);
        return getDataTable(list);
    }

    /**
     * 导出职业分布列表
     */
    @RequiresPermissions("stat:stat_career_distribute:export")
    @Log(title = "职业分布", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StatCareerDistribute statCareerDistribute,String channelNames,Integer serverId)
    {
        List<StatCareerDistribute> list = statCareerDistributeService.selectStatCareerDistributeList(statCareerDistribute,channelNames,serverId);
        ExcelUtil<StatCareerDistribute> util = new ExcelUtil<StatCareerDistribute>(StatCareerDistribute.class);
        return util.exportExcel(list, "职业分布数据");
    }
}
