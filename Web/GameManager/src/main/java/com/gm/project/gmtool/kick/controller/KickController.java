package com.gm.project.gmtool.kick.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.banAccount.domain.BanAccount;
import com.gm.project.gmtool.banAccount.service.IBanAccountService;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.HttpConnectionUtils;
import com.gm.project.gmtool.utils.TimeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 踢人Controller
 * 
 * @author gm
 * @date 2021-11-21
 */
@Controller
@RequestMapping("/gmtool/kick")
public class KickController extends BaseController
{
    private String prefix = "gmtool/kick";

    private static final Logger log = LoggerFactory.getLogger(KickController.class);

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private IGameRoleService gameRoleService;

    @RequiresPermissions("gmtool:kick:view")
    @GetMapping()
    public String kick()
    {
        return prefix + "/kick";
    }

    /**
     * 账号封禁
     */
//    @RequiresPermissions("gmtool:kick:kickRole")
    @PostMapping("/kickRole")
    @ResponseBody
    public AjaxResult kickRole(Integer serverId, String roleId, String reason)
    {
        if (StringUtils.isEmpty(roleId)) {
            return AjaxResult.info("param error").put("ok",false);
        }

        //通知游戏服踢下线
        try {
            List<RoleState> roles = gameRoleService.queryByRoleId(serverId, roleId, 0);
            if (roles == null || roles.isEmpty()) {
                return AjaxResult.info("没有找到该账号信息").put("ok",false);
            }

            TServer tserver = tServerService.selectTServerByServerId(serverId);
            for (RoleState roleState:roles) {
                AjaxResult result = GameServerRequestUtil.gmKickPlayer(tserver, roleId, 5000);
                if (Boolean.valueOf(result.get("ok").toString())) {
                    GMLogUtil.log("踢人下线,serverId:" + serverId + "roleId:" + roleId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通知游戏服踢人下线报错", e);
        }

        return AjaxResult.info("踢人下线完成").put("ok", true);
    }
}
