/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.structs;

import game.core.map.Position;

public class TeamCall {
    private int id;
    private long MapID;
    private int line;       
    private Position pos;   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMapID() {
        return MapID;
    }

    public void setMapID(long MapID) {
        this.MapID = MapID;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
    
}
