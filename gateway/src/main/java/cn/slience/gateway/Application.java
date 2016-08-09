/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.gateway;

import cn.slience.gateway.filter.SimpleFilter;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author slience
 */
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
@EnableCircuitBreaker
//@EnableH
//@RestController
//@RibbonClient(name = "service-hello", configuration = SayHelloConfiguration.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
        //SpringApplication.run(Application.class, args);

    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        factory.setConnectTimeout(2000);
        return factory;
    }

//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @RequestMapping("/hi")
//    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
//        String greeting = this.restTemplate.getForObject("http://service-hello/greeting", String.class);
//        return String.format("%s, %s!", greeting, name);
//    }
}

@RestController
class ServiceInstanceRestController {

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
