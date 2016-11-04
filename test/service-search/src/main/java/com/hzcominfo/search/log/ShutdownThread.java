/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.log;

/**
 *
 * @author xzh
 */
public class ShutdownThread extends  Thread{

  @Override
    public void run ()
    {
        SearchLogThread.stopFlag=1;
        if(SearchLogThread.searchLogThread!=null)
        {
            try
            {
                SearchLogThread.searchLogThread.join();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("shutdown thread end");
    }    
}