/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.controller;

import com.hzcominfo.relation.pojo.RelationDefine;
import com.hzcominfo.relation.pojo.RelationDetail;
import com.hzcominfo.relation.service.RelationBasicService;
import com.hzcominfo.relation.service.SinglePersonRelationService;
import com.hzcominfo.relation.util.IDCardNoChecker;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class RelationController {

    //private final Logger logger = LoggerFactory.getLogger(RelationController.class);
    @Autowired
    private SinglePersonRelationService singlePersonRelationService;

    @Autowired
    private RelationBasicService relationBasicService;

    /**
     *
     * @param request
     * @param sfzh
     * @return
     */
    @RequestMapping(value = "types", method = RequestMethod.GET)
    @ResponseBody
    public List<RelationDefine> fetchRelationTypes() {

        return relationBasicService.getRelationTypes();
    }

    /**
     *
     * @param request
     * @param sfzh
     * @return
     */
    @RequestMapping(value = "single", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> fetchSingleWithTypes(@RequestParam String sfzh, @RequestParam(required = false) String types) {

        if (!IDCardNoChecker.check(sfzh)) {
            return null;
        }

        if (types == null) {
            return singlePersonRelationService.fetchRelationsWithOrder(sfzh);
        }

        if (types.trim().isEmpty()) {
            return null;
        }

        String[] typeArray = types.split(",");

        return singlePersonRelationService.fetchRelationsWithOrder(sfzh, typeArray);
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "single_detail", method = RequestMethod.GET)
    @ResponseBody
    public Collection<RelationDetail> fetchSingleDetail(@RequestParam String pair, @RequestParam String type) {

        if (pair == null || pair.trim().isEmpty()) {
            return null;
        }

        String[] pairArray = pair.split(",");
        if (pairArray.length != 2) {
            return null;
        }

        for (String sfzh : pairArray) {
            if (!IDCardNoChecker.check(sfzh)) {
                return null;
            }
        }

        if (type == null || type.trim().isEmpty()) {
            return null;
        }

        return singlePersonRelationService.fetchRelationDetail(pairArray, type);
    }

}
