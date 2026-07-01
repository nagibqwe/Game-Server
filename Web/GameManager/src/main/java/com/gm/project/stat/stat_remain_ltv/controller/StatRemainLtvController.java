package com.gm.project.stat.stat_remain_ltv.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_ltv.service.impl.StatLtvServiceImpl;
import com.gm.project.stat.stat_remain_ltv.service.StatRemainLtvServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 留存LTV多维度分析Controller
 */
@Controller
@RequestMapping("/stat/stat_remain_ltv")
public class StatRemainLtvController extends BaseController {
    private String prefix = "stat/stat_remain_ltv";

//    @Autowired
//    private StatRemainServiceImpl statRemainService;
    @Autowired
    private StatRemainLtvServiceImpl statRemainLtvService;

    @GetMapping()
    public String stat_remain()
    {
        return prefix + "/stat_remain_ltv";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String startDate, String endDate, String selectServerIdList, String remainType)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        TableDataInfo tableDataInfo = null;
        //按照用户计算留存

        tableDataInfo =  statRemainLtvService.caclRemainLtv(startDate,endDate,selectServerIdList,"new_user_remain","new_user_pay_remain");

        return tableDataInfo;
    }

}
