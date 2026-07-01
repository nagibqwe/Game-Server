package com.gm.project.gamelog.rolestate.controller;

import com.github.pagehelper.PageHelper;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gamelog.rechargelog.domain.Rechargelog;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gamelog.rolestate.service.IRoleStateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色快照日志Controller
 *
 * @author gm
 * @date 2021-09-07
 */
@Controller
@RequestMapping("/gamelog/rolestate")
public class RoleStateController extends BaseController {
    private String prefix = "gamelog/rolestate";

    @Autowired
    private IRoleStateService roleStateService;

    @RequiresPermissions("gamelog:rolestate:view")
    @GetMapping()
    public String rolestate() {
        return prefix + "/rolestate";
    }

    /**
     * 查询角色快照日志列表
     */
    @RequiresPermissions("gamelog:rolestate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RoleState roleState, Integer serverId) {
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = new HashMap<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        param.put("serverId",serverId);
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
        }
//        param.put("startDate",startDate);
//        param.put("endDate",endDate);


        TableDataInfo tableDataInfo = getDataTable(new ArrayList<>());
        List<RoleState> list = roleStateService.selectRoleStateList(roleState, param);
        if(param.containsKey("count")){
            tableDataInfo.setTotal((int)param.get("count"));
        }
        tableDataInfo.setRows(list);
        return tableDataInfo;


    }

    /**
     * 导出角色快照日志列表
     */
    @RequiresPermissions("gamelog:rolestate:export")
    @Log(title = "角色快照日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RoleState roleState, Integer serverId) {

//        if(serverId == null || serverId == 0){
//            return getDataTableErrorMsg("请选择服务器列表");
//        }
        Map<String,Object> param = new HashMap<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("serverId",serverId);
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
        }
        List<RoleState> list = roleStateService.selectRoleStateList(roleState, param);
        ExcelUtil<RoleState> util = new ExcelUtil<RoleState>(RoleState.class);
        return util.exportExcel(list, "角色快照日志数据");
    }
}
