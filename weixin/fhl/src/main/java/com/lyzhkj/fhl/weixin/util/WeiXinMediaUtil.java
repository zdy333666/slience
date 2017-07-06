/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
public class WeiXinMediaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinMediaUtil.class);

    public static String upload(String accessToken, String type, byte[] data) {
        String qrMediaId = null;
        File file = null;
        try {

            file = new File(new StringBuilder().append(File.separator).append(UUID.randomUUID().toString()).append(".jpg").toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();

            qrMediaId = upload(file.getAbsolutePath(), accessToken, type);
        } catch (Exception e) {
            LOGGER.error("", e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }

        return qrMediaId;
    }

    /**
     * 获得素材的媒体文件标识
     *
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static String upload(String filePath, String accessToken, String type) {
        String qrMediaId = null;
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new IOException("指定文件不存在！filePath：" + filePath);
            }

            String url = WeiXinConfig.UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

            URL urlObj = new URL(url);
            //连接微信服务器
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");

            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] head = sb.toString().getBytes("utf-8");

            OutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(head);

            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");

            //文件流
            out.write(foot);
            out.flush();
            out.close();

            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            String result = null;
            try {
                //读取结果
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (result == null) {
                    result = buffer.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            JSONObject jsonObj = JSONObject.fromObject(result);
            String typeName = "media_id";
            if (!"image".equals(type)) {
                typeName = type + "_media_id";
            }
            qrMediaId = jsonObj.getString(typeName);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return qrMediaId;
    }

    public static byte[] download(String accessToken, String mediaId) {

        String url = WeiXinConfig.GET_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);

        byte[] data = new RestTemplate().getForObject(url, byte[].class);

        return data;
    }

}
