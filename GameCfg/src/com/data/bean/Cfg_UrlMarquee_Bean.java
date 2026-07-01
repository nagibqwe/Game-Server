/**
 * Auto generated, do not edit it
 *
 * UrlMarquee配置表
 */
package com.data.bean;

	
public class Cfg_UrlMarquee_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 链接文字
     */
    private final String url_text;
    /**
     * 链接文字
     * @return
     */
    public final String getUrl_text(){
        return url_text;
    }
    /**
     * 功能id
     */
    private final String functionId;
    /**
     * 功能id
     * @return
     */
    public final String getFunctionId(){
        return functionId;
    }
    /**
     * 描述(client ignore)
     */
    private final String comment;
    /**
     * 描述(client ignore)
     * @return
     */
    public final String getComment(){
        return comment;
    }

    public Cfg_UrlMarquee_Bean(int id,String url_text,String functionId,String comment){
        this.id = id;
        this.url_text = url_text;
        this.functionId = functionId;
        this.comment = comment;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("url_text:").append(url_text).append(";");
        str.append("functionId:").append(functionId).append(";");
        str.append("comment:").append(comment).append(";");
        return str.toString();
    }
}
