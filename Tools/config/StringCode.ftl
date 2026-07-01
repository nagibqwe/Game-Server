/**
 * Auto generated, do not edit it
 *
 * 语言提示表
 */
<#if package != "" >
package ${package};
<#else>

</#if>

public enum StringCode{

    <#list list as field>
    /**
     * ${field.filedDesc}
     */
    ${field.filedName?cap_first}(${field.javaClassName},${field.filedType}),
    </#list>
    ;

    /**
     * 语言包id
     */
    public final int code;
    /**
     * 提示类型：1：tips；2：系统聊天4：跑马灯8：喇叭消息16：messagebox
     */
    public final int type;
    /**
     * 字符
     */
    public final String str;

    /**
     *
     * @param name 语言包id
     * @param code 提示类型：1：tips；2：系统聊天4：跑马灯8：喇叭消息16：messagebox
     */
    private StringCode(int code, int type) {
        this.code = code;
        this.type = type;
        this.str = "@" + code;
    }

    @Override
    public String toString(){
        return str;
    }
}
