/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

import java.util.ArrayList;

/**
 *模型组与关键字（key）的映射
 * @author Administrator
 */
public class ModelGroupKeyMap {
    public int keyId;//关键字编号
    public String keyName;//关键字组名称
    public String keyDisplayName;//关键字组显示名称
    public ArrayList<KeyModelMap> keyModelList;//单个key所包含的模型
}
