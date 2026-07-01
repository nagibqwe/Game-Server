package com.game.buff.script;

import com.game.buff.structs.Buff;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import game.message.CommonMessage;

/**
 * @author zenghai
 */
public interface IBuffScript extends IBuffEffect {

    /**
     * 上线计算buff
     * @param player
     */
    void online(Player player);

    /**
     * 离线计算buff
     * @param player
     * */
    void offline(Player player);

    /**
     * 玩家死亡计算buff
     * @param player
     */
    void onDie(Player player);

    /**
     * 宠物死亡计算buff
     * @param pet
     */
    void onDie(Pet pet);

    /**
     * 切换地图 清理玩家的BUFF
     * @param player
     */
    void changeMapClear(Player player);

    /**
     * 脱战检查buff
     * @param entity
     */
    void onLiveBattle(Entity entity);

    /**
     * 添加buff
     * @param source
     * @param target
     * @param buffId
     * @return
     */
    boolean onAddBuff(Fighter source, Fighter target, int buffId);

    /**
     * 移除buff
     * @param entity
     * @param buffId
     * @return
     */
    boolean onRemoveBuff(Fighter entity, int buffId);

    void tick(Fighter player);

    void tick(Pet pet, Fighter acter);

    /**
     * 能否增加buff
     * @param player
     * @param buffId
     * @return
     */
    boolean isCanAdd(Player player, int buffId);

    Buff getBuff(Fighter fighter, int buffId);

    /**
     * 去掉隐身buff
     * @param player
     */
    void removeBuffInvisible(Entity player);

    /**
     * 是否具体某个类型的BUFF
     * @param fighter
     * @param type
     * @return
     */
    Buff isHaveBuff(Fighter fighter, int type, int groupId);

    /**
     * 是否具体某个类型的BUFF
     * @param fighter
     * @param type
     * @return
     */
    boolean isHaveBuff(Fighter fighter, int type);

    /**
     * 移除所有减溢buff
     * @param fighter
     */
    void removeAllHarmBuff(Fighter fighter);

    /**
     * buff是否存在
     * @param fighter
     * @param buffId
     */
    boolean isExist(Fighter fighter, int buffId);

    /**
     * 获取增益或者减buff数量
     * @param fighter
     * @param type
     * @return
     */
    int getBuffNum(Fighter fighter, int type);

    /**
     * buff 更新
     * @param owner
     * @param buff
     */
    void sendBuffUpdate(Fighter owner, Buff buff);

    /**
     * 添加变身BUFF
     * @param player
     * @param id
     */
    void onReqAddChangeModeBuff(Player player,int id);

    /**
     * 删除变身buff
     * @param player
     * @param id
     */
    void onReqRemoveChangeModeBuff(Player player,int id);


    /**
     * 删除变身BUFF身上中的技能
     * @param source
     * @param id
     */
    void removeChangeModeSkill(Fighter source,int id);

    /**
     * 添加变身BUFF身上中的技能
     * @param source
     * @param id
     */
    void addChangeModeSkill(Fighter source,int id);

    /**
     * buff序列化
     * @param buff
     * @return
     */
    CommonMessage.Buff.Builder build(Buff buff);

}
