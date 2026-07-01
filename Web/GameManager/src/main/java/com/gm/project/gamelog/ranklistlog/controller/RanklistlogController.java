package com.gm.project.gamelog.ranklistlog.controller;

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
import com.gm.project.gamelog.ranklistlog.domain.Ranklistlog;
import com.gm.project.gamelog.ranklistlog.service.IRanklistlogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 排行榜日志Controller
 * 
 * @author gm
 * @date 2021-09-08
 */
@Controller
@RequestMapping("/gamelog/ranklistlog")
public class RanklistlogController extends BaseController
{
    private String prefix = "gamelog/ranklistlog";

    @Autowired
    private IRanklistlogService ranklistlogService;

    @RequiresPermissions("gamelog:ranklistlog:view")
    @GetMapping()
    public String ranklistlog()
    {
        return prefix + "/ranklistlog";
    }
    /**
     * 查询排行榜日志列表
     */
    @RequiresPermissions("gamelog:ranklistlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Ranklistlog ranklistlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Ranklistlog> list = ranklistlogService.selectRanklistlogList(ranklistlog,param);
        return getDataTable(list);
    }
    /**
     * 导出排行榜日志列表
     */
    @RequiresPermissions("gamelog:ranklistlog:export")
    @Log(title = "排行榜日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Ranklistlog ranklistlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);

        List<Ranklistlog> list = ranklistlogService.selectRanklistlogList(ranklistlog,param);
        ExcelUtil<Ranklistlog> util = new ExcelUtil<Ranklistlog>(Ranklistlog.class);
        return util.exportExcel(list, "排行榜日志数据");
    }
}
