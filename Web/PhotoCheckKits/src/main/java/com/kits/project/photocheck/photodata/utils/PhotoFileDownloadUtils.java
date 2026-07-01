package com.kits.project.photocheck.photodata.utils;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

public class PhotoFileDownloadUtils {
    /**
     * 文件下载
     * @param absolutePath
     * @param response
     */
    public static void download(String absolutePath, HttpServletResponse response) throws Exception{

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        PhotoFileUtils.setAttachmentResponseHeader(response, absolutePath);
        PhotoFileUtils.writeBytes(absolutePath, response.getOutputStream());
    }
}
