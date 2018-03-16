/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import cn.slience.springboot.demo.pojo.HypermediaGreeting;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author breeze
 */
@RestController
public class HypermediaController {

    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping("/greeting")
    public HttpEntity<HypermediaGreeting> greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {

        HypermediaGreeting greeting = new HypermediaGreeting(String.format(TEMPLATE, name));
        greeting.add(linkTo(methodOn(HypermediaController.class).greeting(name)).withSelfRel());

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
}
