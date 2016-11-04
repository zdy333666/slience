/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.graph.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author slience
 */
@RestController
public class GraphController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate template;

    @RequestMapping("/person/{key}")
    public Object hi(@PathVariable String key) {
        //@RequestParam(value = "name", defaultValue = "Artaban") String name) {

        String GET_MOVIE_QUERY = "MATCH (n:Person {key:{1}}) RETURN n LIMIT 1";

        long start = System.currentTimeMillis();

        Object obj = template.queryForObject(GET_MOVIE_QUERY, Map.class, key);

        log.info(new StringBuilder().append("------------- spend time : ").append(System.currentTimeMillis() - start).append(" ms").toString());

        return obj;
    }

}
