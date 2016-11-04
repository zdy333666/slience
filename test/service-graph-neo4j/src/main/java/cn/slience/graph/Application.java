/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.graph;

import cn.slience.graph.filter.SimpleFilter;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author slience
 */
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
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
    
     @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:neo4j://localhost:7474?user=neo4j,password=333xxx");
    }

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