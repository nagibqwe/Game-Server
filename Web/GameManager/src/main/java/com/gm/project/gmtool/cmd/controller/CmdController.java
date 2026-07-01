package com.gm.project.gmtool.cmd.controller;

import com.alibaba.fastjson.JSON;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.cmd.domain.CmdLog;
import com.gm.project.gmtool.cmd.service.ICmdLogService;
import com.gm.project.gmtool.manager.GameInfoManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.HttpConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

/**
 * gm命令热更新Controller
 */
@Controller
@RequestMapping("/gmtool/gmcmd")
public class CmdController extends BaseController {

    private final static Logger log = LoggerFactory.getLogger(CmdController.class);

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private ICmdLogService cmdLogService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    /**
     * 发送游戏服命令
     * @param request
     * @param serverIdStr
     * @param action
     * @param params
     * @return
     */
    @PostMapping("/sendCommand")
    @ResponseBody
    public AjaxResult sendCommand(HttpServletRequest request, String serverIdStr, String action, String params) {
        if (StringUtils.isEmpty(action)) {
            return AjaxResult.error("命令为空！");
        }
        if (null == serverIdStr || "".equals(serverIdStr)) {
            return AjaxResult.error("没有选择服务器Id");
        }

        int isOk = 1;
        StringBuilder sb = new StringBuilder();
        int[] serverId = StringUtils.stringArrTointArr(Convert.toStrArray(serverIdStr));
        for (int id : serverId) {
            TServer ser = tServerService.selectTServerByServerId(id);
            if (ser == null) {
                return AjaxResult.error("选择服务器Id不存在");
            }
            if (ser.getIsHeFu() == 1) {
                continue;
            }

            AjaxResult map = GameServerRequestUtil.gmOrderSendMess(ser, action, params,8000);
            if (map.getCode() != 0) {
                isOk = 0;
            }

            String result = "执行GM命令,区服:" + ser.getServerName() + "(" + ser.getServerId() + ")"
                    + "命令：" + action + ", 参数:" + params
                    + "结果：" + map.toString() + "\n" ;
            sb.append(result);

            writeCmdLog(ser, 0, action, params, isOk, result);
        }
        if (isOk == 1){
            return AjaxResult.success(sb.toString());
        }
        return AjaxResult.error(sb.toString());
    }

    /**
     * 发送公共服命令
     * @param request
     * @param serverIdStr
     * @param action
     * @param params
     * @return
     */
    @PostMapping("/sendPSCommand")
    @ResponseBody
    public AjaxResult sendPSCommand(HttpServletRequest request, String serverIdStr, String action, String params) {
        if (StringUtils.isEmpty(action)) {
            return AjaxResult.error("命令错误！");
        }
        if (null == serverIdStr || "".equals(serverIdStr)) {
            return AjaxResult.error("没有选择服务器Id");
        }
        HashMap<String,String> paramMap = new HashMap<>();
        if(!"".equals(params)){
            String[] param = params.split("&");
            for (String pStr:param) {
                if(!"".equals(pStr)){
                    String[] ps = pStr.split("=");
                    paramMap.put(ps[0], ps[1]);
                }
            }
        }

        params += "secret_key=" + gameManagerConfig.getRequestServerKey();
        paramMap.put("secret_key",gameManagerConfig.getRequestServerKey());

        String url;
        StringBuilder result = new StringBuilder();
        int isOk = 1;
        int[] serverId = StringUtils.stringArrTointArr(Convert.toStrArray(serverIdStr));
        for (int id : serverId) {
            TServer ser = tServerService.selectTServerByServerId(id);
            if (ser == null) {
                return AjaxResult.error("选择服务器Id不存在");
            }
            if (ser.getIsHeFu() == 1) {
                continue;
            }

            url = "http://" + ser.getServerIP() + ":" + ser.getServerPort() + "/" + action;
            log.info("发送公共服命令: URL = {}, param = {}",url, JSON.toJSONString(paramMap));
            String re = HttpConnectionUtils.get(url, null, paramMap);
            if (StringUtils.isBlank(re)) {
                isOk = 0;
                result.append("请求异常!");
            }else {
                result.append(re);
            }

            writeCmdLog(ser, 1, action, params, isOk, re);
        }
        if (isOk == 1){
            return AjaxResult.success(result.toString());
        }
        return AjaxResult.error(result.toString());
    }

    /**
     * 查询服务器开服时间
     * @param serverId
     * @return
     */
    @PostMapping("/queryOpsTime")
    @ResponseBody
    public AjaxResult queryOpsTime(Integer serverId){
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmQueryOpsTime(server);
    }

    /**
     * 查询服务器注册限制人数/设置注册限制人数
     * @param serverId
     * @return
     */
    @PostMapping("/setRegisterLimitNum")
    @ResponseBody
    public AjaxResult setRegisterLimitNum(Integer serverId, Integer num){
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("选择服务器Id不存在");
        }
        if (num == null){
            num = 0;
        }
        return GameServerRequestUtil.gmSetRegisterLimitNum(server,num);
    }

    /**
     * 设置服务器开服时间
     * @param serverId
     * @param time
     * @return
     */
    @PostMapping("/setOpsTime")
    @ResponseBody
    public AjaxResult setOpsTime(Integer serverId, String time) {
        TServer server = tServerService.selectTServerByServerId(serverId);
        if (null == server){
            return AjaxResult.error("选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmSetOpsTime(server, time);
    }
    /**
     *写入cmdlog日志
     */
    private void writeCmdLog(TServer server, int type, String action, String param, int isOk, String result) {
        CmdLog cmdLog = new CmdLog();
        String operName = ShiroUtils.getLoginName();
        String operIp = ShiroUtils.getIp();
        cmdLog.setAction(action);
        cmdLog.setIsOk(isOk);
        cmdLog.setOperDate(new Date());
        cmdLog.setParams(param);
        cmdLog.setResult(result);
        cmdLog.setServerId(server.getServerId());
        cmdLog.setServerName(server.getServerName());
        cmdLog.setUser(operName);
        cmdLog.setIp(operIp);
        cmdLog.setGmType(type);
        cmdLogService.insertCmdLog(cmdLog);
    }
}
