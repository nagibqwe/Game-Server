package com.gm.project.gamelog.goldchangelog.controller;

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
import com.gm.project.gamelog.goldchangelog.domain.Goldchangelog;
import com.gm.project.gamelog.goldchangelog.service.IGoldchangelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 元宝变化日志Controller
 * 
 * @author gm
 * @date 2021-09-11
 */
@Controller
@RequestMapping("/gamelog/goldchangelog")
public class GoldchangelogController extends BaseController
{
    private String prefix = "gamelog/goldchangelog";

    @Autowired
    private IGoldchangelogService goldchangelogService;

    @RequiresPermissions("gamelog:goldchangelog:view")
    @GetMapping()
    public String goldchangelog()
    {
        return prefix + "/goldchangelog";
    }
    /**
     * 查询元宝变化日志列表
     */
    @RequiresPermissions("gamelog:goldchangelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Goldchangelog goldchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Goldchangelog> list = goldchangelogService.selectGoldchangelogList(goldchangelog,param);
        return getDataTable(list);
    }
    /**
     * 导出元宝变化日志列表
     */
    @RequiresPermissions("gamelog:goldchangelog:export")
    @Log(title = "元宝变化日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Goldchangelog goldchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Goldchangelog> list = goldchangelogService.selectGoldchangelogList(goldchangelog,param);
        ExcelUtil<Goldchangelog> util = new ExcelUtil<Goldchangelog>(Goldchangelog.class);
        return util.exportExcel(list, "元宝变化日志数据");
    }
}
