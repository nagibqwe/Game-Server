package com.game.marriage.struct;

import com.data.CfgManager;
import com.data.bean.Cfg_Marry_dinner_Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeddingOperation {
    private static final Logger logger = LogManager.getLogger(WeddingOperation.class);

    /**
     * 免费烟花次数
     */
    private int freeFireCount;
    /**
     * 购买烟花次数
     */
    private int buyFireCount;
    /**
     * 免费礼炮次数
     */
    private int freeSaluteCount;
    /**
     * 购买礼炮次数
     */
    private int buySaluteCount;
    /**
     * 免费喜糖次数
     */
    private int freeCandiesCount;
    /**
     * 购买喜糖次数
     */
    private int buyCandiesCount;
    /**
     * 赠送礼金数目
     */
    private int cashGift;

    /**
     * 采集次数
     */
    private int gatherNum;

    /**
     * 当前采集吃的数量
     */
    private int eatFoodNum;

    /**
     * 婚礼热度
     */
    private int weddingHot;

    /**
     * 婚礼现场增加的 累计经验
     */
    private long addExp;


    /**
     * 使用小烟花次数
     */
    private int useSamllFireCount;

    /**
     * 使用大烟花次数
     */
    private int useBigFireCount;

    /**
     * 婚姻是否签到
     */
    private boolean marryCopySign;
    /////////////////////////////方法/////////////////////////////////////////


    public int getUseSamllFireCount() {
        return useSamllFireCount;
    }

    public void setUseSamllFireCount(int useSamllFireCount) {
        this.useSamllFireCount = useSamllFireCount;
    }

    public int getUseBigFireCount() {
        return useBigFireCount;
    }

    public void setUseBigFireCount(int useBigFireCount) {
        this.useBigFireCount = useBigFireCount;
    }

    public int getEatFoodNum() {
        return eatFoodNum;
    }

    public void setEatFoodNum(int eatFoodNum) {
        this.eatFoodNum = eatFoodNum;
    }

    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    public int getWeddingHot() {
        return weddingHot;
    }

    public void setWeddingHot(int weddingHot) {
        this.weddingHot = weddingHot;
    }

    public int getGatherNum() {
        return gatherNum;
    }

    public void setGatherNum(int gatherNum) {
        this.gatherNum = gatherNum;
    }

    /**
     * 礼金累计
     */
    public void addCashGift(int addNum) {
        cashGift += addNum;
    }

    /**
     * 初始化
     *
     * @param level   婚宴档次
     * @param isMarry 是否是结婚双方
     */
    public void init(int level, boolean isMarry) {
        Cfg_Marry_dinner_Bean bean = CfgManager.getCfg_Marry_dinner_Container().getValueByKey(level);
        if (bean == null) {
            logger.error("婚宴操作数据初始化失败，Cfg_Marry_dinner_Bean无法找到指定数据，id = " + level);
            return;
        }
       //if (isMarry) {
       //    freeFireCount = bean.getFireworks().get(0);
       //    buyFireCount = bean.getFireworks().get(1);
       //    freeSaluteCount = bean.getSalute().get(0);
       //    buySaluteCount = bean.getSalute().get(1);
       //    freeCandiesCount = bean.getCandy().get(0);
       //    buyCandiesCount = bean.getCandy().get(1);
       //} else {
       //    freeFireCount = bean.getFireworks1().get(0);
       //    buyFireCount = bean.getFireworks1().get(1);
       //    freeSaluteCount = bean.getSalute1().get(0);
       //    buySaluteCount = bean.getSalute1().get(1);
       //}
    }

    ////////////////////////getter and setter/////////////////////////////////////

    /**
     * 获取 免费烟花次数
     *
     * @return freeFireCount 免费烟花次数
     */
    public int getFreeFireCount() {
        return this.freeFireCount;
    }

    /**
     * 设置 免费烟花次数
     *
     * @param freeFireCount 免费烟花次数
     */
    public void setFreeFireCount(int freeFireCount) {
        this.freeFireCount = freeFireCount;
    }

    /**
     * 获取 购买烟花次数
     *
     * @return buyFireCount 购买烟花次数
     */
    public int getBuyFireCount() {
        return this.buyFireCount;
    }

    /**
     * 设置 购买烟花次数
     *
     * @param buyFireCount 购买烟花次数
     */
    public void setBuyFireCount(int buyFireCount) {
        this.buyFireCount = buyFireCount;
    }

    /**
     * 获取 免费礼炮次数
     *
     * @return freeSaluteCount 免费礼炮次数
     */
    public int getFreeSaluteCount() {
        return this.freeSaluteCount;
    }

    /**
     * 设置 免费礼炮次数
     *
     * @param freeSaluteCount 免费礼炮次数
     */
    public void setFreeSaluteCount(int freeSaluteCount) {
        this.freeSaluteCount = freeSaluteCount;
    }

    /**
     * 获取 购买礼炮次数
     *
     * @return buySaluteCount 购买礼炮次数
     */
    public int getBuySaluteCount() {
        return this.buySaluteCount;
    }

    /**
     * 设置 购买礼炮次数
     *
     * @param buySaluteCount 购买礼炮次数
     */
    public void setBuySaluteCount(int buySaluteCount) {
        this.buySaluteCount = buySaluteCount;
    }

    /**
     * 获取 免费喜糖次数
     *
     * @return freeCandiesCount 免费喜糖次数
     */
    public int getFreeCandiesCount() {
        return this.freeCandiesCount;
    }

    /**
     * 设置 免费喜糖次数
     *
     * @param freeCandiesCount 免费喜糖次数
     */
    public void setFreeCandiesCount(int freeCandiesCount) {
        this.freeCandiesCount = freeCandiesCount;
    }

    /**
     * 获取 购买喜糖次数
     *
     * @return buyCandiesCount 购买喜糖次数
     */
    public int getBuyCandiesCount() {
        return this.buyCandiesCount;
    }

    /**
     * 设置 购买喜糖次数
     *
     * @param buyCandiesCount 购买喜糖次数
     */
    public void setBuyCandiesCount(int buyCandiesCount) {
        this.buyCandiesCount = buyCandiesCount;
    }

    /**
     * 获取 赠送礼金数目
     *
     * @return cashGift 赠送礼金数目
     */
    public int getCashGift() {
        return this.cashGift;
    }

    /**
     * 设置 赠送礼金数目
     *
     * @param cashGift 赠送礼金数目
     */
    public void setCashGift(int cashGift) {
        this.cashGift = cashGift;
    }

    public boolean isMarryCopySign() {
        return marryCopySign;
    }

    public void setMarryCopySign(boolean marryCopySign) {
        this.marryCopySign = marryCopySign;
    }
}
