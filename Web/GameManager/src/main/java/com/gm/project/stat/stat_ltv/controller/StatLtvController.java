package com.gm.project.stat.stat_ltv.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_ltv.service.impl.StatLtvServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gm.framework.web.controller.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller
 * 
 * @author gm
 * @date 2021-04-27
 */
@Controller
@RequestMapping("/stat/stat_ltv")
public class StatLtvController extends BaseController
{
    private String prefix = "stat/stat_ltv";
    @Autowired
    private StatLtvServiceImpl statLtvServiceImpl;
    @GetMapping()
    public String stat_ltv()
    {
        return prefix + "/stat_ltv";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String selectGroupName, String selectServerIdList,String channelNames,String startDate, String endDate,Boolean isBlack) {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        TableDataInfo tableDataInfo = null;
        //按照用户计算留存

        tableDataInfo =  statLtvServiceImpl.caclLtv(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack);

        return tableDataInfo;
    }
}
