/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.util;

import com.swetake.util.Qrcode;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

/**
 * 第三方Jar包生成二维码 不带场景
 *
 * @author breeze
 */
public class QRCreateUtil {

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content
     * @param imgPath
     */
    public static byte[] createQR(String content) {

        byte[] result = null;

        try {

            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            qrcodeHandler.setQrcodeVersion(7);

            byte[] contentBytes = content.getBytes("gb2312");

            BufferedImage bufImg = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);

            Graphics2D gs = bufImg.createGraphics();

            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, 140, 140);

            // 设定图像颜色> BLACK 
            gs.setColor(Color.BLACK);

            // 设置偏移量 不设置可能导致解析出错 
            int pixoff = 2;
            // 输出内容> 二维码 
            if (contentBytes.length > 0 && contentBytes.length < 120) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = "
                        + contentBytes.length + " not in [ 0,120 ]. ");
            }

            gs.dispose();
            bufImg.flush();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // 生成二维码QRCode图片 
            ImageIO.write(bufImg, "jpg", out);

            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
