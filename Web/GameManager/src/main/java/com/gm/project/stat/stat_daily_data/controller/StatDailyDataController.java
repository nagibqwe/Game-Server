package com.gm.project.stat.stat_daily_data.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_daily_data.domain.StatDailyDataBean;
import com.gm.project.stat.stat_daily_data.service.IStatDailyDataService;
import com.gm.project.stat.stat_dau.domain.StatDauBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * dau 统计Controller
 * 
 * @author gm
 * @date 2021-08-06
 */
@Controller
@RequestMapping("/stat/stat_daily_data")
public class StatDailyDataController extends BaseController
{
    private String prefix = "stat/stat_daily_data";
    @Autowired
    private IStatDailyDataService statDailyDataService;

    @GetMapping("to_stat_daily_data")
    public String toStatDailyData()
    {
        return prefix + "/stat_daily_data";
    }

    /**
     * dau 统计
     */
    @PostMapping("/stat_daily_data")
    @ResponseBody
    public TableDataInfo statDailyData(String selectGroupName,String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack)
    {
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }

        startPage();
        List<StatDailyDataBean> dataList = this.statDailyDataService.statDailyData(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack);
        return getDataTable(dataList);
    }

}
