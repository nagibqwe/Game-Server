/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.skill.structs.SkillDefine;
import com.game.structs.Position;


/**
 *
 * @author zenghai
 */
public class ShapeUtils {

    //判断两点间是否超过一定距离  
    private static boolean isFarThanDistance(Position a, Position b, int distance) {
        float x = (a.getX() - b.getX());
        float y = (a.getY() - b.getY());

        return x * x + y * y > distance * distance;
    }

    //判断一个点是否在矩形内，这个要求与坐标轴平行  
    private static boolean inRect(float minx, float maxx, float miny, float maxy, Position p) {
        //判断点p的xy是否在矩形上下左右之间
        return p.getX() >= minx && p.getX() <= maxx && p.getY() >= miny && p.getY() <= maxy;
    }

    //获取检测点的相对坐标  
    private static Position changeAbsolute2Relative(Position oPoint, Position dir, Position checkPoint)//需要转换的坐标  
    {

        Position dirA2B = Utils.getDir(oPoint, checkPoint);
        float disA2B = Utils.getDistance(oPoint, checkPoint);

        //平行向量
        if (dirA2B.getX() == dir.getX() && dirA2B.getY() == dir.getY()) {
            dirA2B.setX(0);
            dirA2B.setY(disA2B);
            return dirA2B;
        } else {

            float axb = dir.getX() * dirA2B.getX() + dir.getY() * dirA2B.getY();
            float a_ = (float) Math.sqrt(dir.getX() * dir.getX() + dir.getY() * dir.getY());
            float b_ = (float) Math.sqrt(dirA2B.getX() * dirA2B.getX() + dirA2B.getY() * dirA2B.getY());
            float y = axb / (a_ * b_) * disA2B;
            float x = (float) Math.sqrt(disA2B * disA2B - y * y);

            dirA2B.setX(x);//相对坐标x  
            dirA2B.setY(y); //相对坐标y  
            return dirA2B;
        }
    }

    private static boolean inRectRelat(Shape shape, Position oPoint, Position dir, Position checkPoint) {

        Position rePoint = changeAbsolute2Relative(oPoint, dir, checkPoint);

        float skillWidth = shape.getWidth() + 1f;//矩形攻击区域的宽度  
        float skillLong = shape.getHeight() + 0.5f;//矩形攻击区域的高度  

        //宽度是被AB平分的，从A点开始延伸长度  
        return inRect(-skillWidth / 2, skillWidth / 2, 0, skillLong, rePoint);//相对坐标下攻击范围  
    }

    private static boolean inFan(Shape shape, Position oPoint, Position dir, Position checkPoint) {

        float disA2B = Utils.getDistance(oPoint, checkPoint);
        //超出扇形半径
        if (disA2B > shape.getR()) {
            return false;
        }

        Position dirA2B = Utils.getDir(oPoint, checkPoint);

        if (dirA2B.getX() == dir.getX() && dirA2B.getY() == dir.getY()) {
            return true;
        }

        float axb = dir.getX() * dirA2B.getX() + dir.getY() * dirA2B.getY();
        float a_ = (float) Math.sqrt(dir.getX() * dir.getX() + dir.getY() * dir.getY());
        float b_ = (float) Math.sqrt(dirA2B.getX() * dirA2B.getX() + dirA2B.getY() * dirA2B.getY());
        float r = (float) (Math.acos(axb / (a_ * b_)) * 180 / Math.PI);

        return 2 * r < shape.getAngle();

    }

    private static boolean inCircle(Shape shape, Position oPoint, Position dir, Position checkPoint) {

        float dis = Utils.getDistance(oPoint, checkPoint);
        return dis < shape.getR() + 1f;
    }

    /**
     * 检测目标是否在攻击范围
     *
     * @param shape
     * @param oPoint 原点
     * @param dir 攻击方向
     * @param checkPoint 目标位置
     * @return
     */
    public static boolean inAttackArea(Shape shape, Position oPoint, Position dir, Position checkPoint) {

        //矩形
        if (shape.getType() == SkillDefine.SkillShape_Rectangle) {
            return inRectRelat(shape, oPoint, dir, checkPoint);
        }
        if (shape.getType() == SkillDefine.SkillShape_Sector) {
            return inFan(shape, oPoint, dir, checkPoint);
        }
        if (shape.getType() == SkillDefine.SkillShape_Circle) {
            return inCircle(shape, oPoint, dir, checkPoint);
        }

        return false;
    }

}
