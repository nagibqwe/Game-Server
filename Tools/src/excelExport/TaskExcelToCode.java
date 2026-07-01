/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author hewei
 */
public class TaskExcelToCode {

    public final static Logger logger = Logger.getLogger("ERRORLOG");

    /**
     * 怪物名字对应id
     */
    public final static HashMap<String, Integer> monsters = new HashMap<>();
    /**
     * 收集物名字对应id
     */
    public final static HashMap<String, Integer> objects = new HashMap<>();
    /**
     * npc名字对应id
     */
    public final static HashMap<String, Integer> npcs = new HashMap<>();
    /**
     * 地图名字对应id
     */
    public final static HashMap<String, Integer> maps = new HashMap<>();

    private static final int TargetFinish_Talk = 0;//对话
    private static final int TargetFinish_kill = 1;//杀怪
    private static final int TargetFinish_killget = 2;//杀怪收集
    private static final int TargetFinish_collectv = 3;//采集
    private static final int TargetFinish_finishcopy = 4;//完成副本
    private static final int TargetFinish_minicopy = 5;//完成位面
    private static final int TargetFinish_Specila = 6;//完成特定功能
    private static final int TargetFinish_EnterActive = 7;//进入特定活动
    private static final int TargetFinish_Level = 8;//达到等级
    private static final int TargetFinish_FinishStory = 9;//完成剧情
    private static final int TargetFinish_Client = 10;//需客户端统计类

    /**
     * 字段对应配置表所在列数
     */
    private static final int Id = 0;
    private static final int MapDataId = 1;
    private static final int Task_Type = 2;
    private static final int Target_type = 3;
    private static final int Target_Subtype = 4;
    private static final int Target_Subpara1 = 5;
    private static final int Target_Subpara2 = 6;
    private static final int Task_commitlv = 9;
    private static final int Dailytask_group = 10;
    private static final int Next_id = 11;
    private static final int Front_id = 12;
    private static final int Target_kill = 19;
    private static final int Target_killget = 20;
    private static final int Target_collectv = 21;
    private static final int Target_finishcopy = 22;
    private static final int Target_minicopy = 23;
    private static final int Task_committype = 24;
    private static final int Task_commitnpc = 25;
    private static final int Task_reward_exp = 28;
    private static final int Task_reward_goldtype = 29;
    private static final int Task_reward_goldnum = 30;
    private static final int Task_reward_item = 31;
    private static final int NeedSave = 34;
    private static final int AddBuff = 36;
    private static final int AddAngerValue = 37;
    private static final int CanFly = 38;

    /**
     * 名字对应key值
     *
     * @param fileName
     * @param filed
     * @param row
     * @param cellNumber
     */
    public static void stringToKey(String fileName, ExcelLoadFiled filed, XSSFRow row, int cellNumber) {
        try {
            switch (fileName) {
                case "npc":
                    if (!filed.getFiledName().equals("refname")) {
                        return;
                    }
                    npcs.put(row.getCell(cellNumber).toString(), Integer.parseInt(row.getCell(0).getRawValue()));
                    break;
                case "monster":
                    if (!filed.getFiledName().equals("name")) {
                        return;
                    }
                    monsters.put(row.getCell(cellNumber).toString(), Integer.parseInt(row.getCell(0).getRawValue()));
                    break;
                case "mapsetting":
                    if (!filed.getFiledName().equals("desc")) {
                        return;
                    }
                    maps.put(row.getCell(cellNumber).toString(), Integer.parseInt(row.getCell(0).getRawValue()));
                    break;
                case "task_item":
                    if (!filed.getFiledName().equals("task_objectname")) {
                        return;
                    }
                    objects.put(row.getCell(cellNumber).toString(), Integer.parseInt(row.getCell(0).getRawValue()));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(fileName + "中:" + cellNumber + "行中的字符串没有找到key值", e);
        }
    }

    /**
     * 获得excel数据
     *
     * @param excelPath
     * @param javaCodeBasePathes
     */
    public static void taskToCode(String excelPath, List<String> javaCodeBasePathes) {
        File f = new File(excelPath + File.separator + "task.xlsx");
        if (!f.canRead()) {
            return;
        }
        try {
            try (FileInputStream in = new FileInputStream(excelPath + File.separator + "task.xlsx")) {
                ExcelLoadData datas = initTaskExcelLoadData();
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    logger.error("任务导表失败，没有表数据");
                    return;
                }
                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表
                String sheetName = sheet.getSheetName();
                if (sheetName == null) {
                    logger.info("tesk工作表名无效,不能以Sheet开始命名");
                    return;
                }
                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 2) {
                    logger.info("task工作表行数格式错误，不得低于2行");
                    return;
                }
                //取出所有数据值
                for (int i = 3; i <= rows; i++) {
                    readRows(sheet, i, datas);
                }
                datas.setIsSuccess(true);
                datas.setBeanName("task");
                for (String s : javaCodeBasePathes) {
                    ExcelToCode.toJavaCode(s, datas);
                }
                logger.info("task生成java代码成功");
            }
        } catch (Exception e) {
            logger.error("task表读取错误", e);
        }
    }

    private static void readRows(XSSFSheet sheet, int rowNum, ExcelLoadData datas) {
        int colNum = 0;
        try {
            XSSFRow row = sheet.getRow(rowNum);
            if (row == null) {
                logger.error("task表" + rowNum + "数据为空");
                return;
            }
            Field field = new Field();
            //读取任务id
            colNum = Id;
            XSSFCell cell = row.getCell(colNum);
            if (cell == null) {
                logger.error("task表读" + rowNum + "行" + colNum + "列错误");
                return;
            }
            String str = cell.getRawValue();
            if (str == null) {
                logger.error("task表读" + rowNum + "行" + colNum + "列错误");
                return;
            }
            field.setKey(str);
            StringBuilder value = new StringBuilder(str).append(",");
            //读取任务地图id
            colNum = MapDataId;
            cell = row.getCell(colNum);
            if (cell == null) {
                str = "0";
            } else {
                str = cell.toString();
                if (str == null || str.isEmpty()) {
                    str = "0";
                } else {
                    Integer numValue = maps.get(str);
                    if (numValue == null) {
                        str = "0";
                    } else {
                        str = numValue.toString();
                    }
                }
            }
            value.append(str).append(",");
            //读取任务类型
            colNum = Task_Type;
            normalIntRead(row, rowNum, colNum, value);

            //读取完成任务类型
            int targetType;
            colNum = Target_type;
            cell = row.getCell(colNum);
            if (cell == null) {
                logger.error("task表读" + rowNum + "行" + colNum + "列错误");
                return;
            }
            str = cell.toString();
            if (null != str) {
                switch (str) {
                    case "对话":
                        targetType = TargetFinish_Talk;
                        break;
                    case "杀怪":
                        targetType = TargetFinish_kill;
                        break;
                    case "打怪收集":
                        targetType = TargetFinish_killget;
                        break;
                    case "采集":
                        targetType = TargetFinish_collectv;
                        break;
                    case "完成副本":
                        targetType = TargetFinish_finishcopy;
                        break;
                    case "完成剧情":
                        targetType = TargetFinish_FinishStory;
                        break;
                    case "完成位面":
                        targetType = TargetFinish_minicopy;
                        break;
                    case "完成特定功能":
                        targetType = TargetFinish_Specila;
                        break;
                    case "进入特定活动":
                        targetType = TargetFinish_EnterActive;
                        break;
                    case "达到等级":
                        targetType = TargetFinish_Level;
                        break;
                    case "需客户端统计类":
                        targetType = TargetFinish_Client;
                        break;
                    default:
                        logger.error("task表读" + rowNum + "行" + colNum + "列完成任务类型配置错误");
                        return;
                }
            } else {
                logger.error("task表读" + rowNum + "行" + colNum + "列完成任务类型配置错误");
                return;
            }

            value.append(targetType).append(",");
            //读取提交任务子类型
            colNum = Target_Subtype;
            normalIntRead(row, rowNum, colNum, value);
            //读取提交任务子类型所需参数1
            colNum = Target_Subpara1;
            normalIntRead(row, rowNum, colNum, value);
            //读取提交任务子类型所需参数2
            colNum = Target_Subpara2;
            normalIntRead(row, rowNum, colNum, value);
            //读取提交任务所需等级
            colNum = Task_commitlv;
            normalIntRead(row, rowNum, colNum, value);
            //读取日常任务的组id
            colNum = Dailytask_group;
            normalIntRead(row, rowNum, colNum, value);
            //读取下一任务
            colNum = Next_id;
            normalListRead(row, rowNum, colNum, value, "[int]");
            //读取上一任务
            colNum = Front_id;
            normalIntRead(row, rowNum, colNum, value);
            //读取完成任务条件
            readTargetFinish(row, rowNum, targetType, value);
            //读取提交任务类型
            colNum = Task_committype;
            normalIntRead(row, rowNum, colNum, value);
            colNum = Task_commitnpc;
            //读取提交任务npcid
            cell = row.getCell(colNum);
            if (cell == null) {
                str = "0";
            } else if (cell.getRawValue() == null) {
                str = "0";
            } else {
                str = cell.toString();
                if (npcs.containsKey(str)) {
                    str = ("" + npcs.get(str));
                } else {
                    logger.info("task表读" + rowNum + "行" + colNum + "提交任务npc未找到：" + str);
                    str = "0";
                }
            }
            value.append(str).append(",");
            //读取经验奖励
            colNum = Task_reward_exp;
            normalIntRead(row, rowNum, colNum, value);
            //读取奖励货币类型
            colNum = Task_reward_goldtype;
            normalIntRead(row, rowNum, colNum, value);
            //读取经验货币数量
            colNum = Task_reward_goldnum;
            normalIntRead(row, rowNum, colNum, value);
            //读取是否任务保存
            colNum = NeedSave;
            normalIntRead(row, rowNum, colNum, value);
            //读取增加的buffId
            colNum = AddBuff;
            normalIntRead(row, rowNum, colNum, value);
            //读取增加的怒气值
            colNum = AddAngerValue;
            normalIntRead(row, rowNum, colNum, value);
            //读取是否可以飞行
            colNum = CanFly;
            normalIntRead(row, rowNum, colNum, value);
            //读取奖励列表
            colNum = Task_reward_item;
            cell = row.getCell(colNum);
            if (cell == null) {
                str = "\"\"";
            } else if (cell.getRawValue() == null) {
                str = "\"\"";
            } else {
                str = "\"" + cell.toString() + "\"";
            }
            str = ConfigExcelReader.getReallyStr("{int}", str);
            value.append(str);
            field.setValue(value.toString());
            datas.getServerValues().add(field);
        } catch (Exception e) {
            logger.error("task表读" + (rowNum + 1) + "行" + colNum + "列错误", e);
        }
    }

    private static void readTargetFinish(XSSFRow row, int rowNum, int targetType, StringBuilder value) throws Exception {
        switch (targetType) {
            case TargetFinish_Talk:
            case TargetFinish_Specila:
            case TargetFinish_EnterActive:
            case TargetFinish_Level:
            case TargetFinish_Client:
                value.append("\"\"").append(",");
                break;
            case TargetFinish_kill:
                readTargetFinish_kill(row, rowNum, value);
                break;
            case TargetFinish_killget:
                readTargetFinish_killGet(row, rowNum, value);
                break;
            case TargetFinish_collectv:
                readTargetFinish_collectv(row, rowNum, value);
                break;
            case TargetFinish_finishcopy:
            case TargetFinish_FinishStory:
                readTargetFinish_finishcopy(row, rowNum, value);
                break;
            case TargetFinish_minicopy:
                readTargetFinish_minicopy(row, rowNum, value);
                break;
            default:
                break;
        }
    }

    private static void readTargetFinish_kill(XSSFRow row, int rowNum, StringBuilder value) throws Exception {
        String str = getString(row, Target_kill);
        if (str == null) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列数据不应该为空");
        }
        String[] strList = str.split("&");
        if (strList.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列数据数据格式错误");
        }
        String[] ss = strList[0].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列数据数据格式错误");
        }
        if (!ss[0].equals("mst")) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列第一个值不是mst开始");
        }
        Integer numValue = monsters.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列击杀的怪物名未找到" + ";moster:" + ss[1]);
        }
        value.append("\"").append(numValue).append(",");

        ss = strList[1].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列数据数据格式错误");
        }
        if (!ss[0].equals("mstnum")) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列第二个值不是mstnum开始");
        }
        numValue = Integer.parseInt(ss[1]);
        if (numValue <= 0) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列怪物击杀数量小于0了");
        }
        value.append(numValue).append("\",");
    }

    private static void readTargetFinish_killGet(XSSFRow row, int rowNum, StringBuilder value) throws Exception {
        String str = getString(row, Target_killget);
        if (str == null) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列数据不应该为空");
        }
        String[] strList = str.split("&");
        if (strList.length != 4) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列数据数据格式错误");
        }
        String[] ss = strList[0].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列数据数据格式错误");
        }
        if (!ss[0].equals("kitem")) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列第一个值不是kitem");
        }
        Integer numValue = objects.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列击杀怪收集的物品名未找到" + ";object:" + ss[1]);
        }
        value.append("\"").append(numValue).append(",");

        ss = strList[1].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列数据数据格式错误");
        }
        if (!ss[0].equals("knum")) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列第二个值不是knum");
        }
        numValue = Integer.parseInt(ss[1]);
        if (numValue <= 0) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列杀怪收集的物品数量小于0了");
        }
        value.append(numValue).append(",");

        ss = strList[2].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列数据数据格式错误");
        }
        if (!ss[0].equals("kmst")) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列第三个值不是kmst");
        }
        numValue = monsters.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_kill + "列击杀的怪物名未找到" + ";moster:" + ss[1]);
        }
        value.append(numValue).append(",");

        ss = strList[3].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列数据数据格式错误");
        }
        if (!ss[0].equals("krate")) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列第四个值不是krate");
        }
        numValue = Integer.parseInt(ss[1]);
        if (numValue <= 0) {
            throw new Exception("task表" + rowNum + "行" + Target_killget + "列掉落概率小于0了");
        }
        value.append(numValue).append("\",");
    }

    private static void readTargetFinish_collectv(XSSFRow row, int rowNum, StringBuilder value) throws Exception {
        String str = getString(row, Target_collectv);
        if (str == null) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列数据不应该为空");
        }
        String[] strList = str.split("&");
        if (strList.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列数据数据格式错误");
        }
        String[] ss = strList[0].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列数据数据格式错误");
        }
        if (!ss[0].equals("cnum")) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列第一个值不是cnum");
        }

        String collectNum = ss[1];

        ss = strList[1].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列数据数据格式错误");
        }
        if (!ss[0].equals("cobj")) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列第三个值不是cobj");
        }
        Integer numValue = objects.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_collectv + "列采集物名未找到" + ";object:" + ss[1]);
        }

        //目标在前，数量灾后
        value.append("\"").append(numValue).append(",");
        value.append(collectNum).append("\",");
    }

    private static void readTargetFinish_finishcopy(XSSFRow row, int rowNum, StringBuilder value) throws Exception {
        String str = getString(row, Target_finishcopy);
        if (str == null) {
            throw new Exception("task表" + rowNum + "行" + Target_finishcopy + "列数据不应该为空");
        }
        String[] strList = str.split("&");
        if (strList.length != 1) {
            throw new Exception("task表" + rowNum + "行" + Target_finishcopy + "列数据数据格式错误");
        }
        String[] ss = strList[0].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_finishcopy + "列数据数据格式错误");
        }
        if (!ss[0].equals("getcopy")) {
            throw new Exception("task表" + rowNum + "行" + Target_finishcopy + "列第一个值不是getcopy");
        }
        Integer numValue = maps.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_finishcopy + "列副本名未找到" + ";map:" + ss[1]);
        }

        value.append("\"").append(numValue).append("\",");
    }

    private static void readTargetFinish_minicopy(XSSFRow row, int rowNum, StringBuilder value) throws Exception {
        String str = getString(row, Target_minicopy);
        if (str == null) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列数据不应该为空");
        }
        String[] strList = str.split("&");
        if (strList.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列数据数据格式错误");
        }
        String[] ss = strList[0].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列数据数据格式错误");
        }
        if (!ss[0].equals("getminicopy")) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列第一个值不是getminicopy");
        }
        Integer numValue = maps.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列位面名未找到" + ";map:" + ss[1]);
        }
        value.append("\"").append(numValue).append(",");

        ss = strList[1].split("=");
        if (ss.length != 2) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列数据数据格式错误");
        }
        if (!ss[0].equals("mininpc")) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列第二个值不是mininpc");
        }
        numValue = npcs.get(ss[1]);
        if (numValue == null) {
            throw new Exception("task表" + rowNum + "行" + Target_minicopy + "列npc名未找到" + ";npc:" + ss[1]);
        }
        value.append(numValue).append("\",");
    }

    private static String getString(XSSFRow row, int colNum) {
        XSSFCell cell = row.getCell(colNum);
        String str;
        if (cell == null) {
            str = null;
        } else if (cell.getRawValue() == null) {
            str = null;
        } else {
            str = cell.toString().replaceAll(" ", "");
        }
        return str;
    }

    private static void normalIntRead(XSSFRow row, int rowNum, int colNum, StringBuilder value) {
        XSSFCell cell = row.getCell(colNum);
        String str;
        if (cell == null) {
            str = "0";
        } else {
            str = cell.getRawValue();
            if (str == null || str.isEmpty()) {
                str = "0";
            }
        }
        value.append(str).append(",");
    }

    private static void normalListRead(XSSFRow row, int rowNum, int colNum, StringBuilder value, String type) {
        XSSFCell cell = row.getCell(colNum);
        String str;
        if (cell == null) {
            str = "\"\"";
        } else if (cell.getRawValue() == null) {
            str = "\"\"";
        } else {
            str = "\"" + cell.toString() + "\"";
        }
        str = ConfigExcelReader.getReallyStr(type, str);
        value.append(str).append(",");
    }

    private static ExcelLoadData initTaskExcelLoadData() {
        ExcelLoadData datas = new ExcelLoadData();
        datas.setFilename("task");
        datas.addImports("com.data.struct.ReadIntegerArray");
        datas.addImports("com.data.struct.ReadIntegerArrayEs");
        datas.getBeanParms().append("int id,int mapDataId,int task_type,int target_type,int target_subtype,int target_subpara1,int target_subpara2,int task_commitlv,int dailytask_group,"
                + "String next_idStr,int front_id,String targetStr,int task_committype,int task_commitnpc,int task_reward_exp,int task_reward_goldtype,int task_reward_goldnum,"
                + "int needSave,int addbuff,int addAngerValue,int canFly,String task_reward_itemStr");
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("id", "任务id", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("mapDataId", "完成任务目标的地图id", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_type", "任务类型", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("target_type", "完成任务类型,0对话，1杀怪，2杀怪收集，3采集，4完成副本，5完成位面，6完成特定功能", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("target_subtype", "1选择阵营;2强化装备;3装备升品;4装备冲星;5宝石镶嵌;6宠物吞噬;7坐骑冲星;8翅膀升星", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("target_subpara1", "强化装备次数;装备升品次数;装备冲星次数;宝石镶嵌次数;宠物吞噬次数;坐骑冲星次数;翅膀升星次数", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("target_subpara2", "使用物品id", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_commitlv", "任务可提交的等级", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("dailytask_group", "日常任务的组id", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("next_id", "下列任务", "[int]", "ReadIntegerArray"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("front_id", "上一个任务", "int", "int"));
        StringBuilder builder = new StringBuilder("完成任务所需数据:1杀怪时list.get(0)为怪物id,list.get(1)为数量；2收集时list.get(0)为task_item表id,list.get(1)为数量,list.get(2)为怪物id,list.get(3)为掉落概率；");
        builder.append("\n").append("     * 3采集时list.get(0)为task_item表id,list.get(1)为数量；4完成副本时list.get(0)为地图配置表id；5完成位面时list.get(0)为地图配置表id,list.get(1)为npcid");
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("target", builder.toString(), "[int]", "ReadIntegerArray"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_committype", "任务提交的类型0在NPC处提交;1直接在任务面板上提交", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_commitnpc", "提交任务npc", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_reward_exp", "奖励的经验", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_reward_goldtype", "奖励货币类型", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_reward_goldnum", "奖励货币数量", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("needSave", "是否保存，1保存0不保存", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("addbuff", "接取任务增加的buffId", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("addAngerValue", "完成任务获得的怒气值", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("canFly", "是否可以飞行", "int", "int"));
        datas.getFiledsServerUseList().add(new ExcelLoadFiled("task_reward_item", "任务奖励的道具:[{id,数量，绑定},{id2,数量，绑定}]0 不绑定1 绑定", "{int}", "ReadIntegerArrayEs"));
        return datas;
    }

}
