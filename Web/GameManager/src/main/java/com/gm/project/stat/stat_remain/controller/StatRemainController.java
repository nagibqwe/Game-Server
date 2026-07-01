package com.gm.project.stat.stat_remain.controller;

import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_remain.service.StatRemainServiceImpl;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 留存统计Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/stat_remain")
public class StatRemainController extends BaseController
{
    private String prefix = "stat/stat_remain";

    @Autowired
    private StatRemainServiceImpl statRemainService;

    @GetMapping()
    public String stat_remain()
    {
        return prefix + "/stat_remain";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack,String remainType)
    {
    	  System.out.println("f");
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        TableDataInfo tableDataInfo = null;
        //按照用户计算留存
      
        tableDataInfo =  statRemainService.caclRemain(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack,remainType);

        return tableDataInfo;
    }

}
