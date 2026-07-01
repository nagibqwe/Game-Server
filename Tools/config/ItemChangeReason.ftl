/**
 * Auto generated, do not edit it
 *
 * ItemChangeReason表
 */
<#if package != "" >
package ${package};
<#else>

</#if>

<#list imports as import>
import ${import}; 
</#list>

public final class ItemChangeReason{

    <#list list as field>
    /**
     * ${field.filedDesc}:${field.javaClassName}
     */
    <#if field.filedType == "int">
    public static int ${field.filedName?cap_first} = ${field.javaClassName};
    <#elseif field.filedType == "string" >
    public static String ${field.filedName?cap_first} = ${field.javaClassName};
    <#elseif field.filedType == "char" >
    public static String ${field.filedName?cap_first} = ${field.javaClassName};
    <#elseif field.filedType == "float" >
    public static float ${field.filedName?cap_first} = ${field.javaClassName};
    <#elseif field.filedType == "long" >
    public static long ${field.filedName?cap_first} = ${field.javaClassName};
    <#elseif field.filedType == "[int]" >
    public static ReadIntegerArray ${field.filedName?cap_first};
    <#elseif field.filedType == "[float]" >
    public static ReadFloatArray ${field.filedName?cap_first};
    <#elseif field.filedType == "{int}" >
    public static ReadIntegerArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "{float}" >
    public static ReadFloatArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "[[int]]" >
    public static ReadIntegerArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "[[float]]" >
    public static ReadFloatArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "{char}" >
    public static ReadStringArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "[char]" >
    public static ReadStringArray ${field.filedName?cap_first};
    <#elseif field.filedType == "{long}" >
    public static ReadLongArrayEs ${field.filedName?cap_first};
    <#elseif field.filedType == "[long]" >
    public static ReadLongArray ${field.filedName?cap_first};
    <#else>

    </#if>
    </#list>

}
