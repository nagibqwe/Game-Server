package game.core.dblog;

import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class tbllog_pvp extends BaseLogBean
{

    private static final Logger logger = LogManager.getLogger("tbllog_pvp");

    private String userId;


    @Override
    public TableCheckStepEnum getRollingStep()
    {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile()
    {
        logger.error(buildSql());
    }

    @Log(logField = "userId", fieldType = "varchar(100)", index = "1")
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }


}
