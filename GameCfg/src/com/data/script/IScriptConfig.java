/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.script;

import java.util.Map;

/**
 *
 * @author hewei
 */
public interface IScriptConfig<Bean> {
    
    /**
     * 数据加载
     * @param contaioners
     */
    public void load(Map<Integer,Bean> contaioners);
}

