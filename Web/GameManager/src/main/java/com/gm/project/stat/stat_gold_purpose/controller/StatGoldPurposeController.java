package com.gm.project.stat.stat_gold_purpose.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_gold_purpose.domain.GoldPurposeBean;
import com.gm.project.stat.stat_gold_purpose.service.IStatGoldPurposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 元宝用途统计Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/stat/stat_gold_purpose")
public class StatGoldPurposeController extends BaseController
{
    private String prefix = "stat/stat_gold_purpose";

    @Autowired
    private IStatGoldPurposeService statGoldPurposeService;
    /**
     * 跳转到付费次数统计界面
     * @return
     */
    @GetMapping("/to_stat_gold_purpose")
    public String toStatGoldPurpose()
    {
        return prefix + "/stat_gold_purpose";
    }

    @PostMapping("/stat_gold_purpose")
    @ResponseBody
    public  TableDataInfo statGoldPurpose(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack,Integer goldType){
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<GoldPurposeBean> dataList= this.statGoldPurposeService.statGoldPurpose(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack,goldType);
        return  getDataTable(dataList);
    }
 }
