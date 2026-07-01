package com.game.chum.struct;

import com.game.db.bean.ChumBean;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @explain: 挚友扩展数据
 * @time Created on 2019/10/22 17:57.
 * @author: tc
 */
public class ChumBeanExt extends ChumBean {
	private List<ChumMember> members;

	public List<ChumMember> getMembers() {
		return members;
	}

	public void setMembers(List<ChumMember> members) {
		this.members = members;
	}

	public ChumBeanExt(ChumBean bean) {
		this.setId(bean.getId());
		this.setRid1(bean.getRid1());
		this.setRid2(bean.getRid2());
		this.setName(bean.getName());
		this.setAnno(bean.getAnno());
		this.setLvl(bean.getLvl());
		this.setExp(bean.getExp());
		this.setFreet(bean.getFreet());
		this.setDatas(bean.getDatas());
		this.setLastfreshtime(bean.getLastfreshtime());

		setMembers(JsonUtils.parseObject(bean.getDatas(), new TypeReference<ArrayList<ChumMember>>(){}));
	}

	public ChumBeanExt(Long id, Long rid1, Long rid2) {
		List<ChumMember> members = new ArrayList<ChumMember>();
		members.add(new ChumMember(rid1, 0));
		members.add(new ChumMember(rid2, 0));
		setMembers(members);

		this.setId(id);
		this.setRid1(rid1);
		this.setRid2(rid2);
		this.setName("");
		this.setAnno("");
		this.setLvl(1);
		this.setExp(0);
		this.setFreet((short) 1);
		this.setDatas("");
		this.setLastfreshtime(TimeUtils.Time());
	}

	/**
	 * 获取保存数据
	 * @return
	 */
	public ChumBean getSave() {
		this.setDatas(JsonUtils.toJSONString(getMembers()));
		return this;
	}
}
