package com.game.equip.manager;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_inten_main_Bean;
import com.game.backpack.structs.Equip;
import com.game.equip.script.IEquipScript;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipWash;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.EquipPartBaseType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.EquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 装备管理器
 *
 * @author Administrator
 */
public class EquipManager {

    private static final Logger log = LogManager.getLogger(EquipManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        EquipManager processor;

        Singleton() {
            this.processor = new EquipManager();
        }

        EquipManager getProcessor() {
            return processor;
        }
    }

    public static EquipManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 装备物品
     *
     * @param player  玩家
     * @param equipId 装备id
     * @param inherit 是否继承星级
     */
    public void wear(Player player, long equipId, boolean inherit) {
        Equip equip = (Equip) Manager.backpackManager.manager().getItemById(player, equipId);
        if (equip == null) {
            return;
        }
        equip.use(player, 1, inherit?1:0, IDConfigUtil.getLogId());
    }

    /**
     * 卸下物品 (如果身上找不到，再去找坐骑身上)
     *
     * @param player  玩家
     * @param equipId 装备Id
     */
    public void unwear(Player player, long equipId) {
        //获得卸下装备
        Equip equip = getEquipById(player, equipId);
        if (equip == null) {
            return;
        }
        equip.unuse(player, 1, IDConfigUtil.getLogId());
    }

    /**
     * 获得装备栏中装备
     *
     * @param player  玩家
     * @param equipId 装备id
     * @return equip
     */
    public Equip getEquipById(Player player, long equipId) {
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart != null) {
                Equip equip = equipPart.getEquip();
                if(null != equip && equip.getId() == equipId) {
                    return equipPart.getEquip();
                }
            }
        }
        return null;
    }

    /**
     * 初始化装备部位
     * */
    public void initAllEquipParts(Player player) {
        List<EquipPart> parts = player.getEquipParts();
        if (null == parts) {
            parts = new ArrayList<>();
        }

        if (0 != parts.size()) {
            return;
        }
        /**
         * 遍历所有的数据
         * 生成新的equip part实例
         * */
        int type = EquipPartBaseType.PLAYER;
        for (Cfg_Equip_inten_main_Bean bean : CfgManager.getCfg_Equip_inten_main_Container().getValuees()) {
            if (type == bean.getType()) {
                type++;
                EquipPart part = new EquipPart();
                part.setCurrentExp(0);
                part.setType(bean.getType() - EquipPartBaseType.PLAYER);
                part.setLevel(0);
                parts.add(part);
            }
        }
        sendEquipPartInfoToClient(player);
    }

    /**
     * 发送所有装备部位信息给前端
     * 用于新建玩家和玩家上线
     * */
    public void sendEquipPartInfoToClient(Player player) {
        EquipMessage.ResEquipPartInfo.Builder builder = EquipMessage.ResEquipPartInfo.newBuilder();
        player.getEquipParts().forEach(
                equipPart -> {
                    addPartInfoToMessage(player, equipPart, builder);
                }
        );
        MessageUtils.send_to_player(player, EquipMessage.ResEquipPartInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
    /**
     * 发送某个部位的某个方面的信息更新
     * 比如装备更新套装id，等
     * */
    public void updateOneEquipPartInfo(Player player, int partIndex) {
        EquipMessage.ResEquipPartInfo.Builder builder = EquipMessage.ResEquipPartInfo.newBuilder();
        EquipPart part = player.getEquipParts().get(partIndex);
        if(null == part) {
            log.error("updateOneEquipPartInfo wrong partIndex: " + partIndex);
            return;
        }

        addPartInfoToMessage(player, part, builder);
        MessageUtils.send_to_player(player, EquipMessage.ResEquipPartInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取某个部位的装备
     * list的下标0-7表示部位，配置表中是1-8
     * */
    public Equip getEquipByType(Player player, int part) {
        if(0 > part || part >= player.getEquipParts().size()) {
            return null;
        }
        return player.getEquipParts().get(part).getEquip();
    }

    //获取最低新
    public int getAllStar(Player player) {
        int star = 0;
        for (int i = 0; i < EquipDefine.EquipPart_Num; ++i) {
            star += getEquipStar(player, i);
        }
        return star;
    }

    /**
     * 获取某个装备的星级/钻石数
     * */
    public int getOneEquipStar(Player player, int partIndex) {
        return getEquipStar(player, partIndex);
    }

    //获取衣服星级
    public int getClothesStar(Player player) {
        return getEquipStar(player, EquipDefine.EquipPart_Breastplate);
    }

    //获取武器的星级
    public int getWeaponStar(Player player) {
        return getEquipStar(player, EquipDefine.EquipPart_Weapon);
    }

    public IEquipScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EquipManagerBaseScript);
        if (is instanceof IEquipScript) {
            return (IEquipScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    private int getEquipStar(Player player, int part) {
        Equip equip = getEquipByType(player, part);
        if(null == equip) {
            return 0;
        }

        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        return bean.getDiamond_Number();
    }
    /**
     * 用于生成每个部位的信息的协议
     * */
    private void addPartInfoToMessage(Player player, EquipPart equipPart, EquipMessage.ResEquipPartInfo.Builder builder) {
        EquipMessage.EquipPartInfo.Builder partBuilder = EquipMessage.EquipPartInfo.newBuilder();
        /**
         * 设置部位编号
         * */
        partBuilder.setType(equipPart.getType());
        /**
         * 设置装备
         * */
        if(null != equipPart.getEquip()) {
            partBuilder.setEquip(Manager.backpackManager.manager().buildItemInfo(equipPart.getEquip()));
        }
        /**
         * 设置强化信息
         * */
        EquipMessage.EquipStrengthInfo.Builder infoBuilder = EquipMessage.EquipStrengthInfo.newBuilder();
        infoBuilder.setExp(equipPart.getCurrentExp());
        infoBuilder.setLevel(equipPart.getLevel());
        infoBuilder.setType(equipPart.getType());
        partBuilder.setStrengthInfo(infoBuilder);

        /**
         * 装备洗练
         */
        if (equipPart.getEquipWashs().size() != 0) {
            for (EquipWash equipWash : equipPart.getEquipWashs().values()) {
                EquipMessage.EquipWashInfo.Builder msgWashInfo = EquipMessage.EquipWashInfo.newBuilder();
                msgWashInfo.setIndex(equipWash.getId());
                msgWashInfo.setValue(equipWash.getValue());
                msgWashInfo.setPer(equipWash.getPer());
                partBuilder.addWashInfo(msgWashInfo);
            }
        }

        /**
         * 部位宝石
         */
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LianQiGem)) {
            EquipMessage.gemPartInfo.Builder gemPartInfo = EquipMessage.gemPartInfo.newBuilder();
            gemPartInfo.addAllGemIds(equipPart.getGemInfo().getGemIds());
            gemPartInfo.addAllJadeIds(equipPart.getGemInfo().getJadeIds());
            gemPartInfo.setLevel(equipPart.getGemInfo().getLevel());
            gemPartInfo.setExp(equipPart.getGemInfo().getExp());
            partBuilder.setGemInfo(gemPartInfo);
        }

        builder.addInfos(partBuilder);
    }

}
