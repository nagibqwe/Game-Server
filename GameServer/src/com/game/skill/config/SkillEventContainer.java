/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config;

import com.game.skill.config.event.*;
import com.game.skill.structs.SkillDefine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class SkillEventContainer {

    private static final Logger log = LogManager.getLogger(SkillEventContainer.class);

    private final ConcurrentHashMap<String, SkillVisual> visuals = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, SkillVisual> getVisuals() {
        return visuals;
    }

    public SkillVisual GetSkillVisualBySV(String key) {
        return visuals.get(key);
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        SkillEventContainer manager;

        Singleton() {
            this.manager = new SkillEventContainer();
        }

        SkillEventContainer getProcessor() {
            return manager;
        }
    }

    public static SkillEventContainer getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //加载技能事件
    public void load(String SkillXml) throws Exception {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try (InputStream in = new FileInputStream(SkillXml)) {
            Document doc = builder.parse(in);
            NodeList root = doc.getElementsByTagName("Root");
            Node node = root.item(0);
            NodeList configs = node.getChildNodes();
            for (int index = 0; index < configs.getLength(); ++index) {
                Node config = configs.item(index);
                String SkillInfo = config.getNodeName();
                if (!SkillInfo.equals("SkillInfo")) {
                    continue;
                }
                //解析字段
                String name = config.getAttributes().getNamedItem("Name").getNodeValue();
                int ftp = Integer.parseInt(config.getAttributes().getNamedItem("FrameCount").getNodeValue());

                int superArmorCount = 0;//Integer.parseInt(config.getAttributes().getNamedItem("SuperArmorCount").getNodeValue());

                boolean canMove = Integer.parseInt(config.getAttributes().getNamedItem("IsSkillObject").getNodeValue()) == 1;

                boolean canPlayerValid = Integer.parseInt(config.getAttributes().getNamedItem("IsPlayerValid").getNodeValue()) == 1;
                //初始化技能效果
                SkillVisual visusl = new SkillVisual();
                visusl.setId(name);
                visusl.setCanMove(canMove);
                visusl.setSuperArmorCount(superArmorCount);
                visusl.setCombofps(0);
                visusl.setFps(ftp);
                visusl.setCanPlayerValid(canPlayerValid);

                NodeList datas = config.getChildNodes();
                for (int i = 0; i < datas.getLength(); ++i) {
                    Node data = datas.item(i);
                    if (!data.getNodeName().equals("Event")) {
                        continue;
                    }
                    //开始解析技能事件
                    int eventType = Integer.parseInt(data.getAttributes().getNamedItem("EventType").getNodeValue());
                    int envetFps = Integer.parseInt(data.getAttributes().getNamedItem("Frame").getNodeValue());
                    int eventId = Integer.parseInt(data.getAttributes().getNamedItem("ID").getNodeValue());
                    String param = data.getAttributes().getNamedItem("Param").getNodeValue();

                    SkillEvent event = newEvent(eventType);
                    event.setEventType(eventType);
                    event.setEventFrame(envetFps);
                    event.setEventID(eventId);
                    event.split(param);
                    visusl.getEvents().put(event.getEventID(), event);
                    visusl.getEventList().add(event);
                    if (event instanceof PlayLockTrajectoryEvent) {
                        visusl.setLockTrajact(true);
                    }

                    if (event instanceof PlaySelfMoveEvent) {
                        PlaySelfMoveEvent move = (PlaySelfMoveEvent) event;
                        visusl.setMoveDis(move.getMoveDis());
                    }
                    if (event instanceof PlaySkillObjectEvent) {
                        visusl.setSummon(true);
                        PlaySkillObjectEvent move = (PlaySkillObjectEvent) event;
                        visusl.setMoveDis(move.getMaxDis());
                    }

                    if (event instanceof PlaySimpleSkillObjectEvent) {
                        visusl.setSummon(true);
                        PlaySimpleSkillObjectEvent move = (PlaySimpleSkillObjectEvent) event;
                        visusl.setMoveDis(move.getMaxDis());
                    }

                    if (event instanceof  PlayHitEvent) {
                        PlayHitEvent hit = (PlayHitEvent) event;
                        if (visusl.getAttackDis() == 0 || visusl.getAttackDis() > hit.getFindInfo().getAttackDis()) {
                            visusl.setAttackDis(hit.getFindInfo().getAttackDis());
                        }
                    }
                }
                visuals.put(visusl.getId(), visusl);
            }
        }
    }

    private SkillEvent newEvent(int eventId) {
        switch (eventId) {
            case SkillDefine.PLAY_LOCK_TRAJECTORY_EVENT:
                return new PlayLockTrajectoryEvent();
            case SkillDefine.PLAY_SELF_MOVE_EVENT:
                return new PlaySelfMoveEvent();
            case SkillDefine.PLAY_HIT_EVENT:
                return new PlayHitEvent();
            case SkillDefine.PLAY_SKILL_OBJECT_EVENT:
                return new PlaySkillObjectEvent();
            case SkillDefine.PLAY_SIMPLE_SKILL_OBJECT_EVENT:
                return new PlaySimpleSkillObjectEvent();
            default: //其他类型的
                return new CommonEvent();

        }
    }

}
