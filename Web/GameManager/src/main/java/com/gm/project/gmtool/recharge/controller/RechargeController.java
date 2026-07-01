package com.gm.project.gmtool.recharge.controller;

import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import com.gm.project.gmtool.recharge.domain.Recharge;
import com.gm.project.gmtool.recharge.service.IRechargeService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 后台模拟充值Controller
 * 
 * @author gm
 * @date 2021-11-28
 */
@Controller
@RequestMapping("/gmtool/recharge")
public class RechargeController extends BaseController
{
    private String prefix = "gmtool/recharge";

    @Autowired
    private IRechargeService rechargeService;

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private IGameRoleService gameRoleService;

    @RequiresPermissions("gmtool:recharge:view")
    @GetMapping()
    public String recharge()
    {
        return prefix + "/recharge";
    }

    /**
     * 查询后台模拟充值列表
     */
    @RequiresPermissions("gmtool:recharge:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Recharge recharge)
    {
        startPage();
        List<Recharge> list = rechargeService.selectRechargeList(recharge);
        return getDataTable(list);
    }

    /**
     * 导出后台模拟充值列表
     */
    @RequiresPermissions("gmtool:recharge:export")
    @Log(title = "后台模拟充值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Recharge recharge)
    {
        List<Recharge> list = rechargeService.selectRechargeList(recharge);
        ExcelUtil<Recharge> util = new ExcelUtil<Recharge>(Recharge.class);
        return util.exportExcel(list, "后台模拟充值数据");
    }

    /**
     * 新增后台模拟充值
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存后台模拟充值
     */
    @RequiresPermissions("gmtool:recharge:add")
    @Log(title = "后台模拟充值", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Recharge recharge)
    {
        //检查角色是否存在
        List<RoleState> roles = gameRoleService.queryByRoleId(recharge.getToServerId(), recharge.getRoleId(), 0);
        if (roles == null || roles.isEmpty()) {
            return AjaxResult.info("没有找到该角色信息").put("ok",false);
        }

        return toAjax(rechargeService.insertRecharge(recharge));
    }

    /**
     * 同意此次充值
     */
    @RequiresPermissions("gmtool:recharge:verify")
    @Log(title = "后台模拟充值", businessType = BusinessType.UPDATE)
    @PostMapping( "/agree")
    @ResponseBody
    public AjaxResult agree(Integer id)
    {
        Recharge recharge = rechargeService.selectRechargeById(id.longValue());
        if(recharge == null){
            return AjaxResult.info("获取充值信息失败").put("ok", false);
        }

        int serverId = recharge.getToServerId();
        TServer server = tServerService.selectTServerByServerId(recharge.getToServerId());
        if (server == null) {
            return AjaxResult.info("获取服务失败").put("ok", false);
        }

        AjaxResult resultMap = GameServerRequestUtil.gmRecharge(server, Long.parseLong(recharge.getRoleId()), recharge.getRechargeNumber(), recharge.getRechargeTotalGold(), recharge.getRechargeVipExp());
        if (Boolean.valueOf(resultMap.get("ok").toString())) {
            GMLogUtil.log("审核同意成功,serverId:" + serverId);
            recharge.setRechargeState(1);
            rechargeService.updateRecharge(recharge);
            return AjaxResult.success().put("ok", true);
        }else{
            GMLogUtil.log("审核同意失败,serverId:" + serverId);
            return AjaxResult.info("审核同意失败").put("ok", false);
        }
    }

    /**
     * 拒绝此次充值
     */
    @RequiresPermissions("gmtool:recharge:verify")
    @Log(title = "后台模拟充值", businessType = BusinessType.UPDATE)
    @PostMapping( "/reject")
    @ResponseBody
    public AjaxResult reject(Integer id)
    {
        Recharge recharge = rechargeService.selectRechargeById(id.longValue());
        if(recharge == null){
            return AjaxResult.info("获取充值信息失败").put("ok", false);
        }

        recharge.setRechargeState(2);
        rechargeService.updateRecharge(recharge);
        return AjaxResult.success().put("ok", true);
    }

    /**
     * 修改后台模拟充值
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Recharge recharge = rechargeService.selectRechargeById(id);
        mmap.put("recharge", recharge);
        return prefix + "/edit";
    }

    /**
     * 修改后台模拟充值
     */
    @RequiresPermissions("gmtool:recharge:edit")
    @Log(title = "后台模拟充值", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Recharge recharge)
    {
        //agree
        recharge.setRechargeState(1);
        return toAjax(rechargeService.updateRecharge(recharge));
    }

    /**
     * 删除后台模拟充值
     */
    @RequiresPermissions("gmtool:recharge:remove")
    @Log(title = "后台模拟充值", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        //reject
        Recharge recharge = new Recharge();
        recharge.setRechargeState(2);
        return toAjax(rechargeService.deleteRechargeByIds(ids));
    }
}
