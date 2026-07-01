package com.backend.utils;

import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHandleUtil {
    private static final Logger log = Logger.getLogger(ImageHandleUtil.class);
    static BASE64Decoder decoder = new BASE64Decoder();
    static SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");

    /**
     * 对图片进行处理 成功则进行分组存储，失败则删除
     *
     * @param picData
     * @param playerID
     * @return
     */
    public static Boolean headHandle(String picData, String playerID) {
        String path = System.getProperty("catalina.home") + "/webapps/headIconHandle";
        File iconHandleFolder = new File(path);
        if (judeDirExists(iconHandleFolder)) {
            iconHandleFolder.mkdir();
        }
        String jpgPath = "/" + playerID + ".jpg";
        String pngPath = "/" + playerID + ".png";
        byte[] bytes;
        try {
            bytes = decoder.decodeBuffer(picData);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bi1 = ImageIO.read(bais);
            File w2 = new File(path + jpgPath);//可以是jpg,png,gif格式
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
            //转换成圆形
            if (ImageHandleUtil.imgToIcon(path + jpgPath, path + pngPath)) {
                //转为小图
                if (ImageHandleUtil.cutDown(path + pngPath)) {
                    return true;
                }
            }
        } catch (Exception e) {
            writeLog(playerID);
            log.error(sdfs.format(new Date()) + "--图片上传异常");
            return false;
        }

        return false;
    }

    public static void writeLog(String playerID) {
        String path = System.getProperty("catalina.home") + "/webapps/headLog";
        File f = new File(path);
        if (judeDirExists(f)) {
            f.mkdir();
        }
        path += "/" + sd.format(new Date()) + ".log";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        String writeDate = "时间:" + sdfs.format(new Date()) + "---" + "error:" + playerID + " 上传头像异常";
        try {
            //设置为:True,表示写入的时候追加数据
            fw = new FileWriter(file, true);
            //回车并换行
            fw.write(writeDate + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 图片裁剪128x128
     *
     * @param path 文件路径
     * @return
     */
    public static Boolean cutDown(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            //读取图片
            BufferedInputStream ins = new BufferedInputStream(in);
            //字节流转图片对象
            Image bi = ImageIO.read(ins);
            in.close();
            //获取图像的高度，宽度
            int height = bi.getHeight(null);
            int width = bi.getWidth(null);
            //构建图片流
            BufferedImage tag = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
            //绘制改变尺寸后的图
            tag.getGraphics().drawImage(bi, 0, 0, 128, 128, null);
            //输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(tag);
            ImageIO.write(tag, "PNG", out);
            ins.close();
            out.close();
            return true;
        } catch (Exception e) {
            log.error("--图片缩减异常");
            return false;
        }
    }


    /**
     * @param path1 文件路径
     * @param path2 生成文件路径
     * @return
     */
    public static Boolean imgToIcon(String path1, String path2) {
        try {
            //图片的本地地址
            Image src = ImageIO.read(new File(path1));
            BufferedImage url = (BufferedImage) src;
            //处理图片将其压缩成正方形的小图
            BufferedImage convertImage = scaleByPercentage(url, 128, 128);
            //裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
            convertImage = convertCircular(url);
            //生成的图片位置
            String imagePath = path2;
            ImageIO.write(convertImage, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     *
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage bi1) throws IOException {

//		BufferedImage bi1 = ImageIO.read(new File(url));

        // 这种是黑色底的
//		BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }

    /**
     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
     *
     * @param inputImage
     * @param newWidth   ：压缩后宽度
     * @param newHeight  ：压缩后高度
     * @throws IOException return
     */
    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight) throws Exception {
        // 获取原始图像透明度类型
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        // 开启抗锯齿
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 使用高质量压缩
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHints(renderingHints);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;
    }

    // 判断文件夹是否存在
    public static Boolean judeDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return false;
            } else {
                log.error("the same name file exists, can not create dir");
            }
        } else {
            return true;
        }
        return null;

    }
}
