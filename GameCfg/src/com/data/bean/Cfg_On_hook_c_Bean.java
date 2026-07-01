/**
 * Auto generated, do not edit it
 *
 * on_hook_c配置表
 */
package com.data.bean;

	
public class Cfg_On_hook_c_Bean{
    /**
     * 序号
排序是按照配置表顺序排的（配置时需要注意最大值）
     */
    private final int Id;
    /**
     * 序号
排序是按照配置表顺序排的（配置时需要注意最大值）
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 文字显示
     */
    private final String describe;
    /**
     * 文字显示
     * @return
     */
    public final String getDescribe(){
        return describe;
    }
    /**
     * 图片
     */
    private final int picture;
    /**
     * 图片
     * @return
     */
    public final int getPicture(){
        return picture;
    }
    /**
     * 是否有+号扩展（0无1有）
     */
    private final int extend;
    /**
     * 是否有+号扩展（0无1有）
     * @return
     */
    public final int getExtend(){
        return extend;
    }
    /**
     * 最大百分百进度条
     */
    private final int max_progress;
    /**
     * 最大百分百进度条
     * @return
     */
    public final int getMax_progress(){
        return max_progress;
    }

    public Cfg_On_hook_c_Bean(int Id,String describe,int picture,int extend,int max_progress){
        this.Id = Id;
        this.describe = describe;
        this.picture = picture;
        this.extend = extend;
        this.max_progress = max_progress;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("describe:").append(describe).append(";");
        str.append("picture:").append(picture).append(";");
        str.append("extend:").append(extend).append(";");
        str.append("max_progress:").append(max_progress).append(";");
        return str.toString();
    }
}
