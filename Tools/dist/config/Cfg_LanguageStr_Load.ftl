/**
 * Auto generated, do not edit it
 *
 * ${explain}
 */
 
package load;

import java.util.HashMap;
import com.data.script.ScriptConfigManager;
import com.data.script.IScriptConfig;
import java.util.concurrent.ConcurrentHashMap;
<#if package != "" >
import ${package}.bean.${classBeanName}; 
<#else>
import bean.${classBeanName}; 
</#if> 

	
public class ${classLoadName} implements IScriptConfig {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(ConcurrentHashMap<Integer,Object> contaioners){
        contaioners.clear();
        String str;
        HashMap<Integer, String> allServerStr = ScriptConfigManager.loadLanguage();
        <#assign index=1>
        <#assign x=1>
        <#list values as field>
        <#if x != 500 >
        str = allServerStr.get(${field?c});
        if(str != null){
            contaioners.put(${field?c}, new ${classBeanName}(${field?c},str));
        }
	<#assign x = x + 1>
        <#else>
        loadIndex${index}(contaioners,allServerStr);
    }
        
    private void loadIndex${index}(ConcurrentHashMap<Integer,Object> contaioners,HashMap<Integer, String> allServerStr){
        String str;
        str = allServerStr.get(${field?c});
        if(str != null){
            contaioners.put(${field?c}, new ${classBeanName}(${field?c},str));
        }
        <#assign index = index + 1 >
        <#assign x=1>
        </#if> 
        </#list>
    }

}
