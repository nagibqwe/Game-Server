package com.game.commercialize.struct;

import java.util.ArrayList;
import java.util.List;

public class FCCharge {
	// 首充
	private Charge first;

	// 续充
	private Charge next;

	//百元首冲
	private Charge hundred;

	// 首充是否已经完成
	private boolean isFcComplete;

	private boolean isFcHundredComplete;

	// 已经领取的配置ID
	private List<Integer> rewards;

	public static FCCharge New() {
		FCCharge fcCharge = new FCCharge();
		fcCharge.setFirst(Charge.New(0));
		fcCharge.setNext(Charge.New(1));
		fcCharge.setHundred(Charge.New(2));
		fcCharge.setRewards(new ArrayList<>());
		fcCharge.setFcComplete(false);
		return fcCharge;
	}

	public Charge get(int typ) {
	    if (typ == 0)
	        return first;
	    else if (typ == 1)
	        return next;
		else if (typ == 2)
			return hundred;

	    return null;
    }

	public boolean isReward(int cfgID) {
		return this.rewards.contains(cfgID);
	}

	public List<Integer> getRewards() {
		return rewards;
	}

	public void setRewards(List<Integer> rewards) {
		this.rewards = rewards;
	}

	public void addReward(int cfgID) {
		if (!isReward(cfgID))
			this.rewards.add(cfgID);
	}

	public Charge getFirst() {
		return first;
	}

	public void setFirst(Charge first) {
		this.first = first;
	}

	public Charge getNext() {
		return next;
	}

	public void setNext(Charge next) {
		this.next = next;
	}

    public boolean isFcComplete() {
        return isFcComplete;
    }

    public void setFcComplete(boolean fcComplete) {
        isFcComplete = fcComplete;
    }

	public Charge getHundred() {
		return hundred;
	}

	public void setHundred(Charge hundred) {
		this.hundred = hundred;
	}

	public boolean isFcHundredComplete() {
		return isFcHundredComplete;
	}

	public void setFcHundredComplete(boolean fcHundredComplete) {
		isFcHundredComplete = fcHundredComplete;
	}
}
