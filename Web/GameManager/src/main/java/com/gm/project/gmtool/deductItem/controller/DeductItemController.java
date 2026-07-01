package com.gm.project.gmtool.deductItem.controller;

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
import com.gm.project.gmtool.deductItem.domain.DeductItem;
import com.gm.project.gmtool.deductItem.service.IDeductItemService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * 道具扣除Controller
 * 
 * @author gm
 * @date 2021-10-30
 */
@Controller
@RequestMapping("/gmtool/deductItem")
public class DeductItemController extends BaseController
{
    private String prefix = "gmtool/deductItem";
    private static Logger log = LoggerFactory.getLogger(DeductItemController.class);

    @Autowired
    private IDeductItemService deductItemService;
    @Autowired
    private ITServerService tServerService;
    @Autowired
    private IGameRoleService gameRoleService;

    @RequiresPermissions("gmtool:deductItem:view")
    @GetMapping()
    public String deductItem()
    {
        return prefix + "/deductItem";
    }

    /**
     * 查询道具扣除列表
     */
//    @RequiresPermissions("gmtool:deductItem:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(int serverId)
    {
        DeductItem deductItem = new DeductItem();
        deductItem.setServerId(serverId);
        startPage();
        List<DeductItem> list = deductItemService.selectDeductItemList(deductItem);
        return getDataTable(list);
    }

    /**
     * 导出道具扣除列表
     */
    @RequiresPermissions("gmtool:deductItem:export")
    @Log(title = "道具扣除", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DeductItem deductItem)
    {
        List<DeductItem> list = deductItemService.selectDeductItemList(deductItem);
        ExcelUtil<DeductItem> util = new ExcelUtil<DeductItem>(DeductItem.class);
        return util.exportExcel(list, "道具扣除数据");
    }

    /**
     * 新增道具扣除
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存道具扣除
     */
    @RequiresPermissions("gmtool:deductItem:add")
    @Log(title = "道具扣除", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DeductItem deductItem)
    {
        return toAjax(deductItemService.insertDeductItem(deductItem));
    }

    /**
     * 修改道具扣除
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        DeductItem deductItem = deductItemService.selectDeductItemById(id);
        mmap.put("deductItem", deductItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存道具扣除
     */
    @RequiresPermissions("gmtool:deductItem:edit")
    @Log(title = "道具扣除", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DeductItem deductItem)
    {
        return toAjax(deductItemService.updateDeductItem(deductItem));
    }

    /**
     * 删除道具扣除
     */
    @RequiresPermissions("gmtool:deductItem:remove")
    @Log(title = "道具扣除", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(deductItemService.deleteDeductItemByIds(ids));
    }

    /**
     * 根据角色ID扣除道具
     * @param deductItem
     * @param serverId
     * @param request
     * @return
     */
    @PostMapping( "/deductItemByRoleId")
    @ResponseBody
    public AjaxResult deductItemByRoleId(DeductItem deductItem, String serverId, HttpServletRequest request) {
        User user = ShiroUtils.getSysUser();
        int sid = DBServerMgr.getInstance().getHeFuId(deductItem.getServerId());

        TServer server = tServerService.selectTServerByServerId(sid);
        if (server == null) {
            return AjaxResult.info("服务器DB连接信息获取失败").put("ok",false);
        }

        //检查角色ID
        List<RoleState> roles = gameRoleService.queryByRoleId(sid, deductItem.getRoleId(), 0);
        if (roles.isEmpty()) {
            return AjaxResult.info("没有找到此角色ID").put("ok",false);
        }
        if (roles.get(0).getIsDelete() != 0) {
            return AjaxResult.info("此角色已删除").put("ok",false);
        }
        try {
            deductItem.setDedTime(new Date());
            deductItem.setSendUser(user.getLoginName());
            deductItem.setIsDelete(0);
            //发送消息到GameServer
            TServer tserver = tServerService.selectTServerByServerId(Integer.parseInt(serverId));
            if (tserver == null) {
                return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
            }
            AjaxResult resultMap = GameServerRequestUtil.gmDeductItemopt(tserver, deductItem);
            HashMap dataMap = (HashMap) resultMap.get("data");
            //得到实际扣除的数量
            int realCount = Integer.parseInt(dataMap.get("data").toString());
            deductItem.setRealCount(realCount);
            String prompt;
            if (Boolean.valueOf(resultMap.get("ok").toString())) {
                prompt = "操作成功！";
                deductItemService.insertDeductItem(deductItem);
            } else {
                prompt = "操作失败！";
            }
            log.error("道具扣除：sid=" + serverId + ",roleId=" + deductItem.getRoleId() + ",操作结果:" + resultMap.get("msg").toString());
            GMLogUtil.log("删除角色(roleIds=" + deductItem.getRoleId() + ")"  + deductItem.getRealCount() + "个道具");
            return AjaxResult.info(prompt).put("ok",Boolean.valueOf(resultMap.get("ok").toString()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.info("扣除失败").put("ok",false);
        }
    }

    /**
     * 删除扣除道具记录
     * @param id
     * @return
     */
    @PostMapping( "/delDeductItem")
    @ResponseBody
    public AjaxResult delDeductItem(int id) {
        DeductItem deductItem = deductItemService.selectDeductItemById(id);
        if (deductItem != null) {
            deductItem.setIsDelete(1);
            int num = deductItemService.updateDeductItem(deductItem);
            GMLogUtil.log("删除扣除道具记录"+deductItem.getId());
            return AjaxResult.info("删除成功！").put("ok",num == 1);
        }
        return AjaxResult.info("删除失败！").put("ok",false);
    }
}
