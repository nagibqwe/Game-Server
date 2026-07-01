package game.core.dblog;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

public class q2log extends BaseLogBean
{

    private static final Logger logger = LogManager.getLogger("StarArrayLog");

    private String userName;
    private String userId;
    private String roleId;
    private String opt;
    private long action;
    private int sid;
    private int model;
    private long overtime;
    private int bless;
    private int dayTimes;
    private int exp;

    @Override
    public TableCheckStepEnum getRollingStep()
    {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile()
    {
        logger.error(buildSql());
    }

    @Log(logField = "userName", fieldType = "varchar(100)", index = "0")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Log(logField = "userId", fieldType = "varchar(100)", index = "0")
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Log(logField = "roleId", fieldType = "varchar(100)", index = "2")
    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    @Log(logField = "opt", fieldType = "varchar(32)", index = "3")
    public String getOpt()
    {
        return opt;
    }

    public void setOpt(String opt)
    {
        this.opt = opt;
    }

    @Log(logField = "action", fieldType = "bigint", index = "4")
    public long getAction()
    {
        return action;
    }

    public void setAction(long action)
    {
        this.action = action;
    }

    @Log(logField = "sid", fieldType = "int", index = "1")
    public int getSid()
    {
        return sid;
    }

    public void setSid(int sid)
    {
        this.sid = sid;
    }

    @Log(logField = "model", fieldType = "int", index = "5")
    public int getModel()
    {
        return model;
    }

    public void setModel(int model)
    {
        this.model = model;
    }

    @Log(logField = "overtime", fieldType = "bigint", index = "5")
    public long getOvertime()
    {
        return overtime;
    }

    public void setOvertime(long overtime)
    {
        this.overtime = overtime;
    }

    @Log(logField = "bless", fieldType = "int", index = "5")
    public int getBless()
    {
        return bless;
    }

    public void setBless(int bless)
    {
        this.bless = bless;
    }

    @Log(logField = "dayTimes", fieldType = "int", index = "7")
    public int getDayTimes()
    {
        return dayTimes;
    }

    public void setDayTimes(int times)
    {
        this.dayTimes = times;
    }

    @Log(logField = "exp", fieldType = "int", index = "5")
    public int getExp()
    {
        return exp;
    }

    public void setExp(int exp)
    {
        this.exp = exp;
    }
}
