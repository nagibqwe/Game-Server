package com.gm.project.stat.stat_last_insert.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.backgmcmdlog.domain.Backgmcmdlog;
import com.gm.project.stat.stat_last_insert.domain.StatLastInsertBean;
import com.gm.project.stat.stat_last_insert.service.IStatLastInsertService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 日志收集记录Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/stat/stat_last_insert")
public class StatLastInsertController extends BaseController
{
    private String prefix = "stat/stat_last_insert";

    @Autowired
    private IStatLastInsertService statLastInsertService;
    /**
     *
     * @return
     */
    @GetMapping("/to_stat_last_insert")
    public String toStatLastInsert()
    {
        return prefix + "/stat_last_insert";
    }

    @PostMapping("/list")
    @ResponseBody
    public  TableDataInfo list(StatLastInsertBean statLastInsertBean, Integer serverId){
        startPage();
        List<StatLastInsertBean> dataList=  this.statLastInsertService.selectAllStatLastInsert(serverId);
        return  getDataTable(dataList);
    }

    /**
     * 删除
     */
    @Log(title = "日志拉取记录复位", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(statLastInsertService.deleteStatLastInsertByIds(ids));
    }


 }
