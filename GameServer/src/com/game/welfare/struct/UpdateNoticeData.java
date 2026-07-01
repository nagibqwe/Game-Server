package com.game.welfare.struct;

import io.netty.util.internal.ConcurrentSet;

import java.util.List;
import java.util.Vector;

/**
 *@desc 更新公告数据结构
 *@author gaozhaoguang
 *@date Created on 2020/7/14 12:15
 **/
public class UpdateNoticeData {

    //公告文本
    private String noticeText = "";
    //奖励物品
    private String awardItems = "";
    //已经领取的角色
    private Vector<Long> receiveRoles = new Vector();


    public String getNoticeText() {
        return noticeText;
    }

    public void setNoticeText(String noticeText) {
        this.noticeText = noticeText;
    }

    public String getAwardItems() { return awardItems; }

    public void setAwardItems(String awardItems) {
        this.awardItems = awardItems;
    }

    public void setReceiveRoles(Vector<Long> receiveRoles) { this.receiveRoles = receiveRoles; }

    public Vector<Long> getReceiveRoles() { return this.receiveRoles; }

    /**
     * 清理已经领取的角色列表
     */
    public void clearReceives(){
        receiveRoles.clear();
    }

    /**
     * 添加角色,标记已经领取
     * @param roleID
     */
    public void addReceive(Long roleID){
        receiveRoles.add(roleID);
    }

    /**
     * 判断档期那更新公告是否有内容
     */
    public boolean isValidNotice(){
        return noticeText != null && !noticeText.isEmpty();
    }
    /**
     * 判断当前更新公告是否有效
     * @return
     */
    public boolean isValidItems(){
        return awardItems != null && !awardItems.isEmpty();
    }
    /**
     * 检查能够领取奖励
     * @param roleID
     * @return
     */
    public boolean isReceivableAward(Long roleID){
        if (isValidNotice() &&  isValidItems()){
            return receiveRoles.contains(roleID);
        }
        return false;
    }
}
