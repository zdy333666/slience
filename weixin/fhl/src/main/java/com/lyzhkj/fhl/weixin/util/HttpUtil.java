package com.lyzhkj.fhl.weixin.util;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

/**
 * Http工具类
 * @author wt
 *
 */
public class HttpUtil {
    
    private static Logger Logger=LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * 取得HTTP结果
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url) {
		JSONObject jsonObject = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();		
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
				
				if(jsonObject!=null && jsonObject.containsKey("errcode")){
					if(!jsonObject.getString("errcode").equals("0")){
						StringBuilder errStr = new StringBuilder();
						errStr.append("\n\t errcode:" + jsonObject.getString("errcode"));
						errStr.append("\n\t errmsg:"+ jsonObject.getString("errmsg"));
						errStr.append("\n\t url:"+ url);
						throw new Exception(errStr.toString());
					}
				}
			}
		} catch (Exception e) {
			Logger.error("",e);
		}

		return jsonObject;
	}
	
	/**
	 * 发生HTTP请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		return doPostStr(url,outStr,true);
	}
	
	/**
	 * 发生HTTP请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static String doPostStr2(String url,String outStr){
		return doPostStr2(url,outStr,true);
	}
	
	/**
	 * 发生HTTP请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr,boolean printerrlog){
		JSONObject jsonObject = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = client.execute(httpost);
			HttpEntity entity = response.getEntity();				
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
				
				if(jsonObject!=null && jsonObject.containsKey("errcode")){
					if(!jsonObject.getString("errcode").equals("0")){
						StringBuilder errStr = new StringBuilder();
						errStr.append("\n\t errcode:" + jsonObject.getString("errcode"));
						errStr.append("\n\t errmsg:"+ jsonObject.getString("errmsg"));
						errStr.append("\n\t url:"+ url);
						errStr.append("\n\t outStr:"+ outStr);
						jsonObject = null; //TODO 
						throw new Exception(errStr.toString());
					}
				}
			}
		} catch (Exception e) {
			if(printerrlog){ 
				Logger.error("",e);
			}			
		}

		return jsonObject;
	}
	
	/**
	 * 发生HTTP请求 -- 和NET服务器
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static String doPostStr(String url){
		String result = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(url);
			HttpResponse response = client.execute(httpost);
			HttpEntity entity = response.getEntity();				
			if(entity != null){
				result = EntityUtils.toString(entity,"UTF-8");
			}
		} catch (Exception e) {
			Logger.error("",e);
		}

		return result;
	}	
	
	/**
	 * 发生HTTP请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static String doPostStr2(String url,String outStr,boolean printerrlog){
		String result = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = client.execute(httpost);
			HttpEntity entity = response.getEntity();				
			if(entity != null){
				result = EntityUtils.toString(entity,"UTF-8");
			}
		} catch (Exception e) {
			if(printerrlog){ 
				Logger.error("",e);
			}			
		}

		return result;
	}

}
