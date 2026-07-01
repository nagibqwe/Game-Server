package common.pushmess;

import com.game.script.structs.ScriptEnum;
import com.game.txpushsdk.script.IPushMessageScript;
import com.game.txpushsdk.struct.PushMessInfo;
import com.tencent.xinge.Message;
import static com.tencent.xinge.Message.TYPE_NOTIFICATION;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;
import game.core.net.Config.ServerConfig;
import game.core.util.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class PushMessageScript implements IPushMessageScript {

    private static final Logger log = LogManager.getLogger(PushMessageScript.class);

    private final XingeApp xinge;
    private final XingeApp xingeIOS;

    private final long XingeAccessIDIOS = 2200280304L;
    private final String XingeKeyIOS = "a2fb61a7027f5c05a16c982364f59935";

    private final long XingeAccessID = 2100269535L;
    private final String XingeKey = "81752f991bac90d0d429990a150d4163";
    //IOSENV_DEV
    private final int IOSDEV = XingeApp.IOSENV_DEV;//IOSENV_PROD;

    public PushMessageScript() {
        xinge = new XingeApp(getAccessID(1), getXingeKey(1));
        xingeIOS = new XingeApp(getAccessID(2), getXingeKey(2));
    }

    private long getAccessID(int type) {
        switch (ServerConfig.getLangType()) {
            case "tw":
                if (type == 1) {
                    return 2100295526L;
                } else {
                    return 2200295528L;
                }
            case "ros": {
                if (type == 1) {
                    return 2100293965L;
                } else {
                    return 2200293981L;
                }
            }
            default:
                break;
        }
        if (type == 1) {
            return XingeAccessID;
        } else {
            return XingeAccessIDIOS;
        }
    }

    private String getXingeKey(int type) {
        switch (ServerConfig.getLangType()) {
            case "tw":
                if (type == 1) {
                    return "ad956b91c3af1e7edf4f518b98bbcfd6";
                } else {
                    return "39c815f3eaacf7c4c2e87a8a72770e2f";
                }
            case "ros": {
                if (type == 1) {
                    return "331e1422f91ced70d1a4a220bf3557e3";
                } else {
                    return "150a9e67666d1a7bef0ebdf99542f5bd";
                }
            }
            default:
                break;
        }
        if (type == 1) {
            return XingeKey;
        } else {
            return XingeKeyIOS;
        }
    }

    private int getIOSDEV() {
        switch (ServerConfig.getLangType()) {
            case "tw":
                break;
            default:
                break;
        }
        return XingeApp.IOSENV_DEV;
    }

    public XingeApp getXingeIOS() {
        return xingeIOS;
    }

    @Override
    public int getId() {
        return ScriptEnum.PushMessageBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void dealPush(PushMessInfo pmi) {
        if (pmi.getBigtype() == 1) {
            pushSinglerMess(pmi);
        } else {
            if (pmi.getBigtype() == 3) {
                //后台公告通知处理
                int serverId = ServerConfig.getServerId() % 2048;
                //非1服与999服， 不通知活动的开启
                if (!(serverId == 1 || serverId == 501)) {
                    return;
                }
                String httpurl = ServerConfig.getErrorLogUrl();
                StringBuilder result = new StringBuilder();
                StringBuilder sb = new StringBuilder();
                sb.append("title=").append(pmi.getTitle()).append("&content=").append(pmi.getContext());
                int code = HttpUtils.sendPost(httpurl + "/xingeApp/pushAllMess", sb.toString(), result);
                log.error("发送" + pmi.getTitle() + "code=" + code + ", result = " + result.toString());
                return;
            }

            pushAllMess(pmi);
        }
    }

    private void pushSinglerMess(PushMessInfo pmi) {
//        if (pmi.getDectype() == 1) {
//            Message mess = new Message();
//            mess.setType(TYPE_NOTIFICATION);
//            mess.setTitle(pmi.getTitle());
//            mess.setMultiPkg(1);
//            mess.setContent(pmi.getContext());
////            mess.setStyle(new Style(0, 1, 1, 0, 0));
//            JSONObject json = xinge.pushSingleAccount(0, pmi.getTag(), mess);
//            LOGGER.error("androd, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
//        } else {
//        MessageIOS iomess = new MessageIOS();
//        iomess.setAlert(pmi.getContext());
//        iomess.setType(TYPE_NOTIFICATION);
//        iomess.setBadge(1);
        MessageIOS message = new MessageIOS();
        message.setExpireTime(86400);
        message.setAlert(pmi.getContext());
        message.setBadge(1);
        message.setSound("beep.wav");
        TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
        message.addAcceptTime(acceptTime1);
        JSONObject json = xingeIOS.pushSingleAccount(0, pmi.getTag(), message, getIOSDEV());
        log.error("ios, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
//            if (pmi.getDectype() == 0) {
        Message mess = new Message();
        mess.setType(TYPE_NOTIFICATION);
        mess.setTitle(pmi.getTitle());
        mess.setMultiPkg(1);
        mess.setContent(pmi.getContext());
        mess.setStyle(new Style(0, 1, 1, 0, 0));
        json = xinge.pushSingleAccount(0, pmi.getTag(), mess);
        log.error("androd, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
//            }
//        }
    }

    private void pushAllMess(PushMessInfo pmi) {
        JSONObject json = XingeApp.pushAllAndroid(getAccessID(1), getXingeKey(1), pmi.getTitle(), pmi.getContext());
        log.error("androd all, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
        json = XingeApp.pushAllIos(getAccessID(2), getXingeKey(2), pmi.getContext(), getIOSDEV());
        log.error("ios all, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
    }

    public static void main(String[] args) {
        PushMessInfo pmi = new PushMessInfo();
        pmi.setBigtype(1);
        pmi.setContext("test");
        pmi.setDectype(0);
        pmi.setTag("857472546024291717");
        pmi.setTitle("test");
        PushMessageScript pms = new PushMessageScript();
        pms.dealPush(pmi);
        JSONObject json = pms.getXingeIOS().queryTags(0, 100);
//        pms.getXingeIOS().
//XingeApp.pushAccountAndroid(2100269535L, "81752f991bac90d0d429990a150d4163", pmi.getTitle(), pmi.getContext(), "717015665825254638");
//        xinge.pushSingleAccount(0, "717015665825254638", pmi.getContext());
//        JSONObject json = null;// XingeApp.pushAllAndroid(2100264649L, "3b0532c27bf90789f5e55183f2e07eb5", pmi.getTitle(), pmi.getContext());
        System.out.println("androd all, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
//        if (json.has("ret_code")) {
//            int ret_code = json.getInt("ret_code");
//
//            System.out.println("androd all, ret_code=" + ret_code);
//        }xingeIOS
        json = XingeApp.pushAllIos(2200280304L, "a2fb61a7027f5c05a16c982364f59935", pmi.getContext(), XingeApp.IOSENV_DEV);
        System.out.println("ios all, " + pmi.getTag() + pmi.getContext() + " =" + json.toString());
//        if (json.has("ret_code")) {
//            int ret_code = json.getInt("ret_code");
//            System.out.println("ios all, ret_code=" + ret_code);
//        }
    }

}
