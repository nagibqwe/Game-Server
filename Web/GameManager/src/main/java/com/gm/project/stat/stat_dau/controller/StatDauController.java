package com.gm.project.stat.stat_dau.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_dau.domain.StatDauBean;
import com.gm.project.stat.stat_dau.service.IStatDauService;
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
@RequestMapping("/stat/stat_dau")
public class StatDauController extends BaseController
{
    private String prefix = "stat/stat_dau";
    @Autowired
    private IStatDauService statDauService;

    @GetMapping("to_stat_dau")
    public String to_stat_dau()
    {
        return prefix + "/stat_dau";
    }

    /**
     * dau 统计
     */
    @PostMapping("/statDau")
    @ResponseBody
    public TableDataInfo statDau(String selectGroupName,String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack, Integer level)
    {
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        if(level == null){
            level = 0;
        }
        startPage();
        List<StatDauBean> dataList = this.statDauService.statDau(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack,level);
        return getDataTable(dataList);
    }

}
