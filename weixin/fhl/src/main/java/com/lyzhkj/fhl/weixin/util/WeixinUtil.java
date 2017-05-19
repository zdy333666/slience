package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.WeiXinMessage;
import com.lyzhkj.fhl.weixin.util.WeiXinMessageUtil;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import net.sf.json.JSONObject;
import org.apache.http.ParseException;

/**
 * 微信服务器交互工具类
 *
 * @author wt
 *
 */
public class WeixinUtil {
    //令牌

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //获取二维码ticket
    private static final String CREATE_QR_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    //用ticket换取二维码图片
    private static final String GET_QR_IMAGE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
    //上传二维码图片，获得素材标识
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    //消息回复
    private static final String REPLY_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    //微信JSSDK的ticket
    public final static String JSSDK_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

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

            String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

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
            e.printStackTrace();
        }
        return qrMediaId;
    }

    /**
     * 消息回复
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
//    public static void replyMessage(WeiXinMessage message) {
//        replyMessage(message, true);
//    }

//    /**
//     * 消息回复
//     *
//     * @return
//     * @throws ParseException
//     * @throws IOException
//     */
//    public static void replyMessage(WeiXinMessage message, boolean printerrlog) {
//
//        WeiXinMessageUtil.replyTextMessage(WeiXinConfig.REPLY_MESSAGE, WeiXinAccessTokenUtil.getAccessToken().getAccessToken(), message);
//
//    }

}
