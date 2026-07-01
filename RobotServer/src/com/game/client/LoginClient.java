/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.client;

import com.game.server.RobotServer;
import com.game.server.worker.InnerWorker;
import com.game.server.worker.SendMessWorker;
import com.game.structs.Config;
import com.game.utils.RandomUtils;
import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.codec.RobotProtocolCodecFactoryimplements;
import game.core.net.communication.CommunicationC;
import game.core.net.server.IServer;
import game.core.util.TimeUtils;
import game.message.LoginMessage.ReqLogin;
import game.message.RecycleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

public class LoginClient implements IServer {

    private final Logger log = LogManager.getLogger(Client.class);

    public int id = 0;

    private IoSession session;

    private long waitTime = 60000;//1分钟执行一次

    private long lastDoTime = 0l;
    @Override
    public void doCommand(IoSession session, IoBuffer buf) {
        try {
            // 读取消息ID
            int msgId = buf.getInt();
            log.error(" ----- msgId ---- {}",msgId);
            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
            if (dic != null) {
                Handler handler = dic.getHandlerInstance();
                // 读取消息内容
                byte[] msgData = new byte[buf.remaining()];
                buf.get(msgData);
                // 创建消息对象
                //RMessage message = new RMessage();
                //message.setId(msgId);
                //message.setSession(session);
                //message.setData(dic.getMessage(), msgData);
                //handler.action(message);
                Client.getRecvExcutor().addTask(session.getId(), new InnerWorker(msgId, session, msgData));
                if (msgId == 100102){
                    lastDoTime =  TimeUtils.Time();
                }

            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

    @Override
    public void sessionCreate(IoSession session) {

        this.session = session;
        ReqLogin.Builder msg1 = ReqLogin.newBuilder();
        msg1.setPlatformUid("5c7631a49f16f30a6dddb4e12bcc86de");
        msg1.setPlatformName("haowanrtx" + id);
        msg1.setAccessToken("185UYRLXqTfpmm9buC8ncgooJBUzoYuDuMiFzguh33hm8aaJRhNZToAsKUWB6Vmp2zCD73nnPod6RKAlqzroVgpswqti50B8D6Dtd-3wEEPLf60dbvtMNHHSncBmwnERqPWqD-ShwNeKNJrDT0fH2cdPbBHbtYjroqxKULUUiLNReVWnVpATFNxOchEDGwAU7oAqWR9jwPhFxbbBWv5bhSygkXPdMx0u5AaETE2zhICpQOneJziuILrJGDHfADRj0A5ctFy6roGkBE14gP4jGNHBf_8RVA1gq18_prxWmUkuT-G2ieIyP-llFMdqf0Ig40daY_7O6ciOrsep0HUToqk4WusoN9AgsDznIvP2ZV4jxakl0_0Xm0Jlh_Ls5eUD2S1GJkQuL2JFoCKXSqDLHYHnM41pfCqwMN_zeYWO_PVoH9sz2FaTtoyduWJa4OUv896w3xVk");
        ReqLogin.Builder msg2 = ReqLogin.newBuilder();
        msg2.setPlatformUid("1b1a37f78d60d17eac370c0058b34091");
        msg2.setPlatformName("haowanrtx1"+ id);
        msg2.setAccessToken("185VUMGVQWaXUYG109Ip93FOnwb3FdKMGybX0botGn0U9VaiqWp-DtZFWo6zkM8_IUV7EGUkKb79ybUoPEbWkXa0SlSOerYAbvki16C64UgcxWzpiFLSvt4fjl7j_TVLCU_vRGeb3OCvS4nMVA46EligwJeU4zvRU5k_JbvO35q5WToO_RnZcIAvLoUKdlSgEvsi8V4IYKbztk2vYZwMr_h_JSRxBYLPM9kW-NtsP7Y6EdU_HtRBY4O4JDUDC4fiOn-e6ScqITeM9Iqy5ISsPKxcadRpyzfqHHrDRURqW4vFnzMw-MiyBJkXSeHoXFhMJuG9LpLnnjTOTMKhTi9JiFMHoKHg5ZFN6JEr5QX8K_WTN15tkGGyaIY4isHcYvpcscSBJ8qgGbAJ14PgO4GjLKOECg3JETQFVcXNq8e1Hwz62uF_55vHmCKGiHs_GCFM_fuVaWOY");
        ReqLogin.Builder msg3 = ReqLogin.newBuilder();
        msg3.setPlatformUid("58418ec318b554a74a419b7034623952");
        msg3.setPlatformName("haowanrtx2"+ id);
        msg3.setAccessToken("185QGRVAttop3H5IG8ezqms9AKY9eRRKyyV0-WcPom44vyCkuKnOn_caQkIiF97X31w1TEIkZXXPM5yudSdzAo_ff1LOc4brbJXzVt54LiDfgBxv34aelD3VU3mkm6SkjSS9coYU7meYs3nW5fDiwBd0VQPaflsQxHGupBuXgSFNkyYyOp_uXyEI86NCXrS8YldAVjhzuNkdUfbj9HiObddlVQHHYdVq37_XUTOA2reukwPYBQJ27iODnCUMYQqfur57dAIjryEl33uuRx11mTZlL3TO152fweF0X5q2MkMfsW5vukpWL0VKdEp-bOPRi5Dm0Dgi4DHS83d_H577kVfb3oN7lXUV5JpW2DgJejO9OIabqSQeBYsSPshUquI9qxl46-zLENEaizWypitwYooxwZafFYcYXYxDkqC3FshA16BujSGZZmdI_RUuquRsVf4p6SMF");
        ReqLogin.Builder[] list = {msg1, msg2, msg3};
        ReqLogin.Builder msg = list[RandomUtils.random(1, 2)];
        msg.setImei("1");
        msg.setMachineCode("2");
        msg.setMac("3");
        msg.setPlatformAccount("");
        session.setAttribute("USERR", msg.getPlatformName() + "_" + TimeUtils.NowToString());
        //session.write(new SMessage(ReqLogin.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
        log.error("连接成功 发送 登录信息  id {} session.getId{} ",this.id,session.getId());
        RobotServer.getSendExcutor().addTask(session.getId(), new SendMessWorker(session,
                new SMessage(ReqLogin.MsgID.eMsgID_VALUE, msg.build().toByteArray()),1));
    }
    private void sendMsg(SMessage message) {


    }


    @Override
    public void sessionClosed(IoSession session) {
        log.error(session.getAttribute("USERR") + "连接断开了！");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        log.error(session.getAttribute("USERR") + "连接异常！");
    }

    @Override
    public ProtocolCodecFactory getCodecFactory() {
        return new RobotProtocolCodecFactoryimplements();
    }

    public void start(String ip, int port) {
        CommunicationC connector = new CommunicationC(this);
        connector.initialize();
        connector.connect(ip, port);
    }

    public boolean  canConnect(long timeNow){
        if (lastDoTime<=0){
            return false;
        }
        if (timeNow - lastDoTime < waitTime){
            return false;
        }
        return true;
    }

    public void setLastDoTime(long lastDoTime) {
        this.lastDoTime = lastDoTime;
    }
}
