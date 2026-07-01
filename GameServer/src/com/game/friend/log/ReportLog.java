package com.game.friend.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;

/**
 * 举报日志
 */
public class ReportLog extends CommonLogBean {

    private long otherId;       //被举报玩家ID
    private int type;           //举报类型
    private String content;     //内容

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "otherId", fieldType = "bigint", index = "0")
    public long getOtherId() {
        return otherId;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "content", fieldType = "varchar(255)", index = "0")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
