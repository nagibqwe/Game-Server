package com.backend.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 将数据导出为excel表
 *
 * @author Administrator
 */
public class ExportExcelUtil {
    public void exportExcel(String tableName, List<Map<String, Object>> dataList, HttpServletResponse response, String fileName) {
        exportExcelTable(tableName, dataList, response, fileName);
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title    表格标题名
     * @param dataList 需要显示的数据列表,列表为map数组
     * @param response HttpServletResponse对象
     * @param fileName 文件的名字
     */
    private void exportExcelTable(String title, List<Map<String, Object>> dataList, HttpServletResponse response, String fileName) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);

        workbook.setSheetName(0, title);
        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        //判断是否已经创建了excel表的标题
        boolean isCreateHeader = false;
        //行标记
        int index = 0;
        XSSFCell cell = null;
        for (Map<String, Object> dataMap : dataList) {
            //列标记
            int cellIndex = 0;
            if (!isCreateHeader) {
                for (String columnName : dataMap.keySet()) {
                    cell = row.createCell(cellIndex);
                    XSSFRichTextString text = new XSSFRichTextString(columnName);
                    String textStr = text.toString();
                    sheet.setColumnWidth(cellIndex, (textStr.length() + 5) * 255);
                    cell.setCellValue(textStr);
                    cellIndex++;
                }
                isCreateHeader = true;
            }
            index++;
            row = sheet.createRow(index);
            cellIndex = 0;
            for (String columnName : dataMap.keySet()) {
                cell = row.createCell(cellIndex);
                String textValue = "";
                Object value = dataMap.get(columnName);
                // 数据类型都当作字符串简单处理
                if (value != null) {
                    textValue = value.toString();
                }
                cell.setCellValue(textValue);
                cellIndex++;
            }
        }

        OutputStream out = null;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");

            // 转码防止乱码
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1")
                    + ".xlsx");
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            out = response.getOutputStream();
            response.flushBuffer();
            workbook.write(out);
            out.flush();
            out.close();
            System.out.println("==========Export To Excel Success!!!==========");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
