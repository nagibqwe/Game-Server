package com.gm.project.gmtool.announce.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gm.common.utils.security.ShiroUtils;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.StringUtils;
import com.gm.project.system.user.domain.User;
import com.google.common.base.Joiner;
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
import com.gm.project.gmtool.announce.domain.Announce;
import com.gm.project.gmtool.announce.service.IAnnounceService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * Мгновенное объявлениеController
 * 
 * @author gm
 * @date 2021-10-21
 */
@Controller
@RequestMapping("/gmtool/announce")
public class AnnounceController extends BaseController
{
    private String prefix = "gmtool/announce";
    private static SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private IAnnounceService announceService;

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:announce:view")
    @GetMapping()
    public String announce()
    {
        return prefix + "/announce";
    }

    /**
     * 查询Мгновенное объявление列表
     */
//    @RequiresPermissions("gmtool:announce:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Announce announce)
    {
        startPage();
        announce.setType(0);
        List<Announce> list = announceService.selectAnnounceList(announce);
        return getDataTable(list);
    }

    /**
     * ЭкспортМгновенное объявление列表
     */
    @RequiresPermissions("gmtool:announce:export")
    @Log(title = "Мгновенное объявление", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Announce announce)
    {
        List<Announce> list = announceService.selectAnnounceList(announce);
        ExcelUtil<Announce> util = new ExcelUtil<Announce>(Announce.class);
        return util.exportExcel(list, "Мгновенное объявлениеДанные");
    }

    /**
     * ДобавитьМгновенное объявление
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьМгновенное объявление
     */
    @RequiresPermissions("gmtool:announce:add")
    @Log(title = "Мгновенное объявление", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Announce announce)
    {
        return toAjax(announceService.insertAnnounce(announce));
    }

    /**
     * ИзменитьМгновенное объявление
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Announce announce = announceService.selectAnnounceById(id);
        mmap.put("announce", announce);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьМгновенное объявление
     */
    @RequiresPermissions("gmtool:announce:edit")
    @Log(title = "Мгновенное объявление", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Announce announce)
    {
        return toAjax(announceService.updateAnnounce(announce));
    }

    /**
     * УдалитьМгновенное объявление
     */
    @RequiresPermissions("gmtool:announce:remove")
    @Log(title = "Мгновенное объявление", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int row = announceService.deleteAnnounceByIds(ids);
        boolean b = row > 0;
        if (row > 0){
            GMLogUtil.log("УдалитьМгновенное объявление，Результат：" + b);
        }
        return toAjax(row);
    }

    /**
     * СохранитьМгновенное объявление
     * @param content
     * @return
     */
    @PostMapping( "/addAnnounceTemplate")
    @ResponseBody
    public AjaxResult addAnnounceTemplate(String content) {
        User user = ShiroUtils.getSysUser();
        Announce a = new Announce();
        a.setContent(content);
        a.setUserId(Integer.parseInt(String.valueOf(user.getUserId())));
        a.setUserName(user.getLoginName());
        a.setCreateTime(System.currentTimeMillis());
        a.setCreateDate(sdfhm.format(new Date()));
        a.setType(0);
        int row = announceService.insertAnnounce(a);
        if (row > 0) {
            return AjaxResult.info("Сохранить当前公告Успешно!").put("ok",true);
        }
        return AjaxResult.info("Сохранить当前公告Ошибка了!").put("ok",false);
    }

    /**
     * 发布Мгновенное объявление
     * @param request
     * @param selectGroupName
     * @param selectServerIdList
     * @param type
     * @param content
     * @param reason
     * @return
     */
    @PostMapping( "/addImmediateAnnounce")
    @ResponseBody
    public AjaxResult addImmediateAnnounce(HttpServletRequest request, String selectGroupName, String[] selectServerIdList, int type, String content, String reason) {
        User user = ShiroUtils.getSysUser();
        if (selectServerIdList == null || selectServerIdList.length <= 0) {
            return AjaxResult.info("没有选择Сервер").put("ok",false);
        }
        List<Integer> serverIdList = new ArrayList<>();
        for (String sid : selectServerIdList) {
            if (StringUtils.isNumber(sid)) {
                int id = Integer.parseInt(sid);
                serverIdList.add(id);
            }
        }

        if (StringUtils.isBlank(reason)) {
            return AjaxResult.info("理由不能为空，请检查！").put("ok",false);
        }
        if (serverIdList.size() < 1) {
            return AjaxResult.info("没有选择Сервер").put("ok",false);
        }

        List<TServer> serverList =tServerService.selectServerByServerIds(Joiner.on(",").join(serverIdList));

        StringBuilder sb = new StringBuilder();
        for (TServer server : serverList) {
            AjaxResult ret = GameServerRequestUtil.gmPublishAnnounce(server, type, content);
            if (Boolean.valueOf(ret.get("ok").toString())) {
                sb.append(server.getServerName()).append(ret.get("msg")).append("\n");
            } else {
                sb.append(server.getServerName()).append("Ошибка，原因：").append(ret.get("msg")).append("\n");
            }
        }

        Announce a = new Announce();
        a.setUserId(Integer.parseInt(String.valueOf(user.getUserId())));
        a.setUserName(user.getLoginName());
        a.setCreateTime(System.currentTimeMillis());
        a.setCreateDate(sdfhm.format(new Date()));
        a.setGroupName(selectGroupName);
        a.setServerIds(JsonUtils.toJSONString(selectServerIdList));
        a.setType(type);
        a.setContent(content);
        a.setReason(reason);
        announceService.insertAnnounce(a);
        //Сохранить公告Содержимое
        GMLogUtil.log("ДобавитьМгновенное объявление\t理由：" + reason + "\t" + JsonUtils.toJSONString(serverIdList) + "\t" + content + "\tРезультат：" + sb.toString());
        return AjaxResult.info(sb.toString()).put("ok",true);
    }
}
