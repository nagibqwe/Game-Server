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
 * 首领死亡复活日志Controller
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
     * 查询首领死亡复活日志列表
     */
    @RequiresPermissions("gamelog:bossdierelivelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Bossdierelivelog bossdierelivelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Bossdierelivelog> list = bossdierelivelogService.selectBossdierelivelogList(bossdierelivelog,param);
        return getDataTable(list);
    }
    /**
     * 导出首领死亡复活日志列表
     */
    @RequiresPermissions("gamelog:bossdierelivelog:export")
    @Log(title = "首领死亡复活日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Bossdierelivelog bossdierelivelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Bossdierelivelog> list = bossdierelivelogService.selectBossdierelivelogList(bossdierelivelog,param);
        ExcelUtil<Bossdierelivelog> util = new ExcelUtil<Bossdierelivelog>(Bossdierelivelog.class);
        return util.exportExcel(list, "首领死亡复活日志数据");
    }
}
