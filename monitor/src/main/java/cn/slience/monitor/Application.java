/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.monitor;

import cn.slience.monitor.filter.SimpleFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author slience
 */
@EnableDiscoveryClient
//@EnableZuulProxy
//@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableTurbine
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

//    @Bean
//    ClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(5000);
//        factory.setConnectTimeout(5000);
//
//        return factory;
//    }
//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
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

//@RestController
//class ServiceInstanceRestController {
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @RequestMapping("/service-instances/{applicationName}")
//    public List<ServiceInstance> serviceInstancesByApplicationName(
//            @PathVariable String applicationName) {
//        return this.discoveryClient.getInstances(applicationName);
//    }

//}
