package com.gm.project.gamelog.coinchangelog.controller;

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
import com.gm.project.gamelog.coinchangelog.domain.Coinchangelog;
import com.gm.project.gamelog.coinchangelog.service.ICoinchangelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 货币变化日志Controller
 * 
 * @author gm
 * @date 2021-11-08
 */
@Controller
@RequestMapping("/gamelog/coinchangelog")
public class CoinchangelogController extends BaseController
{
    private String prefix = "gamelog/coinchangelog";

    @Autowired
    private ICoinchangelogService coinchangelogService;

    @GetMapping()
    public String coinchangelog()
    {
        return prefix + "/coinchangelog";
    }
    /**
     * 查询货币变化日志列表
     */
    @RequiresPermissions("gamelog:coinchangelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Coinchangelog coinchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }

        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Coinchangelog> list = coinchangelogService.selectCoinchangelogList(coinchangelog,param);
        return getDataTable(list);
    }
    /**
     * 导出货币变化日志列表
     */
    @RequiresPermissions("gamelog:coinchangelog:export")
    @Log(title = "货币变化日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Coinchangelog coinchangelog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Coinchangelog> list = coinchangelogService.selectCoinchangelogList(coinchangelog,param);
        ExcelUtil<Coinchangelog> util = new ExcelUtil<Coinchangelog>(Coinchangelog.class);
        return util.exportExcel(list, "货币变化日志数据");
    }
}
