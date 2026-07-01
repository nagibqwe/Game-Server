/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * excel文件读取
 *
 * @author hewei
 */
public class ConfigExcelReader {

    public final static Logger logger = Logger.getLogger(ConfigExcelReader.class);

    public final static Logger error = Logger.getLogger("ERRORLOG");

    //System.getProperty("user.dir") + File.separator +
    public static String javaCodeBase = "../GameCfg";
    public static String javaPath = "/src";
    public static String javaScriptPath = "/script/config";
    //scriptConfigManager.java所在相对路径
    public static String scriptConfigManagerPath = "/src/com/data/script";
    //languageCsvPath所在相对路径
    public static String languageCsvPath = "../../Client/Assets/StreamingAssets/Database/_LocaleText.csv";
    //System.getProperty("user.dir") + File.separator +
    public static String excelFilePath = "../../Gamedata/Config";
   // public static String excelServerFilePath = "../../Gamedata/Config/server";
    public static String javaPackageName = "com.data";

    public static String excelFilePathOtherBase = "../../Gamedata/ConfigEx/";
    public static String excelFilePathOther = "cht_eng_enu_jpn_kor_rus_tha_vietnam";

    public static String excelFileBase = "M:\\project\\tzj\\策划专用\\配置表\\Gamedata\\";

    public static String excelServerFilePath = "";

    public static int typeRow = 3;

    /**
     * 获得excel数据
     *
     * @param excelFilePath 路径名
     * @param fileName 文件名
     * @param languageType
     * @return
     */
    public static ExcelLoadData load(String excelFilePath, String fileName, String languageType) {
        if (fileName.equals("color.xls") || fileName.equals("globalsetup.xls") || fileName.equals("strings.xlsx") || fileName.equals("task_new.xlsx")
                || fileName.endsWith("task_group.xlsx") || fileName.equals("global.xlsx") || fileName.equals("drop_gold.xlsm") || fileName.equals("drop_item.xlsm")
                || fileName.equals("drop_item.xlsx")
                || fileName.equals("drop_package.xlsm")
                || fileName.equals("drop_package.xlsx")
                ||fileName.equals("MessageString.xlsx")) {
            logger.info(fileName + "不用导出");
            return null;
        }
        if (fileName.endsWith(".xls")) {
            logger.info(fileName + "不支持导出.xls文件，请转化为.xlsx或者.xlsm");
            return null;
        }

        ExcelLoadData datas = new ExcelLoadData();
        int index = fileName.indexOf('.');
        String strName = fileName.substring(0, index);
        //datas.setFilename(strName);
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelFilePath + File.separator + fileName)) {

                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    error.error(fileName + "工作表不存在");
                    return datas;
                }

                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表

               // String sheetName = sheet.getSheetName();
               // if (sheetName == null || sheetName.contains("Sheet") || sheetName.contains("sheet")) {
               //     logger.info(fileName + "工作表名无效,不能以Sheet开始命名");
               //     return datas;
               // }

               // sheetName = sheetName.replace(" ","");
                datas.setFilename(strName);
                datas.setBeanName(strName);
                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 5) {
                    logger.info(fileName + "工作表行数格式错误，不得低于5行");
                    return datas;
                }

                //找key
                XSSFRow key_row = sheet.getRow(0);
                int key_index = 0;
                int cellNumber = key_row.getLastCellNum();
                for (int i = 0;i<cellNumber;i++) {
                    if (key_row.getCell(i) == null) {
                        continue;
                    }
                    if (key_row.getCell(i).toString().contains("1")) {
                        key_index = i;
                    }
                }
                //取第二行的值,字段名,和字段描述
                XSSFRow row = sheet.getRow(1);
                XSSFRow row_1 = sheet.getRow(4);
                rowNum = 2;
                cellNumber = row.getLastCellNum();
                String str = "";
                for (int i = 0; i < cellNumber; i++) {
                    colNum = i + 1;
                    if (row.getCell(i) == null) {
                        continue;
                    }
                    if (row_1.getCell(i)!=null && row_1.getCell(i).getStringCellValue().contains("hide") ){
                        continue;
                    }
                    ExcelLoadFiled filed = new ExcelLoadFiled();
                    str = row.getCell(i).getStringCellValue();
                    if (str.contains(" ")) {
                        error.error(fileName + "字段" + str + "里面含有空格字符");
                        str =str.replace(" ","");
                    }
                    filed.setFiledName(str);
                    if (row_1.getCell(i) == null) {
                        filed.setFiledDesc("");
                    } else{
                        filed.setFiledDesc(row_1.getCell(i).getStringCellValue());
                    }

                    //if (row.getCell(i).getCellComment() != null && row.getCell(i).getCellComment().getString() != null && row.getCell(i).getCellComment().getString().toString() != null) {
                    //    filed.setFiledDesc(row.getCell(i).getCellComment().getString().toString().replace("\n", ""));
                    //} else {
                    //    filed.setFiledDesc("");
                    //}

                    datas.getFileds().put(i, filed);
                    datas.getFiledsServerUseList().add(filed);
                }

                //取第四行的值，字段类型:int、long、float、char（char原样输出）、string（转为语言包id）、[int]、{int}、[float]、{float}
                row = sheet.getRow(typeRow);
                rowNum = 1;
                for (int i = 0; i < cellNumber; i++) {
                    colNum = i + 1;
                    ExcelLoadFiled filed = datas.getFileds().get(i);
                    if (filed == null) {
                        continue;
                    }
                    if (row.getCell(i) == null) {
                        if (i == 0) {
                            error.error(fileName + "第3行第一列没有设置类型");
                            return datas;
                        }
                        filed.setFiledType("");
                        continue;
                    }
                    if (row_1.getCell(i)!=null && row_1.getCell(i).getStringCellValue().contains("hide") ){
                        filed.setFiledType("");
                        continue;
                    }
                    String strarray = row.getCell(i).getStringCellValue().trim();
                    if (strarray.isEmpty()) {
                        if (i == 0) {
                            error.error(fileName + "第3行第一列没有设置类型");
                            return datas;
                        }
                        filed.setFiledType("");
                        continue;
                    }
                    if (!datas.getBeanParms().toString().isEmpty()) {
                        datas.getBeanParms().append(",");
                    }
                    String[] ss = strarray.split(",|，| ");
                    str = ss[0].trim();
                    switch (str) {
                        case "int":
                            filed.setJavaClassName("int");
                            datas.getBeanParms().append("int ").append(filed.getFiledName());
                            break;
                        case "long":
                            filed.setJavaClassName("long");
                            datas.getBeanParms().append("long ").append(filed.getFiledName());
                            break;
                        case "float":
                            filed.setJavaClassName("float");
                            datas.getBeanParms().append("float ").append(filed.getFiledName());
                            break;
                        case "char":
                        case "Char":
                            filed.setJavaClassName("String");
                            datas.getBeanParms().append("String ").append(filed.getFiledName());
                            break;
                        case "define":
                            filed.setJavaClassName("String");
                            datas.getBeanParms().append("String ").append(filed.getFiledName());
                            break;
                        case "[char]":
                            filed.setJavaClassName("ReadStringArray");
                            datas.addImports("com.data.struct.ReadStringArray");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "{char}":
                            filed.setJavaClassName("ReadStringArrayEs");
                            datas.addImports("com.data.struct.ReadStringArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "string":
                            filed.setFiledDesc(filed.getFiledDesc() + "语言包id");
                            filed.setJavaClassName("int");
                            datas.getBeanParms().append("int ").append(filed.getFiledName());
                            datas.getLanguageList().add(filed);
                            break;
                        case "[int]":
                            filed.setJavaClassName("ReadIntegerArray");
                            datas.addImports("com.data.struct.ReadIntegerArray");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "[long]":
                            filed.setJavaClassName("ReadLongArray");
                            datas.addImports("com.data.struct.ReadLongArray");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "[float]":
                            filed.setJavaClassName("ReadFloatArray");
                            datas.addImports("com.data.struct.ReadFloatArray");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "[string]":
                            filed.setJavaClassName("ReadStringArray");
                            datas.addImports("com.data.struct.ReadStringArray");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "{int}":
                            filed.setJavaClassName("ReadIntegerArrayEs");
                            datas.addImports("com.data.struct.ReadIntegerArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "{long}":
                            filed.setJavaClassName("ReadLongArrayEs");
                            datas.addImports("com.data.struct.ReadLongArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "{float}":
                            filed.setJavaClassName("ReadFloatArrayEs");
                            datas.addImports("com.data.struct.ReadFloatArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "[[int]]":
                            filed.setJavaClassName("ReadIntegerArrayEs");
                            datas.addImports("com.data.struct.ReadIntegerArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                        case "[[float]]":
                            filed.setJavaClassName("ReadFloatArrayEs");
                            datas.addImports("com.data.struct.ReadFloatArrayEs");
                            datas.getBeanParms().append("String ").append(filed.getFiledName()).append("Str");
                            break;
                    }
                    filed.setFiledType(str);
                }

                //已经导出的key
                List<String> keys = new ArrayList<>();
                //取出所有数据值
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }
                    if (row.getCell(key_index) == null
                            ||row.getCell(key_index).getRawValue() == null
                            || row.getCell(key_index).toString().trim().isEmpty()) {
                        continue;
                    }
                    Field filedValue = new Field();
                    StringBuilder v = new StringBuilder();
                    for (int j = 0; j < cellNumber; j++) {
                        colNum = j + 1;
                        ExcelLoadFiled filed = datas.getFileds().get(j);
                        if (filed == null) {
                            continue;
                        }
                        TaskExcelToCode.stringToKey(datas.getFilename(), filed, row, j);
                        if (filed.getFiledType() == null) {
                            filed.setFiledType("");
                            continue;
                        }
                        if (filed.getFiledType().isEmpty()) {
                            continue;
                        }
                        if (!v.toString().isEmpty()) {
                            v.append(",");
                        }
                        switch (filed.getFiledType()) {
                            case "int":
                                if (row.getCell(j) == null) {
                                    str = "0";
                                } else if (row.getCell(j).getRawValue() == null) {
                                    str = "0";
                                } else if (row.getCell(j).toString().trim().isEmpty()) {
                                    str = "0";
                                } else {
                                    str = row.getCell(j).getRawValue();
                                    if (str.contains(".")) {
                                        index = str.indexOf(".");
                                        String newStr = str.substring(0,index);
                                        str = newStr;
                                    }
                                }
                                break;

                            case "long":
                                if (row.getCell(j) == null) {
                                    str = "0";
                                } else if (row.getCell(j).getRawValue() == null) {
                                    str = "0";
                                } else if (row.getCell(j).toString().trim().isEmpty()) {
                                    str = "0";
                                }else {
                                    str = new BigDecimal( row.getCell(j).getRawValue()).toPlainString();
                                    if (str.contains(".")) {
                                        index = str.indexOf(".");
                                        String newStr = str.substring(0,index);
                                        str = newStr + "L";
                                    }else
                                        str =str+"L";
                                }
                                break;
                            case "string":
                            case "String":
                                if (row.getCell(j) == null) {
                                    str = "0";
                                } else if (row.getCell(j).toString() == null) {
                                    str = "0";
                                } else {
                                    str = ExcelToCode.decodeEscape(row.getCell(j).toString());
                                    str = ExcelToCode.getLanguageId(str, strName, languageType) + "";
                                }
                                break;
                            case "float":
                                if (row.getCell(j) == null) {
                                    str = "0";
                                } else if (row.getCell(j).toString() == null) {
                                    str = "0";
                                } else if (row.getCell(j).toString().trim().isEmpty()) {
                                    str = "0";
                                } else {
                                    str = row.getCell(j).toString() + "f";
                                }
                                break;
                            case "[int]":
                            case "[float]":
                            case "[string]":
                            case "[char]":
                            case "[long]":
                                if (row.getCell(j) == null) {
                                    str = "\"\"";
                                } else if (row.getCell(j).getRawValue() == null) {
                                    str = "\"\"";
                                } else {
                                    str = "\"" + row.getCell(j).toString() + "\"";
                                }
                                if (str.startsWith("###")) {
                                    str = str.replaceFirst("###", "");
                                }
                                if (str.contains(";"))
                                    error.error(fileName +  "  "+ filed.getFiledName() +  "  "+ filed.getFiledType() +"第" +rowNum  +"行里面的分隔符填写不对 ; " );
                                else if(str.contains("[") || str.contains("]"))
                                    error.error(fileName +  "  "+ filed.getFiledName() +  "  "+ filed.getFiledType() +"第" +rowNum  +"行里面 不支持  [] " );
                                str = getReallyStr(filed.getFiledType(), str);
                                break;
                            case "{int}":
                            case "{float}":
                            case "{string}":
                            case "{char}":
                            case "{long}":
                                if (row.getCell(j) == null) {
                                    str = "\"\"";
                                } else if (row.getCell(j).getRawValue() == null) {
                                    str = "\"\"";
                                } else {
                                    str = "\"" + row.getCell(j).toString() + "\"";
                                }
                                if (str.startsWith("###")) {
                                    str = str.replaceFirst("###", "");
                                }
                                if (str.contains(","))
                                    error.error(fileName +  "  "+ filed.getFiledName() +  "  "+ filed.getFiledType() +"第" +rowNum  +"行里面的分隔符填写不对 , " );
                                else if(str.contains("|"))
                                    error.error(fileName +  "  "+ filed.getFiledName() +  "  "+ filed.getFiledType() +"第" +rowNum  +"不支持  | " );
                                str = getReallyStr(filed.getFiledType(), str);
                                break;
                            default:
                                if (row.getCell(j) == null) {
                                    str = "\"\"";
                                } else if (row.getCell(j).getRawValue() == null) {
                                    str = "\"\"";
                                } else {
                                    row.getCell(j).setCellType(row.getCell(j).CELL_TYPE_STRING);
                                    str = row.getCell(j).toString().replaceAll("\\\\", "\\\\\\\\");
                                    str = str.replaceAll("\"","\\\\\"");
                                    str = "\"" +str + "\"";
                                }
                                if (str.startsWith("###")) {
                                    str = str.replaceFirst("###", "");
                                }
                                str = getReallyStr(filed.getFiledType(), str);
                                break;
                        }
                        v.append(str);
                        if (j != key_index) {
                            continue;
                        }
                        filedValue.setKey(str);
                        if (keys.contains(str)) {
                            error.error(fileName + "第" + (i + 1) + "行;key值重复:" + str);
                        } else {
                            keys.add(str);
                        }
                    }
                    filedValue.setValue(v.toString());
                    datas.getServerValues().add(filedValue);
                }
            }
        } catch (Exception e) {
            error.error(fileName + "导出工作表异常,第" + rowNum + "行，第" + colNum + "列", e);
            return datas;
        }
        datas.setIsSuccess(true);
        return datas;
    }

    public static String getReallyStr(String type, String str) {
        if ("\"0\"".equals(str) || "\"0.0\"".equals(str)) {
            if (type.equals ("[int]") ||type.equals("{int}") || type.equals("[long]") || type.equals("{long}") )
                return str;
            else
                return "\"\"";
        }
        switch (type) {
            case "[int]":
            case "[float]":
            case "[string]":
            case "[char]":
            case "[long]":
                str = str.replace(" ", "");
                if (str.contains(";"))
                    str = str.replace(";", ",");//1;2;3;4变为1,2,3,4
                else if(str.contains("["))
                    str = str.replace("[", "").replace("]","").replace("_",",");
                else
                    str = str.replace("_", ",");//1_2_3_4变为1,2,3,4
                break;
            case "{int}":
            case "{float}":
            case "{string}":
            case "{char}":
            case "{long}":
                str = str.replace(" ", "");
                if (str.contains(",")){
                    str = str.replace(";", "}");    //1,2;2,3;3,4变为1,2}2,3}3,4
                }else
                    str = str.replace(";", "}").replace("_", ",");    //1_2;2_3;3_4变为1,2}2,3}3,4
                break;
          //暂时不用
          //  case "[[int]]":
          //  case "[[float]]":
          //      str = str.replace("[[", "").replace("]]", "").replace(",[", "");//[[1,2],[2,3],[3,4]]变为1,2]2,3]3,4
          //      break;
            default:
                break;
        }
        return str;
    }

    /**
     * 加载excel配置表文件名列表
     *
     * @param excelPath
     * @return
     */
    public static List<String> getExcelFileList(String excelPath) {
        List<String> list = new ArrayList<>();
        File dir = new File(System.getProperty("user.dir") + File.separator + excelPath);
        if (dir == null || dir.listFiles() == null) {
            return list;
        }
        for (File f : dir.listFiles()) {
            if (!f.isFile()) {
                continue;
            }
            if (!f.canRead()) {
                continue;
            }
            if (f.getName().startsWith("~$")) {
                continue;
            }
            if (!f.getName().endsWith(".xlsx") && !f.getName().endsWith(".xlsm") && !f.getName().endsWith(".xls")) {
                continue;
            }
            list.add(f.getName());
        }
        return list;
    }



}
