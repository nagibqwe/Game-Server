/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.manager;

import com.game.activity.manager.ActivityManager;
import com.game.bravepeak.manager.BravePeakManager;
import com.game.count.manager.CountManager;
import com.game.couplefight.manager.CouplefightManager;
import com.game.crosshorseboss.manager.CrossHorseBossManager;
import com.game.crossrank.manager.CrossRankManager;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.eightdiagrams.manager.EightDiagramsManager;
import com.game.fightroom.manager.FightManager;
import com.game.gameserver.manager.GameServerManager;
import com.game.goddevilwar.manager.GodDevilWarManager;
import com.game.guildcrossfud.manager.FudManager;
import com.game.http.manager.HttpManager;
import com.game.ninedaysfocused.manager.NineDaysFocusedManager;
import com.game.peak.manager.PeakManager;
import com.game.questionnaire.manager.QuestionnaireManager;
import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import com.game.universe.manager.UniverseManager;
import com.game.worldanswer.manager.WorldAnswerManager;
import com.game.worldbonfire.manager.WorldBonfireManager;
import com.game.zone.manager.ZoneManager;
import game.core.script.ScriptManager;

/**
 * 所有单例管理器的工厂类
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class Manager {

    public static ScriptManager scriptManager = ScriptManager.getInstance();
    public static GameServerManager gameServerManager = GameServerManager.getInstance();
    public static FightManager fightManager = FightManager.getInstance();
    public static ZoneManager zoneManager = ZoneManager.getInstance();
    public static ActivityManager activityManager = ActivityManager.getInstance();
    public static SoulAnimalForestManager soulAnimalForestManager = SoulAnimalForestManager.getInstance();
    public static DailyActiveManager dailyActiveManager = DailyActiveManager.getInstance();
    public static WorldAnswerManager worldAnswerManager = WorldAnswerManager.getInstance();
    public static NineDaysFocusedManager nineDaysFocusedManager = NineDaysFocusedManager.getInstance();
    public static GodDevilWarManager godDevilWarManager = GodDevilWarManager.getInstance();
    public static BravePeakManager bravePeakManager = BravePeakManager.getInstance();
    public static WorldBonfireManager worldBonfireManager = WorldBonfireManager.getInstance();
    public static QuestionnaireManager questionnaireManager = QuestionnaireManager.getInstance();
    public static UniverseManager universeManager = UniverseManager.getInstance();
    public static HttpManager httpManager = HttpManager.getInstance();
    public static CrossRankManager crossRankManager = CrossRankManager.getInstance();
    public static FudManager fudManager =FudManager.getInstance();
    public static PeakManager peakManager = PeakManager.getInstance();
    public static CountManager countManager = CountManager.getInstance();
    public static EightDiagramsManager eightDiagramsManager = EightDiagramsManager.getInstance();
    public static CrossHorseBossManager crossHorseBossManager = CrossHorseBossManager.getInstance();
    public static CouplefightManager couplefightManager = CouplefightManager.getInstance();

}
