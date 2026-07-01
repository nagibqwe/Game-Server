package com.game.db.bean;

import java.util.ArrayList;
import java.util.List;

public class ChumBeanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChumBeanExample() {
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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andRid1IsNull() {
            addCriterion("rID1 is null");
            return (Criteria) this;
        }

        public Criteria andRid1IsNotNull() {
            addCriterion("rID1 is not null");
            return (Criteria) this;
        }

        public Criteria andRid1EqualTo(Long value) {
            addCriterion("rID1 =", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1NotEqualTo(Long value) {
            addCriterion("rID1 <>", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1GreaterThan(Long value) {
            addCriterion("rID1 >", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1GreaterThanOrEqualTo(Long value) {
            addCriterion("rID1 >=", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1LessThan(Long value) {
            addCriterion("rID1 <", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1LessThanOrEqualTo(Long value) {
            addCriterion("rID1 <=", value, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1In(List<Long> values) {
            addCriterion("rID1 in", values, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1NotIn(List<Long> values) {
            addCriterion("rID1 not in", values, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1Between(Long value1, Long value2) {
            addCriterion("rID1 between", value1, value2, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid1NotBetween(Long value1, Long value2) {
            addCriterion("rID1 not between", value1, value2, "rid1");
            return (Criteria) this;
        }

        public Criteria andRid2IsNull() {
            addCriterion("rID2 is null");
            return (Criteria) this;
        }

        public Criteria andRid2IsNotNull() {
            addCriterion("rID2 is not null");
            return (Criteria) this;
        }

        public Criteria andRid2EqualTo(Long value) {
            addCriterion("rID2 =", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2NotEqualTo(Long value) {
            addCriterion("rID2 <>", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2GreaterThan(Long value) {
            addCriterion("rID2 >", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2GreaterThanOrEqualTo(Long value) {
            addCriterion("rID2 >=", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2LessThan(Long value) {
            addCriterion("rID2 <", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2LessThanOrEqualTo(Long value) {
            addCriterion("rID2 <=", value, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2In(List<Long> values) {
            addCriterion("rID2 in", values, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2NotIn(List<Long> values) {
            addCriterion("rID2 not in", values, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2Between(Long value1, Long value2) {
            addCriterion("rID2 between", value1, value2, "rid2");
            return (Criteria) this;
        }

        public Criteria andRid2NotBetween(Long value1, Long value2) {
            addCriterion("rID2 not between", value1, value2, "rid2");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andAnnoIsNull() {
            addCriterion("anno is null");
            return (Criteria) this;
        }

        public Criteria andAnnoIsNotNull() {
            addCriterion("anno is not null");
            return (Criteria) this;
        }

        public Criteria andAnnoEqualTo(String value) {
            addCriterion("anno =", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoNotEqualTo(String value) {
            addCriterion("anno <>", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoGreaterThan(String value) {
            addCriterion("anno >", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoGreaterThanOrEqualTo(String value) {
            addCriterion("anno >=", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoLessThan(String value) {
            addCriterion("anno <", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoLessThanOrEqualTo(String value) {
            addCriterion("anno <=", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoLike(String value) {
            addCriterion("anno like", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoNotLike(String value) {
            addCriterion("anno not like", value, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoIn(List<String> values) {
            addCriterion("anno in", values, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoNotIn(List<String> values) {
            addCriterion("anno not in", values, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoBetween(String value1, String value2) {
            addCriterion("anno between", value1, value2, "anno");
            return (Criteria) this;
        }

        public Criteria andAnnoNotBetween(String value1, String value2) {
            addCriterion("anno not between", value1, value2, "anno");
            return (Criteria) this;
        }

        public Criteria andLvlIsNull() {
            addCriterion("lvl is null");
            return (Criteria) this;
        }

        public Criteria andLvlIsNotNull() {
            addCriterion("lvl is not null");
            return (Criteria) this;
        }

        public Criteria andLvlEqualTo(Integer value) {
            addCriterion("lvl =", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlNotEqualTo(Integer value) {
            addCriterion("lvl <>", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlGreaterThan(Integer value) {
            addCriterion("lvl >", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlGreaterThanOrEqualTo(Integer value) {
            addCriterion("lvl >=", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlLessThan(Integer value) {
            addCriterion("lvl <", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlLessThanOrEqualTo(Integer value) {
            addCriterion("lvl <=", value, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlIn(List<Integer> values) {
            addCriterion("lvl in", values, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlNotIn(List<Integer> values) {
            addCriterion("lvl not in", values, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlBetween(Integer value1, Integer value2) {
            addCriterion("lvl between", value1, value2, "lvl");
            return (Criteria) this;
        }

        public Criteria andLvlNotBetween(Integer value1, Integer value2) {
            addCriterion("lvl not between", value1, value2, "lvl");
            return (Criteria) this;
        }

        public Criteria andExpIsNull() {
            addCriterion("exp is null");
            return (Criteria) this;
        }

        public Criteria andExpIsNotNull() {
            addCriterion("exp is not null");
            return (Criteria) this;
        }

        public Criteria andExpEqualTo(Integer value) {
            addCriterion("exp =", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotEqualTo(Integer value) {
            addCriterion("exp <>", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpGreaterThan(Integer value) {
            addCriterion("exp >", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpGreaterThanOrEqualTo(Integer value) {
            addCriterion("exp >=", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpLessThan(Integer value) {
            addCriterion("exp <", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpLessThanOrEqualTo(Integer value) {
            addCriterion("exp <=", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpIn(List<Integer> values) {
            addCriterion("exp in", values, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotIn(List<Integer> values) {
            addCriterion("exp not in", values, "exp");
            return (Criteria) this;
        }

        public Criteria andExpBetween(Integer value1, Integer value2) {
            addCriterion("exp between", value1, value2, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotBetween(Integer value1, Integer value2) {
            addCriterion("exp not between", value1, value2, "exp");
            return (Criteria) this;
        }

        public Criteria andFreetIsNull() {
            addCriterion("freeT is null");
            return (Criteria) this;
        }

        public Criteria andFreetIsNotNull() {
            addCriterion("freeT is not null");
            return (Criteria) this;
        }

        public Criteria andFreetEqualTo(Short value) {
            addCriterion("freeT =", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetNotEqualTo(Short value) {
            addCriterion("freeT <>", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetGreaterThan(Short value) {
            addCriterion("freeT >", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetGreaterThanOrEqualTo(Short value) {
            addCriterion("freeT >=", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetLessThan(Short value) {
            addCriterion("freeT <", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetLessThanOrEqualTo(Short value) {
            addCriterion("freeT <=", value, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetIn(List<Short> values) {
            addCriterion("freeT in", values, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetNotIn(List<Short> values) {
            addCriterion("freeT not in", values, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetBetween(Short value1, Short value2) {
            addCriterion("freeT between", value1, value2, "freet");
            return (Criteria) this;
        }

        public Criteria andFreetNotBetween(Short value1, Short value2) {
            addCriterion("freeT not between", value1, value2, "freet");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeIsNull() {
            addCriterion("lastFreshTime is null");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeIsNotNull() {
            addCriterion("lastFreshTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeEqualTo(Long value) {
            addCriterion("lastFreshTime =", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeNotEqualTo(Long value) {
            addCriterion("lastFreshTime <>", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeGreaterThan(Long value) {
            addCriterion("lastFreshTime >", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeGreaterThanOrEqualTo(Long value) {
            addCriterion("lastFreshTime >=", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeLessThan(Long value) {
            addCriterion("lastFreshTime <", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeLessThanOrEqualTo(Long value) {
            addCriterion("lastFreshTime <=", value, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeIn(List<Long> values) {
            addCriterion("lastFreshTime in", values, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeNotIn(List<Long> values) {
            addCriterion("lastFreshTime not in", values, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeBetween(Long value1, Long value2) {
            addCriterion("lastFreshTime between", value1, value2, "lastfreshtime");
            return (Criteria) this;
        }

        public Criteria andLastfreshtimeNotBetween(Long value1, Long value2) {
            addCriterion("lastFreshTime not between", value1, value2, "lastfreshtime");
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