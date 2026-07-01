package com.gm.project.gmquerydata.rechargerank.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmquerydata.rechargerank.domain.RechargeRankBean;
import com.gm.project.gmquerydata.rechargerank.service.IRechargeRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 充值充值 排行榜 Controller
 * 
 * @author gm
 * @date 2021-08-06
 */
@Controller
@RequestMapping("/gmquerydata/rechargerank")
public class RechargeRankController extends BaseController
{
    private String prefix = "gmquerydata/rechargerank";
    @Autowired
    private IRechargeRankService rechargeRankService;

    @GetMapping("to_rechargerank")
    public String toStatDailyData()
    {
        return prefix + "/rechargerank";
    }

    /**
     * 充值排行榜统计
     */
    @PostMapping("/rechargerank")
    @ResponseBody
    public TableDataInfo rechargeRank(String groupName,String selectServerIdList, String channelNames, String startDate, String endDate, Integer type, Boolean isStatTestOrder) throws ParseException {
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }

//        startPage();
        Map<String,Object> param = new HashMap<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
        }

        param.put("isStatTestOrder",isStatTestOrder);

        TableDataInfo tableDataInfo = getDataTable(new ArrayList<>());
        List<RechargeRankBean> dataList = this.rechargeRankService.rechargeRank(groupName,selectServerIdList,channelNames,startDate,endDate,type,param);
        if(param.containsKey("count")){
            tableDataInfo.setTotal((int)param.get("count"));
        }
        tableDataInfo.setRows(dataList);

        return tableDataInfo;
    }

}
