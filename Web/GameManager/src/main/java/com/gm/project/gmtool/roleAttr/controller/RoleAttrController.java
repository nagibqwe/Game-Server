package com.gm.project.gmtool.roleAttr.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.system.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;
import com.gm.project.gmtool.roleAttr.service.IRoleAttrService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * 修改属性Controller
 * 
 * @author gm
 * @date 2021-11-02
 */
@Controller
@RequestMapping("/gmtool/roleAttr")
public class RoleAttrController extends BaseController
{
    private String prefix = "gmtool/roleAttr";
    private static Logger log = LoggerFactory.getLogger(RoleAttrController.class);

    @Autowired
    private IRoleAttrService roleAttrService;
    @Autowired
    private ITServerService tServerService;
    @Autowired
    private IGameRoleService gameRoleService;

    @RequiresPermissions("gmtool:roleAttr:view")
    @GetMapping()
    public String roleAttr()
    {
        return prefix + "/roleAttr";
    }

    /**
     * 查询修改属性列表
     */
//    @RequiresPermissions("gmtool:roleAttr:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(int serverId)
    {
        RoleAttr roleAttr = new RoleAttr();
        roleAttr.setServerId(serverId);
        startPage();
        List<RoleAttr> list = roleAttrService.selectRoleAttrList(roleAttr);
        return getDataTable(list);
    }

    /**
     * 导出修改属性列表
     */
    @RequiresPermissions("gmtool:roleAttr:export")
    @Log(title = "修改属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RoleAttr roleAttr)
    {
        List<RoleAttr> list = roleAttrService.selectRoleAttrList(roleAttr);
        ExcelUtil<RoleAttr> util = new ExcelUtil<RoleAttr>(RoleAttr.class);
        return util.exportExcel(list, "修改属性数据");
    }

    /**
     * 新增修改属性
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存修改属性
     */
    @RequiresPermissions("gmtool:roleAttr:add")
    @Log(title = "修改属性", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RoleAttr roleAttr)
    {
        return toAjax(roleAttrService.insertRoleAttr(roleAttr));
    }

    /**
     * 修改修改属性
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        RoleAttr roleAttr = roleAttrService.selectRoleAttrById(id);
        mmap.put("roleAttr", roleAttr);
        return prefix + "/edit";
    }

    /**
     * 修改保存修改属性
     */
    @RequiresPermissions("gmtool:roleAttr:edit")
    @Log(title = "修改属性", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RoleAttr roleAttr)
    {
        return toAjax(roleAttrService.updateRoleAttr(roleAttr));
    }

    /**
     * 删除修改属性
     */
    @RequiresPermissions("gmtool:roleAttr:remove")
    @Log(title = "修改属性", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(roleAttrService.deleteRoleAttrByIds(ids));
    }


    /**
     * 设置玩家属性操作
     * @param roleAttr
     * @param serverId
     * @param request
     * @return
     */
    @PostMapping( "/setRoleAttrByRoleId")
    @ResponseBody
    public Object setRoleAttrByRoleId(RoleAttr roleAttr, String serverId, HttpServletRequest request) {
        User user = ShiroUtils.getSysUser();
        int sid = DBServerMgr.getInstance().getHeFuId(roleAttr.getServerId());

        TServer server = tServerService.selectTServerByServerId(sid);
        if (server == null) {
            return AjaxResult.info("服务器DB连接信息获取失败").put("ok",false);
        }

        //检查角色ID
        List<RoleState> roles = gameRoleService.queryByRoleId(sid, roleAttr.getRoleId(), 0);
        if (roles.isEmpty()) {
            return AjaxResult.info("没有找到此角色ID").put("ok",false);
        }
        if (roles.get(0).getIsDelete() != 0) {
            return AjaxResult.info("此角色已删除").put("ok",false);
        }
        try {
            roleAttr.setActionTime(new Date());
            roleAttr.setActionUser(user.getLoginName());
            roleAttr.setIsDelete(0);
            //发送消息到GameServer
            TServer tserver = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
            if (tserver == null) {
                return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
            }
            AjaxResult resultMap = GameServerRequestUtil.gmSetRoleAttrOpt(server, roleAttr);
            HashMap dataMap = (HashMap) resultMap.get("data");
            //得到实际扣除的数量
            int realValue = Integer.parseInt(dataMap.get("data").toString());
            roleAttr.setRealValue(realValue);
            String prompt;
            if (Boolean.valueOf(resultMap.get("ok").toString())) {
                prompt = "操作成功！";
                roleAttrService.insertRoleAttr(roleAttr);
            } else {
                prompt = "操作失败！";
            }
            log.error("属性设置：sid=" + serverId + ",roleId=" + roleAttr.getRoleId() + ",操作结果:" + resultMap.get("msg").toString());
            GMLogUtil.log("设置角色(roleId=" + roleAttr.getRoleId() + ")的类型"+roleAttr.getAttrType()+"属性为："+ roleAttr.getRealValue());
            return AjaxResult.info(prompt).put("ok",Boolean.valueOf(resultMap.get("ok").toString()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.info("操作失败").put("ok",false);
        }
    }

    /**
     * 删除操作
     * @param id
     * @return
     */
    @PostMapping( "/delRoleAttr")
    @ResponseBody
    public Object delRoleAttr(int id) {
        RoleAttr roleAttr = roleAttrService.selectRoleAttrById(id);
        if (roleAttr != null) {
            roleAttr.setIsDelete(1);
            int num = roleAttrService.updateRoleAttr(roleAttr);
            GMLogUtil.log("删除设置玩家属性记录"+roleAttr.getId());
            return AjaxResult.info("删除成功！").put("ok",num == 1);
        }
        return AjaxResult.info("删除失败！").put("ok",false);
    }
}
