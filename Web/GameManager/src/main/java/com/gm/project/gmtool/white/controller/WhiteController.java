package com.gm.project.gmtool.white.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.framework.config.GameManagerConfig;
import com.gm.project.gmtool.banAccount.domain.BanAccount;
import com.gm.project.gmtool.banChat.domain.BanChat;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.HttpConnectionUtils;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.white.domain.White;
import com.gm.project.gmtool.white.service.IWhiteService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 白名单管理Controller
 * 
 * @author gm
 * @date 2021-11-22
 */
@Controller
@RequestMapping("/gmtool/white")
public class WhiteController extends BaseController
{
    private String prefix = "gmtool/white";

    @Autowired
    private IWhiteService whiteService;

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    @RequiresPermissions("gmtool:white:view")
    @GetMapping()
    public String white()
    {
        return prefix + "/white";
    }

    /**
     * 设置白名单
     */
    //    @RequiresPermissions("gmtool:white:addWhite")
    @PostMapping("/addWhite")
    @ResponseBody
    public Object addWhite(String con) {
        if (StringUtils.isEmpty(con)) {
            return AjaxResult.error("param error").put("ok",false);
        }

        //检查白名单是否已经设置
        White white = new White();
        white.setCon(con);
        List<White> whites = whiteService.selectWhiteList(white);
        if (whites != null && !whites.isEmpty()) {
            return AjaxResult.error("该白名单已经设置,无需再设置").put("ok", false);
        }

        //发送到登录服
        TServer sc = new TServer();
        sc.setServerType(2);
        List<TServer> lsList = tServerService.selectTServerList(sc);
        StringBuilder sb = new StringBuilder();
        for (TServer ls:lsList) {
            String url = "http://" + ls.getServerIP() + ":" + ls.getServerPort() + "/whiteadd";

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
            paramMap.put("whiteStr", con);

            String res = HttpConnectionUtils.get(url, null, paramMap);
            sb.append(res).append("\n");
        }


        white.setCon(con);
        white.setState(0);
        whiteService.insertWhite(white);

        return AjaxResult.success("添加白名单完成").put("ok", true);
    }

    /**
     * 改变白名单状态
     */
    //    @RequiresPermissions("gmtool:white:changeWhite")
    @PostMapping("/changeWhite")
    @ResponseBody
    public Object changeWhite(Integer id) {
        White white = whiteService.selectWhiteById(id.longValue());
        if (white == null) {
            return AjaxResult.error("白名单信息未找到").put("ok", false);
        }

        //发送到登录服
        TServer sc = new TServer();
        sc.setServerType(2);
        List<TServer> lsList = tServerService.selectTServerList(sc);
        StringBuilder sb = new StringBuilder();
        for (TServer ls:lsList) {
            String url = "http://" + ls.getServerIP() + ":" + ls.getServerPort() + (white.getState()==0?"/whitecancel":"/whiteadd");

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
            paramMap.put("whiteStr", white.getCon());

            String res = HttpConnectionUtils.get(url, null, paramMap);
            sb.append(res).append("\n");
        }

//        //操作登录服数据库数据
//        String sqlStr = "select count(*) as num from white where `str`= \"" + white.getCon() + "\";";
//        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
//        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
//        if (null != resultMap && resultMap.size() > 0 && resultMap.get(0).get("num")!=null && (long)resultMap.get(0).get("num")>0){
//            sqlStr = "delete from white where str = \"" + white.getCon() + "\"";
//            try {
//                loginDao.executeUpdate(sqlStr);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

        white.setState(white.getState()==0?1:0);
        whiteService.updateWhite(white);

        return AjaxResult.success("改变白名单状态完成").put("ok", true);
    }

    /**
     * 查询白名单管理列表
     */
    @RequiresPermissions("gmtool:white:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(White white)
    {
        startPage();
        List<White> list = whiteService.selectWhiteList(white);
        return getDataTable(list);
    }

    /**
     * 导出白名单管理列表
     */
    @RequiresPermissions("gmtool:white:export")
    @Log(title = "白名单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(White white)
    {
        List<White> list = whiteService.selectWhiteList(white);
        ExcelUtil<White> util = new ExcelUtil<White>(White.class);
        return util.exportExcel(list, "白名单管理数据");
    }

    /**
     * 新增白名单管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存白名单管理
     */
    @RequiresPermissions("gmtool:white:add")
    @Log(title = "白名单管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(White white)
    {
        return toAjax(whiteService.insertWhite(white));
    }

    /**
     * 修改白名单管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        White white = whiteService.selectWhiteById(id);
        mmap.put("white", white);
        return prefix + "/edit";
    }

    /**
     * 修改保存白名单管理
     */
    @RequiresPermissions("gmtool:white:edit")
    @Log(title = "白名单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(White white)
    {
        return toAjax(whiteService.updateWhite(white));
    }

    /**
     * 删除白名单管理
     */
    @RequiresPermissions("gmtool:white:remove")
    @Log(title = "白名单管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(whiteService.deleteWhiteByIds(ids));
    }
}
