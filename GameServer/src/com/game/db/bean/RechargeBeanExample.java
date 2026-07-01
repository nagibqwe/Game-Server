package com.game.db.bean;

import java.util.ArrayList;
import java.util.List;

public class RechargeBeanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RechargeBeanExample() {
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

        public Criteria andOrderNoIsNull() {
            addCriterion("order_no is null");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNotNull() {
            addCriterion("order_no is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNoEqualTo(String value) {
            addCriterion("order_no =", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotEqualTo(String value) {
            addCriterion("order_no <>", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThan(String value) {
            addCriterion("order_no >", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("order_no >=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThan(String value) {
            addCriterion("order_no <", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThanOrEqualTo(String value) {
            addCriterion("order_no <=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLike(String value) {
            addCriterion("order_no like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotLike(String value) {
            addCriterion("order_no not like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoIn(List<String> values) {
            addCriterion("order_no in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotIn(List<String> values) {
            addCriterion("order_no not in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoBetween(String value1, String value2) {
            addCriterion("order_no between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotBetween(String value1, String value2) {
            addCriterion("order_no not between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("role_id is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("role_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Long value) {
            addCriterion("role_id =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Long value) {
            addCriterion("role_id <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Long value) {
            addCriterion("role_id >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("role_id >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Long value) {
            addCriterion("role_id <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Long value) {
            addCriterion("role_id <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Long> values) {
            addCriterion("role_id in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Long> values) {
            addCriterion("role_id not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Long value1, Long value2) {
            addCriterion("role_id between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Long value1, Long value2) {
            addCriterion("role_id not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andSrvIdIsNull() {
            addCriterion("srv_id is null");
            return (Criteria) this;
        }

        public Criteria andSrvIdIsNotNull() {
            addCriterion("srv_id is not null");
            return (Criteria) this;
        }

        public Criteria andSrvIdEqualTo(Integer value) {
            addCriterion("srv_id =", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdNotEqualTo(Integer value) {
            addCriterion("srv_id <>", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdGreaterThan(Integer value) {
            addCriterion("srv_id >", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("srv_id >=", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdLessThan(Integer value) {
            addCriterion("srv_id <", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdLessThanOrEqualTo(Integer value) {
            addCriterion("srv_id <=", value, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdIn(List<Integer> values) {
            addCriterion("srv_id in", values, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdNotIn(List<Integer> values) {
            addCriterion("srv_id not in", values, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdBetween(Integer value1, Integer value2) {
            addCriterion("srv_id between", value1, value2, "srvId");
            return (Criteria) this;
        }

        public Criteria andSrvIdNotBetween(Integer value1, Integer value2) {
            addCriterion("srv_id not between", value1, value2, "srvId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdIsNull() {
            addCriterion("goods_id is null");
            return (Criteria) this;
        }

        public Criteria andGoodsIdIsNotNull() {
            addCriterion("goods_id is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsIdEqualTo(Integer value) {
            addCriterion("goods_id =", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotEqualTo(Integer value) {
            addCriterion("goods_id <>", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdGreaterThan(Integer value) {
            addCriterion("goods_id >", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_id >=", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdLessThan(Integer value) {
            addCriterion("goods_id <", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdLessThanOrEqualTo(Integer value) {
            addCriterion("goods_id <=", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdIn(List<Integer> values) {
            addCriterion("goods_id in", values, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotIn(List<Integer> values) {
            addCriterion("goods_id not in", values, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdBetween(Integer value1, Integer value2) {
            addCriterion("goods_id between", value1, value2, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_id not between", value1, value2, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIsNull() {
            addCriterion("goods_type is null");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIsNotNull() {
            addCriterion("goods_type is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeEqualTo(String value) {
            addCriterion("goods_type =", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotEqualTo(String value) {
            addCriterion("goods_type <>", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeGreaterThan(String value) {
            addCriterion("goods_type >", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeGreaterThanOrEqualTo(String value) {
            addCriterion("goods_type >=", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLessThan(String value) {
            addCriterion("goods_type <", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLessThanOrEqualTo(String value) {
            addCriterion("goods_type <=", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeLike(String value) {
            addCriterion("goods_type like", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotLike(String value) {
            addCriterion("goods_type not like", value, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeIn(List<String> values) {
            addCriterion("goods_type in", values, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotIn(List<String> values) {
            addCriterion("goods_type not in", values, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeBetween(String value1, String value2) {
            addCriterion("goods_type between", value1, value2, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsTypeNotBetween(String value1, String value2) {
            addCriterion("goods_type not between", value1, value2, "goodsType");
            return (Criteria) this;
        }

        public Criteria andGoodsExtIsNull() {
            addCriterion("goods_ext is null");
            return (Criteria) this;
        }

        public Criteria andGoodsExtIsNotNull() {
            addCriterion("goods_ext is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsExtEqualTo(String value) {
            addCriterion("goods_ext =", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtNotEqualTo(String value) {
            addCriterion("goods_ext <>", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtGreaterThan(String value) {
            addCriterion("goods_ext >", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtGreaterThanOrEqualTo(String value) {
            addCriterion("goods_ext >=", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtLessThan(String value) {
            addCriterion("goods_ext <", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtLessThanOrEqualTo(String value) {
            addCriterion("goods_ext <=", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtLike(String value) {
            addCriterion("goods_ext like", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtNotLike(String value) {
            addCriterion("goods_ext not like", value, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtIn(List<String> values) {
            addCriterion("goods_ext in", values, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtNotIn(List<String> values) {
            addCriterion("goods_ext not in", values, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtBetween(String value1, String value2) {
            addCriterion("goods_ext between", value1, value2, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsExtNotBetween(String value1, String value2) {
            addCriterion("goods_ext not between", value1, value2, "goodsExt");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNull() {
            addCriterion("goods_name is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNotNull() {
            addCriterion("goods_name is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameEqualTo(String value) {
            addCriterion("goods_name =", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotEqualTo(String value) {
            addCriterion("goods_name <>", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThan(String value) {
            addCriterion("goods_name >", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("goods_name >=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThan(String value) {
            addCriterion("goods_name <", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("goods_name <=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLike(String value) {
            addCriterion("goods_name like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotLike(String value) {
            addCriterion("goods_name not like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIn(List<String> values) {
            addCriterion("goods_name in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotIn(List<String> values) {
            addCriterion("goods_name not in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameBetween(String value1, String value2) {
            addCriterion("goods_name between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotBetween(String value1, String value2) {
            addCriterion("goods_name not between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgIsNull() {
            addCriterion("goods_cfg is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgIsNotNull() {
            addCriterion("goods_cfg is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgEqualTo(String value) {
            addCriterion("goods_cfg =", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgNotEqualTo(String value) {
            addCriterion("goods_cfg <>", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgGreaterThan(String value) {
            addCriterion("goods_cfg >", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgGreaterThanOrEqualTo(String value) {
            addCriterion("goods_cfg >=", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgLessThan(String value) {
            addCriterion("goods_cfg <", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgLessThanOrEqualTo(String value) {
            addCriterion("goods_cfg <=", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgLike(String value) {
            addCriterion("goods_cfg like", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgNotLike(String value) {
            addCriterion("goods_cfg not like", value, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgIn(List<String> values) {
            addCriterion("goods_cfg in", values, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgNotIn(List<String> values) {
            addCriterion("goods_cfg not in", values, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgBetween(String value1, String value2) {
            addCriterion("goods_cfg between", value1, value2, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andGoodsCfgNotBetween(String value1, String value2) {
            addCriterion("goods_cfg not between", value1, value2, "goodsCfg");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIsNull() {
            addCriterion("total_fee is null");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIsNotNull() {
            addCriterion("total_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTotalFeeEqualTo(Integer value) {
            addCriterion("total_fee =", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotEqualTo(Integer value) {
            addCriterion("total_fee <>", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeGreaterThan(Integer value) {
            addCriterion("total_fee >", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_fee >=", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeLessThan(Integer value) {
            addCriterion("total_fee <", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeLessThanOrEqualTo(Integer value) {
            addCriterion("total_fee <=", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIn(List<Integer> values) {
            addCriterion("total_fee in", values, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotIn(List<Integer> values) {
            addCriterion("total_fee not in", values, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeBetween(Integer value1, Integer value2) {
            addCriterion("total_fee between", value1, value2, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotBetween(Integer value1, Integer value2) {
            addCriterion("total_fee not between", value1, value2, "totalFee");
            return (Criteria) this;
        }

        public Criteria andItemIdIsNull() {
            addCriterion("item_id is null");
            return (Criteria) this;
        }

        public Criteria andItemIdIsNotNull() {
            addCriterion("item_id is not null");
            return (Criteria) this;
        }

        public Criteria andItemIdEqualTo(Integer value) {
            addCriterion("item_id =", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdNotEqualTo(Integer value) {
            addCriterion("item_id <>", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdGreaterThan(Integer value) {
            addCriterion("item_id >", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("item_id >=", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdLessThan(Integer value) {
            addCriterion("item_id <", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdLessThanOrEqualTo(Integer value) {
            addCriterion("item_id <=", value, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdIn(List<Integer> values) {
            addCriterion("item_id in", values, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdNotIn(List<Integer> values) {
            addCriterion("item_id not in", values, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdBetween(Integer value1, Integer value2) {
            addCriterion("item_id between", value1, value2, "itemId");
            return (Criteria) this;
        }

        public Criteria andItemIdNotBetween(Integer value1, Integer value2) {
            addCriterion("item_id not between", value1, value2, "itemId");
            return (Criteria) this;
        }

        public Criteria andGameMoneyIsNull() {
            addCriterion("game_money is null");
            return (Criteria) this;
        }

        public Criteria andGameMoneyIsNotNull() {
            addCriterion("game_money is not null");
            return (Criteria) this;
        }

        public Criteria andGameMoneyEqualTo(Integer value) {
            addCriterion("game_money =", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyNotEqualTo(Integer value) {
            addCriterion("game_money <>", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyGreaterThan(Integer value) {
            addCriterion("game_money >", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyGreaterThanOrEqualTo(Integer value) {
            addCriterion("game_money >=", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyLessThan(Integer value) {
            addCriterion("game_money <", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyLessThanOrEqualTo(Integer value) {
            addCriterion("game_money <=", value, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyIn(List<Integer> values) {
            addCriterion("game_money in", values, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyNotIn(List<Integer> values) {
            addCriterion("game_money not in", values, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyBetween(Integer value1, Integer value2) {
            addCriterion("game_money between", value1, value2, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andGameMoneyNotBetween(Integer value1, Integer value2) {
            addCriterion("game_money not between", value1, value2, "gameMoney");
            return (Criteria) this;
        }

        public Criteria andExtParamIsNull() {
            addCriterion("ext_param is null");
            return (Criteria) this;
        }

        public Criteria andExtParamIsNotNull() {
            addCriterion("ext_param is not null");
            return (Criteria) this;
        }

        public Criteria andExtParamEqualTo(String value) {
            addCriterion("ext_param =", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamNotEqualTo(String value) {
            addCriterion("ext_param <>", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamGreaterThan(String value) {
            addCriterion("ext_param >", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamGreaterThanOrEqualTo(String value) {
            addCriterion("ext_param >=", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamLessThan(String value) {
            addCriterion("ext_param <", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamLessThanOrEqualTo(String value) {
            addCriterion("ext_param <=", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamLike(String value) {
            addCriterion("ext_param like", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamNotLike(String value) {
            addCriterion("ext_param not like", value, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamIn(List<String> values) {
            addCriterion("ext_param in", values, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamNotIn(List<String> values) {
            addCriterion("ext_param not in", values, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamBetween(String value1, String value2) {
            addCriterion("ext_param between", value1, value2, "extParam");
            return (Criteria) this;
        }

        public Criteria andExtParamNotBetween(String value1, String value2) {
            addCriterion("ext_param not between", value1, value2, "extParam");
            return (Criteria) this;
        }

        public Criteria andSignTypeIsNull() {
            addCriterion("sign_type is null");
            return (Criteria) this;
        }

        public Criteria andSignTypeIsNotNull() {
            addCriterion("sign_type is not null");
            return (Criteria) this;
        }

        public Criteria andSignTypeEqualTo(String value) {
            addCriterion("sign_type =", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeNotEqualTo(String value) {
            addCriterion("sign_type <>", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeGreaterThan(String value) {
            addCriterion("sign_type >", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeGreaterThanOrEqualTo(String value) {
            addCriterion("sign_type >=", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeLessThan(String value) {
            addCriterion("sign_type <", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeLessThanOrEqualTo(String value) {
            addCriterion("sign_type <=", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeLike(String value) {
            addCriterion("sign_type like", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeNotLike(String value) {
            addCriterion("sign_type not like", value, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeIn(List<String> values) {
            addCriterion("sign_type in", values, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeNotIn(List<String> values) {
            addCriterion("sign_type not in", values, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeBetween(String value1, String value2) {
            addCriterion("sign_type between", value1, value2, "signType");
            return (Criteria) this;
        }

        public Criteria andSignTypeNotBetween(String value1, String value2) {
            addCriterion("sign_type not between", value1, value2, "signType");
            return (Criteria) this;
        }

        public Criteria andSignIsNull() {
            addCriterion("sign is null");
            return (Criteria) this;
        }

        public Criteria andSignIsNotNull() {
            addCriterion("sign is not null");
            return (Criteria) this;
        }

        public Criteria andSignEqualTo(String value) {
            addCriterion("sign =", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotEqualTo(String value) {
            addCriterion("sign <>", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignGreaterThan(String value) {
            addCriterion("sign >", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignGreaterThanOrEqualTo(String value) {
            addCriterion("sign >=", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLessThan(String value) {
            addCriterion("sign <", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLessThanOrEqualTo(String value) {
            addCriterion("sign <=", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLike(String value) {
            addCriterion("sign like", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotLike(String value) {
            addCriterion("sign not like", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignIn(List<String> values) {
            addCriterion("sign in", values, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotIn(List<String> values) {
            addCriterion("sign not in", values, "sign");
            return (Criteria) this;
        }

        public Criteria andSignBetween(String value1, String value2) {
            addCriterion("sign between", value1, value2, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotBetween(String value1, String value2) {
            addCriterion("sign not between", value1, value2, "sign");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNull() {
            addCriterion("add_time is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNotNull() {
            addCriterion("add_time is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEqualTo(Long value) {
            addCriterion("add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Long value) {
            addCriterion("add_time <>", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThan(Long value) {
            addCriterion("add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Long value) {
            addCriterion("add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Long value) {
            addCriterion("add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeIn(List<Long> values) {
            addCriterion("add_time in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotIn(List<Long> values) {
            addCriterion("add_time not in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Long value1, Long value2) {
            addCriterion("add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotBetween(Long value1, Long value2) {
            addCriterion("add_time not between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andSrcIsNull() {
            addCriterion("src is null");
            return (Criteria) this;
        }

        public Criteria andSrcIsNotNull() {
            addCriterion("src is not null");
            return (Criteria) this;
        }

        public Criteria andSrcEqualTo(Byte value) {
            addCriterion("src =", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcNotEqualTo(Byte value) {
            addCriterion("src <>", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcGreaterThan(Byte value) {
            addCriterion("src >", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcGreaterThanOrEqualTo(Byte value) {
            addCriterion("src >=", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcLessThan(Byte value) {
            addCriterion("src <", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcLessThanOrEqualTo(Byte value) {
            addCriterion("src <=", value, "src");
            return (Criteria) this;
        }

        public Criteria andSrcIn(List<Byte> values) {
            addCriterion("src in", values, "src");
            return (Criteria) this;
        }

        public Criteria andSrcNotIn(List<Byte> values) {
            addCriterion("src not in", values, "src");
            return (Criteria) this;
        }

        public Criteria andSrcBetween(Byte value1, Byte value2) {
            addCriterion("src between", value1, value2, "src");
            return (Criteria) this;
        }

        public Criteria andSrcNotBetween(Byte value1, Byte value2) {
            addCriterion("src not between", value1, value2, "src");
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