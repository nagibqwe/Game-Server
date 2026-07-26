package com.gm.project.gmtool.roleTransfer.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import com.gm.project.gmtool.roleTransfer.domain.RoleTransfer;
import com.gm.project.gmtool.roleTransfer.service.IRoleTransferService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * Перенос персонажаController
 * 
 * @author gm
 * @date 2021-11-03
 */
@Controller
@RequestMapping("/gmtool/roleTransfer")
public class RoleTransferController extends BaseController
{
    private String prefix = "gmtool/roleTransfer";

    @Autowired
    private IRoleTransferService roleTransferService;
    @Autowired
    private ITServerService tServerService;
    @Autowired
    private IGameRoleService gameRoleService;

    @RequiresPermissions("gmtool:roleTransfer:view")
    @GetMapping()
    public String roleTransfer()
    {
        return prefix + "/roleTransfer";
    }

    /**
     * 查询Перенос персонажа列表
     */
//    @RequiresPermissions("gmtool:roleTransfer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(int serverId)
    {
        startPage();
        RoleTransfer roleTransfer = new RoleTransfer();
        roleTransfer.setServerId(serverId);
        List<RoleTransfer> list = roleTransferService.selectRoleTransferList(roleTransfer);
        return getDataTable(list);
    }

    /**
     * ЭкспортПеренос персонажа列表
     */
    @RequiresPermissions("gmtool:roleTransfer:export")
    @Log(title = "Перенос персонажа", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RoleTransfer roleTransfer)
    {
        List<RoleTransfer> list = roleTransferService.selectRoleTransferList(roleTransfer);
        ExcelUtil<RoleTransfer> util = new ExcelUtil<RoleTransfer>(RoleTransfer.class);
        return util.exportExcel(list, "Перенос персонажаДанные");
    }

    /**
     * ДобавитьПеренос персонажа
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьПеренос персонажа
     */
    @RequiresPermissions("gmtool:roleTransfer:add")
    @Log(title = "Перенос персонажа", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RoleTransfer roleTransfer)
    {
        return toAjax(roleTransferService.insertRoleTransfer(roleTransfer));
    }

    /**
     * ИзменитьПеренос персонажа
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        RoleTransfer roleTransfer = roleTransferService.selectRoleTransferById(roleId);
        mmap.put("roleTransfer", roleTransfer);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьПеренос персонажа
     */
    @RequiresPermissions("gmtool:roleTransfer:edit")
    @Log(title = "Перенос персонажа", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RoleTransfer roleTransfer)
    {
        return toAjax(roleTransferService.updateRoleTransfer(roleTransfer));
    }

    /**
     * УдалитьПеренос персонажа
     */
    @RequiresPermissions("gmtool:roleTransfer:remove")
    @Log(title = "Перенос персонажа", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(roleTransferService.deleteRoleTransferByIds(ids));
    }


    /**
     * Перенос персонажаДействия
     * @param serverId
     * @param roleId
     * @param userId
     * @param reason
     * @param request
     * @return
     */
    @PostMapping( "/transfer")
    @ResponseBody
    public AjaxResult transfer(int serverId, String roleId, String userId, String reason, HttpServletRequest request) {
        if (serverId <= 0) {
            return AjaxResult.info("Игровой сервер错误").put("ok",false);
        }

        if (StringUtils.isBlank(roleId)) {
            return AjaxResult.info("ID персонажа为空").put("ok",false);
        }

        if (StringUtils.isBlank(userId)) {
            return AjaxResult.info("转移帐号ID为空").put("ok",false);
        }

        if (StringUtils.isBlank(reason)) {
            return AjaxResult.info("转移原因为空").put("ok",false);
        }

        TServer server = tServerService.selectTServerByServerId(serverId);
        if (server == null) {
            return AjaxResult.info("Не удалось получить подключение к БД сервера").put("ok",false);
        }

        //检查ID персонажа
        List<RoleState> roles = gameRoleService.queryByRoleId(serverId, roleId, 0);
        if (roles.isEmpty()) {
            return AjaxResult.info("Персонаж с таким ID не найден").put("ok",false);
        }
        if (roles.get(0).getIsDelete() != 0) {
            return AjaxResult.info("此角色已Удалить").put("ok",false);
        }

        //发送消息到GameServer
        TServer tserver = tServerService.selectTServerByServerId(serverId);
        if (tserver == null) {
            return AjaxResult.info("Не удалось получить данные подключения к серверу").put("ok",false);
        }

        AjaxResult result = GameServerRequestUtil.gmTranRole(tserver, roleId, userId);
        if (Boolean.valueOf(result.get("ok").toString())) {
            RoleTransfer roleTransfer = new RoleTransfer();
            roleTransfer.setRoleId(roleId);
            roleTransfer.setSrcUserId(String.valueOf(roles.get(0).getUserId()));
            roleTransfer.setTargetUserId(userId);
            roleTransfer.setServerId(serverId);
            roleTransfer.setReason(reason);
            roleTransfer.setIsDeleted(0);
            roleTransfer.setTime(new Date());
            roleTransferService.insertRoleTransfer(roleTransfer);
            GMLogUtil.log("Перенос персонажаУспешно,serverId:" + serverId + "roleId:" + roleId + ">>>userId:" + userId + ",原因:" + reason);
        }
        return result;
    }
}
