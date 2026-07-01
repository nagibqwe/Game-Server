package com.gm.project.gmtool.db.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.MessageUtils;
import com.gm.common.utils.text.Convert;
import com.gm.project.gmtool.utils.GMLogUtil;
import io.swagger.models.auth.In;
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
import com.gm.project.gmtool.db.domain.TDb;
import com.gm.project.gmtool.db.service.ITDbService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 游戏逻辑库列Controller
 * 
 * @author gm
 * @date 2021-09-08
 */
@Controller
@RequestMapping("/gmtool/db")
public class TDbController extends BaseController
{
    private String prefix = "gmtool/db";

    @Autowired
    private ITDbService tDbService;

    @RequiresPermissions("gmtool:db:view")
    @GetMapping()
    public String db()
    {
        return prefix + "/db";
    }

    /**
     * 查询日志库列列表
     */
    @RequiresPermissions("gmtool:db:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TDb tDb)
    {
        startPage();
        List<TDb> list = tDbService.selectTDbList(tDb);
        return getDataTable(list);
    }

    /**
     * 导出日志库列列表
     */
    @RequiresPermissions("gmtool:db:export")
    @Log(title = "日志库列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TDb tDb)
    {
        List<TDb> list = tDbService.selectTDbList(tDb);
        ExcelUtil<TDb> util = new ExcelUtil<TDb>(TDb.class);
        return util.exportExcel(list, "日志库列数据");
    }

    /**
     * 新增日志库列
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存日志库列
     */
    @RequiresPermissions("gmtool:db:add")
    @Log(title = "日志库列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TDb tDb)
    {
        int row = tDbService.insertTDb(tDb);
        if (row > 0){
            GMLogUtil.log("增加逻辑服数据库信息，Id:" + tDb.getId());
        }
        return toAjax(row);
    }

    /**
     * 修改日志库列
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        TDb tDb = tDbService.selectTDbById(id);
        mmap.put("tDb", tDb);
        return prefix + "/edit";
    }

    /**
     * 修改保存日志库列
     */
    @RequiresPermissions("gmtool:db:edit")
    @Log(title = "日志库列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TDb tDb)
    {
        int row = tDbService.updateTDb(tDb);
        if (row > 0){
            GMLogUtil.log("修改逻辑服数据库信息，Id:" + tDb.getId());
            DBServerMgr.getInstance().clearDBClient(null,tDb);
        }
        return toAjax(row);
    }

    /**
     * 删除日志库列
     */
    @RequiresPermissions("gmtool:db:remove")
    @Log(title = "日志库列", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int row = tDbService.deleteTDbByIds(ids);
        String[] idsArr = Convert.toStrArray(ids);
        for (String id:idsArr){
            TDb tDb = tDbService.selectTDbById(Integer.valueOf(id));
            DBServerMgr.getInstance().clearDBClient(null,tDb);
        }
        GMLogUtil.log("删除逻辑服数据库信息，Id:" + ids);
        return toAjax(row);
    }

    @PostMapping("/dbTest")
    @ResponseBody
    public Object dbTest(Integer id){
        TDb tDb = tDbService.selectTDbById(id);
        if (tDb == null) {
            return AjaxResult.error(MessageUtils.message("server.combine.noExist"));
        }
        DBClient dbClient = DBServerMgr.getInstance().getLogicDBClient(tDb);
        try {
            Connection connection = dbClient.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("测试失败!请检查配置或者权限");
        }
        return AjaxResult.success("测试成功!");
    }
}
