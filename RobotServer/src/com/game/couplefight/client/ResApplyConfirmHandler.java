package com.game.couplefight.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 报名确认
 * @Auther: gouzhongliang
 * @Date: 2021/8/10 17:01
 */
public class ResApplyConfirmHandler extends Handler {
    @Override
    public void action(RMessage mess) {
        CouplefightMessage.ResApplyConfirm res = (CouplefightMessage.ResApplyConfirm)mess.getData();

        CouplefightMessage.ReqApplyConfirm.Builder req = CouplefightMessage.ReqApplyConfirm.newBuilder();
        req.setConfirm(true);
        IoSession iosession = mess.getSession();
        Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
        player.sendMsg(CouplefightMessage.ReqApplyConfirm.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }
}
