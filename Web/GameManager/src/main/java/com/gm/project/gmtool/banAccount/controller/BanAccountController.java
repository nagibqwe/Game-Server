package com.gm.project.gmtool.banAccount.controller;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gamelog.rolestate.service.IRoleStateService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 账号封禁Controller
 * 
 * @author gm
 * @date 2021-11-21
 */
@Controller
@RequestMapping("/gmtool/banAccount")
public class BanAccountController extends BaseController
{
    private String prefix = "gmtool/banAccount";

    private static final Logger log = LoggerFactory.getLogger(BanAccountController.class);

    //平台账号ID
    private static final int BanType1 = 1;
    //IP
    private static final int BanType2 = 2;
    //唯一机器码
    private static final int BanType3 = 3;
    //IMEI
    private static final int BanType4 = 4;
    //MAC
    private static final int BanType5 = 5;
    //根据角色ID查封号条件
    private static final int BanType6 = 6;

    @Autowired
    private IBanAccountService banAccountService;

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private IGameRoleService gameRoleService;

    @Autowired
    private IRoleStateService roleStateService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    @RequiresPermissions("gmtool:banAccount:view")
    @GetMapping()
    public String banAccount()
    {
        return prefix + "/banAccount";
    }

    @RequiresPermissions("gmtool:unBanAccount:view")
    @GetMapping("/unBanAccount")
    public String unBanAccount()
    {
        return prefix + "/unBanAccount";
    }

    /**
     * 账号封禁
     */
//    @RequiresPermissions("gmtool:banAccount:ban")
    @PostMapping("/ban")
    @ResponseBody
    public AjaxResult ban(Integer serverId, int banType, String con, String endTime, String reason)
    {
        if (StringUtils.isEmpty(con) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(reason)) {
            return AjaxResult.info("param error").put("ok",false);
        }
        int banEndTime;
        try {
            Date eDate = TimeUtils.getDateByString1(endTime);
            banEndTime = (int) (eDate.getTime() / 1000);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
            return AjaxResult.info("param error").put("ok",false);
        }

        List<RoleState> roles = null;
        String[] conditionStrs = con.split(",");
        HashMap<String, String> conditions = new HashMap<>();
        if (banType == BanType6) {
            roles = roleStateService.selectRoleStateList(serverId, con);
            for (RoleState roleState:roles) {
                conditions.put(roleState.getFuncellUUid(), roleState.getFuncellUUid());
            }
        }else{
            roles = gameRoleService.queryByPlatFormUid(serverId, con);
            for (String c: conditionStrs) {
                conditions.put(c, c);
            }
        }
        if (roles == null || roles.isEmpty()) {
            return AjaxResult.info("没有找到该账号信息").put("ok",false);
        }

        //通知游戏服踢下线
        try {
            TServer tserver = tServerService.selectTServerByServerId(serverId);
            for (RoleState roleState:roles) {
                AjaxResult result = GameServerRequestUtil.gmKickPlayer(tserver, roleState.getRoleId().toString(), 5000);
                if (Boolean.valueOf(result.get("ok").toString())) {
                    GMLogUtil.log("封禁账号并踢人下线,serverId:" + serverId + "roleId:" + roleState.getRoleId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通知游戏服踢人下线报错", e);
        }

        for (String condition:conditions.values()) {
            //发送封禁信息到登录服
            TServer sc = new TServer();
            sc.setServerType(2);
            List<TServer> lsList = tServerService.selectTServerList(sc);
            StringBuilder sb = new StringBuilder();
            for (TServer ls:lsList) {
                String url = "http://" + ls.getServerIP() + ":" + ls.getServerPort() + "/forbiddenuser";

                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
                paramMap.put("forbidStr", condition);
                paramMap.put("forbiddenTime", String.valueOf(banEndTime));

                String res = HttpConnectionUtils.get(url, null, paramMap);
                sb.append(res).append("\n");
            }

            BanAccount banAccount = new BanAccount();
            banAccount.setCon(condition);
            List<BanAccount> banAccounts = banAccountService.selectBanAccountList(banAccount);
            if(!banAccounts.isEmpty()){
                banAccount = banAccounts.get(0);
            }
            boolean isUpdate = banAccount.getId()!=null;
            banAccount.setEndTime(endTime);
            banAccount.setReason(reason);
            banAccount.setState(0);

            if (isUpdate) {
                banAccountService.updateBanAccount(banAccount);
            }else{
                banAccountService.insertBanAccount(banAccount);
            }
        }
        return AjaxResult.info("封禁账号完成").put("ok", true);
    }

    /**
     * 解封账号
     */
    //    @RequiresPermissions("gmtool:banAccount:unBan")
    @PostMapping("/unBan")
    @ResponseBody
    public Object unBan(Integer id) {
        BanAccount banAccount = banAccountService.selectBanAccountById(id.longValue());
        if (banAccount == null) {
            return AjaxResult.info("封禁账号未找到").put("ok", false);
        }

        //发送解封信息到登录服
        TServer sc = new TServer();
        sc.setServerType(2);
        List<TServer> lsList = tServerService.selectTServerList(sc);
        StringBuilder sb = new StringBuilder();
        for (TServer ls:lsList) {
            String url = "http://" + ls.getServerIP() + ":" + ls.getServerPort() + "/cancelforbiddenuser";

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
            paramMap.put("forbidStr", banAccount.getCon());

            String res = HttpConnectionUtils.get(url, null, paramMap);
            sb.append(res).append("\n");
        }

        banAccount.setState(1);
        banAccountService.updateBanAccount(banAccount);

        return AjaxResult.info("解封账号完成").put("ok", true);
    }

    /**
     * 查询账号封禁列表
     */
    @RequiresPermissions("gmtool:banAccount:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BanAccount banAccount)
    {
        startPage();
        banAccount.setState(0);
        List<BanAccount> list = banAccountService.selectBanAccountList(banAccount);
        return getDataTable(list);
    }

    /**
     * 导出账号封禁列表
     */
    @RequiresPermissions("gmtool:banAccount:export")
    @Log(title = "账号封禁", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BanAccount banAccount)
    {
        List<BanAccount> list = banAccountService.selectBanAccountList(banAccount);
        ExcelUtil<BanAccount> util = new ExcelUtil<BanAccount>(BanAccount.class);
        return util.exportExcel(list, "账号封禁数据");
    }

    /**
     * 新增账号封禁
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存账号封禁
     */
    @RequiresPermissions("gmtool:banAccount:add")
    @Log(title = "账号封禁", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BanAccount banAccount)
    {
        return toAjax(banAccountService.insertBanAccount(banAccount));
    }

    /**
     * 修改账号封禁
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BanAccount banAccount = banAccountService.selectBanAccountById(id);
        mmap.put("banAccount", banAccount);
        return prefix + "/edit";
    }

    /**
     * 修改保存账号封禁
     */
    @RequiresPermissions("gmtool:banAccount:edit")
    @Log(title = "账号封禁", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BanAccount banAccount)
    {
        return toAjax(banAccountService.updateBanAccount(banAccount));
    }

    /**
     * 删除账号封禁
     */
    @RequiresPermissions("gmtool:banAccount:remove")
    @Log(title = "账号封禁", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(banAccountService.deleteBanAccountByIds(ids));
    }
}
