/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backgrand.structs;

/**
 * @author Administrator
 */
public class RankData {

    private int rank;            //玩家排行名次

    private long roleId;         //玩家角色Id

    private String roleName;     //玩家角色名

    private String rankData;     //排行数据

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

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

}
