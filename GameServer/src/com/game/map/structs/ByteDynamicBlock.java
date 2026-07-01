/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

import com.game.map.utils.MapInfoStream;
import com.game.structs.Vector3;
import game.core.map.Position;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author zenghai
 */
public class ByteDynamicBlock {

    public int m_id;
    public String m_name;
    public Vector3 m_pos = Vector3.zero; //原点
    public Vector3 axis;
    public float angle;
    public int m_modeType = 0;
    public String m_modelResourcePath = "";
    public Vector3 m_modelOffset = Vector3.zero;
    public Vector3 m_modelRotation = Vector3.zero;
    public Vector3 m_modelScale = Vector3.one;
    public int m_type = MapDefine.DYNAMIC_BLOCK_NONE;
    public float m_radius = 1.0f;  //半径
    public Position m_size = Position.ZEROPOS;  //大小
    public String m_openMessageId = "";
    public String m_openArgFormat = "";
    public List<String> m_openArgs = null;
    public float m_openDelayTime = 0.0f;
    public String m_closeMessageId = "";
    public String m_closeArgFormat = "";
    public List<String> m_closeArgs = null;
    public float m_closeDelayTime = 0.0f;
    public int openCondition = 0;
    public String OpenConditionParam = "";
    public Position m_dir = Position.ZEROPOS;  //OBB方向

    public void params(MapInfoStream r) throws IOException {
        m_pos = r.readVector3();
        axis = r.readVector3();
        angle = r.readFloat();
        m_name = r.readString(32);
        m_id = r.readInt();

        m_modeType = r.readShort();
        m_modelResourcePath = r.readString();
        m_modelOffset = r.readVector3();
        m_modelRotation = r.readVector3();
        m_modelScale = r.readVector3();
        m_dir = r.readVector2();
        m_type = r.readShort();
        m_radius = r.readFloat();
        m_size = r.readVector2();

        m_openMessageId = r.readString();
        m_openArgFormat = r.readString();
        m_openArgs = r.readStringList();
        m_openDelayTime = r.readFloat();

        m_closeMessageId = r.readString();
        m_closeArgFormat = r.readString();
        m_closeArgs = r.readStringList();
        m_closeDelayTime = r.readFloat();

        openCondition = r.readShort();
        OpenConditionParam = r.readString();
    }

    public Vector3 getAxis() {
        return axis;
    }

    public void setAxis(Vector3 axis) {
        this.axis = axis;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getM_modeType() {
        return m_modeType;
    }

    public void setM_modeType(int m_modeType) {
        this.m_modeType = m_modeType;
    }

    public String getM_modelResourcePath() {
        return m_modelResourcePath;
    }

    public void setM_modelResourcePath(String m_modelResourcePath) {
        this.m_modelResourcePath = m_modelResourcePath;
    }

    public Vector3 getM_modelOffset() {
        return m_modelOffset;
    }

    public void setM_modelOffset(Vector3 m_modelOffset) {
        this.m_modelOffset = m_modelOffset;
    }

    public Vector3 getM_modelRotation() {
        return m_modelRotation;
    }

    public void setM_modelRotation(Vector3 m_modelRotation) {
        this.m_modelRotation = m_modelRotation;
    }

    public Vector3 getM_modelScale() {
        return m_modelScale;
    }

    public void setM_modelScale(Vector3 m_modelScale) {
        this.m_modelScale = m_modelScale;
    }

    public int getM_type() {
        return m_type;
    }

    public void setM_type(int m_type) {
        this.m_type = m_type;
    }

    public float getM_radius() {
        return m_radius;
    }

    public void setM_radius(float m_radius) {
        this.m_radius = m_radius;
    }

    public Position getM_size() {
        return m_size;
    }

    public void setM_size(Position m_size) {
        this.m_size = m_size;
    }

    public String getM_openMessageId() {
        return m_openMessageId;
    }

    public void setM_openMessageId(String m_openMessageId) {
        this.m_openMessageId = m_openMessageId;
    }

    public String getM_openArgFormat() {
        return m_openArgFormat;
    }

    public void setM_openArgFormat(String m_openArgFormat) {
        this.m_openArgFormat = m_openArgFormat;
    }

    public List<String> getM_openArgs() {
        return m_openArgs;
    }

    public void setM_openArgs(List<String> m_openArgs) {
        this.m_openArgs = m_openArgs;
    }

    public float getM_openDelayTime() {
        return m_openDelayTime;
    }

    public void setM_openDelayTime(float m_openDelayTime) {
        this.m_openDelayTime = m_openDelayTime;
    }

    public String getM_closeMessageId() {
        return m_closeMessageId;
    }

    public void setM_closeMessageId(String m_closeMessageId) {
        this.m_closeMessageId = m_closeMessageId;
    }

    public String getM_closeArgFormat() {
        return m_closeArgFormat;
    }

    public void setM_closeArgFormat(String m_closeArgFormat) {
        this.m_closeArgFormat = m_closeArgFormat;
    }

    public List<String> getM_closeArgs() {
        return m_closeArgs;
    }

    public void setM_closeArgs(List<String> m_closeArgs) {
        this.m_closeArgs = m_closeArgs;
    }

    public float getM_closeDelayTime() {
        return m_closeDelayTime;
    }

    public void setM_closeDelayTime(float m_closeDelayTime) {
        this.m_closeDelayTime = m_closeDelayTime;
    }

    public int getOpenCondition() {
        return openCondition;
    }

    public void setOpenCondition(int openCondition) {
        this.openCondition = openCondition;
    }

    public String getOpenConditionParam() {
        return OpenConditionParam;
    }

    public void setOpenConditionParam(String OpenConditionParam) {
        this.OpenConditionParam = OpenConditionParam;
    }

}
