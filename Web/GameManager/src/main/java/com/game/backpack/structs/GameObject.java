/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.structs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.game.util.IDConfigUtil;

import java.io.Serializable;

/**
 *
 * @author lanxiang@haowan123.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public abstract class GameObject implements Serializable {

	protected long id;  

	protected GameObject(){
		this.id = IDConfigUtil.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
