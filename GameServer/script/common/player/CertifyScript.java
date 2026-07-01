package common.player;

import com.data.FunctionStart;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.player.script.ICertifyScript;
import com.game.player.structs.Player;
import com.game.register.structs.UserInfo;
import com.game.script.structs.ScriptEnum;

import java.util.List;

/**
 * 实名认证脚本
 */
public class CertifyScript implements ICertifyScript {

    @Override
    public int getId() {
        return ScriptEnum.CertifyScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void playerOnline(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Certification)) {
            return;
        }
        if(player.isCertify()){
            return;
        }
        UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());
        if(userInfo == null){
            return;
        }
        if(!userInfo.isCertify()){
            return;
        }

        player.setCertify(true);

        sendMail(player);
    }

    @Override
    public void onReqNoticeCertifySuccess(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Certification)) {
            return;
        }
        if(player.isCertify()){
            return;
        }
        UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());
        if(userInfo != null){
            if(!userInfo.isCertify()){
                userInfo.setCertify(true);
            }
        }

        player.setCertify(true);

        sendMail(player);
    }

    private void sendMail(Player player){
//        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.Real_Name_Authentication_MailTitle, MessageString.Real_Name_Authentication_Mail, null);
        List<Item> createItems = Item.createItems(Global.Name_Certification_Award);
        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.Name_Certification_Award_MailTitle
                , MessageString.Name_Certification_Award_Mail, createItems, ItemChangeReason.NameCertificationAward);
    }
}
