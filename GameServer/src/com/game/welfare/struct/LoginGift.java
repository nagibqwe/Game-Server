package com.game.welfare.struct;

import com.data.CfgManager;
import com.data.bean.Cfg_Sevenday_login_Bean;

import java.util.ArrayList;
import java.util.List;

public class LoginGift {
    // 登陆天数
    private int loginNum;
    // 已经领取的天数
    private List<Integer> receives;
    // 最后计算登陆天数的时间
    private long lastCalcTime;
    private int maxDay;

    /**
     * newLoginGift
     * @return
     */
    public static LoginGift newLoginGift() {
        LoginGift gift = new LoginGift();
        gift.setLoginNum(0);
        gift.setLastCalcTime(0);
        gift.setReceives(new ArrayList<>());

        // 设置一个最大天数
        int maxDay = 0;
        for (Cfg_Sevenday_login_Bean bean : CfgManager.getCfg_Sevenday_login_Container().getValuees()) {
            if (bean.getDay() > maxDay)
                maxDay = bean.getDay();
        }
        gift.setMaxDay(maxDay);
        return gift;
    }

    @Override
    public String toString() {
        return "LoginGift{" +
                "loginNum=" + loginNum +
                ", receives=" + receives +
                ", lastCalcTime=" + lastCalcTime +
                ", maxDay=" + maxDay +
                '}';
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public List<Integer> getReceives() {
        return receives;
    }

    public void setReceives(List<Integer> receives) {
        this.receives = receives;
    }

    public long getLastCalcTime() {
        return lastCalcTime;
    }

    public void setLastCalcTime(long lastCalcTime) {
        this.lastCalcTime = lastCalcTime;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }
}
