package com.game.bigplayer.struct;

/**
 * 大玩咖
 * 腾讯返回模板
 */
public class QQReturnData {

    private int score;//大玩咖分值

    private int level;//大玩咖等级

    private boolean binding;//是否被超级大玩咖绑定且在有效期内

    private int discount;//充值返利数量，百分比。例如，20代表20%


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
