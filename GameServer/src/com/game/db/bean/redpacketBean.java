/**
 * Auto generated, do not edit it
 *
 * redpacket
 */
package com.game.db.bean;

import game.core.db.BaseBean;

public class redpacketBean extends BaseBean {

    private long rpId; // 红包的ID值
    private String redpacket; // 红包的实例内容
    private long rpCreateTime; // 红包创建时间
    private int rpType; // 红包类型

    /**
     * get 红包的ID值
     *
     * @return
     */
    public long getRpId() {
        return rpId;
    }

    /**
     * set 红包的ID值
     *
     * @param rpId
     */
    public void setRpId(long rpId) {
        this.rpId = rpId;
    }

    /**
     * get 红包的实例内容
     *
     * @return
     */
    public String getRedpacket() {
        return redpacket;
    }

    /**
     * set 红包的实例内容
     *
     * @param redpacket
     */
    public void setRedpacket(String redpacket) {
        this.redpacket = redpacket;
    }

    /**
     * get 红包创建时间
     *
     * @return
     */
    public long getRpCreateTime() {
        return rpCreateTime;
    }

    /**
     * set 红包创建时间
     *
     * @param rpCreateTime
     */
    public void setRpCreateTime(long rpCreateTime) {
        this.rpCreateTime = rpCreateTime;
    }

    /**
     * get 红包类型
     *
     * @return
     */
    public int getRpType() {
        return rpType;
    }

    /**
     * set 红包类型
     *
     * @param rpType
     */
    public void setRpType(int rpType) {
        this.rpType = rpType;
    }

}
