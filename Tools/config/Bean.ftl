/**
 * Auto generated, do not edit it
 *
 * ${explain}
 */
<#if package != "" >
package ${package}.bean;
<#else>
package bean;
</#if> 

<#list imports as import>
import ${import}; 
</#list>
	
public class ${classBeanName}{
    <#list fields as field>
    <#if field.filedType != "" >
    /**
     * ${field.filedDesc}
     */
    private final ${field.javaClassName} ${field.filedName};
    /**
     * ${field.filedDesc}
     * @return
     */
    public final ${field.javaClassName} get${field.filedName?cap_first}(){
        return ${field.filedName};
    }
    </#if> 
    </#list>

    public ${classBeanName}(${parms}){
        <#list fields as field>
        <#if field.filedType == "[int]">
        this.${field.filedName} = new ReadIntegerArray(${field.filedName}Str,",");
        <#elseif field.filedType == "[float]" >
        this.${field.filedName} = new ReadFloatArray(${field.filedName}Str,",");
        <#elseif field.filedType == "[string]">
        this.${field.filedName} = new ReadStringArray(${field.filedName}Str,",");
        <#elseif field.filedType == "{int}" >
        this.${field.filedName} = new ReadIntegerArrayEs(${field.filedName}Str,"}",",");
        <#elseif field.filedType == "{float}" >
        this.${field.filedName} = new ReadFloatArrayEs(${field.filedName}Str,"}",",");
        <#elseif field.filedType == "[[int]]" >
        this.${field.filedName} = new ReadIntegerArrayEs(${field.filedName}Str,"]",",");
        <#elseif field.filedType == "[[float]]" >
        this.${field.filedName} = new ReadFloatArrayEs(${field.filedName}Str,"]",",");
        <#elseif field.filedType == "[char]" >
        this.${field.filedName} = new ReadStringArray(${field.filedName}Str,",");
        <#elseif field.filedType == "{char}" >
        this.${field.filedName} = new ReadStringArrayEs(${field.filedName}Str,"}",",");
        <#elseif field.filedType == "[long]" >
        this.${field.filedName} = new ReadLongArray(${field.filedName}Str,",");
        <#elseif field.filedType == "{long}" >
        this.${field.filedName} = new ReadLongArrayEs(${field.filedName}Str,"}",",");
        <#elseif field.filedType != "" >
        this.${field.filedName} = ${field.filedName};
        <#else>
        </#if>
        </#list>
    }

    <#list languageList as field>
    /**
     * ${field.filedDesc}
     * @return
     */
    public final String get${field.filedName?cap_first}Str(){
        return "@" + ${field.filedName};
    }

    </#list>

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        <#list fields as field>
        <#if field.filedType != "" >
        str.append("${field.filedName}:").append(${field.filedName}).append(";");
        </#if> 
        </#list>
        return str.toString();
    }
}
