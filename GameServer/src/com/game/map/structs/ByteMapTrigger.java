/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

import com.game.structs.Vector3;
import game.core.map.Position;
import java.util.List;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class ByteMapTrigger {
    
    //触发器
    private int modelID;
    //名字
    private String name;
    //触发器坐标
    private Vector3 pos;
    //触发器形状
    private int shape;
    
    //触发器类型
    private String type;

    //参数列表
    private List<String> inArgs;

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getInArgs() {
        return inArgs;
    }

    public void setInArgs(List<String> inArgs) {
        this.inArgs = inArgs;
    }
    
    
    
   
    
    
}
