package com.gm.project.gmtool.item.controller;

import java.util.List;
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
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.item.service.IItemService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 道具装备Controller
 * 
 * @author gm
 * @date 2021-08-31
 */
@Controller
@RequestMapping("/gmtool/item")
public class ItemController extends BaseController
{
    private String prefix = "gmtool/item";

    @Autowired
    private IItemService itemService;

    @RequiresPermissions("gmtool:item:view")
    @GetMapping()
    public String item()
    {
        return prefix + "/item";
    }

    /**
     * 查询道具装备列表
     */
//    @RequiresPermissions("gmtool:item:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Item item)
    {
        startPage();
        List<Item> list = itemService.selectItemList(item);
        return getDataTable(list);
    }

    /**
     * 导出道具装备列表
     */
    @RequiresPermissions("gmtool:item:export")
    @Log(title = "道具装备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Item item)
    {
        List<Item> list = itemService.selectItemList(item);
        ExcelUtil<Item> util = new ExcelUtil<Item>(Item.class);
        return util.exportExcel(list, "道具装备数据");
    }

    /**
     * 新增道具装备
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存道具装备
     */
    @RequiresPermissions("gmtool:item:add")
    @Log(title = "道具装备", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Item item)
    {
        return toAjax(itemService.insertItem(item));
    }

    /**
     * 修改道具装备
     */
    @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable("itemId") Integer itemId, ModelMap mmap)
    {
        Item item = itemService.selectItemById(itemId);
        mmap.put("item", item);
        return prefix + "/edit";
    }

    /**
     * 修改保存道具装备
     */
    @RequiresPermissions("gmtool:item:edit")
    @Log(title = "道具装备", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Item item)
    {
        return toAjax(itemService.updateItem(item));
    }

    /**
     * 删除道具装备
     */
    @RequiresPermissions("gmtool:item:remove")
    @Log(title = "道具装备", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(itemService.deleteItemByIds(ids));
    }
}
