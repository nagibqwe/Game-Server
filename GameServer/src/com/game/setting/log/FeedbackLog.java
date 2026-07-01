package com.game.setting.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 反馈日志
 */
public class FeedbackLog extends CommonLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    private long sender;            //发送者,0自己,1系统
    private int type;               //反馈类型
    private long sendTime;          //发送时间
    private String content;         //内容

    @Log(logField = "sender", fieldType = "bigint", index = "0")
    public long getSender() {
        return sender;
    }
    public void setSender(long sender) {
        this.sender = sender;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "sendTime", fieldType = "bigint", index = "0")
    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    @Log(logField = "content", fieldType = "varchar(255)", index = "0")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

}
