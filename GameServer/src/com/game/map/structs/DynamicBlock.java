/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

import com.game.utils.Utils;
import game.core.map.Position;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class DynamicBlock {
    private String key; //阻挡名字
    private boolean open = false; //是否生效
    private int celCount;
    private final HashMap<Integer, Integer> blocks = new HashMap<>();
    private Position center;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Position getCenter() {
        return center;
    }

    public HashMap<Integer, Integer> getBlocks() {
        return blocks;
    }

    //是否在动态阻挡中
    public boolean isBlock(Position pos) {
        if (open) {
            return false;
        }

        Integer index = celCount * pos.ceilY() + pos.ceilX();
        return blocks.containsKey(index);
    }

    public boolean isBlock(Position from, Position to) {
        if (open) {
            return false;
        }

        if (isBlock(to)) {
            return true;
        }

        float roads = Utils.getDistance(from, to);
        Position dir = Utils.getDir(from, to);
        while (roads > 0) {
            roads -= 0.4;
            if (roads < 0) {
                break;
            }
            Position check = Utils.getPosByDir(from, dir, roads);
            if (isBlock(check)) {
                return true;
            }
        }

        return false;
    }

    public void init(MapObject map, ByteDynamicBlock datas) {
        this.celCount = map.getColCellCount();
        this.key = datas.m_name;
        this.center = datas.m_pos;

        switch (datas.m_type) {
            case MapDefine.DYNAMIC_BLOCK_CIRCLE:
                initCircle(datas.m_pos, datas.m_radius);
                break;
            case MapDefine.DYNAMIC_BLOCK_ABB:
                initAABB(datas.m_pos, datas.m_size.pro_builder(0.5f));
                break;
            case MapDefine.DYNAMIC_BLOCK_OBB:
                initOBB(datas.m_pos, datas.m_size.pro_builder(0.5f), datas.m_dir);
                break;
            default:
        }
    }

    private void initOBB(Position center, Position halfsize, Position dir) {

        Position normal = new Position(dir.getY(), -dir.getX());
        Position h = dir.pro_builder(halfsize.getX());
        Position v = normal.pro_builder(halfsize.getY());
        Position a = center.add_builder(h).add_builder(v);
        Position b = center.add_builder(h).dec_builder(v);
        Position c = center.dec_builder(h).add_builder(v);
        Position d = center.dec_builder(h).dec_builder(v);

        int startX = (int) Math.min(Math.min(a.getX(), b.getX()), Math.min(c.getX(), d.getX())) - 1;
        int endX = (int) Math.max(Math.max(a.getX(), b.getX()), Math.max(c.getX(), d.getX())) + 1;
        int startY = (int) Math.min(Math.min(a.getY(), b.getY()), Math.min(c.getY(), d.getY())) - 1;
        int endY = (int) Math.max(Math.max(a.getY(), b.getY()), Math.max(c.getY(), d.getY())) + 1;

        Position temp;

        for (int y = startY; y <= endY; y++) {

            for (int x = startX; x <= endX; x++) {

                temp = new Position(0.5f + x, 0.5f + y);
                temp.dec(center);
                float tempX = Utils.DirXDir(temp, dir);
                float tempY = Utils.DirXDir(temp, normal);
                
                if (Math.abs(tempX) <= halfsize.getX() && Math.abs(tempY) <= halfsize.getY()) {

                    int blockId = celCount * y + x;
                    blocks.put(blockId, blockId);
                }
            }
        }

    }

    private void initAABB(Position center, Position halfsize) {
        int startX = (int) (center.getX() - halfsize.getX()) - 1;
        int startY = (int) (center.getY() - halfsize.getY()) - 1;
        int endX = (int) (center.getX() + halfsize.getX()) + 1;
        int endY = (int) (center.getY() + halfsize.getY()) + 1;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {

                float checkX = x + 0.5f - center.getX();
                float checkY = y + 0.5f - center.getY();

                if (Math.abs(checkX) <= halfsize.getX() && Math.abs(checkY) <= halfsize.getY()) {
                    int blockId = celCount * y + x;
                    blocks.put(blockId, blockId);
                }
            }
        }
    }

    private void initCircle(Position center, float r) {
        int startX = (int) (center.getX() - r) - 1;
        int startY = (int) (center.getY() - r) - 1;
        int endX = (int) (center.getX() + r) + 1;
        int endY = (int) (center.getY() + r) + 1;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {

                Position check = new Position(x + 0.5f, y + 0.5f);
                if (Utils.getDistance(check, center) > r) {
                    continue;
                }
                int blockId = celCount * y + x;
                blocks.put(blockId, blockId);
            }

        }
    }

}
