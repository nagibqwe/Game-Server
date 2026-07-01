package com.game.db.bean;

import com.game.marriage.struct.Marriage;
import game.core.db.BaseBean;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MarryBean extends BaseBean {
    private final static Logger log = LogManager.getLogger(MarryBean.class);

    private long marriageId;        //婚姻唯一id
    private long aId;               //丈夫id
    private long bId;               //妻子id
    private long time;               //结婚时间
    private String data;

    public long getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(long marriageId) {
        this.marriageId = marriageId;
    }

    public long getaId() {
        return aId;
    }

    public void setaId(long aId) {
        this.aId = aId;
    }

    public long getbId() {
        return bId;
    }

    public void setbId(long bId) {
        this.bId = bId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Marriage toMarriage(MarryBean bean) {
        try {
            Marriage marriage = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(bean.getData()), Marriage.class);
            marriage.setId(bean.getMarriageId());
            marriage.setMarriageId(bean.getaId());
            marriage.setBeMarriageId(bean.getbId());
            marriage.setMarriageTime(bean.getTime());
            return marriage;
        } catch (Exception ex) {
            log.error("婚姻db数据解析出错 id：" + bean.getMarriageId(), ex);
            return null;
        }
    }
}
