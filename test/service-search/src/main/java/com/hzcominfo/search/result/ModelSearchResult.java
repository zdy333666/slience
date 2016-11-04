/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author xzh
 */
public class ModelSearchResult {
public long start;	//	从多少偏移量开始
	public long rows;	//	本次查询返回多少条记录
public long numFound;	//	总共有多少条记录。
public ArrayList < ModelFieldInfo > modelFieldInfoList;	//	模型信息列表,包含字段名和显式名
public ArrayList < HashMap <String,String> > docList;
//	文档内容列表，每个文档的内容都是一个HashMap组成的，Key字段名称在modelInfoList里，Value是文档字段的内容。
public ArrayList < FieldStats > fieldStatsList=null;
public ArrayList < ArrayList < MatchFieldItem > > matchFieldList= null; //  记录哪些字段匹配
    
}
