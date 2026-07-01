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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.WriteFile;

/**
 *
 * @author hewei
 */
public class StringsToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");
    public final static Logger logger = Logger.getLogger("StringsToCode");

    /**
     * 获得excel数据
     *
     * @param excelPath
     * @param javaCodeBasePathes
     * @param languageType
     */
    public static void stringsToCode(String excelPath, List<String> javaCodeBasePathes, String languageType) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "strings.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "strings.xlsx")) {
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    return;
                }

                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表

                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 3) {
                    error.error("global工作表行数格式错误，不得低于2行：");
                    return;
                }
                List<ExcelLoadFiled> list = new ArrayList<>();
                for (int i = 3; i <= rows; i++) {
                    rowNum = i + 1;
                    XSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }

                    //取出第3列
                    colNum = 3;
                    if (row.getCell(colNum - 1) == null) {
                        continue;
                    }
                    String str = row.getCell(colNum - 1).getRawValue();
                    if (str == null) {
                        continue;
                    }
                    str = str.trim();
                    if (str.isEmpty()) {
                        continue;
                    }
                    if ("0".equals(str)) {
                        continue;
                    }

                    //取出第一列
                    colNum = 1;
                    str = row.getCell(colNum - 1).getStringCellValue().trim();
                    if (str == null) {
                        continue;
                    }
                    if (str.isEmpty()) {
                        continue;
                    }
                    ExcelLoadFiled filedValue = new ExcelLoadFiled();
                    filedValue.setFiledName(str);

                    //取出第2列，获取语言包id
                    colNum = 2;
                    str = ExcelToCode.decodeEscape(row.getCell(colNum - 1).toString());
                    int languageId = ExcelToCode.getLanguageId(str, "strings.xlsx", languageType);
                    filedValue.setFiledDesc(str);
                    filedValue.setJavaClassName(languageId + "");

                    //取出第4列
                    colNum = 4;

                    if (row.getCell(colNum - 1) == null) {
                        str = "1";
                    } else {
                        str = row.getCell(colNum - 1).getRawValue();
                        if (str == null || str.isEmpty()) {
                            str = "1";
                        }
                    }
                    filedValue.setFiledType(str);

                    list.add(filedValue);
                }
                try {
                    HashMap<String, Object> root = new HashMap<>();
                    root.put("list", list);
                    root.put("package", ConfigExcelReader.javaPackageName);
                    Template beanTemp = WriteFile.getCfg().getTemplate("StringCode.ftl", "utf-8"); // Global模板
                    //Global文件
                    try (Writer out = new StringWriter()) {
                        beanTemp.process(root, out);
                        String fileContent = out.toString();
                        for (String javaPath : javaCodeBasePathes) {
                            WriteFile.writeFile(javaPath + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "StringCode.java", fileContent);
                        }
                        logger.info("StringCode.java导出成功。");
                    }

                } catch (IOException | TemplateException e) {
                    error.error(e, e);
                }
            }
        } catch (Exception e) {
            error.error("strings表错误行：" + rowNum + ";错误列：" + colNum, e);
        }

    }

}
