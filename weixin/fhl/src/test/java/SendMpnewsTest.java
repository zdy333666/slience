/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lyzhkj.fhl.weixin.util.HttpUtil;
import com.lyzhkj.fhl.weixin.util.WeixinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class SendMpnewsTest {

    private static final String UPLOAD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";

    private static final String SENDALL_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

    public static void send() {

        // Map<String, Object> tokenMap = WeiXinBaseHandler.getAccessToken();
//        Cache.access_token = (String) tokenMap.get("access_token");
//        Cache.expires_in = (int) tokenMap.get("expires_in");
        String token = null;

        String filePath = "D:\\test.png";
        String media_id = WeixinUtil.upload(filePath, token, "thumb");

        //GarArticle garArticle=garArticleService.findGarArticleById(id);
//		WeixinArticle weixinArticle=new WeixinArticle();
//		weixinArticle.setTitle("文章标题");//garArticle.getArticleTitle());
//		weixinArticle.setContent("文章简介");//garArticle.getArticleInfo());
//		weixinArticle.setContent_source_url("http://www.baidu.com");
//		
        JSONObject articleNode = new JSONObject();//(JSONObject) JSONObject.toJSON(weixinArticle);
        articleNode.put("thumb_media_id", media_id);
        articleNode.put("author", "xxx");
        articleNode.put("title", "Happy Day");
        articleNode.put("content_source_url", "http://www.qq.com");
        articleNode.put("content", "content");
        articleNode.put("digest", "digest");
        articleNode.put("show_cover_pic", "1");

        JSONArray articlesNode = new JSONArray();
        articlesNode.add(articleNode);

        JSONObject root = new JSONObject();
        root.put("articles", articlesNode);

        System.out.println("upload_root-->" + root);

        String url = UPLOAD_NEWS_URL.replace("ACCESS_TOKEN", token);

        String upload_result = HttpUtil.doPostStr(url, root.toString()).toString();

        System.out.println("upload_result-->" + upload_result);

        String send_url = SENDALL_NEWS_URL.replace("ACCESS_TOKEN", token);
        JSONObject sendNode = new JSONObject();
        JSONObject filterNode = new JSONObject();
        filterNode.put("is_to_all", false);
        filterNode.put("group_id", "0");

        sendNode.put("filter", filterNode);

        JSONObject mpnewsNode = new JSONObject();
        mpnewsNode.put("media_id", JSONObject.fromObject(upload_result).getString("media_id"));

        sendNode.put("mpnews", mpnewsNode);
        sendNode.put("msgtype", "mpnews");
        //sendNode.put("text", new JSONObject().put("content", "hello,weixin"));
        //sendNode.put("msgtype", "text");

        String send_result = HttpUtil.doPostStr(send_url, sendNode.toString()).toString();
        System.out.println("send_result-->" + send_result);

    }

}
