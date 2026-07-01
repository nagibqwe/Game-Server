package com.game.mail.script;

import com.game.player.structs.Item;
import game.core.script.IScript;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/8/2 15:12
 * @Auth ZUncle
 */
public interface IMailScript extends IScript {

    /**
     * 邮件类容链接
     * @param args
     * @return
     */
    String linkContext(Object... args);

    /**
     * 发送邮件 九 零一起 玩 www.901  75.com
     * @param receiverId  邮件接收者
     * @param mailType    邮件类型
     * @param sender      邮件发送者
     * @param mailTitle   邮件标题
     * @param mailContent       邮件内容
     * @param items    邮件附加道具
     * @param changeReason      邮件发送原因码
     */
    void sendMail(long receiverId, int mailType, int sender, Object mailTitle, Object mailContent, List<Item> items, int changeReason);

    /**
     * 发送系统邮件
     * @param receiverId  邮件接收者
     * @param mailTitle   邮件标题
     * @param mailContent       邮件内容
     * @param items    邮件附加道具
     * @param changeReason      邮件发送原因码
     */
    void sendMail(long receiverId, Object mailTitle, Object mailContent, List<Item> items, int changeReason);

}
