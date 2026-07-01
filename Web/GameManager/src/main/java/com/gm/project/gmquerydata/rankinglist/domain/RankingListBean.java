package com.gm.project.gmquerydata.rankinglist.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 后台指令日志对象 log_backgmcmdlog
 * 
 * @author gm
 * @date 2021-09-10
 */
public class RankingListBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "排名")
    private String rank;

    /** 时间 */
    @Excel(name = "角色ID")
    private String roleId;

    /** backUser */
    @Excel(name = "角色名")
    private String roleName;

    /** cmd */
    @Excel(name = "排行数据")
    private String rankData;




    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRankData() {
        return rankData;
    }

    public void setRankData(String rankData) {
        this.rankData = rankData;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
