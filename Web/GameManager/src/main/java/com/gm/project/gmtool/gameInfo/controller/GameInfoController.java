package com.gm.project.gmtool.gameInfo.controller;

import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;
import com.gm.project.gmtool.gameInfo.service.IGameInfoService;
import com.gm.project.gmtool.manager.GameInfoManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 游戏参数信息Controller
 * 
 * @author gm
 * @date 2021-11-15
 */
@Controller
@RequestMapping("/gmtool/gameInfo")
public class GameInfoController extends BaseController
{
    private String prefix = "gmtool/gameInfo";

    @Autowired
    private IGameInfoService gameInfoService;

    @RequiresPermissions("gmtool:gameInfo:view")
    @GetMapping()
    public String gameInfo()
    {
        return prefix + "/gameInfo";
    }

    /**
     * 查询游戏参数信息列表
     */
    @RequiresPermissions("gmtool:gameInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GameInfo gameInfo)
    {
        startPage();
        List<GameInfo> list = gameInfoService.selectGameInfoList(gameInfo);
        return getDataTable(list);
    }

    /**
     * 导出游戏参数信息列表
     */
    @RequiresPermissions("gmtool:gameInfo:export")
    @Log(title = "游戏参数信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GameInfo gameInfo)
    {
        List<GameInfo> list = gameInfoService.selectGameInfoList(gameInfo);
        ExcelUtil<GameInfo> util = new ExcelUtil<GameInfo>(GameInfo.class);
        return util.exportExcel(list, "游戏参数信息数据");
    }

    /**
     * 新增游戏参数信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存游戏参数信息
     */
    @RequiresPermissions("gmtool:gameInfo:add")
    @Log(title = "游戏参数信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GameInfo gameInfo)
    {
        int rows = gameInfoService.insertGameInfo(gameInfo);
        if(rows>0){
            if(gameInfo.getGameId()>0&&gameInfo.getGameId() == GameInfoManager.getInstance().getGameInfo().getGameId()){//同步游戏信息到APIServer
                //更新当前GM后台匹配游戏的参数信息
                GameInfoManager.getInstance().updateGameInfo(gameInfo);
                //通知APIServer更新游戏的参数信息
                GameInfoManager.getInstance().noticeUpdateGameInfo(gameInfo);
            }
        }
        return toAjax(rows);
    }

    /**
     * 修改游戏参数信息
     */
    @GetMapping("/edit/{gameId}")
    public String edit(@PathVariable("gameId") Integer gameId, ModelMap mmap)
    {
        GameInfo gameInfo = gameInfoService.selectGameInfoById(gameId);
        mmap.put("gameInfo", gameInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存游戏参数信息
     */
    @RequiresPermissions("gmtool:gameInfo:edit")
    @Log(title = "游戏参数信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GameInfo gameInfo)
    {
        int rows = gameInfoService.updateGameInfo(gameInfo);
        if(rows>0){
            if(gameInfo.getGameId()>0&&gameInfo.getGameId() == GameInfoManager.getInstance().getGameInfo().getGameId()){//同步游戏信息到APIServer
                //更新当前GM后台匹配游戏的参数信息
                GameInfoManager.getInstance().updateGameInfo(gameInfo);
                //通知APIServer更新游戏的参数信息
                GameInfoManager.getInstance().noticeUpdateGameInfo(gameInfo);
            }
        }
        return toAjax(rows);
    }

    /**
     * 删除游戏参数信息
     */
    @RequiresPermissions("gmtool:gameInfo:remove")
    @Log(title = "游戏参数信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(gameInfoService.deleteGameInfoByIds(ids));
    }
}
