package com.kits.project.serverList.domain;

public class ChannelGrid {

    /** x8内的发行渠道编号 */
    private Long chnmId;

    /** 渠道号 */
    private Long chnId;

    /** x8内渠道名称 */
    private String chnmName;

    public Long getChnmId() {
        return chnmId;
    }

    public void setChnmId(Long chnmId) {
        this.chnmId = chnmId;
    }

    public Long getChnId() {
        return chnId;
    }

    public void setChnId(Long chnId) {
        this.chnId = chnId;
    }

    public String getChnmName() {
        return chnmName;
    }

    public void setChnmName(String chnmName) {
        this.chnmName = chnmName;
    }
}
