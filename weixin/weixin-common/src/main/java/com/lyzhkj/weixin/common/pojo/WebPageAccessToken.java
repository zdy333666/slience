/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.weixin.common.pojo;

/**
 * 网页授权access_token 注意：与基础支持中的access_token不同
 *
 * @author breeze
 */
public class WebPageAccessToken {

    //网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
    private String access_token;
    //access_token接口调用凭证超时时间，单位（秒）
    private int expiresIn;
    //用户刷新access_token
    private String refresh_token;
    //用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
    private String openid;
    //用户授权的作用域，使用逗号（,）分隔
    private String scope;
    //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
