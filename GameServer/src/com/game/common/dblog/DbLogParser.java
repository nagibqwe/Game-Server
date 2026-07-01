package com.game.common.dblog;

import com.game.equip.log.EquipSynthesisLog;

public class DbLogParser {
    
    static DbLog parseType(int type) throws NullPointerException{
        DbLog logBean;
        switch (type) {
            case DbLogEnum.EQUIP_SYNTHETIC_LOG:
                logBean = new EquipSynthesisLog();
                break;
            default:
                throw new NullPointerException("条件类型无法识别!type:" + type);
        }
        return logBean;
    }
}
