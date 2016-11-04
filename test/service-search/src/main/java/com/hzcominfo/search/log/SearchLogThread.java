/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.log;

import com.hzcominfo.accesslog.service.AccessLogService;
import com.hzcominfo.search.service.SearchInput;
import com.hzcominfo.search.service.SearchService;
import com.mongodb.DB;
import com.mongodb.Mongo;
import java.util.LinkedList;

/**
 *
 * @author xzh
 */
public class SearchLogThread extends Thread {
    public static SearchLogThread searchLogThread=new  SearchLogThread();
    public static DB db=null;
    public static String dbname;
    public static LinkedList < SearchAccessLog > localLogList=new  LinkedList < SearchAccessLog >();
    public static int stopFlag=0;
    public static void SetLog(SearchInput searchParam,String ip,String username)
    {
        SearchAccessLog localLog=new SearchAccessLog();
        localLog.searchParam=searchParam;
        localLog.ip=ip;
        localLog.username=username;
        synchronized(localLogList)
        {
            localLogList.addLast(localLog);
        }
    }
    
    public static SearchAccessLog GetLog()
    {
        SearchAccessLog localLog=null;
        synchronized(localLogList)
        {
            if(!localLogList.isEmpty())
            {
                localLog=localLogList.removeFirst();
            }
        }
        return localLog;
    }
    
      @Override
    public void run()
    {
        SearchAccessLog localLog;
        while(stopFlag==0)
        {
            localLog=GetLog();
            if(localLog==null)
            {
                try
                {
                    Thread.sleep(10000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                continue;
            }
            SearchService.saveSearchAccessLog(db, localLog.searchParam,localLog.ip,localLog.username);
            AccessLogService.addAccessSearchLog(db, 0, 1);
        }
        
        
    }
    
}
