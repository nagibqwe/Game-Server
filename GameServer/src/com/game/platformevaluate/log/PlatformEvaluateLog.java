package com.game.platformevaluate.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * 平台评价
 */
public class PlatformEvaluateLog extends CommonLogBean {
    
    private static final Logger logger = LogManager.getLogger("PlatformEvaluateLog");
    
    private int type;//评价类型1点赞2分享3评价

    private int actType;//操作类型1请求操作2领取奖励

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "actType", fieldType = "int", index = "0")
    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
    
    
    
  
}
