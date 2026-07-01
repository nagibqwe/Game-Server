package com.gm.project.gamelog.guildbaselog.controller;

import com.gm.common.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gmtool.utils.TimeUtils;
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
import com.gm.project.gamelog.guildbaselog.domain.Guildbaselog;
import com.gm.project.gamelog.guildbaselog.service.IGuildbaselogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.annotation.Resource;


/**
 * 公会基础信息Controller
 * 
 * @author gm
 * @date 2021-12-06
 */
@Controller
@RequestMapping("/gamelog/guildbaselog")
public class GuildbaselogController extends BaseController
{
    private String prefix = "gamelog/guildbaselog";

    @Autowired
    private IGuildbaselogService guildbaselogService;

    @RequiresPermissions("gamelog:guildbaselog:view")
    @GetMapping("/guildbaselog")
    public String guildbaselog()
    {
        return prefix + "/guildbaselog";
    }

    @RequiresPermissions("gamelog:guildmemberlog:view")
    @GetMapping("/guildmemberlog")
    public String guildmemberlog()
    {
        return prefix + "/guildmemberlog";
    }

    @RequiresPermissions("gamelog:guildchangelog:view")
    @GetMapping("/guildchangelog")
    public String guildchangelog()
    {
        return prefix + "/guildchangelog";
    }

    /**
     * 查询公会基础信息列表
     */
    @RequiresPermissions("gamelog:guildbaselog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Guildbaselog guildbaselog, Integer queryType, Integer serverId)
    {
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }

        int year = TimeUtils.getYear(TimeUtils.Time());

        Map<String,Object> param = new HashMap<>();
        param.put("serverId", serverId);
        param.put("tableName", "guildbaselog"+year);
        param.put("queryType", queryType);
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();

//        param.put("serverId", selectServerId);
//        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
//        {
//            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
//            param.put("pageNum",pageNum);
//            param.put("pageSize",pageSize);
//        }
//        param.put("startDate",startDate);
//        param.put("endDate",endDate);

        List<Guildbaselog> list = guildbaselogService.selectGuildbaselogList(guildbaselog, param);
        return getDataTable(list);
    }
    /**
     * 导出公会基础信息列表
     */
    @RequiresPermissions("gamelog:guildbaselog:export")
    @Log(title = "公会基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Guildbaselog guildbaselog, Integer queryType, Integer serverId)
    {
        if(serverId == null || serverId == 0){
            return AjaxResult.error("请选择服务器列表").put("ok", false);
        }

        int year = TimeUtils.getYear(TimeUtils.Time());

        Map<String,Object> param = new HashMap<>();
        param.put("serverId", serverId);
        param.put("tableName", "guildbaselog"+year);
        param.put("queryType", queryType);
        List<Guildbaselog> list = guildbaselogService.selectGuildbaselogList(guildbaselog, param);
        ExcelUtil<Guildbaselog> util = new ExcelUtil<Guildbaselog>(Guildbaselog.class);
        return util.exportExcel(list, "公会基础信息数据");
    }
}
