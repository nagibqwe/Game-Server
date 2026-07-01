package common.ipfind;

import com.game.ipfind.script.IIPFinderScript;
import com.game.ipfind.structs.IP2RegionInfo;
import game.core.script.IScript;
import game.core.stream.BinaryReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaozhaoguang
 * @desc IPFindScript
 * @date Created on 2021/7/29 20:24
 **/
public class IPFinderScript implements IScript, IIPFinderScript {

    private static final Logger logger = LogManager.getLogger(IPFinderScript.class);
    //数据 九  零一 起玩 www.9 0175. c   om
    List<IP2RegionInfo> dataList = null;

    /**
     * 通过ip地址获取区域ID
     * @param ip 0.0.0.1 ~ 255.255.255.255
     * @return
     */
    @Override
    public int getRegion(String ip) {
        if(dataList == null){
            loadData();
        }
        IP2RegionInfo res = findEx(ip);
        if(res != null){
            return res.getId();
        }
        return 0;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 进行二分法查找
     * @param ip
     * @return
     */
    private IP2RegionInfo findEx(String ip)
    {
        long n = ipAddress2Long(ip);
        int sidx = 0;
        int eidx = dataList.size()/2;
        int findcount = 0;
        while (sidx < eidx){

            findcount++;
            IP2RegionInfo info = dataList.get(eidx);
            //logger.info("_ipData:" + sidx + "---"+ eidx + "=" +info.getSipValue() + "::" + (info.getSipValue()  > n) + "----" + info.getEipValue() + "::" + (info.getEipValue() < n) + "::::" + n + ":::" + sidx + "::");
            if (info.getSipValue() > n){
                eidx = sidx + (eidx - sidx) / 2;
            }
            else if (info.getEipValue() < n){
                int tmp = eidx - sidx;
                sidx = eidx;
                eidx = sidx + tmp / 2 + 1;
            }
            else{//info.getSipValue() <= n && info.getEipValue() >= n 找到了.
                return info;
            }
        }
        logger.info("没有找到IP地址:" + sidx + ":::" + eidx + "::" + findcount + "::" + n + "::" + ip);
        return null;
    }
    /**
     * ip地址转换为Long型
     * @param ip
     * @return
     */
    private long ipAddress2Long(String ip)
    {
        try
        {
            String[] iarr = ip.split("\\.");

            long res =  Long.parseLong(iarr[0]) * 1000000000;
                 res += Long.parseLong(iarr[1]) * 1000000;
                 res += Long.parseLong(iarr[2]) * 1000;
                 res += Long.parseLong(iarr[3]);
            logger.info(ip+"::"+iarr.length+"::"+res);
            return res;
        }
        catch(Exception ex)
        {
            logger.error(ex);
        }
        return -1;

    }

    /**
     * 加载文件数据,注意这里的文件数据一定经过排序之后的.
     */
    @Override
    public void loadData(){
        dataList = new ArrayList<>();
        //这个文件的数据是经过从小到大排序的
        //格式:Long,Long,Int,String(int,byte[]),String(int,byte[])
        String path = "config/ip2region.bytes";
        File file = new File(path);
        logger.info("开始加载ip2region.bytes文件... ...");
        try {
            BinaryReader bos = new BinaryReader(new FileInputStream(file));
            /////////////////////////////读取基本信息/////////////////////
            int count = bos.readInt();
            for (int i = 0; i < count; i++) {
                IP2RegionInfo ri = new IP2RegionInfo();
                //开始ip地址
                ri.setSipValue(bos.readLong());
                //结束ip地址
                ri.setEipValue(bos.readLong());
                //区域ID
                ri.setId(bos.readInt());
                //国家
                int len = bos.readInt();
                byte[] lb = new byte[len];
                bos.read(lb);
                ri.setCountry(new String(lb, "UTF8"));
                len = bos.readInt();
                //省/市
                lb = new byte[len];
                bos.read(lb);
                ri.setState(new String(lb, "UTF8"));
                dataList.add(ri);
                /*
                if(i > 162853){
                    logger.info("::"+ri.getId()+"::"+ri.getSipValue()+"::"+ri.getEipValue()+"::"+ri.getCountry()+"::"+ri.getState());
                }*/
            }
            logger.info("加载ip2region.bytes文件完成,数据数量:"+dataList.size());
            bos.close();

            /*
            //测试代码
            String testip = "128.90.57.10";
            IP2RegionInfo ri = findEx(testip);
            if(ri!=null) {
                logger.info("查找:" + testip + "::" + ri.getId() + "::" + ri.getSipValue() + "::" + ri.getEipValue() + "::" + ri.getCountry() + "::" + ri.getState());
            }
            else{
                logger.info("查找:" + testip + "失败!!");
            }
           */

        } catch (IOException e) {
            logger.error(e, e);
            logger.error("加载ip2region文件失败!"+path);
            System.exit(1);
        }
    }

}
