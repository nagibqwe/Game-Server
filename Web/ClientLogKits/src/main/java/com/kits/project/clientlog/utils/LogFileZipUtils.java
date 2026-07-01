package com.kits.project.clientlog.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author gaozhaoguang
 * @desc LogFileZipUtils
 * @date Created on 2021/6/19 16:52
 **/
public class LogFileZipUtils {

    /**
     * 压缩目录
     * @param srcDir 源目录
     * @param desFile 目标文件
     * @throws Exception
     */
    public static void zipDir(String srcDir,String desFile) throws Exception {

        File file = new File(srcDir);

        ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(desFile)));

        String base = file.getName();

        zip(zipOutput, file, base);
        zipOutput.closeEntry();
        zipOutput.close();
    }

    /**
     * 因为子文件夹中可能还有文件夹，所以进行递归
     *
     */
    private static void zip(ZipOutputStream zipOutput, File file, String base) throws IOException {

        if(file.isDirectory()){
            File[] listFiles = file.listFiles();// 列出所有的文件

            for(File fi : listFiles){
                if(fi.isDirectory()){
                    zip(zipOutput, fi, base + "/" + fi.getName());
                }else{
                    zipFileEx(zipOutput, fi, base);
                }
            }
        }else{
            zipFileEx(zipOutput, file, base);
        }
    }

    /**
     * 压缩的具体操作
     *
     */
    private static void zipFileEx(ZipOutputStream zipOutput, File file, String base) throws IOException, FileNotFoundException {
        ZipEntry zEntry = new ZipEntry(base + File.separator + file.getName());
        zipOutput.putNextEntry(zEntry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[1024];
        int read = 0;
        while((read =bis.read(buffer)) != -1){
            zipOutput.write(buffer, 0, read);
        }
        bis.close();
    }
}
