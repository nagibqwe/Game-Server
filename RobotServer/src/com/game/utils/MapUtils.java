/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.map.manager.MapsConfigManager;
import com.game.map.structs.ByteMapCfg;
import com.game.map.structs.MapDefine;
import com.game.structs.Position;
import com.game.structs.RoadPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hewei@haowan123.com
 */
public class MapUtils {

    public final static int TransportControlType_World = 1;
    public final static int TransportControlType_Task = 2;
    public final static int TransportControlType_WorldBoss = 3;
    public final static int TransportControlType_WorldNpc = 4;
    public final static int TransportControlType_CopyMap = 5;//资源争夺战 城市争夺战传送

    private final static Logger log = LogManager.getLogger(MapUtils.class);

    /**
     * 寻路算法
     *
     * @param mapDataId
     * @param start 开始节点
     * @param end 结束节点
     * @param steps 计算步数
     * @return
     */
    public static ArrayList<Position> findRoads(int mapDataId, Position start, Position end, int steps) {
        //返回的移动路径
        ArrayList<Position> result = new ArrayList<>();
        if (start.compare(end)) {
            result.add(end);
            return result;
        }

        RoadPoint startRoad = new RoadPoint();
        startRoad.setPos(start);

        RoadPoint endRoad = new RoadPoint();
        endRoad.setPos(end);

        //待计算路点
        List<RoadPoint> waitting = new ArrayList<>();
        //已计算路点
        HashMap<Integer, RoadPoint> counted = new HashMap<>();
        //遍历过的路点索引
        HashSet<Integer> passed = new HashSet<>();

        waitting.add(startRoad);
        passed.add(start.getIndex(mapDataId));

        int step = 0;

        while (waitting.size() > 0 && (step < steps || steps == -1)) {
            //取出优先级最高的路点(权值最小)
            RoadPoint road = waitting.remove(0);
            step++;

            //到达终点
            if (road.getPos().compare(end)) {
                endRoad = road;
                break;
            }
            //加入已计算的路点
            counted.put(road.getIndex(mapDataId), road);

            //获取周围格子信息
            List<Position> rounds = getRoundGrid(road.getPos(), 1);

            for (Position round : rounds) {
                //已经遍历过
                if (passed.contains(round.getIndex(mapDataId))) {
                    continue;
                }
                //在地图内是不可行走点
                if (!isCanMove(mapDataId, round)) {
                    continue;
                }

                RoadPoint roundPoint = new RoadPoint();
                roundPoint.setPos(round);
                roundPoint.setFarther(road.getIndex(mapDataId));
                //加入遍历过格子
                passed.add(round.getIndex(mapDataId));
                //计算权值
                roundPoint.setWeight(countWeight(round, end));
                //插入到待计算列表
                insert(waitting, roundPoint);
            }
        }

        //计算路径
        if (endRoad.getFarther() != -1) {
            //已经找到终点
            RoadPoint _node = endRoad;
            result.add(0, end);
            while (_node.getFarther() != -1) {
                _node = counted.get(_node.getFarther());
                result.add(0, _node.getPos());
            }
        } else if (step == steps && waitting.size() > 0) {
            //到达寻路最大步数
            RoadPoint _node = waitting.get(0);
            result.add(0, _node.getPos());
            while (_node.getFarther() != -1) {
                _node = counted.get(_node.getFarther());

                result.add(0, _node.getPos());
            }
        }

        return result;
    }

    /**
     * 获得周围的格子
     *
     * @param pos
     * @param radius
     * @return
     */
    private static List<Position> getRoundGrid(Position pos, int radius) {
        List<Position> points = new ArrayList<>();
        //左边界
        int left = (int) pos.getX() - radius;
        left = left > 0 ? left : 0;
        //右边界
        int right = (int) pos.getX() + radius;
        //下边界
        int down = (int) pos.getY() - radius;
        down = down > 0 ? down : 0;
        //上边界
        int up = (int) pos.getY() + radius;

        for (int i = left; i <= right; i++) {
            for (int j = down; j <= up; j++) {
                points.add(new Position(i, j));
            }
        }
        return points;
    }

    /**
     * 计算权值 曼哈顿方法
     *
     * @param start 开始格子
     * @param end 结束给子
     * @return
     */
    private static int countWeight(Position start, Position end) {
        return (int) (Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY()));
    }

    /**
     * 按权值插入队列
     *
     * @param waitting 队列
     * @param road 路点
     */
    private static void insert(List<RoadPoint> waitting, RoadPoint road) {

        if (waitting.isEmpty()) {
            waitting.add(road);
            return;
        }

        //头部插入
        for (int i = 0; i < waitting.size(); i++) {
            RoadPoint _temp = waitting.get(i);
            if (_temp.getWeight() < road.getWeight()) {
                continue;
            }
            waitting.add(i, road);
            return;
        }
        waitting.add(road);
    }

    /**
     * 获取一个 以curPos为原点、 range 为半径 的一个圆内的 一个随机点
     *
     * @param curPos
     * @param range
     * @return
     */
    public static Position getRandomPos(Position curPos, int range) {
        //随机出一个不超过range的距离
        float distance = (float) getDistance(curPos, new Position());
        float r = RandomUtils.randomFloatValue(5, range) / distance;
        //向量随机转动一个角度
        float pX = -curPos.getX();
        float pY = -curPos.getY();
        float angle = (float) Math.PI * RandomUtils.randomFloatValue(0, 2.0f);
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        //b = （xcosa-ysina,xsina+ycosa）
        float rX = r * (pX * cos - pY * sin);
        float rY = r * (pX * sin + pY * cos);
        float x = curPos.getX() + rX;
        float y = curPos.getY() + rY;
        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;
        return new Position(x, y);
    }

    //是否阻挡
    public static boolean isCanMove(int mapDataId, Position pos) {
        //地图格子类型  大于2的类型可以移动， 大于1的可以跳跃
        int type = getPosType(mapDataId, pos);
        if (type == MapDefine.Cell_Type_None) {
            return false;
        }
        if (type == MapDefine.Cell_Type_Block) {
            return false;
        }
        if (type == MapDefine.Cell_Type_Jump) {
            return false;
        }
        if (type == MapDefine.Cell_Type_UserBlock) {
            return false;
        }

        return !isDynamicBlock(mapDataId, pos);
    }

    //是否动态阻挡点
    public static boolean isDynamicBlock(int mapDataId, Position pos) {
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(mapDataId);
        if (null == mapCfg) {
            log.error("初始化地图失败mapCfg == null, mapID" + mapDataId);
            return false;
        }

        return false;
    }

    //获取地图格子类型
    public static int getPosType(int mapDataId, Position pos) {
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(mapDataId);
        if (null == mapCfg) {
            log.error("初始化地图失败mapCfg == null, mapID" + mapDataId);
            return MapDefine.Cell_Type_UserBlock;
        }

        if (pos.getY() >= mapCfg.getRowCellCount()) {
            //log.info("getBlocks error curPos overflow");
            return MapDefine.Cell_Type_Block;
        }

        if (pos.getX() >= mapCfg.getColCellCount()) {
            return MapDefine.Cell_Type_Block;
        }

        int ix = (int) pos.getX();
        int iy = (int) pos.getY();

        if (ix < 0 || iy < 0) {
            return MapDefine.Cell_Type_UserBlock;
        }
//        int index = mapCfg.getColCellCount() * iy + ix;
//
//        if (index >= mapCfg.getBlocks().length || index < 0) {
//            return MapDefine.Cell_Type_UserBlock;
//        }

        return mapCfg.getBlocks()[iy][ix];
    }

    //获取两点距离
    public static double getDistance(Position pos1, Position pos2) {
        return Math.sqrt((pos1.getX() - pos2.getX()) * (pos1.getX() - pos2.getX()) + (pos1.getY() - pos2.getY()) * (pos1.getY() - pos2.getY()));
    }

    //沿着向量方向curPos -> targetPos， 取距离curPos 点 range位置处的一个坐标点
    public static Position getDirPos(Position curPos, Position targetPos, float range) {
        if (range == 0f) {
            return curPos;
        }
        float distance = (float) getDistance(curPos, targetPos);
        if (distance == 0f) {
            return curPos;
        }
        float r = range / distance;
        float x = r * targetPos.getX() - r * curPos.getX() + curPos.getX();
        float y = r * targetPos.getY() - r * curPos.getY() + curPos.getY();
        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;
        return new Position(x, y);
    }
}
