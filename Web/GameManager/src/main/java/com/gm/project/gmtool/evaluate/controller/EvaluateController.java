package com.gm.project.gmtool.evaluate.controller;

import java.util.Date;
import java.util.List;

import com.gm.common.utils.security.ShiroUtils;
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
import com.gm.project.gmtool.evaluate.domain.Evaluate;
import com.gm.project.gmtool.evaluate.service.IEvaluateService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * 评价开关Controller
 * 
 * @author gm
 * @date 2021-11-04
 */
@Controller
@RequestMapping("/gmtool/evaluate")
public class EvaluateController extends BaseController
{
    private String prefix = "gmtool/evaluate";
    private static Logger log = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:evaluate:view")
    @GetMapping()
    public String evaluate()
    {
        return prefix + "/evaluate";
    }

    /**
     * 查询评价开关列表
     */
//    @RequiresPermissions("gmtool:evaluate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(int serverId)
    {
        Evaluate evaluate = new Evaluate();
        evaluate.setServerId(serverId);
        startPage();
        List<Evaluate> list = evaluateService.selectEvaluateList(evaluate);
        return getDataTable(list);
    }

    /**
     * 导出评价开关列表
     */
    @RequiresPermissions("gmtool:evaluate:export")
    @Log(title = "评价开关", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Evaluate evaluate)
    {
        List<Evaluate> list = evaluateService.selectEvaluateList(evaluate);
        ExcelUtil<Evaluate> util = new ExcelUtil<Evaluate>(Evaluate.class);
        return util.exportExcel(list, "评价开关数据");
    }

    /**
     * 新增评价开关
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存评价开关
     */
    @RequiresPermissions("gmtool:evaluate:add")
    @Log(title = "评价开关", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Evaluate evaluate)
    {
        return toAjax(evaluateService.insertEvaluate(evaluate));
    }

    /**
     * 修改评价开关
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Evaluate evaluate = evaluateService.selectEvaluateById(id);
        mmap.put("evaluate", evaluate);
        return prefix + "/edit";
    }

    /**
     * 修改保存评价开关
     */
    @RequiresPermissions("gmtool:evaluate:edit")
    @Log(title = "评价开关", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Evaluate evaluate)
    {
        return toAjax(evaluateService.updateEvaluate(evaluate));
    }

    /**
     * 删除评价开关
     */
    @RequiresPermissions("gmtool:evaluate:remove")
    @Log(title = "评价开关", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(evaluateService.deleteEvaluateByIds(ids));
    }


    @PostMapping( "/setEvaluateState")
    @ResponseBody
    public AjaxResult setEvaluateState(Evaluate evaluate, HttpServletRequest request) {
        User user = ShiroUtils.getSysUser();
        try {
            evaluate.seteType(evaluate.geteType());
            evaluate.setState(evaluate.getState());
            evaluate.setActionTime(new Date());
            evaluate.setActionUser(user.getLoginName());
            evaluate.setIsDelete(0);
            //发送消息到GameServer
            TServer server = tServerService.selectTServerByServerId(evaluate.getServerId());
            if (server == null) {
                return AjaxResult.info("服务器连接信息获取失败").put("ok",false);
            }
            AjaxResult resultMap = GameServerRequestUtil.gmSetEvaluate(server, evaluate);
            String prompt;
            if (Boolean.valueOf(resultMap.get("ok").toString())) {
                prompt = "操作成功！";
                evaluateService.insertEvaluate(evaluate);
            } else {
                prompt = "操作失败！";
            }
            log.error("评价开关：sid=" + evaluate.getServerId() + ",操作结果:" + resultMap.get("msg").toString());
            GMLogUtil.log("评价开关"+ evaluate.geteType()+",状态："+evaluate.getState());
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
    @PostMapping( "/delEvaluate")
    @ResponseBody
    public Object delEvaluate(int id) {
        Evaluate evaluate = evaluateService.selectEvaluateById(id);
        if (evaluate != null) {
            evaluate.setIsDelete(1);
            int num = evaluateService.updateEvaluate(evaluate);
            GMLogUtil.log("删除设置评价开关记录"+evaluate.getId());
            return AjaxResult.info("删除成功！").put("ok",num == 1);
        }
        return AjaxResult.info("删除失败！").put("ok",false);
    }
}
