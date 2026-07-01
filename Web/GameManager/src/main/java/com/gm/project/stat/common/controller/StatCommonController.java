package com.gm.project.stat.common.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.common.service.StatCommonServiceImpl;
import com.gm.project.stat.stat_remain.service.StatRemainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 留存统计Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/common")
public class StatCommonController extends BaseController
{
    private String prefix = "stat/common";

    @Autowired
    private StatCommonServiceImpl statCommonServiceImpl;


    @GetMapping()
    public String stat_remain()
    {
        return prefix + "/common";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/caclStat")
    @ResponseBody
    public TableDataInfo caclStat(String startDate,String endDate,String selectServerIdList)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        TableDataInfo tableDataInfo = null;

        return tableDataInfo;
    }

}
