/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.graph.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author slience
 */

@RestController
public class ServiceInstanceRestController {
    
@Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/service-instances") //{applicationName}
    public Map<String, Object> serviceInstancesByApplicationName() {
        //@PathVariable String applicationName) {

        Map<String, Object> instances = new TreeMap<>();

        for (String serviceName : this.discoveryClient.getServices()) {
            List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(serviceName);
            if (serviceInstances == null) {
                continue;
            }

            List<Map<String, Object>> children = new LinkedList<>();
            for (ServiceInstance serviceInstance : serviceInstances) {
                Map<String, Object> child = new TreeMap<>();
                child.put("name", serviceInstance.getUri());

                children.add(child);
            }
            instances.put("name", serviceName);
            instances.put("children", children);
        }

        return instances;
    }
}
