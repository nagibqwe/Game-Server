/**
 * Auto generated, do not edit it
 *
 * 配置表管理
 */
package com.data;

import com.data.script.ScriptConfigManager;
import com.data.container.Cfg_Global_Container;
import com.data.container.Cfg_Drop_item_Container;
import com.data.container.Cfg_Drop_package_Container;
import com.data.container.Cfg_MessageString_Container;


<#list list as value>
<#if package != "" >
import ${package}.container.Cfg_${value.filename?cap_first}_Container; 
<#else>
import container.Cfg_${value.filename?cap_first}_Container; 
</#if> 
</#list>
	
public final class CfgManager{

    public static final Cfg_Global_Container Global_Container = new Cfg_Global_Container();

    public static final Cfg_MessageString_Container MessageString_Container = new Cfg_MessageString_Container();

    public static ConfigBase<?> getContaionersByName(String fileName) {
        ConfigBase<?> configBase = ScriptConfigManager.GetInstance().getScriptConfigBase(getJavaClassLoadName(fileName));
        return configBase;
    }
    
    private static String getJavaClassLoadName(String fileName) {
        char oldChar = fileName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = fileName.replaceFirst(oldChar + "", newChar + "");
        return "config.Cfg_" + replace + "_Load";
    }

    public static Cfg_Drop_item_Container getCfg_Drop_item_Container() {
        return Cfg_Drop_item_Container.GetInstance();
    }
    public static final Cfg_Drop_package_Container getCfg_Drop_package_Container() {
        return Cfg_Drop_package_Container.GetInstance();
    }
    <#list list as value>
    public static Cfg_${value.filename?cap_first}_Container getCfg_${value.filename?cap_first}_Container(){
	return Cfg_${value.filename?cap_first}_Container.GetInstance();
    }
    </#list>

}
