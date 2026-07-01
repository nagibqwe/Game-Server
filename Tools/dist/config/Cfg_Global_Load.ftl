/**
 * Auto generated, do not edit it
 *
 * Global表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
<#if package != "" >
import ${package}.Global; 
<#else>
import Global; 
</#if> 
<#list imports as import>
import ${import}; 
</#list>

	
public final class Cfg_Global_Load implements IScriptConfig<Object> {
	
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
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "string" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "char" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "float" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "long" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "[int]" >
        Global.${field.filedName?cap_first} = new ReadIntegerArray(${field.javaClassName},",");
        <#elseif field.filedType == "[long]" >
        Global.${field.filedName?cap_first} = new ReadLongArray(${field.javaClassName},",");
        <#elseif field.filedType == "[float]" >
        Global.${field.filedName?cap_first} = new ReadFloatArray(${field.javaClassName},",");
        <#elseif field.filedType == "{int}" >
        Global.${field.filedName?cap_first} = new ReadIntegerArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{long}" >
        Global.${field.filedName?cap_first} = new ReadLongArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{float}" >
        Global.${field.filedName?cap_first} = new ReadFloatArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[[int]]" >
        Global.${field.filedName?cap_first} = new ReadIntegerArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[[float]]" >
        Global.${field.filedName?cap_first} = new ReadFloatArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[char]" >
        Global.${field.filedName?cap_first} = new ReadStringArray(${field.javaClassName},",");
        <#elseif field.filedType == "{char}" >
        Global.${field.filedName?cap_first} = new ReadStringArrayEs(${field.javaClassName},"}",",");
        <#else>

        </#if>
        <#assign x = x + 1>
        <#else>
        loadIndex${index}(contaioners);
    }
        
    private void loadIndex${index}(Map<Integer,Object> contaioners){
        <#if field.filedType == "int">
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "string" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "char" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "float" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "long" >
        Global.${field.filedName?cap_first} = ${field.javaClassName};
        <#elseif field.filedType == "[int]" >
        Global.${field.filedName?cap_first} = new ReadIntegerArray(${field.javaClassName},",");
        <#elseif field.filedType == "[char]" >
        Global.${field.filedName?cap_first} = new ReadStringArray(${field.javaClassName},",");
        <#elseif field.filedType == "{char}" >
        Global.${field.filedName?cap_first} = new ReadStringArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[float]" >
        Global.${field.filedName?cap_first} = new ReadFloatArray(${field.javaClassName},",");
        <#elseif field.filedType == "{int}" >
        Global.${field.filedName?cap_first} = new ReadIntegerArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "{float}" >
        Global.${field.filedName?cap_first} = new ReadFloatArrayEs(${field.javaClassName},"}",",");
        <#elseif field.filedType == "[[int]]" >
        Global.${field.filedName?cap_first} = new ReadIntegerArrayEs(${field.javaClassName},"]",",");
        <#elseif field.filedType == "[[float]]" >
        Global.${field.filedName?cap_first} = new ReadFloatArrayEs(${field.javaClassName},"]",",");
        <#else>

        </#if>
        <#assign index = index + 1 >
        <#assign x=1>
        </#if> 
        </#list>
    }

}
