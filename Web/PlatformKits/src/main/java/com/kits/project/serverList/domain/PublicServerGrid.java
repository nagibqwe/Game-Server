package com.kits.project.serverList.domain;

public class PublicServerGrid {
    /** 额外参数 */
    private String extParams;
    /** 策略编号 */
    private Long policyId;
    /** 渠道ID列表 */
    private String chnIds;

    public String getExtParams() {
        return extParams;
    }

    public void setExtParams(String extParams) {
        this.extParams = extParams;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getChnIds() {
        return chnIds;
    }

    public void setChnIds(String chnIds) {
        this.chnIds = chnIds;
    }
}
