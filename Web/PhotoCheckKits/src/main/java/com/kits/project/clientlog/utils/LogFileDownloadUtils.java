package com.kits.project.clientlog.utils;

import com.kits.common.utils.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gaozhaoguang
 * @desc LogFileDownloadUtils
 * @date Created on 2021/6/19 16:52
 **/
public class LogFileDownloadUtils {

    /**
     * 文件下载
     * @param absolutePath
     * @param response
     */
    public static void download(String absolutePath, HttpServletResponse response) throws Exception{

        if (!com.kits.common.utils.file.FileUtils.checkAllowDownload(absolutePath))
        {
            throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", absolutePath));
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        com.kits.common.utils.file.FileUtils.setAttachmentResponseHeader(response, absolutePath);
        com.kits.common.utils.file.FileUtils.writeBytes(absolutePath, response.getOutputStream());
    }

}
