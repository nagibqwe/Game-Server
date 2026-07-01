package com.gm.project.gmtool.hefu.controller;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.dbbak.domain.Dbbak;
import org.apache.commons.lang.math.NumberUtils;
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
import com.gm.project.gmtool.hefu.domain.Hefu;
import com.gm.project.gmtool.hefu.service.IHefuService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 合服Controller
 * 
 * @author gm
 * @date 2021-09-08
 */
@Controller
@RequestMapping("/gmtool/hefu")
public class HefuController extends BaseController
{
    private String prefix = "gmtool/hefu";

    @Autowired
    private IHefuService hefuService;

    @RequiresPermissions("gmtool:hefu:view")
    @GetMapping()
    public String hefu()
    {
        return prefix + "/hefu";
    }

    /**
     * 查询合服列表
     */
//    @RequiresPermissions("gmtool:hefu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Hefu hefu)
    {
        startPage();
        List<Hefu> list = hefuService.selectHefuList(hefu);
        return getDataTable(list);
    }

    /**
     * 导出合服列表
     */
    @RequiresPermissions("gmtool:hefu:export")
    @Log(title = "合服", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Hefu hefu)
    {
        List<Hefu> list = hefuService.selectHefuList(hefu);
        ExcelUtil<Hefu> util = new ExcelUtil<Hefu>(Hefu.class);
        return util.exportExcel(list, "合服数据");
    }

    /**
     * 新增合服
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存合服
     */
    @RequiresPermissions("gmtool:hefu:add")
    @Log(title = "合服", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Hefu hefu)
    {
        String from = hefu.getFromServer();
        String[] arr = from.split(",|，");
        for(String id : arr){
            if(!NumberUtils.isNumber(id)){
                return error("源服务器id不是数字");
            }
        }
        return toAjax(hefuService.insertHefu(hefu));
    }

    /**
     * 修改合服
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Hefu hefu = hefuService.selectHefuById(id);
        mmap.put("hefu", hefu);
        return prefix + "/edit";
    }

    /**
     * 修改保存合服
     */
    @RequiresPermissions("gmtool:hefu:edit")
    @Log(title = "合服", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Hefu hefu)
    {
        return toAjax(hefuService.updateHefu(hefu));
    }

    /**
     * 删除合服
     */
    @RequiresPermissions("gmtool:hefu:remove")
    @Log(title = "合服", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hefuService.deleteHefuByIds(ids));
    }

    /**
     * 配置检测
     * @param id
     * @return
     */
    @RequiresPermissions("gmtool:hefu:start")
    @Log(title = "合服配置检测", businessType = BusinessType.OTHER)
    @PostMapping( "/check")
    @ResponseBody
    public AjaxResult check(Long id){
        try{
            boolean success = hefuService.check(id);
            return success ? success() : error();
        }catch (Exception e){
            return error(e.toString());
        }
    }

    @GetMapping("/start/{id}")
    public String start(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("id", id);
        return prefix + "/start";
    }

    /**
     * 开始合服
     * @param id
     * @return
     */
    @RequiresPermissions("gmtool:hefu:start")
    @Log(title = "合服开始", businessType = BusinessType.OTHER)
    @PostMapping( "/start")
    @ResponseBody
    public AjaxResult start(Long id){
        try{
            boolean success = hefuService.start(id);
            return success ? success() : error();
        }catch (Exception e){
            return error(e.toString());
        }
    }

    /**
     * 检测已合服的服务器
     * @param id
     * @return
     */
    @RequiresPermissions("gmtool:hefu:start")
    @PostMapping( "/checkIsHefu")
    @ResponseBody
    public AjaxResult checkIsHefu(Long id) {
        try{
            Map<Integer, Integer> ss = hefuService.checkIsHefu(id);
            if(ss.size() > 0){
                String msg = "";
                for(Map.Entry<Integer, Integer> entry : ss.entrySet()){
                    msg = msg + "服务器" + entry.getKey() + "已合并到服务器" + entry.getValue() + ",";
                }
                msg += "是否继续？";
                return new AjaxResult(AjaxResult.Type.ERROR, msg);
            }else{
                return new AjaxResult(AjaxResult.Type.SUCCESS, "");
            }
        }catch (Exception e) {
            return error(e.toString());
        }
    }

    @RequiresPermissions("gmtool:hefu:log")
    @Log(title = "合服日志", businessType = BusinessType.OTHER)
    @PostMapping( "/log")
    @ResponseBody
    public AjaxResult getLog(Long id, Integer index){
        Map<String,Object> map = hefuService.getLog(id, index);
        return AjaxResult.success(map);
    }

    @RequiresPermissions("gmtool:hefu:start")
    @Log(title = "数据库备份", businessType = BusinessType.OTHER)
    @PostMapping( "/dbbak")
    @ResponseBody
    public AjaxResult dbbak(Long id, Integer serverId, Integer type){
        try{
            if(id == null){
                return error("缺失参数");
            }
            hefuService.dbbak(id, serverId, type);
            return AjaxResult.success();
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("gmtool:hefu:start")
    @Log(title = "数据库还原", businessType = BusinessType.OTHER)
    @PostMapping( "/dbrestore")
    @ResponseBody
    public AjaxResult dbrestore(Long id, Integer serverId, Integer type){
        try{
            if(id == null){
                return error("缺失参数");
            }
            hefuService.dbrestore(id, serverId, type);
            return AjaxResult.success();
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    /**
     * 停止合服
     * @param id
     * @return
     */
    @RequiresPermissions("gmtool:hefu:stop")
    @Log(title = "停止合服", businessType = BusinessType.OTHER)
    @PostMapping( "/stop")
    @ResponseBody
    public AjaxResult stop(Long id){
        try{
            if(id == null){
                return error("缺失参数");
            }
            hefuService.stop(id);
            return AjaxResult.success();
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    @GetMapping("/bakAndRestore/{id}")
    public String bakAndRestore(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("id", id);
        return prefix + "/bakAndRestore";
    }

    @RequiresPermissions("gmtool:hefu:start")
    @Log(title = "备份列表", businessType = BusinessType.OTHER)
    @PostMapping( "/bakList")
    @ResponseBody
    public TableDataInfo bakList(Long id){
        List<Dbbak> baks = hefuService.bakList(id);
        return getDataTable(baks);
    }

    @GetMapping("/log/{id}")
    public String logRecord(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("id", id);
        return prefix + "/log";
    }

    @PostMapping( "/logRecord")
    @ResponseBody
    public Object logRecord(Long id){
        List<String> ls = hefuService.logRecord(id);
        return AjaxResult.success(ls);
    }
}
