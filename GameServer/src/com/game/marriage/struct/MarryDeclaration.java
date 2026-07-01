package com.game.marriage.struct;

import game.core.db.BaseBean;

/**
 * @Desc TODO 爱情宣言
 * @Date 2020/8/25 18:49
 * @Auth ZUncle
 */
public class MarryDeclaration extends BaseBean{
    long roleId;
    int declarationId;  //宣言ID
    long timeout;       //过期时间

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(int declarationId) {
        this.declarationId = declarationId;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
