/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.horse.structs;

/**
 *
 * @author admin
 */
public enum HorseRideStateEnum {
    UnRide(0),
    Ride(1),
    Fly(2);
    
    private int state;                          //状态
    
    private HorseRideStateEnum(int state){
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public static HorseRideStateEnum getStateEnumByInt(int type){
        for(HorseRideStateEnum temp : values()){
            if(temp.getState() == type){
                return temp;
            }
        }
        return UnRide;
    }
}
