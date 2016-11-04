/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author xzh
 */
public class SearchLimit {
    public String keyFieldName;   //  key字段名
    public String sortFieldName; //  排序字段名
    public int rowNum;  //  同key值的最大记录数
    public int sortType;   //  排序类型    1逆序 2 正序
    
    public static SearchLimit buildLimitParam(String paramLimitStr,
            HashMap < String,SearchModelFieldDefine > paramMap1,
                HashMap < String,SearchModelFieldDefine> paramMap2)
    {
        if((paramLimitStr==null)||
                (paramLimitStr.isEmpty()))
        {
            return null;
        }
        String [] localLimitList=paramLimitStr.split(":");
        if((localLimitList==null)||
            (localLimitList.length<2))
        {
            return null;
        }
        String tmpSortField=localLimitList[1];
        SearchLimit localLimit=new SearchLimit();
        int localLoopFlag=0;
        localLimit.sortType=1;
        //  处理前缀的+-
        do
        {
            localLoopFlag=0;
            if(tmpSortField.startsWith("+"))
            {
                    localLoopFlag=1;
                    tmpSortField=tmpSortField.substring(1);
                    localLimit.sortType=2;
            }
            else if(tmpSortField.startsWith("-"))
            {
                    localLoopFlag=1;
                    tmpSortField=tmpSortField.substring(1);
                    localLimit.sortType=1;
            }
            else if(tmpSortField.endsWith("+"))
            {
                    localLoopFlag=1;
                    tmpSortField=tmpSortField.substring(0,tmpSortField.length()-1);
                    localLimit.sortType=2;
            }
            else if(tmpSortField.endsWith("-"))
            {
                    localLoopFlag=1;
                    tmpSortField=tmpSortField.substring(0,tmpSortField.length()-1);
                    localLimit.sortType=1;
            }
        }
        while(localLoopFlag!=0);
        SearchModelFieldDefine localFieldDefine=paramMap1.get(tmpSortField);
        if(localFieldDefine==null)
        {
            localFieldDefine=paramMap2.get(tmpSortField);
        }
        if(localFieldDefine==null)
        {
            return null;
        }
        localLimit.sortFieldName=localFieldDefine.getSearchFieldName();
        String tmpKeyField=localLimitList[0];
        localFieldDefine=paramMap1.get(tmpKeyField);
        if(localFieldDefine==null)
        {
            localFieldDefine=paramMap2.get(tmpKeyField);
        }
        if(localFieldDefine==null)
        {
            return null;
        }
        localLimit.keyFieldName=localFieldDefine.getSearchFieldName();
        if(localLimitList.length>=3)
        {
           try
           {
                localLimit.rowNum=Integer.parseInt(localLimitList[2]);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return localLimit;
    }
    
}
