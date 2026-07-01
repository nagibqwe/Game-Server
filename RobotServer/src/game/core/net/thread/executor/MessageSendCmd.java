package game.core.net.thread.executor;

import com.game.player.structs.Player;
import game.core.message.SMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * @Desc TODO
 * @Date 2020/9/30 18:16
 * @Auth ZUncle
 */
public class MessageSendCmd implements Cmd {

    static Logger logger = LogManager.getLogger(MessageSendCmd.class);

    static final String MSG_ORDER_KEY = "MSG_ORDER_KEY";

    final Player player;
    final SMessage message;

    public MessageSendCmd(Player player, SMessage msg) {
        this.player = player;
        this.message = msg;
    }

    @Override
    public void action() {
        IoSession session = player.getSession();
        if (session == null) {
            return;
        }
        if (session.isClosing()) {
            return;
        }
        long start = TimeUtils.Time();
        int msgOrder = 0;
        int msgTime = (int) (System.currentTimeMillis() / 1000L); // second
        Object orderObj = session.getAttribute(MSG_ORDER_KEY);
        if (orderObj != null && orderObj instanceof Integer) {
            msgOrder = ((Integer) orderObj) + 1;
        }
        session.setAttribute(MSG_ORDER_KEY, msgOrder);

        IoBuffer buff = IoBuffer.allocate(1024);
        IoBuffer bb = IoBuffer.allocate(1024);
        buff.setAutoExpand(true);
        buff.setAutoShrink(true);
        bb.setAutoExpand(true);
        bb.setAutoShrink(true);

        int length = message.getData().length + Integer.SIZE / Byte.SIZE * 4;//buff.limit();
        int cryptMsgOrder = (msgOrder ^ (0x6b << 10)) ^ length;

        bb.putInt(msgTime);
        bb.putInt(message.getId()); // msgId
        bb.put(message.getData()); // msg content
        bb.flip();
        byte[] byteArray = new byte[bb.limit()];

        byte key = (byte) (cryptMsgOrder % 100);
        int all = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte b = bb.get(i);
            int tm = b;
            if (tm < 0) {
                tm += 256;
            }
            all += tm;
            b ^= key;
            byteArray[i] = b;
        }

        buff.putInt(length);
        buff.putInt(cryptMsgOrder);
        buff.putInt(all);
        buff.put(byteArray);
        buff.flip();
        buff.rewind();

        bb.clear();

        IoBuffer sendbuf = null;
        if (message.getId() == heartMessage.ReqHeart.MsgID.eMsgID_VALUE) {
            session.setAttribute("sendheart", 1);
            logger.info(session.getAttribute("roleId") + "心跳压包！");
        }
        if (session.containsAttribute("SEND_BUF")) {
            sendbuf = (IoBuffer) session.getAttribute("SEND_BUF");
        } else {
            sendbuf = IoBuffer.allocate(1024);
            sendbuf.setAutoExpand(true);
            sendbuf.setAutoShrink(true);
            session.setAttribute("SEND_BUF", sendbuf);
        }
        if (sendbuf == null) {
            return;
        }
        sendbuf.put(buff);
        buff.clear();

        long offset = TimeUtils.Time() - start;
        if (offset > 200) {
            logger.error("发送消息 message={} id={} offset={}", message.getData().getClass().getSimpleName(), message.getId(), offset);
        }
    }
}
