package com.gm.project.gamelog.itemchangelog.controller;

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
import com.gm.project.gamelog.itemchangelog.domain.Itemchangelog;
import com.gm.project.gamelog.itemchangelog.service.IItemchangelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 物品变化日志Controller
 * 
 * @author gm
 * @date 2021-09-09
 */
@Controller
@RequestMapping("/gamelog/itemchangelog")
public class ItemchangelogController extends BaseController
{
    private String prefix = "gamelog/itemchangelog";

    @Autowired
    private IItemchangelogService itemchangelogService;

    @RequiresPermissions("gamelog:itemchangelog:view")
    @GetMapping()
    public String itemchangelog()
    {
        return prefix + "/itemchangelog";
    }
    /**
     * 查询物品变化日志列表
     */
    @RequiresPermissions("gamelog:itemchangelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Itemchangelog itemchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Itemchangelog> list = itemchangelogService.selectItemchangelogList(itemchangelog,param);
        return getDataTable(list);
    }
    /**
     * 导出物品变化日志列表
     */
    @RequiresPermissions("gamelog:itemchangelog:export")
    @Log(title = "物品变化日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Itemchangelog itemchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Itemchangelog> list = itemchangelogService.selectItemchangelogList(itemchangelog,param);
        ExcelUtil<Itemchangelog> util = new ExcelUtil<Itemchangelog>(Itemchangelog.class);
        return util.exportExcel(list, "物品变化日志数据");
    }
}
