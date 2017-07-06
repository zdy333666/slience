/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author breeze
 */
public class Test {
    
    public static void main(String[] args){
        
       Map<String,String> map =new HashMap<>();
       map.put("1", "1");
       map.put(null, null);
        
        System.out.println(map);
    }
      
}
