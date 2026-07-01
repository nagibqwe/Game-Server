package com.game.db.bean;

import java.util.ArrayList;
import java.util.List;

public class ShopBeanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopBeanExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andItemidIsNull() {
            addCriterion("itemID is null");
            return (Criteria) this;
        }

        public Criteria andItemidIsNotNull() {
            addCriterion("itemID is not null");
            return (Criteria) this;
        }

        public Criteria andItemidEqualTo(Integer value) {
            addCriterion("itemID =", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotEqualTo(Integer value) {
            addCriterion("itemID <>", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThan(Integer value) {
            addCriterion("itemID >", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThanOrEqualTo(Integer value) {
            addCriterion("itemID >=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThan(Integer value) {
            addCriterion("itemID <", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThanOrEqualTo(Integer value) {
            addCriterion("itemID <=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidIn(List<Integer> values) {
            addCriterion("itemID in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotIn(List<Integer> values) {
            addCriterion("itemID not in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidBetween(Integer value1, Integer value2) {
            addCriterion("itemID between", value1, value2, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotBetween(Integer value1, Integer value2) {
            addCriterion("itemID not between", value1, value2, "itemid");
            return (Criteria) this;
        }

        public Criteria andShopidIsNull() {
            addCriterion("shopID is null");
            return (Criteria) this;
        }

        public Criteria andShopidIsNotNull() {
            addCriterion("shopID is not null");
            return (Criteria) this;
        }

        public Criteria andShopidEqualTo(Integer value) {
            addCriterion("shopID =", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotEqualTo(Integer value) {
            addCriterion("shopID <>", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThan(Integer value) {
            addCriterion("shopID >", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThanOrEqualTo(Integer value) {
            addCriterion("shopID >=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThan(Integer value) {
            addCriterion("shopID <", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThanOrEqualTo(Integer value) {
            addCriterion("shopID <=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidIn(List<Integer> values) {
            addCriterion("shopID in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotIn(List<Integer> values) {
            addCriterion("shopID not in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidBetween(Integer value1, Integer value2) {
            addCriterion("shopID between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotBetween(Integer value1, Integer value2) {
            addCriterion("shopID not between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andLabelidIsNull() {
            addCriterion("labelID is null");
            return (Criteria) this;
        }

        public Criteria andLabelidIsNotNull() {
            addCriterion("labelID is not null");
            return (Criteria) this;
        }

        public Criteria andLabelidEqualTo(Integer value) {
            addCriterion("labelID =", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidNotEqualTo(Integer value) {
            addCriterion("labelID <>", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidGreaterThan(Integer value) {
            addCriterion("labelID >", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidGreaterThanOrEqualTo(Integer value) {
            addCriterion("labelID >=", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidLessThan(Integer value) {
            addCriterion("labelID <", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidLessThanOrEqualTo(Integer value) {
            addCriterion("labelID <=", value, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidIn(List<Integer> values) {
            addCriterion("labelID in", values, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidNotIn(List<Integer> values) {
            addCriterion("labelID not in", values, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidBetween(Integer value1, Integer value2) {
            addCriterion("labelID between", value1, value2, "labelid");
            return (Criteria) this;
        }

        public Criteria andLabelidNotBetween(Integer value1, Integer value2) {
            addCriterion("labelID not between", value1, value2, "labelid");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelIsNull() {
            addCriterion("militaryLevel is null");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelIsNotNull() {
            addCriterion("militaryLevel is not null");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelEqualTo(Integer value) {
            addCriterion("militaryLevel =", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelNotEqualTo(Integer value) {
            addCriterion("militaryLevel <>", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelGreaterThan(Integer value) {
            addCriterion("militaryLevel >", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("militaryLevel >=", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelLessThan(Integer value) {
            addCriterion("militaryLevel <", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelLessThanOrEqualTo(Integer value) {
            addCriterion("militaryLevel <=", value, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelIn(List<Integer> values) {
            addCriterion("militaryLevel in", values, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelNotIn(List<Integer> values) {
            addCriterion("militaryLevel not in", values, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelBetween(Integer value1, Integer value2) {
            addCriterion("militaryLevel between", value1, value2, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andMilitarylevelNotBetween(Integer value1, Integer value2) {
            addCriterion("militaryLevel not between", value1, value2, "militarylevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelIsNull() {
            addCriterion("guildLevel is null");
            return (Criteria) this;
        }

        public Criteria andGuildlevelIsNotNull() {
            addCriterion("guildLevel is not null");
            return (Criteria) this;
        }

        public Criteria andGuildlevelEqualTo(Integer value) {
            addCriterion("guildLevel =", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelNotEqualTo(Integer value) {
            addCriterion("guildLevel <>", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelGreaterThan(Integer value) {
            addCriterion("guildLevel >", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("guildLevel >=", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelLessThan(Integer value) {
            addCriterion("guildLevel <", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelLessThanOrEqualTo(Integer value) {
            addCriterion("guildLevel <=", value, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelIn(List<Integer> values) {
            addCriterion("guildLevel in", values, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelNotIn(List<Integer> values) {
            addCriterion("guildLevel not in", values, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelBetween(Integer value1, Integer value2) {
            addCriterion("guildLevel between", value1, value2, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildlevelNotBetween(Integer value1, Integer value2) {
            addCriterion("guildLevel not between", value1, value2, "guildlevel");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartIsNull() {
            addCriterion("guildShopLvlStart is null");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartIsNotNull() {
            addCriterion("guildShopLvlStart is not null");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartEqualTo(Integer value) {
            addCriterion("guildShopLvlStart =", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartNotEqualTo(Integer value) {
            addCriterion("guildShopLvlStart <>", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartGreaterThan(Integer value) {
            addCriterion("guildShopLvlStart >", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartGreaterThanOrEqualTo(Integer value) {
            addCriterion("guildShopLvlStart >=", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartLessThan(Integer value) {
            addCriterion("guildShopLvlStart <", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartLessThanOrEqualTo(Integer value) {
            addCriterion("guildShopLvlStart <=", value, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartIn(List<Integer> values) {
            addCriterion("guildShopLvlStart in", values, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartNotIn(List<Integer> values) {
            addCriterion("guildShopLvlStart not in", values, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartBetween(Integer value1, Integer value2) {
            addCriterion("guildShopLvlStart between", value1, value2, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlstartNotBetween(Integer value1, Integer value2) {
            addCriterion("guildShopLvlStart not between", value1, value2, "guildshoplvlstart");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendIsNull() {
            addCriterion("guildShopLvlEnd is null");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendIsNotNull() {
            addCriterion("guildShopLvlEnd is not null");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendEqualTo(Integer value) {
            addCriterion("guildShopLvlEnd =", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendNotEqualTo(Integer value) {
            addCriterion("guildShopLvlEnd <>", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendGreaterThan(Integer value) {
            addCriterion("guildShopLvlEnd >", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendGreaterThanOrEqualTo(Integer value) {
            addCriterion("guildShopLvlEnd >=", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendLessThan(Integer value) {
            addCriterion("guildShopLvlEnd <", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendLessThanOrEqualTo(Integer value) {
            addCriterion("guildShopLvlEnd <=", value, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendIn(List<Integer> values) {
            addCriterion("guildShopLvlEnd in", values, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendNotIn(List<Integer> values) {
            addCriterion("guildShopLvlEnd not in", values, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendBetween(Integer value1, Integer value2) {
            addCriterion("guildShopLvlEnd between", value1, value2, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andGuildshoplvlendNotBetween(Integer value1, Integer value2) {
            addCriterion("guildShopLvlEnd not between", value1, value2, "guildshoplvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartIsNull() {
            addCriterion("worldLvlStart is null");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartIsNotNull() {
            addCriterion("worldLvlStart is not null");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartEqualTo(Integer value) {
            addCriterion("worldLvlStart =", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartNotEqualTo(Integer value) {
            addCriterion("worldLvlStart <>", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartGreaterThan(Integer value) {
            addCriterion("worldLvlStart >", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartGreaterThanOrEqualTo(Integer value) {
            addCriterion("worldLvlStart >=", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartLessThan(Integer value) {
            addCriterion("worldLvlStart <", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartLessThanOrEqualTo(Integer value) {
            addCriterion("worldLvlStart <=", value, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartIn(List<Integer> values) {
            addCriterion("worldLvlStart in", values, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartNotIn(List<Integer> values) {
            addCriterion("worldLvlStart not in", values, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartBetween(Integer value1, Integer value2) {
            addCriterion("worldLvlStart between", value1, value2, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlstartNotBetween(Integer value1, Integer value2) {
            addCriterion("worldLvlStart not between", value1, value2, "worldlvlstart");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendIsNull() {
            addCriterion("worldLvlEnd is null");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendIsNotNull() {
            addCriterion("worldLvlEnd is not null");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendEqualTo(Integer value) {
            addCriterion("worldLvlEnd =", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendNotEqualTo(Integer value) {
            addCriterion("worldLvlEnd <>", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendGreaterThan(Integer value) {
            addCriterion("worldLvlEnd >", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendGreaterThanOrEqualTo(Integer value) {
            addCriterion("worldLvlEnd >=", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendLessThan(Integer value) {
            addCriterion("worldLvlEnd <", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendLessThanOrEqualTo(Integer value) {
            addCriterion("worldLvlEnd <=", value, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendIn(List<Integer> values) {
            addCriterion("worldLvlEnd in", values, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendNotIn(List<Integer> values) {
            addCriterion("worldLvlEnd not in", values, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendBetween(Integer value1, Integer value2) {
            addCriterion("worldLvlEnd between", value1, value2, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andWorldlvlendNotBetween(Integer value1, Integer value2) {
            addCriterion("worldLvlEnd not between", value1, value2, "worldlvlend");
            return (Criteria) this;
        }

        public Criteria andIsdiscountIsNull() {
            addCriterion("isDiscount is null");
            return (Criteria) this;
        }

        public Criteria andIsdiscountIsNotNull() {
            addCriterion("isDiscount is not null");
            return (Criteria) this;
        }

        public Criteria andIsdiscountEqualTo(Integer value) {
            addCriterion("isDiscount =", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountNotEqualTo(Integer value) {
            addCriterion("isDiscount <>", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountGreaterThan(Integer value) {
            addCriterion("isDiscount >", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountGreaterThanOrEqualTo(Integer value) {
            addCriterion("isDiscount >=", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountLessThan(Integer value) {
            addCriterion("isDiscount <", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountLessThanOrEqualTo(Integer value) {
            addCriterion("isDiscount <=", value, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountIn(List<Integer> values) {
            addCriterion("isDiscount in", values, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountNotIn(List<Integer> values) {
            addCriterion("isDiscount not in", values, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountBetween(Integer value1, Integer value2) {
            addCriterion("isDiscount between", value1, value2, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andIsdiscountNotBetween(Integer value1, Integer value2) {
            addCriterion("isDiscount not between", value1, value2, "isdiscount");
            return (Criteria) this;
        }

        public Criteria andViplevelIsNull() {
            addCriterion("vipLevel is null");
            return (Criteria) this;
        }

        public Criteria andViplevelIsNotNull() {
            addCriterion("vipLevel is not null");
            return (Criteria) this;
        }

        public Criteria andViplevelEqualTo(Integer value) {
            addCriterion("vipLevel =", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelNotEqualTo(Integer value) {
            addCriterion("vipLevel <>", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelGreaterThan(Integer value) {
            addCriterion("vipLevel >", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("vipLevel >=", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelLessThan(Integer value) {
            addCriterion("vipLevel <", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelLessThanOrEqualTo(Integer value) {
            addCriterion("vipLevel <=", value, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelIn(List<Integer> values) {
            addCriterion("vipLevel in", values, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelNotIn(List<Integer> values) {
            addCriterion("vipLevel not in", values, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelBetween(Integer value1, Integer value2) {
            addCriterion("vipLevel between", value1, value2, "viplevel");
            return (Criteria) this;
        }

        public Criteria andViplevelNotBetween(Integer value1, Integer value2) {
            addCriterion("vipLevel not between", value1, value2, "viplevel");
            return (Criteria) this;
        }

        public Criteria andOccupationIsNull() {
            addCriterion("occupation is null");
            return (Criteria) this;
        }

        public Criteria andOccupationIsNotNull() {
            addCriterion("occupation is not null");
            return (Criteria) this;
        }

        public Criteria andOccupationEqualTo(Integer value) {
            addCriterion("occupation =", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotEqualTo(Integer value) {
            addCriterion("occupation <>", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationGreaterThan(Integer value) {
            addCriterion("occupation >", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationGreaterThanOrEqualTo(Integer value) {
            addCriterion("occupation >=", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationLessThan(Integer value) {
            addCriterion("occupation <", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationLessThanOrEqualTo(Integer value) {
            addCriterion("occupation <=", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationIn(List<Integer> values) {
            addCriterion("occupation in", values, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotIn(List<Integer> values) {
            addCriterion("occupation not in", values, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationBetween(Integer value1, Integer value2) {
            addCriterion("occupation between", value1, value2, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotBetween(Integer value1, Integer value2) {
            addCriterion("occupation not between", value1, value2, "occupation");
            return (Criteria) this;
        }

        public Criteria andLimittypeIsNull() {
            addCriterion("limitType is null");
            return (Criteria) this;
        }

        public Criteria andLimittypeIsNotNull() {
            addCriterion("limitType is not null");
            return (Criteria) this;
        }

        public Criteria andLimittypeEqualTo(Integer value) {
            addCriterion("limitType =", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeNotEqualTo(Integer value) {
            addCriterion("limitType <>", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeGreaterThan(Integer value) {
            addCriterion("limitType >", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("limitType >=", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeLessThan(Integer value) {
            addCriterion("limitType <", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeLessThanOrEqualTo(Integer value) {
            addCriterion("limitType <=", value, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeIn(List<Integer> values) {
            addCriterion("limitType in", values, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeNotIn(List<Integer> values) {
            addCriterion("limitType not in", values, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeBetween(Integer value1, Integer value2) {
            addCriterion("limitType between", value1, value2, "limittype");
            return (Criteria) this;
        }

        public Criteria andLimittypeNotBetween(Integer value1, Integer value2) {
            addCriterion("limitType not between", value1, value2, "limittype");
            return (Criteria) this;
        }

        public Criteria andBuynumIsNull() {
            addCriterion("buyNum is null");
            return (Criteria) this;
        }

        public Criteria andBuynumIsNotNull() {
            addCriterion("buyNum is not null");
            return (Criteria) this;
        }

        public Criteria andBuynumEqualTo(Integer value) {
            addCriterion("buyNum =", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumNotEqualTo(Integer value) {
            addCriterion("buyNum <>", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumGreaterThan(Integer value) {
            addCriterion("buyNum >", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumGreaterThanOrEqualTo(Integer value) {
            addCriterion("buyNum >=", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumLessThan(Integer value) {
            addCriterion("buyNum <", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumLessThanOrEqualTo(Integer value) {
            addCriterion("buyNum <=", value, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumIn(List<Integer> values) {
            addCriterion("buyNum in", values, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumNotIn(List<Integer> values) {
            addCriterion("buyNum not in", values, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumBetween(Integer value1, Integer value2) {
            addCriterion("buyNum between", value1, value2, "buynum");
            return (Criteria) this;
        }

        public Criteria andBuynumNotBetween(Integer value1, Integer value2) {
            addCriterion("buyNum not between", value1, value2, "buynum");
            return (Criteria) this;
        }

        public Criteria andCurrencyidIsNull() {
            addCriterion("currencyID is null");
            return (Criteria) this;
        }

        public Criteria andCurrencyidIsNotNull() {
            addCriterion("currencyID is not null");
            return (Criteria) this;
        }

        public Criteria andCurrencyidEqualTo(Integer value) {
            addCriterion("currencyID =", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidNotEqualTo(Integer value) {
            addCriterion("currencyID <>", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidGreaterThan(Integer value) {
            addCriterion("currencyID >", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidGreaterThanOrEqualTo(Integer value) {
            addCriterion("currencyID >=", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidLessThan(Integer value) {
            addCriterion("currencyID <", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidLessThanOrEqualTo(Integer value) {
            addCriterion("currencyID <=", value, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidIn(List<Integer> values) {
            addCriterion("currencyID in", values, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidNotIn(List<Integer> values) {
            addCriterion("currencyID not in", values, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidBetween(Integer value1, Integer value2) {
            addCriterion("currencyID between", value1, value2, "currencyid");
            return (Criteria) this;
        }

        public Criteria andCurrencyidNotBetween(Integer value1, Integer value2) {
            addCriterion("currencyID not between", value1, value2, "currencyid");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Integer value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Integer value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Integer value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Integer value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Integer value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Integer> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Integer> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Integer value1, Integer value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceIsNull() {
            addCriterion("discountPrice is null");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceIsNotNull() {
            addCriterion("discountPrice is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceEqualTo(Integer value) {
            addCriterion("discountPrice =", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceNotEqualTo(Integer value) {
            addCriterion("discountPrice <>", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceGreaterThan(Integer value) {
            addCriterion("discountPrice >", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("discountPrice >=", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceLessThan(Integer value) {
            addCriterion("discountPrice <", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceLessThanOrEqualTo(Integer value) {
            addCriterion("discountPrice <=", value, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceIn(List<Integer> values) {
            addCriterion("discountPrice in", values, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceNotIn(List<Integer> values) {
            addCriterion("discountPrice not in", values, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceBetween(Integer value1, Integer value2) {
            addCriterion("discountPrice between", value1, value2, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountpriceNotBetween(Integer value1, Integer value2) {
            addCriterion("discountPrice not between", value1, value2, "discountprice");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNull() {
            addCriterion("discount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNotNull() {
            addCriterion("discount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountEqualTo(Integer value) {
            addCriterion("discount =", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotEqualTo(Integer value) {
            addCriterion("discount <>", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThan(Integer value) {
            addCriterion("discount >", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThanOrEqualTo(Integer value) {
            addCriterion("discount >=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThan(Integer value) {
            addCriterion("discount <", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThanOrEqualTo(Integer value) {
            addCriterion("discount <=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountIn(List<Integer> values) {
            addCriterion("discount in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotIn(List<Integer> values) {
            addCriterion("discount not in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountBetween(Integer value1, Integer value2) {
            addCriterion("discount between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotBetween(Integer value1, Integer value2) {
            addCriterion("discount not between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andPromotionIsNull() {
            addCriterion("promotion is null");
            return (Criteria) this;
        }

        public Criteria andPromotionIsNotNull() {
            addCriterion("promotion is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionEqualTo(Integer value) {
            addCriterion("promotion =", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotEqualTo(Integer value) {
            addCriterion("promotion <>", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionGreaterThan(Integer value) {
            addCriterion("promotion >", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionGreaterThanOrEqualTo(Integer value) {
            addCriterion("promotion >=", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionLessThan(Integer value) {
            addCriterion("promotion <", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionLessThanOrEqualTo(Integer value) {
            addCriterion("promotion <=", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionIn(List<Integer> values) {
            addCriterion("promotion in", values, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotIn(List<Integer> values) {
            addCriterion("promotion not in", values, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionBetween(Integer value1, Integer value2) {
            addCriterion("promotion between", value1, value2, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotBetween(Integer value1, Integer value2) {
            addCriterion("promotion not between", value1, value2, "promotion");
            return (Criteria) this;
        }

        public Criteria andSortIsNull() {
            addCriterion("sort is null");
            return (Criteria) this;
        }

        public Criteria andSortIsNotNull() {
            addCriterion("sort is not null");
            return (Criteria) this;
        }

        public Criteria andSortEqualTo(Integer value) {
            addCriterion("sort =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Integer value) {
            addCriterion("sort <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Integer value) {
            addCriterion("sort >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Integer value) {
            addCriterion("sort <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Integer value) {
            addCriterion("sort <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Integer> values) {
            addCriterion("sort in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Integer> values) {
            addCriterion("sort not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Integer value1, Integer value2) {
            addCriterion("sort between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Integer value1, Integer value2) {
            addCriterion("sort not between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andUptimeIsNull() {
            addCriterion("upTime is null");
            return (Criteria) this;
        }

        public Criteria andUptimeIsNotNull() {
            addCriterion("upTime is not null");
            return (Criteria) this;
        }

        public Criteria andUptimeEqualTo(String value) {
            addCriterion("upTime =", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeNotEqualTo(String value) {
            addCriterion("upTime <>", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeGreaterThan(String value) {
            addCriterion("upTime >", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeGreaterThanOrEqualTo(String value) {
            addCriterion("upTime >=", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeLessThan(String value) {
            addCriterion("upTime <", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeLessThanOrEqualTo(String value) {
            addCriterion("upTime <=", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeLike(String value) {
            addCriterion("upTime like", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeNotLike(String value) {
            addCriterion("upTime not like", value, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeIn(List<String> values) {
            addCriterion("upTime in", values, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeNotIn(List<String> values) {
            addCriterion("upTime not in", values, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeBetween(String value1, String value2) {
            addCriterion("upTime between", value1, value2, "uptime");
            return (Criteria) this;
        }

        public Criteria andUptimeNotBetween(String value1, String value2) {
            addCriterion("upTime not between", value1, value2, "uptime");
            return (Criteria) this;
        }

        public Criteria andDowntimeIsNull() {
            addCriterion("downTime is null");
            return (Criteria) this;
        }

        public Criteria andDowntimeIsNotNull() {
            addCriterion("downTime is not null");
            return (Criteria) this;
        }

        public Criteria andDowntimeEqualTo(String value) {
            addCriterion("downTime =", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeNotEqualTo(String value) {
            addCriterion("downTime <>", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeGreaterThan(String value) {
            addCriterion("downTime >", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeGreaterThanOrEqualTo(String value) {
            addCriterion("downTime >=", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeLessThan(String value) {
            addCriterion("downTime <", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeLessThanOrEqualTo(String value) {
            addCriterion("downTime <=", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeLike(String value) {
            addCriterion("downTime like", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeNotLike(String value) {
            addCriterion("downTime not like", value, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeIn(List<String> values) {
            addCriterion("downTime in", values, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeNotIn(List<String> values) {
            addCriterion("downTime not in", values, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeBetween(String value1, String value2) {
            addCriterion("downTime between", value1, value2, "downtime");
            return (Criteria) this;
        }

        public Criteria andDowntimeNotBetween(String value1, String value2) {
            addCriterion("downTime not between", value1, value2, "downtime");
            return (Criteria) this;
        }

        public Criteria andOverdueIsNull() {
            addCriterion("overdue is null");
            return (Criteria) this;
        }

        public Criteria andOverdueIsNotNull() {
            addCriterion("overdue is not null");
            return (Criteria) this;
        }

        public Criteria andOverdueEqualTo(String value) {
            addCriterion("overdue =", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueNotEqualTo(String value) {
            addCriterion("overdue <>", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueGreaterThan(String value) {
            addCriterion("overdue >", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueGreaterThanOrEqualTo(String value) {
            addCriterion("overdue >=", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueLessThan(String value) {
            addCriterion("overdue <", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueLessThanOrEqualTo(String value) {
            addCriterion("overdue <=", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueLike(String value) {
            addCriterion("overdue like", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueNotLike(String value) {
            addCriterion("overdue not like", value, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueIn(List<String> values) {
            addCriterion("overdue in", values, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueNotIn(List<String> values) {
            addCriterion("overdue not in", values, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueBetween(String value1, String value2) {
            addCriterion("overdue between", value1, value2, "overdue");
            return (Criteria) this;
        }

        public Criteria andOverdueNotBetween(String value1, String value2) {
            addCriterion("overdue not between", value1, value2, "overdue");
            return (Criteria) this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(Integer value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(Integer value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(Integer value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(Integer value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(Integer value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(Integer value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<Integer> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<Integer> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(Integer value1, Integer value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(Integer value1, Integer value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andBindIsNull() {
            addCriterion("bind is null");
            return (Criteria) this;
        }

        public Criteria andBindIsNotNull() {
            addCriterion("bind is not null");
            return (Criteria) this;
        }

        public Criteria andBindEqualTo(Integer value) {
            addCriterion("bind =", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindNotEqualTo(Integer value) {
            addCriterion("bind <>", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindGreaterThan(Integer value) {
            addCriterion("bind >", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindGreaterThanOrEqualTo(Integer value) {
            addCriterion("bind >=", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindLessThan(Integer value) {
            addCriterion("bind <", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindLessThanOrEqualTo(Integer value) {
            addCriterion("bind <=", value, "bind");
            return (Criteria) this;
        }

        public Criteria andBindIn(List<Integer> values) {
            addCriterion("bind in", values, "bind");
            return (Criteria) this;
        }

        public Criteria andBindNotIn(List<Integer> values) {
            addCriterion("bind not in", values, "bind");
            return (Criteria) this;
        }

        public Criteria andBindBetween(Integer value1, Integer value2) {
            addCriterion("bind between", value1, value2, "bind");
            return (Criteria) this;
        }

        public Criteria andBindNotBetween(Integer value1, Integer value2) {
            addCriterion("bind not between", value1, value2, "bind");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyIsNull() {
            addCriterion("refreshCurrency is null");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyIsNotNull() {
            addCriterion("refreshCurrency is not null");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyEqualTo(Integer value) {
            addCriterion("refreshCurrency =", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyNotEqualTo(Integer value) {
            addCriterion("refreshCurrency <>", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyGreaterThan(Integer value) {
            addCriterion("refreshCurrency >", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyGreaterThanOrEqualTo(Integer value) {
            addCriterion("refreshCurrency >=", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyLessThan(Integer value) {
            addCriterion("refreshCurrency <", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyLessThanOrEqualTo(Integer value) {
            addCriterion("refreshCurrency <=", value, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyIn(List<Integer> values) {
            addCriterion("refreshCurrency in", values, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyNotIn(List<Integer> values) {
            addCriterion("refreshCurrency not in", values, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyBetween(Integer value1, Integer value2) {
            addCriterion("refreshCurrency between", value1, value2, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshcurrencyNotBetween(Integer value1, Integer value2) {
            addCriterion("refreshCurrency not between", value1, value2, "refreshcurrency");
            return (Criteria) this;
        }

        public Criteria andRefreshnumIsNull() {
            addCriterion("refreshNum is null");
            return (Criteria) this;
        }

        public Criteria andRefreshnumIsNotNull() {
            addCriterion("refreshNum is not null");
            return (Criteria) this;
        }

        public Criteria andRefreshnumEqualTo(Integer value) {
            addCriterion("refreshNum =", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumNotEqualTo(Integer value) {
            addCriterion("refreshNum <>", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumGreaterThan(Integer value) {
            addCriterion("refreshNum >", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumGreaterThanOrEqualTo(Integer value) {
            addCriterion("refreshNum >=", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumLessThan(Integer value) {
            addCriterion("refreshNum <", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumLessThanOrEqualTo(Integer value) {
            addCriterion("refreshNum <=", value, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumIn(List<Integer> values) {
            addCriterion("refreshNum in", values, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumNotIn(List<Integer> values) {
            addCriterion("refreshNum not in", values, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumBetween(Integer value1, Integer value2) {
            addCriterion("refreshNum between", value1, value2, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andRefreshnumNotBetween(Integer value1, Integer value2) {
            addCriterion("refreshNum not between", value1, value2, "refreshnum");
            return (Criteria) this;
        }

        public Criteria andShoptypeIsNull() {
            addCriterion("shopType is null");
            return (Criteria) this;
        }

        public Criteria andShoptypeIsNotNull() {
            addCriterion("shopType is not null");
            return (Criteria) this;
        }

        public Criteria andShoptypeEqualTo(String value) {
            addCriterion("shopType =", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeNotEqualTo(String value) {
            addCriterion("shopType <>", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeGreaterThan(String value) {
            addCriterion("shopType >", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeGreaterThanOrEqualTo(String value) {
            addCriterion("shopType >=", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeLessThan(String value) {
            addCriterion("shopType <", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeLessThanOrEqualTo(String value) {
            addCriterion("shopType <=", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeLike(String value) {
            addCriterion("shopType like", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeNotLike(String value) {
            addCriterion("shopType not like", value, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeIn(List<String> values) {
            addCriterion("shopType in", values, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeNotIn(List<String> values) {
            addCriterion("shopType not in", values, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeBetween(String value1, String value2) {
            addCriterion("shopType between", value1, value2, "shoptype");
            return (Criteria) this;
        }

        public Criteria andShoptypeNotBetween(String value1, String value2) {
            addCriterion("shopType not between", value1, value2, "shoptype");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}