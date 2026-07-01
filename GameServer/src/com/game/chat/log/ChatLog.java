package com.game.chat.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 聊天日志
 */
public class ChatLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("ChatLog");

    private int channel;            //发送类型
    private long receRoleId;        //接受者id
    private String content;         //内容
    private int level;              //等级
    private String ip;              //IP

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "channel", fieldType = "int", index = "0")
    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Log(logField = "content", fieldType = "blob", index = "0")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Log(logField = "receRoleId", fieldType = "bigint", index = "0")
    public long getReceRoleId() {
        return receRoleId;
    }

    public void setReceRoleId(long receRoleId) {
        this.receRoleId = receRoleId;
    }

    @Log(logField = "level", fieldType = "int", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(logField = "ip", fieldType = "varchar(50)", index = "0")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
