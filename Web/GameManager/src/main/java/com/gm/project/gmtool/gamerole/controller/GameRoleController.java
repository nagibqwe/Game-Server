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
 * Журнал снимков персонажейController
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
     * 查询Журнал снимков персонажей列表
     */
    @RequiresPermissions("gmtool:gamerole:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Integer serverId, Integer queryType, String queryParam) {
        if (serverId == null || serverId <= 0) {
            return getDataTableErrorMsg("Выберите сервер из списка");
        }

        startPage();

        List<RoleState> result = null;
        //根据不同的条件查询Данные
        switch (queryType){
            case 1://Имя персонажа
                result = gameRoleService.queryByRoleName(serverId, queryParam);
                break;
            case 2://Платформа帐号
                result = gameRoleService.queryByPlatFormAccount(serverId, queryParam);
                break;
            case 3://Платформа帐号ID
                result = gameRoleService.queryByPlatFormUid(serverId, queryParam);
                break;
            case 4://游戏ID пользователя
                result = gameRoleService.queryByUserId(serverId, queryParam);
                break;
            case 5://ID персонажа(10进制)
                result = gameRoleService.queryByRoleId(serverId, queryParam, 0);
                break;
            case 6://ID персонажа(36进制)
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
     * ЭкспортЖурнал снимков персонажей列表
     */
    @RequiresPermissions("gmtool:gamerole:export")
    @Log(title = "Журнал снимков персонажей", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RoleState roleState, Integer selectServerId) {
//        List<RoleState> list = roleStateService.selectRoleStateList(roleState, selectServerId);
//        ExcelUtil<RoleState> util = new ExcelUtil<RoleState>(RoleState.class);
//        return util.exportExcel(list, "Журнал снимков персонажейДанные");
        return null;
    }

    /**
     * 检查ID пользователяДаНет存在
     */
    @RequiresPermissions("gmtool:gamerole:checkUserId")
    @Log(title = "检查ID пользователяДаНет存在", businessType = BusinessType.EXPORT)
    @PostMapping("/checkUserId")
    @ResponseBody
    public AjaxResult checkUserId(String userId) {

        String sqlStr = "select count(*) as num from userlogin where `userId` in(" + userId + ");";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        Map<String, Object> resultMap = loginDao.selectOne(sqlStr);
        if (null != resultMap && resultMap.size() > 0 && resultMap.get("num")!=null && (long)resultMap.get("num")>0){
            return AjaxResult.success("ID пользователя存在").put("ok", true);
        }

        return AjaxResult.error("ID пользователя不存在").put("ok", false);
    }

    /**
     * 查询用户登录Информация
     */
    @RequiresPermissions("gmtool:gamerole:userLogin")
    @PostMapping("/userLogin")
    @ResponseBody
    public TableDataInfo userLogin(String startDate, String endDate) {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("Укажите время начала и окончания");
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
        //Время通用 判断
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
            return getDataTableErrorMsg("没有找到用户Данные");
        }

        //分页Данные
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
        return getDataTableErrorMsg("没有找到用户Данные");
    }
}
