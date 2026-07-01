package com.gm.project.stat.stat_recharge_accumulate.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_recharge_accumulate.domain.RechargeAccumulateBean;
import com.gm.project.stat.stat_recharge_accumulate.service.IStatRechargeAccumulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 充值累计统计Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/stat/stat_recharge_accumulate")
public class StatRechargeAccumulateController extends BaseController
{
    private String prefix = "stat/stat_recharge_accumulate";
    @Autowired
    private IStatRechargeAccumulateService statRechargeAccumulateService;
    /**
     * 跳转到充值统计界面
     * @return
     */
    @GetMapping("/to_stat_recharge_accumulate")
    public String toStatAccumulatePay()
    {
        return prefix + "/stat_recharge_accumulate";
    }
    /**
     * 累计充值统计
     * @param channelNames
     * @param selectServerIdList
     * @param startDate
     * @return
     */
    @PostMapping("/stat_recharge_accumulate")
    @ResponseBody
    public  TableDataInfo statRechargeAccumulate(String selectGroupName, String selectServerIdList,String channelNames,String startDate, String endDate,Boolean isBlack){
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<RechargeAccumulateBean> list = this.statRechargeAccumulateService.statRechargeAccumulate(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack);
        return  getDataTable(list);
    }

 }
