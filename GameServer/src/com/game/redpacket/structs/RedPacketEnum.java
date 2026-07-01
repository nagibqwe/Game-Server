package com.game.redpacket.structs;

/**
 * 红包类型
 */
public enum RedPacketEnum {
    /**首冲*/
    firstRecharge(1),
    /**日常充值*/
    dailyRecharge(2),
    /**月卡*/
    monthCard(3),
    /**至尊月卡*/
    superMonthCard(4),
    /**成长基金*/
    invest(5),
    /**巅峰基金*/
    investPeak(6),
    /**VIP*/
    vip1(7),
    vip2(8),
    vip3(9),
    vip4(10),
    vip5(11),
    vip6(12),
    vip7(13),
    vip8(14),
    vip9(15),
    vip10(16),
    vip11(17),
    vip12(18),
    vip13(19),
    vip14(20),
    vip15(21),
    ;
    //红包类型
    int type;

    RedPacketEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static RedPacketEnum getByVip(int vip){
        switch (vip){
            case 1:
                return vip1;
            case 2:
                return vip2;
            case 3:
                return vip3;
            case 4:
                return vip4;
            case 5:
                return vip5;
            case 6:
                return vip6;
            case 7:
                return vip7;
            case 8:
                return vip8;
            case 9:
                return vip9;
            case 10:
                return vip10;
            case 11:
                return vip11;
            case 12:
                return vip12;
            case 13:
                return vip13;
            case 14:
                return vip14;
            case 15:
                return vip15;
        }
        return null;
    }
}
