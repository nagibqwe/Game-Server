package com.kits.project.clientlog.utils;

import com.kits.framework.config.ProjectConfig;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * @author gaozhaoguang
 * @desc 日志文件相关的操作
 * @date Created on 2021/6/19 16:58
 **/
public class LogFileUtils {

    /**
     * 获取日志单个文件路径
     * @param uuid
     * @param game
     * @param logPath
     * @return
     */
    public static String getLogFilePath(String uuid,String game,String logPath){
        return ProjectConfig.getClientLogPath()+File.separator+game + File.separator + uuid + File.separator+logPath;
    }

    /**
     * 获取日志目录文件路径
     * @param uuid
     * @param game
     * @return
     */
    public static String getLogDirPath(String uuid,String game){
        return ProjectConfig.getClientLogPath()+File.separator+game + File.separator + uuid + File.separator;
    }

    /**
     * 获取日志压缩后的文件路径
     * @param id
     * @return
     */
    public static String getZipFilePath(long id){
        return ProjectConfig.getClientLogPath()+ File.separator+"zips" + File.separator + id +".zip";
    }

    /**
     * 统一分隔符
     * @param path
     * @return
     */
    public static String uniSeparator(String path){
        return path.replace("\\","/");
    }

    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件的父目录
     * @param file
     */
    public static void mkParentDirs(File file) throws IOException{
        if(file != null && file.getParentFile() != null){
            if (!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
        }
    }
}
