/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

import java.util.Comparator;

/**
 *
 * @author xzh
 */
public class ResultStatsComparator implements Comparator {
   public int compare ( Object object1,Object object2 )
    {
        if(((ResultStats)object1).total>((ResultStats)object2).total)
        {
            return -1;
        }
        else if(((ResultStats)object1).total<((ResultStats)object2).total)
        {
            return 1;
        }
        return 0;
    }    
    
}
