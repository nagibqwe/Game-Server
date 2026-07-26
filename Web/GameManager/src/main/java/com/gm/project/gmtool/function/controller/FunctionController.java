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
 * Функции игрыController
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
     * 查询Сервер功能
     * @param serverId
     * @return
     */
    @PostMapping("/queryFunction")
    @ResponseBody
    public AjaxResult queryFunction(Integer serverId){
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }
        return GameServerRequestUtil.gmGetFuncOpenList(server);
    }

    /**
     * 设置Сервер功能开关
     * @param serverId
     * @param funcSwitch
     * @return
     */
    @PostMapping("/sendFunctionSwitch")
    @ResponseBody
    public AjaxResult sendFunctionSwitch(Integer serverId, String funcSwitch) {
        if(StringUtils.isEmpty(funcSwitch)){
            return AjaxResult.error("没有ИзменитьДействия");
        }
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("Выбранный ID сервера не существует");
        }
        return GameServerRequestUtil.gmSwitchFunction(server, funcSwitch);
    }

    /**
     * 查询Функции игры列表
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
     * ЭкспортФункции игры列表
     */
    @RequiresPermissions("gmtool:function:export")
    @Log(title = "Функции игры", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Function function)
    {
        List<Function> list = functionService.selectFunctionList(function);
        ExcelUtil<Function> util = new ExcelUtil<Function>(Function.class);
        return util.exportExcel(list, "Функции игрыДанные");
    }

    /**
     * ДобавитьФункции игры
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьФункции игры
     */
    @RequiresPermissions("gmtool:function:add")
    @Log(title = "Функции игры", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Function function)
    {
        return toAjax(functionService.insertFunction(function));
    }

    /**
     * ИзменитьФункции игры
     */
    @GetMapping("/edit/{funcId}")
    public String edit(@PathVariable("funcId") Integer funcId, ModelMap mmap)
    {
        Function function = functionService.selectFunctionById(funcId);
        mmap.put("function", function);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьФункции игры
     */
    @RequiresPermissions("gmtool:function:edit")
    @Log(title = "Функции игры", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Function function)
    {
        return toAjax(functionService.updateFunction(function));
    }

    /**
     * УдалитьФункции игры
     */
    @RequiresPermissions("gmtool:function:remove")
    @Log(title = "Функции игры", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(functionService.deleteFunctionByIds(ids));
    }
}
