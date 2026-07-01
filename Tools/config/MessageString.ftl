/**
 * Auto generated, do not edit it
 *
 * MessageString表
 */
<#if package != "" >
package ${package};
<#else>

</#if>

<#list imports as import>
import ${import}; 
</#list>

public final class MessageString{

    <#list list as field>
    /**
     * ${field.filedDesc}:${field.javaClassName}
     */
    <#if field.filedType == "int">
    public static int ${field.filedName} = ${field.javaClassName};
    <#elseif field.filedType == "string" >
    public static String ${field.filedName} = ${field.javaClassName};
    <#elseif field.filedType == "char" >
    public static String ${field.filedName} = ${field.javaClassName};
    <#elseif field.filedType == "float" >
    public static float ${field.filedName} = ${field.javaClassName};
    <#elseif field.filedType == "long" >
    public static long ${field.filedName} = ${field.javaClassName};
    <#elseif field.filedType == "[int]" >
    public static ReadIntegerArray ${field.filedName};
    <#elseif field.filedType == "[float]" >
    public static ReadFloatArray ${field.filedName};
    <#elseif field.filedType == "{int}" >
    public static ReadIntegerArrayEs ${field.filedName};
    <#elseif field.filedType == "{float}" >
    public static ReadFloatArrayEs ${field.filedName};
    <#elseif field.filedType == "[[int]]" >
    public static ReadIntegerArrayEs ${field.filedName};
    <#elseif field.filedType == "[[float]]" >
    public static ReadFloatArrayEs ${field.filedName};
    <#elseif field.filedType == "{char}" >
    public static ReadStringArrayEs ${field.filedName};
    <#elseif field.filedType == "[char]" >
    public static ReadStringArray ${field.filedName};
    <#elseif field.filedType == "{long}" >
    public static ReadLongArrayEs ${field.filedName};
    <#elseif field.filedType == "[long]" >
    public static ReadLongArray ${field.filedName};
    <#else>

    </#if>
    </#list>

}
