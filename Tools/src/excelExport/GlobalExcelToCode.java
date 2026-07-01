/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.WriteFile;

/**
 * global表单独导出
 *
 * @author hewei
 */
public class GlobalExcelToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");
    public final static Logger logger = Logger.getLogger("GlobalExcelToCode");

    /**
     * 获得excel数据
     *
     * @param excelPath
     * @param javaCodeBasePathes
     */
    public static void globalToCode(String excelPath, List<String> javaCodeBasePathes) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "global.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "global.xlsx")) {
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    return;
                }

                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表

                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 5) {
                    error.error("global工作表行数格式错误，不得低于5行：");
                    return;
                }
                int index=0;
                List<ExcelLoadFiled> list = new ArrayList<>();
                List<String> imports = new ArrayList<>();
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    XSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    if (row.getCell(4) == null) {
                        continue;
                    }
                    ExcelLoadFiled filedValue = new ExcelLoadFiled();
                    //取出第2列
                    colNum = 2;
                    String str = row.getCell(1).getStringCellValue();
                    filedValue.setFiledName(str);
                    colNum = 5;
                    //取出第5列
                    str = row.getCell(4).getStringCellValue().trim();
                    if (str.isEmpty()) {
                        continue;
                    }
                    filedValue.setFiledType(str);
                    colNum = 3;
                    //取出第3列
                    if (row.getCell(2) == null) {
                        continue;
                    }

                    switch (filedValue.getFiledType()) {
                        case "int":
                            if (row.getCell(2) == null) {
                                str = "0";
                            } else if (row.getCell(2).getRawValue() == null) {
                                str = "0";
                            } else if (row.getCell(2).toString().trim().isEmpty()) {
                                str = "0";
                            } else {

                                str = row.getCell(2).getRawValue();
                                if (str.contains(".")) {
                                    index = str.indexOf(".");
                                    String newStr = str.substring(0,index);
                                    str = newStr;
                                }
                            }
                            break;
                        case "long":
                            if (row.getCell(2) == null) {
                                str = "0";
                            } else if (row.getCell(2).getRawValue() == null) {
                                str = "0";
                            } else if (row.getCell(2).toString().trim().isEmpty()) {
                                str = "0";
                            }else
                                str = new BigDecimal( row.getCell(2).getRawValue()).toPlainString() +"L";
                            break;
                        case "float":
                            if (row.getCell(2) == null) {
                                str = "0";
                            } else if (row.getCell(2).toString() == null) {
                                str = "0";
                            } else if (row.getCell(2).toString().isEmpty()) {
                                str = "0";
                            } else {
                                str = row.getCell(2).toString() + "f";
                            }
                            break;
                        case "[int]":
                            if (!imports.contains("com.data.struct.ReadIntegerArray")) {
                                imports.add("com.data.struct.ReadIntegerArray");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[long]":
                            if (!imports.contains("com.data.struct.ReadLongArray")) {
                                imports.add("com.data.struct.ReadLongArray");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[float]":
                            if (!imports.contains("com.data.struct.ReadFloatArray")) {
                                imports.add("com.data.struct.ReadFloatArray");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[string]":
                            if (!imports.contains("com.data.struct.ReadStringArray")) {
                                imports.add("com.data.struct.ReadStringArray");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[char]":
                            if (!imports.contains("com.data.struct.ReadStringArray")) {
                                imports.add("com.data.struct.ReadStringArray");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[[int]]":
                        case "{int}":
                            if (!imports.contains("com.data.struct.ReadIntegerArrayEs")) {
                                imports.add("com.data.struct.ReadIntegerArrayEs");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[[long]]":
                        case "{long}":
                            if (!imports.contains("com.data.struct.ReadLongArrayEs")) {
                                imports.add("com.data.struct.ReadLongArrayEs");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[[float]]":
                        case "{float}":
                            if (!imports.contains("com.data.struct.ReadFloatArrayEs")) {
                                imports.add("com.data.struct.ReadFloatArrayEs");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[[string]]":
                        case "{string}":
                            if (!imports.contains("com.data.struct.ReadStringArrayEs")) {
                                imports.add("com.data.struct.ReadStringArrayEs");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        case "[[char]]":
                        case "{char}":
                            if (!imports.contains("com.data.struct.ReadStringArrayEs")) {
                                imports.add("com.data.struct.ReadStringArrayEs");
                            }
                            str = "\"" + ConfigExcelReader.getReallyStr(filedValue.getFiledType(), row.getCell(2).toString()) + "\"";
                            break;
                        default:
                            str = "\"" + row.getCell(2).toString() + "\"";
                            break;
                    }
                    filedValue.setJavaClassName(str);
                    colNum = 4;
                    //第4列
                    if (row.getCell(3) == null) {
                        filedValue.setFiledDesc("");
                    } else {
                        filedValue.setFiledDesc(row.getCell(3).toString());
                    }
                    list.add(filedValue);
                }
                try {
                    HashMap<String, Object> root = new HashMap<>();
                    root.put("list", list);
                    root.put("package", ConfigExcelReader.javaPackageName);
                    root.put("imports", imports);
                    Template beanTemp = WriteFile.getCfg().getTemplate("Global.ftl", "utf-8"); // Global模板
                    //Global文件
                    try (Writer out = new StringWriter()) {
                        beanTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "Global.java", content);
                        }
                        logger.info("Global.java导出成功。");
                    }
                    //输出load文件
                    Template loadTemp = WriteFile.getCfg().getTemplate("Cfg_Global_Load.ftl", "utf-8"); // Cfg_Global_Load模板
                    try (Writer out = new StringWriter()) {
                        loadTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaScriptPath + File.separator + "Cfg_Global_Load.java", content);
                        }
                        logger.info("Cfg_Global_Load.java导出成功。");
                    }
                    //Global文件
                    Template contaTemp = WriteFile.getCfg().getTemplate("Cfg_Global_Container.ftl", "utf-8"); //Cfg_Global_Container模板
                    try (Writer out = new StringWriter()) {
                        contaTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container" + File.separator + "Cfg_Global_Container.java", content);
                        }
                        logger.info("Cfg_Global_Container.java导出成功。");
                    }
                } catch (IOException | TemplateException e) {
                    error.error(e, e);
                }
            }
        } catch (Exception e) {
            error.error("global表错误行：" + rowNum + ";错误列：" + colNum, e);
        }

    }

}
