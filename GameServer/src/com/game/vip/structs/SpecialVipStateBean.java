package com.game.vip.structs;

/**
 * 特有vip信息
 */
public class SpecialVipStateBean {
    /**
     * 高级VIP 是否激活
     */
    private boolean isNormalVipActivate;
    /**
     * 高级至尊VIP 是否激活
     */
    private boolean isHighVipActivate;
    /**
     * 达到高级VIP 是否通知客户端
     */
    private boolean isNormalVipNotifyClient;
    /**
     * 达到高级至尊VIP 是否通知客户端
     */
    private boolean isHighVipNotifyClient;

    public boolean isNormalVipActivate() {
        return isNormalVipActivate;
    }

    public void setNormalVipActivate(boolean normalVipActivate) {
        isNormalVipActivate = normalVipActivate;
    }

    public boolean isHighVipActivate() {
        return isHighVipActivate;
    }

    public void setHighVipActivate(boolean highVipActivate) {
        isHighVipActivate = highVipActivate;
    }

    public boolean isNormalVipNotifyClient() {
        return isNormalVipNotifyClient;
    }

    public void setNormalVipNotifyClient(boolean normalVipNotifyClient) {
        isNormalVipNotifyClient = normalVipNotifyClient;
    }

    public boolean isHighVipNotifyClient() {
        return isHighVipNotifyClient;
    }

    public void setHighVipNotifyClient(boolean highVipNotifyClient) {
        isHighVipNotifyClient = highVipNotifyClient;
    }
}
