package com.gm.project.stat.stat_recharge_distribute.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gm.project.stat.stat_recharge_distribute.service.IStatRechargeDistributeService;
import com.gm.framework.web.controller.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 充值统计Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/stat/stat_recharge_distribute")
public class StatRechargeDistributeController extends BaseController
{
    private String prefix = "stat/stat_recharge_distribute";

    @Autowired
    private IStatRechargeDistributeService statPayService;

    /**
     * 跳转到充值统计界面
     * @return
     */
    @GetMapping()
    public String to_stat_recharge_distribute()
    {
        return prefix + "/stat_recharge_distribute";
    }

    /**
     *
     * 充值档位分布统计
     * @param channelNames
     * @param selectServerIdList
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("/payLevelStat")
    @ResponseBody
    public TableDataInfo payLevelStat(String selectServerIdList,String channelNames, String startDate, String endDate)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<Map<String, Object>> list = this.statPayService.payLevelStat(selectServerIdList,channelNames,startDate,endDate);
        return  getDataTable(list);
    }

    /**
     * 每日充值金额统计
     * @param channelNames
     * @param startDate
     * @return
     */
    @PostMapping("/payDaylStat")
    @ResponseBody
    public TableDataInfo payDaylStat(String selectServerIdList,String channelNames, String startDate)
    {
        if(StringUtils.isEmpty(startDate)){
            return getDataTableErrorMsg("请选择时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<Map<String, Object>> list = this.statPayService.payDaylStat(selectServerIdList,channelNames,startDate);
        return  getDataTable(list);
    }


    /**
     * 礼包次数统计
     * @param channelNames
     * @param startDate
     * @return
     */
    @PostMapping("/payGoodIdslStat")
    @ResponseBody
    public TableDataInfo payGoodIdslStat(String selectServerIdList,String channelNames, String startDate, String endDate)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<Map<String, Object>> list = this.statPayService.payGoodIdslStat(selectServerIdList,channelNames,startDate,endDate);
        return  getDataTable(list);
    }


 }
