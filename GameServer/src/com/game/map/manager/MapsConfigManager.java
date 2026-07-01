package com.game.map.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.manager.Manager;
import com.game.map.structs.ByteDynamicBlock;
import com.game.map.structs.ByteMapCfg;
import com.game.map.structs.ByteMapItem;
import com.game.map.structs.ByteMapTrigger;
import com.game.map.structs.MapDefine;
import com.game.map.structs.VolumeType;
import com.game.map.utils.MapInfoStream;
import com.game.structs.Vector3;
import game.core.util.TimeUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

public class MapsConfigManager {

    private static final Logger log = LogManager.getLogger(MapsConfigManager.class);

    private final static String MapFileHead = "maps/";

    //地图配置
    private final ConcurrentHashMap<Integer, ByteMapCfg> mapCfgStore = new ConcurrentHashMap<>();

    //跳跃传送点 
    private final ConcurrentHashMap<Integer, Vector3> jumpTransport = new ConcurrentHashMap<>();

    private ByteMapItem OnSplitNpc(MapInfoStream stream) throws IOException {
        Vector3 pos = stream.readVector3();
        stream.readVector3();
        float rotW = stream.readFloat();
        String name = stream.readString(32);
        int modelID = stream.readInt();
        ByteMapItem item = new ByteMapItem();
        item.setPos(pos);
        item.setId(modelID);
        item.setRotW(rotW);
        item.setName(name);
        return item;
    }

    private ByteMapTrigger OnSplitTrigger(MapInfoStream stream) throws IOException {
        ByteMapTrigger trg = new ByteMapTrigger();
        Vector3 pos = stream.readVector3();
        stream.readVector3();
        stream.readFloat();
        String name = stream.readString(32);
        int modelID = stream.readInt();
        int shape = stream.readInt();
        String type = stream.readString(32);
        stream.readString(96);
        stream.readVector3();
        List<String> inArgs = stream.readStringList();
        trg.setPos(pos);
        trg.setModelID(modelID);
        trg.setName(name);
        trg.setShape(shape);
        trg.setType(type);
        trg.setInArgs(inArgs);

        stream.readStringList();

        if (VolumeType.Box.compare(shape)) {
            stream.readString(24);
        }
        if (VolumeType.Sphere.compare(shape)) {
            stream.readString(16);
        }
        return trg;
    }

    private enum Singleton {

        INSTANCE;
        MapsConfigManager manager;

        Singleton() {
            this.manager = new MapsConfigManager();
        }

        MapsConfigManager getProcessor() {
            return manager;
        }
    }

    public static MapsConfigManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ConcurrentHashMap<Integer, Vector3> getJumpTransport() {
        return jumpTransport;
    }

    //加载地图配置
    public boolean loadMaps() {
        //获取map列表
        ByteMapCfg map;
        for (Cfg_Mapsetting_Bean mapsetting : CfgManager.getCfg_Mapsetting_Container().getValuees()) {
            map = new ByteMapCfg();
            map.setMapID(mapsetting.getMap_id());
//            log.error("开始加载地图： id =" + mapsetting.getMap_id() + " type=" + mapsetting.getType() + ", z =" + mapsetting.getMap_info() + ", path=" + mapsetting.getMap_grid());
            if (!readMap(mapsetting.getMap_grid(), map)) { //加载地图纹理
                continue;
            }
            if (!readMapInfo(mapsetting.getMap_info(), map)) { //加载地图各种配置
                if (mapsetting.getType() == MapDefine.WORLD_MAP) {
                    log.error("世界地图资源缺失 mapID" + mapsetting.getMap_id());
                    continue;
                }
            }
            mapCfgStore.put(map.getMapID(), map);
        }
        return true;
    }

    //加载跳跃传送点
    public void reloadJumpTran() {
        String path = "config/FlyTeleportCfg.bytes";
        File file = new File(path);
        try {
            MapInfoStream bos = new MapInfoStream(new FileInputStream(file));

            /////////////////////////////读取基本信息/////////////////////
            int count = bos.readInt();
            for (int i = 0; i < count; i++) {
                int transID = bos.readInt();
                Vector3 vec = bos.readVector3();
                jumpTransport.put(transID, vec);
                //LOGGER.error("跳跃传送ID=" + transID + " pos=" + vec);
            }
            bos.close();

        } catch (IOException e) {
            log.error(e, e);
            log.error("加载跳跃传送出错 ioexception");
            System.exit(1);
        }
    }

    //重新加载某个地图
    public ByteMapCfg reloadMap(int mapModelID) {

        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapModelID);
        if (null == config) {
            return null;
        }

        ByteMapCfg map = new ByteMapCfg();
        map.setMapID(config.getMap_id());
        //加载地图纹理
        if (!readMap(config.getMap_grid(), map)) {
            return null;
        }
        //加载地图各种配置
        if (!readMapInfo(config.getMap_info(), map)) {
            return null;
        }
        mapCfgStore.put(map.getMapID(), map);

        return map;
    }

    //重新加载地图配置
    public boolean reloadMaps() {
        loadMaps();
        return true;
    }

    //获取地图配置
    public ByteMapCfg getMapCfg(int mapModelID) {
        return mapCfgStore.get(mapModelID);
    }

    //获取阻挡数据
    public byte[][] getBlock(int mapModelID) {
        ByteMapCfg map = mapCfgStore.get(mapModelID);
        return map.getBlocks();
    }

    private boolean readMap(String filename, ByteMapCfg map) {

        if (filename.equals("")) {
            log.error("没有阻挡信息 mapID: " + map.getMapID());
            //System.exit(1);
            return false;
        }
        try {

            File file = new File(MapFileHead + filename);

            MapInfoStream bis = new MapInfoStream(new FileInputStream(file));
            //LOGGER.error("加载阻挡信息 mapID: " + map.getMapID() + " 文件内容：" + (MapFileHead + filename));
            /////////////////////////////读取基本信息/////////////////////
            int len = bis.readInt();
            bis.readString(len);
            int weightRadius = bis.readInt();
            int colCellCount = bis.readInt();      //地图总列数
            int rowCellCount = bis.readInt();      //地图总行数
            float cellSize = bis.readFloat();    // 一个cell大小 client 是1.0
            bis.readVector3();
            float limitLow = bis.readFloat();   //高度范围，最低
            float limitHigh = bis.readFloat();    //高度范围，最高

//            int blockInfoOffset = bis.readInt();     //阻挡信息在文件中的偏移量
//            int heightInfoOffset = bis.readInt();    //高度信息在文件中的偏移
//
//            int weightMergeDataOffset = bis.readInt();
//            int blockInfoLen = bis.readInt();      //阻挡信息长度，单位：字节(int)
//            int heightInfoLen = bis.readInt();     //高度信息长度，单位：字节(int)
//
//            int mergeWeightDataLength = bis.readInt();
//            /////////////////////////////////////////////////////////////////////
//            //检测版本
//            if (version != 1) //TODO LOGGER and break
//            {
//                LOGGER.error("mapcfg error version");
//            }
//
//            if (!path.equals("PATH")) //文件不是PathGrid文件，策划配置填错了
//            //TODO LOGGER  and break
//            {
//                LOGGER.error("mapcfg error PATH");
//            }
////
//            //检测文件大小
//            if (fileSize != file.length()) //TODO LOGGER and break
//            {
//                LOGGER.error("mapcfg error filesize");
//            }
//
//            //检测偏移量 is overflow
//            int offset1 = blockInfoOffset + blockInfoLen;
//            int offset2 = heightInfoOffset + heightInfoLen;
//            int offset3 = weightMergeDataOffset + mergeWeightDataLength;
//
//            if (offset1 > fileSize || offset2 > fileSize || offset3 > fileSize) //TODO  LOGGER and break
//            {
//                LOGGER.error("mapcfg error offset12345");
//            }
//            //检测阻挡数据
//            int totalCellCount = colCellCount * rowCellCount;
//            if (blockInfoLen != totalCellCount || heightInfoLen / 4 != totalCellCount) //TODO LOGGER and break
//            {
//                LOGGER.error("mapcfg error blockinfo");
//            }
            map.setColCellCount(colCellCount);
            map.setRowCellCount(rowCellCount);
            //开始初始化阻挡数据
            byte[][] blocks = new byte[rowCellCount][colCellCount];

            int blockslen = bis.readInt();
            int counter = 0;
            long nano = System.currentTimeMillis();
            int curRow = 0;
            int curCol = 0;
            int[][] blockdata = new int[blockslen][2];
            for (int i = 0; i < blockslen; ++i) {
                int curCounter = bis.readUShort();
                byte value = bis.read();
                blockdata[i][0] = curCounter;
                blockdata[i][1] = value;
            }
            //LOGGER.error(mapName + " 读取阻档数据：" + (System.currentTimeMillis() - nano) + "毫秒");
            nano = System.currentTimeMillis();
            for (int[] data : blockdata) {
                int curCounter = data[0];
                for (int j = counter; j < (counter + curCounter); ++j) {
                    blocks[curRow][curCol] = (byte) data[1];
                    ++curCol;
                    if (curCol >= colCellCount) {
                        curCol = 0;
                        ++curRow;
                    }
                }
                counter += curCounter;
            }
            //LOGGER.error(mapName + " 加载阻档数据用时：" + (System.currentTimeMillis() - nano) + "毫秒, 条数：" + " x=" + rowCellCount + ",y=" + colCellCount + " =" + blocks.length * blocks[0].length);
            map.setBlocks(blocks);

//            float[][] heigh = new float[rowCellCount][colCellCount];
//            for (int i = 0; i < rowCellCount; i++) {
//                for (int j = 0; j < colCellCount; j++) {
//                    int heightValue = bis.readUShort();
//                    heigh[i][j] = heightValue / 100f;
//                }
//            }
//
//            map.setHeigh(heigh);

            log.info("load mapModelID:" + map.getMapID() + " success mappath");
            bis.close();
            return true;
        } catch (IOException e) {
            log.error(e, e);
            log.error("阻挡信息出错 ioexception");
            if(!TimeUtils.isIDEEnvironment()){
                System.exit(1);
                return false;
            }
            else{
                return true;
            }
        }
    }

    private boolean readMapInfo(String filename, ByteMapCfg map) {
        if (filename.equals("")) {
            return false;
        }
        try {
            File file = new File(MapFileHead + filename);

            MapInfoStream bis = new MapInfoStream(new FileInputStream(file));

            /////////////////////////////读取基本信息/////////////////////
            String scen = bis.readString(4);   //SCEN
            int version = bis.readInt();      //地图版本信息
            int headerSize = bis.readInt();      //文件头大小
            int fileSize = bis.readInt();      //文件大小

            int triggerNum = bis.readInt();      //触发器数量
            int triggerOffset = bis.readInt();      //触发器在文件中的偏移
            int triggerDataLen = bis.readInt();      //单个触发器数据长度

            int npcNum = bis.readInt();      //NPC数量
            int npcOffset = bis.readInt();      //NPC在文件中的偏移
            int npcDataLen = bis.readInt();      //单个NPC数据长度

            int monsterNum = bis.readInt();      //怪物数量
            int monsterOffset = bis.readInt();      //怪物在文件中的偏移
            int monsterDataLen = bis.readInt();      //单个怪物数据长度

            int relivePtNum = bis.readInt();      //复活点数量
            int relivePtOffset = bis.readInt();      //复活点在文件中的偏移
            int relivePtDataLen = bis.readInt();      //单个复活点数据长度

            int wayPtNum = bis.readInt();      //路点数量
            int wayPtOffset = bis.readInt();      //路点在文件中的偏移
            int wayPtDataLen = bis.readInt();      //单个路点数据长度

            int collectNum = bis.readInt();      //采集对象数量
            int collectDataOffset = bis.readInt();      //采集对象在文件中的偏移
            int collectDataLength = bis.readInt();      //单个采集对象数据长度

            int useItemNum = bis.readInt();      //使用物品数量
            int useItemDataOffset = bis.readInt();      //使用物品在文件中的偏移
            int useItemDataLength = bis.readInt();      //单个使用物品数据长度

            int dynamicBlockerCount = bis.readInt();      //动态阻挡个数
            int dynamicBlockerOffset = bis.readInt();      //动态阻挡在文件中的偏移
            int dynamicBlockerLength = bis.readInt();      //单个动态阻挡数据长度

            String reserved = bis.readString(48);   // 预留的 先不管
            /////////////////////////////////////////////////////////////////////
            //检测版本
            if (version != 1) //TODO LOGGER and break
            {
                log.error("mapinfocfg error version");
            }

            //检测 文件类型
            if (!scen.equals("SCEN")) //TODO LOGGER and break
            {
                log.error("mapinfocfg error type");
            }

            //检测文件大小
            if (fileSize != file.length()) //TODO LOGGER and break
            {
                log.error("mapinfocfg error filesize");
            }

            //检测偏移量
            int Offset1 = triggerOffset + triggerDataLen;
            int Offset2 = npcOffset + npcDataLen;
            int Offset3 = monsterOffset + monsterDataLen;
            int Offset4 = relivePtOffset + relivePtDataLen;
            int Offset5 = wayPtOffset + wayPtDataLen;
            int Offset6 = collectDataOffset + collectDataLength;
            int Offset7 = useItemDataOffset + useItemDataLength;
            int Offset8 = dynamicBlockerOffset + dynamicBlockerLength;
            if (Offset1 > fileSize || Offset2 > fileSize
                    || Offset3 > fileSize || Offset4 > fileSize
                    || Offset5 > fileSize || Offset6 > fileSize
                    || Offset7 > fileSize || Offset8 > fileSize) //TODO LOGGER and break
            {
                log.error("mapinfocfg error version");
            }

            //检测文件结束标志
            bis.mark(fileSize - 4);
            String eof = bis.readString(4);
            if (!eof.equals("EOF")) //TODO LOGGER and break
            {
                log.error("mapinfocfg error eof");
            }

            //解析npc list
            bis.mark(npcOffset);
            for (; npcNum > 0; npcNum--) {
                ByteMapItem item = OnSplitNpc(bis);
                map.getNpcCfg().add(item);
            }

            //解析monster list
            bis.mark(monsterOffset);
            for (; monsterNum > 0; monsterNum--) {
                ByteMapItem item = OnSplitNpc(bis);
                map.getMonsterCfg().add(item);
            }

            //解析 复活点 list
            bis.mark(relivePtOffset);
            for (; relivePtNum > 0; relivePtNum--) {
                ByteMapItem item = OnSplitNpc(bis);
                map.getReliveCfg().add(item);
            }
            //解析 采集物 list
            bis.mark(collectDataOffset);
            for (; collectNum > 0; collectNum--) {
                ByteMapItem item = OnSplitNpc(bis);
                map.getCollectCfg().add(item);
            }
            //解析 使用物品 list
            bis.mark(useItemDataOffset);
            for (; useItemNum > 0; useItemNum--) {
                ByteMapItem item = OnSplitNpc(bis);
                map.getUseItemCfg().add(item);
            }

            //解析触发器
            bis.mark(triggerOffset);
            for (; triggerNum > 0; triggerNum--) {
                ByteMapTrigger trg = OnSplitTrigger(bis);

                map.getTriggerCfg().add(trg);
            }

            //读取动态阻挡数据
            bis.mark(dynamicBlockerOffset);
            for (; dynamicBlockerCount > 0; dynamicBlockerCount--) {
                ByteDynamicBlock dynamic = new ByteDynamicBlock();
                dynamic.params(bis);
                map.getDynamics().add(dynamic);
            }

            bis.close();

            log.info("load mapModelID:" + map.getMapID() + " success mapinfo");

            return true;
        } catch (IOException e) {
            log.error(e, e);
            log.error("mapinfocfg error IOException");
            return false;
        }
    }

}
