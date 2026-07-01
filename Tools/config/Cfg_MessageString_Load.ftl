/**
 * Auto generated, do not edit it
 *
 * Messagestring表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
<#if package != "" >
import ${package}.MessageString;
<#else>
import MessageString;
</#if> 
<#list imports as import>
import ${import}; 
</#list>

	
public final class Cfg_MessageString_Load implements IScriptConfig<Object> {
	
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
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "string" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "char" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "float" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "long" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "[int]" >
            MessageString.${field.filedName} = new ReadIntegerArray(${field.javaClassName},",");
        <#elseif field.filedType == "[long]" >
            MessageString.${field.filedName} = new ReadLongArray(${field.javaClassName},",");
        <#elseif field.filedType == "[float]" >
            MessageString.${field.filedName} = new ReadFloatArray(${field.javaClassName},",");
        <#elseif field.filedType == "{int}" >
            MessageString.${field.filedName} = new ReadIntegerArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{long}" >
            MessageString.${field.filedName} = new ReadLongArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{float}" >
            MessageString.${field.filedName} = new ReadFloatArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[[int]]" >
            MessageString.${field.filedName} = new ReadIntegerArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[[float]]" >
            MessageString.${field.filedName} = new ReadFloatArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[char]" >
            MessageString.${field.filedName} = new ReadStringArray(${field.javaClassName},",");
        <#elseif field.filedType == "{char}" >
            MessageString.${field.filedName} = new ReadStringArrayEs(${field.javaClassName},"}",",");
        <#else>

        </#if>
        <#assign x = x + 1>
        <#else>
        loadIndex${index}(contaioners);
    }
        
    private void loadIndex${index}(Map<Integer,Object> contaioners){
        <#if field.filedType == "int">
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "string" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "char" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "float" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "long" >
            MessageString.${field.filedName} = ${field.javaClassName};
        <#elseif field.filedType == "[int]" >
            MessageString.${field.filedName} = new ReadIntegerArray(${field.javaClassName},",");
        <#elseif field.filedType == "[char]" >
            MessageString.${field.filedName} = new ReadStringArray(${field.javaClassName},",");
        <#elseif field.filedType == "{char}" >
            MessageString.${field.filedName} = new ReadStringArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[float]" >
            MessageString.${field.filedName} = new ReadFloatArray(${field.javaClassName},",");
        <#elseif field.filedType == "{int}" >
            MessageString.${field.filedName} = new ReadIntegerArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{float}" >
            MessageString.${field.filedName} = new ReadFloatArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[[int]]" >
            MessageString.${field.filedName} = new ReadIntegerArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[[float]]" >
            MessageString.${field.filedName} = new ReadFloatArrayEs(${field.javaClassName},"]",",");
        <#else>

        </#if>
        <#assign index = index + 1 >
        <#assign x=1>
        </#if> 
        </#list>
    }

}
