package com.gm.project.gmtool.gamerole.controller;

import com.github.pagehelper.PageHelper;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gamelog.goldchangelog.domain.Goldchangelog;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gamelog.rolestate.service.IRoleStateService;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * 角色快照日志Controller
 *
 * @author gm
 * @date 2021-09-07
 */
@Controller
@RequestMapping("/gmtool/gamerole")
public class GameRoleController extends BaseController {
    private String prefix = "gmtool/gamerole";

    @Resource
    private IGameRoleService gameRoleService;

    @Autowired
    private IRoleStateService roleStateService;

    @RequiresPermissions("gmtool:gamerole:view")
    @GetMapping()
    public String gamerole() {
        return prefix + "/gamerole";
    }

    /**
     * 查询角色快照日志列表
     */
    @RequiresPermissions("gmtool:gamerole:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Integer serverId, Integer queryType, String queryParam) {
        if (serverId == null || serverId <= 0) {
            return getDataTableErrorMsg("请选择服务器列表");
        }

        startPage();

        List<RoleState> result = null;
        //根据不同的条件查询数据
        switch (queryType){
            case 1://角色名
                result = gameRoleService.queryByRoleName(serverId, queryParam);
                break;
            case 2://平台帐号
                result = gameRoleService.queryByPlatFormAccount(serverId, queryParam);
                break;
            case 3://平台帐号ID
                result = gameRoleService.queryByPlatFormUid(serverId, queryParam);
                break;
            case 4://游戏用户ID
                result = gameRoleService.queryByUserId(serverId, queryParam);
                break;
            case 5://角色ID(10进制)
                result = gameRoleService.queryByRoleId(serverId, queryParam, 0);
                break;
            case 6://角色ID(36进制)
                result = gameRoleService.queryByRoleId(serverId, queryParam, 1);
                break;
            default:
                break;
        }

        if (result == null){
            result = new ArrayList<>();
            return getDataTable(result);
        }

        return getDataTable(result);
    }

    /**
     * 导出角色快照日志列表
     */
    @RequiresPermissions("gmtool:gamerole:export")
    @Log(title = "角色快照日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RoleState roleState, Integer selectServerId) {
//        List<RoleState> list = roleStateService.selectRoleStateList(roleState, selectServerId);
//        ExcelUtil<RoleState> util = new ExcelUtil<RoleState>(RoleState.class);
//        return util.exportExcel(list, "角色快照日志数据");
        return null;
    }

    /**
     * 检查用户ID是否存在
     */
    @RequiresPermissions("gmtool:gamerole:checkUserId")
    @Log(title = "检查用户ID是否存在", businessType = BusinessType.EXPORT)
    @PostMapping("/checkUserId")
    @ResponseBody
    public AjaxResult checkUserId(String userId) {

        String sqlStr = "select count(*) as num from userlogin where `userId` in(" + userId + ");";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        Map<String, Object> resultMap = loginDao.selectOne(sqlStr);
        if (null != resultMap && resultMap.size() > 0 && resultMap.get("num")!=null && (long)resultMap.get("num")>0){
            return AjaxResult.success("用户ID存在").put("ok", true);
        }

        return AjaxResult.error("用户ID不存在").put("ok", false);
    }

    /**
     * 查询用户登录信息
     */
    @RequiresPermissions("gmtool:gamerole:userLogin")
    @PostMapping("/userLogin")
    @ResponseBody
    public TableDataInfo userLogin(String startDate, String endDate) {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }

        Map<String,Object> param = new HashMap<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)){
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }

//        startPage();
        //时间通用 判断
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(DateUtils.parseDate(startDate));
        end.setTime(DateUtils.parseDate(endDate));
        long startTime = start.getTimeInMillis()/1000;
        long endTime = end.getTimeInMillis()/1000;

        String sqlStr = "select * from userlogin where createTime between "+startTime+" and "+endTime+" order by createTime desc ";
        String sqlCountStr = "select count(*) as num from userlogin where createTime between "+startTime+" and "+endTime;

        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        Map<String, Object> countMap = loginDao.selectOne(sqlCountStr);
        if (!(null != countMap && countMap.size() > 0 && (long)countMap.get("num")>0)){
            return getDataTableErrorMsg("没有找到用户数据");
        }

        //分页数据
        if(param.containsKey("pageNum")){
            sqlStr+=" limit "+(pageNum-1)*pageSize+","+pageSize;
        }

        TableDataInfo tableDataInfo = getDataTable(new ArrayList<>());
        tableDataInfo.setTotal((long)countMap.get("num"));

        List<HashMap<String, Object>> resultList = loginDao.selectList(sqlStr);
        if (null != resultList && resultList.size() > 0){
            tableDataInfo.setRows(resultList);

            return tableDataInfo;
        }
        return getDataTableErrorMsg("没有找到用户数据");
    }
}
