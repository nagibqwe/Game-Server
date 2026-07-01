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
 * Created by 542 on 2019/6/19.
 */
public class FunctionExcelToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");
    public final static Logger logger = Logger.getLogger("FunctionExcelToCode");
    public static void functionToCode(String excelPath, List<String> javaCodeBasePathes) {
        String[] Functionlist = new String[]{"FunctionStart","FunctionVariable","ItemChangeReason"};
        for (String F:Functionlist)
        {
            loadFunctionExcel(excelPath,javaCodeBasePathes,F);
        }
    }
    private static void loadFunctionExcel(String excelPath, List<String> javaCodeBasePathes,String Name){
        String FileName = Name +".xlsx";
        File f = new File(excelPath + File.separator + FileName);
        if (!f.canRead()) {
            error.error("未找到配置表： " +FileName);
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(excelPath + File.separator + FileName)) {
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
                    error.error("工作表行数格式错误，不得低于5行：");
                    return;
                }
                XSSFRow typerow  = sheet.getRow(ConfigExcelReader.typeRow);
                if ( typerow.getCell(0) == null)
                {
                    error.error(Name + "表类型 未填写");
                    return;
                }
                List<ExcelLoadFiled> list = new ArrayList<>();
                List<String> imports = new ArrayList<>();
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    XSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    if (row.getCell(0) == null || row.getCell(0).getRawValue() == ""
                            || row.getCell(1) == null || row.getCell(1).getRawValue() == "") {
                        continue;
                    }
                    ExcelLoadFiled filedValue = new ExcelLoadFiled();
                    //取出第2列
                    colNum = 2;
                    String str = row.getCell(1).getStringCellValue();
                    String[] definelist =  str.split(";");
                    filedValue.setFiledName(definelist[0]);
                    if (definelist.length<2){
                        error.error("表" +FileName +"行数 " + i + row.getCell(0).getRawValue() +" 没用 ; 符号把字段描述分开" );
                        filedValue.setFiledDesc(definelist[0]);
                    }else{
                        filedValue.setFiledDesc(definelist[1]);
                    }
                    filedValue.setFiledType("int");
                    str = row.getCell(0).getRawValue();
                    filedValue.setJavaClassName(str);
                    list.add(filedValue);
                }
                try {

                    HashMap<String, Object> root = new HashMap<>();
                    root.put("classBeanName", Name);
                    root.put("classContainerName", ExcelToCode.getJavaClassContainerName(Name));
                    root.put("classLoadName", ExcelToCode.getJavaClassLoadName(Name));
                    root.put("package", ConfigExcelReader.javaPackageName);
                    root.put("list", list);
                    root.put("imports", imports);
                    Template beanTemp = WriteFile.getCfg().getTemplate("Function.ftl", "utf-8"); // Global模板
                    //文件
                    try (Writer out = new StringWriter()) {
                        beanTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            logger.info("文件路径：" + s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + Name+".java");
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + Name+".java", content);
                        }
                        logger.info(Name+".java导出成功。");
                    }
                  //  //输出load文件
                  //  Template loadTemp = WriteFile.getCfg().getTemplate("Cfg_Function_Load.ftl", "utf-8"); // Cfg_Global_Load模板
                  //  try (Writer out = new StringWriter()) {
                  //      loadTemp.process(root, out);
                  //      String content = out.toString();
                  //      for (String s : javaCodeBasePathes) {
                  //          WriteFile.writeFile(s + ConfigExcelReader.javaScriptPath + File.separator + "Cfg_"+Name+"_Load.java", content);
                  //      }
                  //      logger.info("Cfg_"+Name+"_Load.java导出成功。");
                  //  }
                  //  //文件
                  //  Template contaTemp = WriteFile.getCfg().getTemplate("Cfg_Function_Container.ftl", "utf-8"); //Cfg_Global_Container模板
                  //  try (Writer out = new StringWriter()) {
                  //      contaTemp.process(root, out);
                  //      String content = out.toString();
                  //      for (String s : javaCodeBasePathes) {
                  //          WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container" + File.separator + "Cfg_"+Name+"_Container.java", content);
                  //      }
                  //      logger.info("Cfg_"+Name+"_Container.java导出成功。");
                  //  }
                } catch (IOException | TemplateException e) {
                    error.error(e, e);
                }
            }
        } catch (Exception e) {
            error.error(FileName+"表错误行：" + rowNum + ";错误列：" + colNum, e);
        }
    }
}
