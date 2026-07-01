package com.game.equip.script;

import com.data.bean.Cfg_Equip_synthesis_Bean;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.Equip;
import com.game.equip.struct.EquipPart;
import com.game.player.structs.Player;
import game.message.EquipMessage;
import game.message.EquipMessage.*;

import java.util.List;
import java.util.Set;

/**
 * 装备脚本接口
 *
 * @author Administrator
 */
public interface IEquipScript {
    /**
     * 装备出售
     * @param player
     * @param messInfo
     */
    void OnReqEquipSell(Player player, EquipMessage.ReqEquipSell messInfo);


    /**
     * GM发送指定属性装备
     * @param player
     * @param id
     * @param bind
     * @return
     */
    boolean sendAppointEquip(Player player, int id, boolean bind);

    /**
     *判断身上装备的品质达到数量 阶数 品质 星数
     * @param player
     * @param layer
     * @param quality
     * @param diamond
     * @return
     */
    int isEquipQuality(Player player, int layer, int quality, int diamond);

    /**
     * 装备售卖
     * @param player
     * @param id
     * @param isAuto
     */
    void equipSell(Player player, List<Long> id, boolean isAuto);

    /**
     * 设置分解的品质
     * @param player
     * @param mess
     */
    void OnReqAutoResolveSet(Player player, ReqAutoResolveSet mess);


    /**
     * 装备的分解设置
     * @param player
     * @param mess
     */
    void OnReqEquipResolveSet(Player player, ReqEquipResolveSet mess);

    void OnEquipSuit(Player player, int suitId, long equipId);

    /**
     * 装备合成
     * @param equipSyn      合成配置表
     * @param eqs           装备材料id列表
     * @param isHaveItem    是否自动购买，不自动购买物品不足则不能合成
     */
    void OnReqEquipSynthetic(Player player, Cfg_Equip_synthesis_Bean equipSyn, List<Long> eqs, boolean isHaveItem, int type);

    /**
     * 移除装备
     * @param player
     * @param equip
     * @param reason
     * @param actionId
     * @return
     */
    boolean removeEquip(Player player, Equip equip, int reason, long actionId);


    /**
     * 移除装备 不通知客户端
     * @param player
     * @param equip
     * @param reason
     * @param actionId
     * @param deleteList
     * @param changeList
     * @return
     */
    boolean removeEquipNotNotic(Player player, Equip equip, int reason, long actionId, Set<Long> deleteList, Set<Long> changeList);

    /**
     * 更新穿戴装备
     * @param player
     * @param source
     * @param target
     */
    void equipSuitInheritance(Player player, Equip source, Equip target);

    /**
     * 装备分解
     * @param player
     * @param messInfo
     */
    void OnReqEquipSynSplit(Player player, ReqEquipSynSplit messInfo);

    /**
     * 处理ReqEquipStrengthUpLevel
     * */
    void onReqEquipStrengthUpLevel(int type, Player player);
    /**
     * 计算装备强化战力
     * @param player
     * @return
     */
    int calculateStrengthenPower(Player player);

    /**
     * 计算装备部位属性
     * @param player
     * @param attribute
     * @param part
     */
    void compEquipPartAttr(Player player, BaseIntAttribute attribute, EquipPart part);

    /**
     * 装备洗练
     * @param player
     * @param part
     * @param type
     * @param indexs
     */
    void onReqEquipWash(Player player, int part, boolean type, List<Integer> indexs);

    /**
     * 装备洗练次数
     * @param player
     * @return
     */
    int equipWashNum(Player player);

    /**
     * 获取套装阶数的个数
     * @param player
     * @return
     */
    int gainSuitNum(Player player, int layer);

    int gainSuitNum(Player player, int layer, int level);

    /**
     * 上线灵体信息
     */
    void sendSpiritInfo(Player player);

    /**
     * 灵体解封蕴养
     *
     * @param cfgId 蕴养配置表id
     */
    void upLevel(Player player, int cfgId);

    /**
     * 点亮灵星
     */
    void upStar(Player player, int star);

    /**
     * 激活灵体
     */
    void activeSpirit(Player player, int spiritId);

    /**
     * 收集装备
     * @param type 0：背包来源，1：合成途径
     */
    void collectEquip(Player player, int spiritId, int equipId, int type, boolean isInherit);

    /**
     * 获取符合条件的灵体件数
     * @param player
     * @param grad
     * @param quality
     * @param star
     */
    int getSpiritNum(Player player, int grad, int quality, int star);

    /**
     * 神品装备提升
     * @param player
     * @param type 类型 1升星 2升阶
     * @param part 部位
     */
    void onReqShenpinEquipUp(Player player, int type, int part);


    /**
     * 获取装备数量
     * @param player
     * @param equipId
     */
    int getEquipNum(Player player ,int equipId);


    /**
     * 获取某部位 装备 阶数和品质
     * @param player
     * @param part
     * @param grade
     * @param quility
     * @return
     */
    int getGrowup_wear_equip(Player player,int part,int grade,int quility);


    /**
     * 合成X件X阶及以上的X色X星装备,条件id_阶数id_品质id_星数_件数:55
     * @param player
     * @param part
     * @param grade
     * @param quility
     * @return
     */
    int getComposeEquip(Player player,int grade, int quility,int start);


    /**
     * 计算经验加成
     * @param player
     * @return
     */
    int calEquipRate(Player player,int partType);

}
