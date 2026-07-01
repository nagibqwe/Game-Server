package com.game.backgrand.command;

import com.game.backgrand.script.IBackCommandScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;
import game.core.script.IScript;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * 跨服使用的后台的命令处理器
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BackCommandHandle implements ICommand {

    private Channel session;
    private Map<String, Object> cmdMap;

    public Channel getSession() {
        return session;
    }

    public void setSession(Channel session) {
        this.session = session;
    }

    public Map<String, Object> getCmdMap() {
        return cmdMap;
    }

    public void setCmdMap(Map<String, Object> cmdMap) {
        this.cmdMap = cmdMap;
    }

    @Override
    public void action() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BackCommandBaseScript);
        if (is instanceof IBackCommandScript) {
            try {
                ((IBackCommandScript) is).dealBackCommand(session, cmdMap);
            } catch (Exception e) {
                LogManager.getLogger("BackCommandHandle").error("后台GM指令过来时出现了异常错误！", e);
            }
        } else {
            LogManager.getLogger("BackCommandHandle").error("后台GM指令过来时找不到后台执行的脚本实例了！");
        }
    }

}
