package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [新手引导]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BINewbie_guide {
	/**
	 * 新手引导步骤id
	 */
	private String step_id = "";
	/**
	 * 操作类型 1:触发 2:完成
	 */
	private String status = "";

	public BINewbie_guide() {}

	public BINewbie_guide(
			String step_id,
			String status
	) {
		setStep_id(step_id);
		setStatus(status);
	}

	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		if (step_id == null || step_id.equals(""))
			this.step_id = "";
		else
			this.step_id = step_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null || status.equals(""))
			this.status = "";
		else
			this.status = status;
	}

}
