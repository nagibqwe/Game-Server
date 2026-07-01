package com.gm.project.gmtool.updateNotice.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.updateNotice.domain.UpdateNotice;
import com.gm.project.gmtool.updateNotice.service.IUpdateNoticeService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 更新公告Controller
 * 
 * @author gm
 * @date 2021-10-30
 */
@Controller
@RequestMapping("/gmtool/updateNotice")
public class UpdateNoticeController extends BaseController
{
    private String prefix = "gmtool/updateNotice";

    @Autowired
    private IUpdateNoticeService updateNoticeService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:updateNotice:view")
    @GetMapping()
    public String updateNotice()
    {
        return prefix + "/updateNotice";
    }

    /**
     * 查询更新公告列表
     */
//    @RequiresPermissions("gmtool:updateNotice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UpdateNotice updateNotice)
    {
        startPage();
        List<UpdateNotice> list = updateNoticeService.selectUpdateNoticeList(updateNotice);
        return getDataTable(list);
    }

    /**
     * 导出更新公告列表
     */
    @RequiresPermissions("gmtool:updateNotice:export")
    @Log(title = "更新公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UpdateNotice updateNotice)
    {
        List<UpdateNotice> list = updateNoticeService.selectUpdateNoticeList(updateNotice);
        ExcelUtil<UpdateNotice> util = new ExcelUtil<UpdateNotice>(UpdateNotice.class);
        return util.exportExcel(list, "更新公告数据");
    }

    /**
     * 新增更新公告
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存更新公告
     */
    @RequiresPermissions("gmtool:updateNotice:add")
    @Log(title = "更新公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UpdateNotice updateNotice)
    {
        return toAjax(updateNoticeService.insertUpdateNotice(updateNotice));
    }

    /**
     * 修改更新公告
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        UpdateNotice updateNotice = updateNoticeService.selectUpdateNoticeById(id);
        mmap.put("updateNotice", updateNotice);
        return prefix + "/edit";
    }

    /**
     * 修改保存更新公告
     */
    @RequiresPermissions("gmtool:updateNotice:edit")
    @Log(title = "更新公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UpdateNotice updateNotice)
    {
        return toAjax(updateNoticeService.updateUpdateNotice(updateNotice));
    }

    /**
     * 删除更新公告
     */
    @RequiresPermissions("gmtool:updateNotice:remove")
    @Log(title = "更新公告", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(updateNoticeService.deleteUpdateNoticeByIds(ids));
    }

    /**
     * 发送更新公告
     * @param request
     * @param selectServerIdList
     * @param content
     * @param items
     * @param type
     * @return
     */
    @PostMapping( "/sendUpdateNotice")
    @ResponseBody
    public AjaxResult sendUpdateNotice(HttpServletRequest request, String[] selectServerIdList, String content, String items, int type) {
        if (selectServerIdList==null||selectServerIdList.length == 0|| StringUtils.isBlank(content) || StringUtils.isBlank(items)) {
            return AjaxResult.info("param error").put("ok",false);
        }
        StringBuilder serverIdStr = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (String sid:selectServerIdList) {
            TServer server = tServerService.selectTServerByServerId(Integer.parseInt(sid));
            if (server == null) {
                return AjaxResult.info("服务器连接信息获取失败,serverId="+sid).put("ok",false);
            }
            AjaxResult resultMap = GameServerRequestUtil.gmSendUpdateNotice(server, content, items, type>0?"1":String.valueOf(type));
            if (Boolean.valueOf(resultMap.get("ok").toString())) {
                sb.append(server.getServerName()).append(resultMap.get("msg")).append("\n");
                serverIdStr.append(sid).append(",");
            }else {
                sb.append(server.getServerName()).append("失败，原因：").append(resultMap.get("msg")).append("\n");
            }
        }

        UpdateNotice bean = new UpdateNotice();
        bean.setServerIds(serverIdStr.toString());
        bean.setContent(content);
        bean.setReward(items);
        bean.setType(type);
        updateNoticeService.insertUpdateNotice(bean);
        GMLogUtil.log("设置更新公告成功,ID="+bean.getId());
        return AjaxResult.info(sb.toString()).put("ok",true);

    }
}
