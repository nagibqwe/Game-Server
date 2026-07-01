/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

/**
 *
 * @author hewei
 */
public class ExcelLoadFiled {
    
    //字段名
    private String filedName;
    //字段描述
    private String filedDesc;
    //第一行：字段类型:int、long、float、char（char原样输出）、string（转为语言包id）、[int]、{int}、[float]、{float}、""、[string]
    private String filedType;
    //java类型名,String、long、int、List<Integer>、List<Float>、List<List<Integer>>、List<List<Float>>、list<String>
    private String javaClassName;
    
    public ExcelLoadFiled(){
        
    }
    
    /**
     * 
     * @param filedName 字段名
     * @param filedDesc 字段描述
     * @param filedType 字段类型:int、long、float、char（char原样输出）、string（转为语言包id）、[int]、{int}、[float]、{float}、""
     * @param javaClassName java类型名,String、long、int、List<Integer>、List<Float>、List<List<Integer>>、List<List<Float>>、List<String>
     */
    public ExcelLoadFiled(String filedName,String filedDesc,String filedType,String javaClassName){
        this.filedName = filedName;
        this.filedDesc = filedDesc;
        this.filedType = filedType;
        this.javaClassName = javaClassName;
    }

    /**
     * 第二行：字段名
     * @return 
     */
    public String getFiledName() {
        return filedName;
    }

    /**
     * 第二行：字段名
     * @param filedName 
     */
    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    /**
     * 第三行：字段描述
     * @return 
     */
    public String getFiledDesc() {
        return filedDesc;
    }

    /**
     * 第三行：字段描述
     * @param filedDesc 
     */
    public void setFiledDesc(String filedDesc) {
        this.filedDesc = filedDesc;
    }

    /**
     * 第五行：字段类型:int、long、float、char（char原样输出）、string（转为语言包id）、[int]、{int}、[float]、{float}、[string]
     * @return 
     */
    public String getFiledType() {
        return filedType;
    }

    /**
     * 第五行：字段类型:int、long、float、char（char原样输出）、string（转为语言包id）、[int]、{int}、[float]、{float}、[string]
     * @param filedType 
     */
    public void setFiledType(String filedType) {
        this.filedType = filedType;
    }

    /**
     * java类型名,String、long、int、List<Integer>、List<Float>、List<List<Integer>>、List<List<Float>>、List<String>
     * @return 
     */
    public String getJavaClassName() {
        return javaClassName;
    }

    /**
     * java类型名,String、long、int、List<Integer>、List<Float>、List<List<Integer>>、List<List<Float>>、List<String>
     * @param javaClassName 
     */
    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }

}
