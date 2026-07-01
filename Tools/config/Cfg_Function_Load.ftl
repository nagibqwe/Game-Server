/**
 * Auto generated, do not edit it
 *
 * Global表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
<#if package != "" >
import ${package}.${classBeanName};
<#else>
import ${classBeanName};
</#if>

<#list imports as import>
import ${import}; 
</#list>



public final class  ${classLoadName}  implements IScriptConfig<Object> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Object> contaioners){
        <#assign index=1>
        <#assign x=1>
        <#list list as field>
        <#if x != 200 >
        <#if field.filedType == "int">
        ${classBeanName}.${field.filedName?cap_first} = ${field.javaClassName};
        <#else>

        </#if>
        <#assign x = x + 1>
        <#else>
        loadIndex${index}(contaioners);
    }
        
    private void loadIndex${index}(Map<Integer,Object> contaioners){
        <#if field.filedType == "int">
        ${classBeanName}.${field.filedName?cap_first} = ${field.javaClassName};
        <#else>

        </#if>
        <#assign index = index + 1 >
        <#assign x=1>
        </#if> 
        </#list>
    }

}
