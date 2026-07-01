package com.game.guild.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 公会的基础日志
 */
public class GuildBaseLog extends CommonLogBean {

    private static final Logger log = LogManager.getLogger("GuildBaseLog");

    //公会Id
    private long guildId;
    //公会名称
    private String guildName;
    //操作类型 1:公会创建2:公会解散3:加入公会4:主动退会5:被踢出公会6：公会信息设置7：公会改名8：开始弹劾会长 9:取消弹劾会长 10:弹劾会长成功
    private byte type;
    //被操作玩家
    private long actId;
    //被操作玩家名称
    private String actName;
    //其他参数
    private String param;
    //关联其他日志Id
    private long actionId;

    public void setPlayer(Player p) {
        setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(fieldType = "bigint", index = "0", logField = "guildId")
    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    @Log(fieldType = "varchar(50)", index = "0", logField = "guildName")
    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    @Log(fieldType = "int", index = "0", logField = "type")
    public byte getType() {
        return type;
    }

    @Log(fieldType = "bigint", index = "0", logField = "actId")
    public long getActId() {
        return actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    @Log(fieldType = "varchar(50)", index = "0", logField = "actName")
    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Log(fieldType = "bigint", index = "0", logField = "actionId")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(fieldType = "varchar(1024)", index = "0", logField = "param")
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
