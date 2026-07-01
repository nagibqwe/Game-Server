package com.gm.project.gmtool.function.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.function.domain.Function;
import com.gm.project.gmtool.function.service.IFunctionService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 游戏功能列表Controller
 * 
 * @author gm
 * @date 2021-10-26
 */
@Controller
@RequestMapping("/gmtool/function")
public class FunctionController extends BaseController
{
    private String prefix = "gmtool/function";

    @Autowired
    private IFunctionService functionService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:function:view")
    @GetMapping()
    public String function()
    {
        return prefix + "/function";
    }

    /**
     * 查询服务器功能
     * @param serverId
     * @return
     */
    @PostMapping("/queryFunction")
    @ResponseBody
    public AjaxResult queryFunction(Integer serverId){
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmGetFuncOpenList(server);
    }

    /**
     * 设置服务器功能开关
     * @param serverId
     * @param funcSwitch
     * @return
     */
    @PostMapping("/sendFunctionSwitch")
    @ResponseBody
    public AjaxResult sendFunctionSwitch(Integer serverId, String funcSwitch) {
        if(StringUtils.isEmpty(funcSwitch)){
            return AjaxResult.error("没有修改操作");
        }
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmSwitchFunction(server, funcSwitch);
    }

    /**
     * 查询游戏功能列表列表
     */
    @RequiresPermissions("gmtool:function:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Function function)
    {
        startPage();
        List<Function> list = functionService.selectFunctionList(function);
        return getDataTable(list);
    }

    /**
     * 导出游戏功能列表列表
     */
    @RequiresPermissions("gmtool:function:export")
    @Log(title = "游戏功能列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Function function)
    {
        List<Function> list = functionService.selectFunctionList(function);
        ExcelUtil<Function> util = new ExcelUtil<Function>(Function.class);
        return util.exportExcel(list, "游戏功能列表数据");
    }

    /**
     * 新增游戏功能列表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存游戏功能列表
     */
    @RequiresPermissions("gmtool:function:add")
    @Log(title = "游戏功能列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Function function)
    {
        return toAjax(functionService.insertFunction(function));
    }

    /**
     * 修改游戏功能列表
     */
    @GetMapping("/edit/{funcId}")
    public String edit(@PathVariable("funcId") Integer funcId, ModelMap mmap)
    {
        Function function = functionService.selectFunctionById(funcId);
        mmap.put("function", function);
        return prefix + "/edit";
    }

    /**
     * 修改保存游戏功能列表
     */
    @RequiresPermissions("gmtool:function:edit")
    @Log(title = "游戏功能列表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Function function)
    {
        return toAjax(functionService.updateFunction(function));
    }

    /**
     * 删除游戏功能列表
     */
    @RequiresPermissions("gmtool:function:remove")
    @Log(title = "游戏功能列表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(functionService.deleteFunctionByIds(ids));
    }
}
