package com.game.fight.structs;

/**
 * Created by huhu on 2017/9/26.
 */
public class FightFanalEnum {
    public enum MagicPosType{

        OwnerPos(0),        //
        MainTargetPos(1),   //
        ;

        private final int value;

        private MagicPosType(int state) {
            this.value = state;
        }

        public int getValue() {
            return value;
        }

        public boolean compare(int state) {
            return (this.value & state) != 0;
        }

    }
}
