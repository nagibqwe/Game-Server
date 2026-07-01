package com.game.manager;

import com.game.boss.manager.BossManager;
import com.game.couplefight.CoupleManager;
import com.game.equip.manager.EquipManager;
import com.game.register.manager.RegisterManager;
import com.game.cooldown.manager.CooldownManager;
import com.game.dailyactivity.manager.DailyActivityManager;
import com.game.gm.manager.CmdManager;
import com.game.guild.manager.GuildManager;
import com.game.map.manager.MapsConfigManager;
import com.game.player.manager.PlayerManager;
import com.game.skill.manager.SkillManager;
import com.game.task.manager.TaskManager;
import game.core.script.ScriptManager;

public class Manager {
    /**
     * 脚本管理类
     */
    public static ScriptManager scriptManager = ScriptManager.getInstance();

    /**
     * 命令处理
     */
    public static CmdManager cmd = CmdManager.getInstance();

    /**
     * 地图配置管理类
     */
    public static MapsConfigManager mapCfgManager = MapsConfigManager.getInstance();

    /**
     * 玩家管理类
     */
    public static PlayerManager playerManager = PlayerManager.getInstance();

    /**
     * 连接注册管理类
     */
    public static RegisterManager registerManager = RegisterManager.getInstance();

    /**
     * 冷却管理类
     */
    public static CooldownManager cooldownManager = CooldownManager.getInstance();

    /**
     * 技能管理类
     */
    public static SkillManager skillManager = SkillManager.getInstance();

    /**
     * 装备管理类
     */
    public static EquipManager equipManager = EquipManager.getInstance();

    /**
     * 日常活动管理类
     */
    public static DailyActivityManager dailyActivityManager = DailyActivityManager.getInstance();

    /**
     * 任务管理类
     */
    public static TaskManager taskManager = TaskManager.getInstance();

    /**
     * BOSS管理类
     */
    public static BossManager bossManager = BossManager.getInstance();

    /**
     * 公会管理类
     */
    public static GuildManager guildManager = GuildManager.getInstance();

    /**
     * 仙侣对决
     */
    public static CoupleManager coupleManager = CoupleManager.getInstance();

}
