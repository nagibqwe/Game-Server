package com.gm.project.stat.stat_role.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.stat.stat_role.domain.RoleInfoBean;
import com.gm.project.stat.stat_role.service.IStatRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * 角色信息统计Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/stat_role")
public class StatRoleController extends BaseController
{
    private String prefix = "stat/stat_role";

    @Autowired
    private IStatRoleService statRoleService;

    @GetMapping("to_stat_role")
    public String to_stat_role()
    {
        return prefix + "/stat_role";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/stat_role")
    @ResponseBody
    public TableDataInfo stat_role(String groupName, Integer serverId, String channelNames, String sortType)
    {
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器");
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<RoleInfoBean> list = new ArrayList<>();
        TableDataInfo tableDataInfo = getDataTable(list);

        this.statRoleService.stat_role(tableDataInfo,groupName,serverId,channelNames,sortType,pageNum,pageSize);
        return  tableDataInfo;
    }

}
