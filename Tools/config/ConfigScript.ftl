/**
 * Auto generated, do not edit it
 *
 * ${explain}
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
<#if package != "" >
import ${package}.bean.${classBeanName}; 
<#else>
import bean.${classBeanName}; 
</#if> 

	
public final class ${classLoadName} implements IScriptConfig<${classBeanName}> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,${classBeanName}> contaioners){
        contaioners.clear();
        <#assign index=1>
        <#assign x=1>
        <#list values as field>
        <#if x != 200 >
        contaioners.put(${field.key}, new ${classBeanName}(${field.value}));
		<#assign x = x + 1>
        <#else>
        loadIndex${index}(contaioners);
    }
        
    private void loadIndex${index}(Map<Integer,${classBeanName}> contaioners){
        contaioners.put(${field.key}, new ${classBeanName}(${field.value}));
        <#assign index = index + 1 >
        <#assign x=1>
        </#if> 
        </#list>
    }

}
