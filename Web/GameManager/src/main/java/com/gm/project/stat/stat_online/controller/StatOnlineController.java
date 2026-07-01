package com.gm.project.stat.stat_online.controller;

import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_online.domain.AllOnlineCountBean;
import com.gm.project.stat.stat_online.service.IStatOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 留存统计Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/stat_online")
public class StatOnlineController extends BaseController
{
    private String prefix = "stat/stat_online";

    @Autowired
    private IStatOnlineService statOnlineService;

    @GetMapping("to_stat_all_online_count_list")
    public String to_stat_all_online_count_list()
    {
        return prefix + "/stat_all_online_count_list";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/stat_all_online_count_list")
    @ResponseBody
    public TableDataInfo stat_all_online_count_list()
    {
        List<AllOnlineCountBean> dataList =  this.statOnlineService.statAllOnlineCountList();
        return   getDataTable(dataList);
    }

}
