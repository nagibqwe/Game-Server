/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author Administrator
 */
public class LogUserDoBean extends BaseLogBean 
{
    private static final Logger logger = LogManager.getLogger("LoginDBLog");
    
    private long userId; // 游戏生成的账号id
    private int type; // 操作类型,0删除角色，1恢复角色，2屏蔽

    /**
     * get 游戏生成的账号id
     * @return
     */
    @Log(logField = "userId", fieldType = "bigint", index = "0")
    public long getUserId()
    {
        return userId;
    }

    /**
     * set 游戏生成的账号id
     */
    public void setUserId(long userId)
    {
        this.userId = userId;
    }
    
    /**
     * get 操作类型,0删除角色，1恢复角色
     * @return
     */
    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType()
    {
        return type;
    }

    /**
     * set 操作类型,0删除角色，1恢复角色
     */
    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
    
}
