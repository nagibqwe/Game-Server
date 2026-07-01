package com.gm.project.stat.stat_level_distribute.controller;

import java.util.List;

import com.gm.common.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;
import com.gm.project.stat.stat_level_distribute.service.IStatLevelDistributeService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 角色等级分布Controller
 * 
 * @author gm
 * @date 2021-08-06
 */
@Controller
@RequestMapping("/stat/stat_level_distribute")
public class StatLevelDistributeController extends BaseController
{
    private String prefix = "stat/stat_level_distribute";

    @Autowired
    private IStatLevelDistributeService statLevelDistributeService;

    @RequiresPermissions("stat:stat_level_distribute:view")
    @GetMapping()
    public String stat_level_distribute()
    {

        return prefix + "/stat_level_distribute";
    }

    /**
     * 查询角色等级分布列表
     */
    @RequiresPermissions("stat:stat_level_distribute:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StatLevelDistribute statLevelDistribute,String channelNames,  Integer condition, Integer level,String startDate,String endDate,Integer serverId)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<StatLevelDistribute> list =  statLevelDistributeService.selectStatLevelDistributeList(channelNames,condition,level,statLevelDistribute,startDate,endDate,serverId);
        return getDataTable(list);
    }

}
