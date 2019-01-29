/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Len
 */
@FeignClient(value = "rest1")
public interface Rest1Client {

    @RequestMapping(value = "echo", method = {RequestMethod.POST})
    public String echo();

}
