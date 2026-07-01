package com.gm.project.gmquerydata.rankinglist.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gm.common.utils.JsonMapper;
import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmquerydata.rankinglist.domain.RankingListBean;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * 排行榜Controller
 * 
 * @author gm
 * @date 2021-09-10
 */
@Controller
@RequestMapping("/gmquerydata/rankinglist")
public class RankingListController extends BaseController
{
    private String prefix = "gmquerydata/rankinglist";

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmquerydata:rankinglist:view")
    @GetMapping()
    public String rankinglist()
    {
        return prefix + "/rankinglist";
    }
    /**
     * 查询后台指令日志列表
     */
    @RequiresPermissions("gmquerydata:rankinglist:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(int serverId, int rankType)
    {
        startPage();
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return getDataTableErrorMsg("选择服务器Id不存在");
        }
        List<RankingListBean> dataList = new ArrayList<>();
        AjaxResult ajaxResult = GameServerRequestUtil.gmQueryRankList(server,rankType);
        if(!ajaxResult.getBoolean("ok"))
        {
            return getDataTableErrorMsg(serverId+"-->排行榜获取失败！");
        }
        HashMap<String,Object> data = (HashMap<String,Object> )ajaxResult.get("data");
        if(data.containsKey("data")){
            if(StringUtils.isBlank(data.get("data").toString())){
                String msg = data.get("msg").toString();
                return getDataTableErrorMsg(serverId+"-->"+msg+"");
            }
        }
        List<LinkedHashMap<String,Object>> list= JsonMapper.parseObject(data.get("data").toString(), new TypeReference<List<LinkedHashMap<String,Object>>>(){});
        for (LinkedHashMap<String,Object> linkedHashMap:list){
            RankingListBean grid = new RankingListBean();
            for(Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
                if (entry.getKey().equals("rank")){
                    grid.setRank(String.valueOf(entry.getValue()));
                }else if (entry.getKey().equals("roleId")){
                    grid.setRoleId(String.valueOf(entry.getValue()));
                }else if (entry.getKey().equals("roleName")){
                    grid.setRoleName(String.valueOf(entry.getValue()));
                }else if (entry.getKey().equals("rankData")){
                    //等级排行榜特殊处理
                    if(rankType == 101){
                        int level =  Integer.parseInt(String.valueOf(entry.getValue()));
                        if(level>360){
                            grid.setRankData("飞升"+(level-360));
                        }else {
                            grid.setRankData(""+level);
                        }
                    }else {
                        grid.setRankData(String.valueOf(entry.getValue()));
                    }
                }
            }
            dataList.add(grid);
        }
        return getDataTable(dataList);
    }

}
