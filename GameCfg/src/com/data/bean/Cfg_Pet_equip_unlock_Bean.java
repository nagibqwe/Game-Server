/**
 * Auto generated, do not edit it
 *
 * pet_equip_unlock配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Pet_equip_unlock_Bean{
    /**
     * 唯一id
     */
    private final int Id;
    /**
     * 唯一id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 槽位id
     */
    private final int site;
    /**
     * 槽位id
     * @return
     */
    public final int getSite(){
        return site;
    }
    /**
     * 槽位解锁条件
     */
    private final ReadIntegerArrayEs siteUnlock;
    /**
     * 槽位解锁条件
     * @return
     */
    public final ReadIntegerArrayEs getSiteUnlock(){
        return siteUnlock;
    }
    /**
     * 槽位解锁  道具_数量
     */
    private final ReadIntegerArray siteUnlockItem;
    /**
     * 槽位解锁  道具_数量
     * @return
     */
    public final ReadIntegerArray getSiteUnlockItem(){
        return siteUnlockItem;
    }
    /**
     * 部位
     */
    private final int partId;
    /**
     * 部位
     * @return
     */
    public final int getPartId(){
        return partId;
    }
    /**
     * 部位解锁条件
     */
    private final ReadIntegerArrayEs partUnlock;
    /**
     * 部位解锁条件
     * @return
     */
    public final ReadIntegerArrayEs getPartUnlock(){
        return partUnlock;
    }

    public Cfg_Pet_equip_unlock_Bean(int Id,int site,String siteUnlockStr,String siteUnlockItemStr,int partId,String partUnlockStr){
        this.Id = Id;
        this.site = site;
        this.siteUnlock = new ReadIntegerArrayEs(siteUnlockStr,"}",",");
        this.siteUnlockItem = new ReadIntegerArray(siteUnlockItemStr,",");
        this.partId = partId;
        this.partUnlock = new ReadIntegerArrayEs(partUnlockStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("site:").append(site).append(";");
        str.append("siteUnlock:").append(siteUnlock).append(";");
        str.append("siteUnlockItem:").append(siteUnlockItem).append(";");
        str.append("partId:").append(partId).append(";");
        str.append("partUnlock:").append(partUnlock).append(";");
        return str.toString();
    }
}
