package excelExport;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.WriteFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 542 on 2019/7/1.
 */
public class MessageStringToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");
    public final static Logger logger = Logger.getLogger("MessageStringToCode");

    public static void messageStringToCode(String excelPath, List<String> javaCodeBasePathes) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "MessageString.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "MessageString.xlsx")) {
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
                    if (row.getCell(0) == null) {
                        continue;
                    }
                    //取出第2列
                    colNum = 2;
                    String str = row.getCell(0).getStringCellValue();
                     String[] strArr = str.split(",");
                    if (strArr.length<2)
                        continue;
                    ExcelLoadFiled filedValue;
                   for (int j = 1;j<strArr.length;j++)
                   {
                       if (strArr[j].length() - 1 == j)
                           continue;
                       if (strArr[j].length()<=0)
                           continue;
                       if (strArr[j]=="")
                           continue;
                       filedValue = new ExcelLoadFiled();
                       str = strArr[j];
                       filedValue.setFiledName(str);
                       filedValue.setFiledType("int");
                       if (row.getCell(1) == null) {
                           str = "0";
                       } else if (row.getCell(1).getRawValue() == null) {
                           str = "0";
                       } else if (row.getCell(1).toString().trim().isEmpty()) {
                           str = "0";
                       } else {
                           str = row.getCell(1).getRawValue();
                           if (str.contains(".")) {
                               index = str.indexOf(".");
                               String newStr = str.substring(0,index);
                               str = newStr;
                           }
                       }

                       if (row.getCell(2) == null) {
                           filedValue.setFiledDesc("");
                       } else {
                           filedValue.setFiledDesc(row.getCell(2).toString());
                       }
                       filedValue.setJavaClassName(str);
                       list.add(filedValue);
                   }
                }
                try {
                    HashMap<String, Object> root = new HashMap<>();
                    root.put("list", list);
                    root.put("package", ConfigExcelReader.javaPackageName);
                    root.put("imports", imports);
                    Template beanTemp = WriteFile.getCfg().getTemplate("MessageString.ftl", "utf-8"); // Global模板
                    //Global文件
                    try (Writer out = new StringWriter()) {
                        beanTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "MessageString.java", content);
                        }
                        logger.info("MessageString.java导出成功。");
                    }
                    //输出load文件
                    Template loadTemp = WriteFile.getCfg().getTemplate("Cfg_MessageString_Load.ftl", "utf-8"); // Cfg_Global_Load模板
                    try (Writer out = new StringWriter()) {
                        loadTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaScriptPath + File.separator + "Cfg_MessageString_Load.java", content);
                        }
                        logger.info("Cfg_MessageString_Load.java导出成功。");
                    }
                    //Global文件
                    Template contaTemp = WriteFile.getCfg().getTemplate("Cfg_MessageString_Container.ftl", "utf-8"); //Cfg_Global_Container模板
                    try (Writer out = new StringWriter()) {
                        contaTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container" + File.separator + "Cfg_MessageString_Container.java", content);
                        }
                        logger.info("Cfg_MessageString_Container.java导出成功。");
                    }
                } catch (IOException | TemplateException e) {
                    error.error(e, e);
                }
            }
        } catch (Exception e) {
            error.error("MessageString表错误行：" + rowNum + ";错误列：" + colNum, e);
        }

    }

}
