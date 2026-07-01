package common.mail;

import com.data.MessageString;
import com.data.struct.ReadArray;
import com.game.mail.script.IMailScript;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.player.structs.Item;
import com.game.script.struct.ScriptEnum;
import com.game.utils.MessageUtils;
import game.message.CrossServerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/8/2 15:38
 * @Auth ZUncle
 */
public class MailScript implements IMailScript {


    /**
     * 邮件类容链接
     * @param args
     * @return
     */
    @Override
    public String linkContext(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object str : args) {
            sb.append(str).append("@_@");
        }
        sb.delete(sb.lastIndexOf("@_@"), sb.length());
        return sb.toString();
    }

    /**
     * 发送邮件
     *
     * @param receiverId   邮件接收者
     * @param mailType     邮件类型
     * @param sender       邮件发送者
     * @param mailTitle    邮件标题
     * @param mailContent  邮件内容
     * @param items        邮件附加道具
     * @param changeReason 邮件发送原因码
     */
    @Override
    public void sendMail(long receiverId, int mailType, int sender, Object mailTitle, Object mailContent, List<Item> items, int changeReason) {

        List<CrossServerMessage.dropItemInfo> list = new ArrayList<>();
        for (Item item : items) {
            CrossServerMessage.dropItemInfo.Builder mItem = CrossServerMessage.dropItemInfo.newBuilder();
            mItem.setItemModelId(item.getModelId());
            mItem.setNum(item.getCount());
            mItem.setIsBind(item.isBind());
            mItem.setNotice(false);
            list.add(mItem.build());
        }

        GlobalPlayerWorldInfo player = Manager.globalPlayerManager.getPlayers().get(receiverId);

        CrossServerMessage.P2GSendMailReward.Builder message = CrossServerMessage.P2GSendMailReward.newBuilder();
        message.setRoleId(receiverId);
        message.setSender(sender);
        message.setMailType(mailType);
        message.setTitle(mailTitle == null ? "1" : mailTitle.toString());
        message.setContent(mailContent == null ? "1" : mailContent.toString());
        message.addAllItems(list);
        message.setReason(changeReason);
        MessageUtils.send_to_server(player.getPlat(), player.getServerId(), CrossServerMessage.P2GSendMailReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 发送系统邮件
     *
     * @param receiverId   邮件接收者
     * @param mailTitle    邮件标题
     * @param mailContent  邮件内容
     * @param items        邮件附加道具
     * @param changeReason 邮件发送原因码
     */
    @Override
    public void sendMail(long receiverId, Object mailTitle, Object mailContent, List<Item> items, int changeReason) {
        sendMail(receiverId, MessageString.System, MessageString.System, mailTitle, mailContent, items, changeReason);
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.MailScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
