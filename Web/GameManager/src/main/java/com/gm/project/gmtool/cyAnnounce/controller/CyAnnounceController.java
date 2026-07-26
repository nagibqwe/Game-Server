package com.gm.project.gmtool.cyAnnounce.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.cyAnnounce.TaskTimerService;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;
import com.gm.project.gmtool.cyAnnounce.service.ICyAnnounceService;
import com.gm.project.gmtool.manager.CyAnnounceManager;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.RandomUtil;
import com.gm.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Циклическое объявлениеController
 * 
 * @author gm
 * @date 2021-10-27
 */
@Controller
@RequestMapping("/gmtool/cyAnnounce")
public class CyAnnounceController extends BaseController
{
    private String prefix = "gmtool/cyAnnounce";

    @Autowired
    private ICyAnnounceService cyAnnounceService;

    private static SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final Logger log = LoggerFactory.getLogger(CyAnnounceController.class);

    @RequiresPermissions("gmtool:cyAnnounce:view")
    @GetMapping()
    public String cyAnnounce()
    {
        return prefix + "/cyAnnounce";
    }

    /**
     * 查询Циклическое объявление列表(Включить列表)
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CyAnnounce cyAnnounce)
    {
        startPage();
        cyAnnounce.setState(0);
        List<CyAnnounce> list = cyAnnounceService.selectCyAnnounceList(cyAnnounce);
        return getDataTable(list);
    }

    /**
     * 查询Циклическое объявление列表(禁用列表)
     */
    @PostMapping("/disableList")
    @ResponseBody
    public TableDataInfo disableList(CyAnnounce cyAnnounce)
    {
        startPage();
        List<CyAnnounce> list = cyAnnounceService.selectCyAnnounceDisableList(cyAnnounce);
        return getDataTable(list);
    }

    /**
     * ЭкспортЦиклическое объявление列表
     */
    @RequiresPermissions("gmtool:cyAnnounce:export")
    @Log(title = "Циклическое объявление", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CyAnnounce cyAnnounce)
    {
        List<CyAnnounce> list = cyAnnounceService.selectCyAnnounceList(cyAnnounce);
        ExcelUtil<CyAnnounce> util = new ExcelUtil<CyAnnounce>(CyAnnounce.class);
        return util.exportExcel(list, "Циклическое объявлениеДанные");
    }

    /**
     * ДобавитьЦиклическое объявление
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьЦиклическое объявление
     */
    @RequiresPermissions("gmtool:cyAnnounce:add")
    @Log(title = "Циклическое объявление", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CyAnnounce cyAnnounce)
    {
        return toAjax(cyAnnounceService.insertCyAnnounce(cyAnnounce));
    }

    /**
     * ИзменитьЦиклическое объявление
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        CyAnnounce cyAnnounce = cyAnnounceService.selectCyAnnounceById(id);
        mmap.put("cyAnnounce", cyAnnounce);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьЦиклическое объявление
     */
    @RequiresPermissions("gmtool:cyAnnounce:edit")
    @Log(title = "Циклическое объявление", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CyAnnounce cyAnnounce)
    {
        return toAjax(cyAnnounceService.updateCyAnnounce(cyAnnounce));
    }

    /**
     * УдалитьЦиклическое объявление
     */
    @RequiresPermissions("gmtool:cyAnnounce:remove")
    @Log(title = "Циклическое объявление", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cyAnnounceService.deleteCyAnnounceByIds(ids));
    }

    /**
     * ДобавитьЦиклическое объявление
     * @param request
     * @param announce
     * @param selectServerIdList
     * @return
     */
    @PostMapping( "/addAnnounce")
    @ResponseBody
    public AjaxResult addAnnounce(HttpServletRequest request, CyAnnounce announce,String selectGroupName, String[] selectServerIdList) {
        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return AjaxResult.info("不存在该用户").put("ok",false);
        }
        if (StringUtils.isBlank(selectGroupName) || selectServerIdList == null || selectServerIdList.length < 1) {
            return AjaxResult.info("没有选择Сервер").put("ok",false);
        }

        if (announce.getTotalTimes() < 0 || announce.getCycleInterval() < 1) {
            return AjaxResult.info("Время调节参数错误！").put("ok",false);
        }

        if (StringUtils.isBlank(announce.getContent()) || announce.getType() <= 0) {
            return AjaxResult.info("公告的Содержимое不能为空").put("ok",false);
        }
        if (!(Pattern.matches("(\\d{1,4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2})", announce.getFromDate())
                && Pattern.matches("(\\d{1,4})-(\\d{1,2})-(\\d{1,2}) (\\d{1,2}):(\\d{1,2})", announce.getToDate()))) {
            return AjaxResult.info("Время格式错误！").put("ok",false);
        }

        StringBuilder sb = new StringBuilder();

        for (String sid : selectServerIdList) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(sid);
        }

        try {
            long createTime = System.currentTimeMillis();
            String createDate = sdfhm.format(new Date(createTime));
            long fromTime = sdfhm.parse(announce.getFromDate()).getTime();
            long toTime = sdfhm.parse(announce.getToDate()).getTime();
            String batchTag = createTime + String.valueOf(RandomUtil.random(100000, 999999));
            announce.setBatchTag(batchTag);
            announce.setServerIds(sb.toString());
            announce.setCreateUserId(Integer.parseInt(String.valueOf(user.getUserId())));
            announce.setCreateUserName(user.getLoginName());
            announce.setCreateTime(createTime);
            announce.setCreateDate(createDate);
            announce.setFromTime(fromTime);
            announce.setToTime(toTime);
            announce.setNowTimes(0L);
            announce.setNextTimes(0L);
            announce.setNextDate("");

            cyAnnounceService.insertCyAnnounce(announce);
            CyAnnounce cb = cyAnnounceService.selectCyAnnounceById(announce.getId());
            if (cb != null) {
                boolean bn = CyAnnounceManager.getInstance().addCyAnnounce(cb);
                if (bn) {
                    //Включено一个新的计时器
                    TaskTimerService.getInstance().StartAnnounceTask(cb.getCycleInterval());
                }
                GMLogUtil.log("发布Мгновенное объявление:" + cb.getBatchTag() + "\t" + cb.getContent() + "\tРезультат：" + cb.getId());
                return AjaxResult.info("Добавить新的公告Успешно!").put("ok",true);
            } else {
                return AjaxResult.info("Добавить新的公告СодержимоеОшибка了！").put("ok",false);
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return AjaxResult.info("处理Ошибка！").put("ok",false);
    }

    /**
     * 改变Циклическое объявление的Статус
     * @param announceId
     * @param state
     * @param request
     * @return
     */
    @PostMapping( "/jYCyAnnounce")
    @ResponseBody
    public AjaxResult jYCyAnnounce(int announceId, int state, HttpServletRequest request) {
        CyAnnounce ca = cyAnnounceService.selectCyAnnounceById(announceId);
        if (ca == null) {
            return AjaxResult.info("查询的记录不存在！").put("ok",false);
        }

        ca.setState(state);
        if (state == 0) {
            boolean bn = CyAnnounceManager.getInstance().addCyAnnounce(ca);
            if (bn) {
                TaskTimerService.getInstance().StartAnnounceTask(ca.getCycleInterval());
            }
        }
        int num = CyAnnounceManager.getInstance().updateSave(ca);
        boolean b = num > 0;
        User user = ShiroUtils.getSysUser();
        if (user != null) {
            GMLogUtil.log(String.format("Объявление об обновленииСтатус，id: %s, state: %s, Результат %s：", announceId, state, b));
        }
        return AjaxResult.info("处理Успешно！").put("ok",b);
    }



    /**
     * УдалитьЦиклическое объявление
     * @param announceId
     * @param request
     * @return
     */
    @PostMapping( "/deleteCyAnnounce")
    @ResponseBody
    public AjaxResult deleteCyAnnounce(int announceId, HttpServletRequest request) {
        CyAnnounce ca = cyAnnounceService.selectCyAnnounceById(announceId);
        if (ca == null) {
            return AjaxResult.info("查询的记录不存在！").put("ok",false);
        }
        CyAnnounceManager.getInstance().remove(ca);
        GMLogUtil.log("Удалить发送公告\t" + announceId);
        return AjaxResult.info("处理Успешно！").put("ok",true);
    }
}
