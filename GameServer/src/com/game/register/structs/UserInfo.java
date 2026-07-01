
package com.game.register.structs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hewei
 */
public class UserInfo {

    //用户ID
    private long userId;
    //是否实名认证
    private boolean isCertify;
    //年龄
    private int age;
    //扩展字段
    private HashMap<String,String> extension;
    //用户关联的角色
    private HashMap<Long, UserRoleInfo> roles = new HashMap<>();

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isCertify() {
        return isCertify;
    }

    public void setCertify(boolean certify) {
        isCertify = certify;
    }

    public HashMap<Long, UserRoleInfo> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<Long, UserRoleInfo> roles) {
        this.roles = roles;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", age=" + age +
                ", roleCount=" + roles.size() +
                '}';
    }

    public HashMap<String, String> getExtension() {
        return extension;
    }

    public void setExtension(HashMap<String, String> extension) {
        this.extension = extension;
    }
}
