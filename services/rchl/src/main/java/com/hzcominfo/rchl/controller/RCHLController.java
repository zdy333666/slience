/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.controller;

import com.hzcominfo.rchl.pojo.CountInput;
import com.hzcominfo.rchl.pojo.DetailInput;
import com.hzcominfo.rchl.pojo.DPTCarCount;
import com.hzcominfo.rchl.pojo.DPTPersonCount;
import com.hzcominfo.rchl.pojo.JCZCount;
import com.hzcominfo.rchl.pojo.RCHLDetailResult;
import com.hzcominfo.rchl.service.RCHL_DPTCountService;
import com.hzcominfo.rchl.service.RCHL_DPTDetailService;
import com.hzcominfo.rchl.service.RCHL_JCZCountService;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class RCHLController {

    //private final Logger logger = LoggerFactory.getLogger(RCHLController.class);
    @Autowired
    private RCHL_DPTCountService RCHL_DPTCountService;

    @Autowired
    private RCHL_JCZCountService _RCHL_JCZCountService;

    @Autowired
    private RCHL_DPTDetailService _RCHL_DPTDetailService;

    /**
     *
     * @param request
     * @param body
     * @return
     */
    @RequestMapping(value = "VIEW-INSPECTPERSON-COUNT_001", method = RequestMethod.POST)
    @ResponseBody
    public Collection<DPTPersonCount> handPersonCountOfDPT(HttpServletRequest request, @RequestBody CountInput body) {

        return RCHL_DPTCountService.personCountOfDPT(body);
    }

    /**
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "VIEW-INSPECTCAR-COUNT_001", method = RequestMethod.POST)
    @ResponseBody
    public Collection<DPTCarCount> handCarCountOfDPT(@RequestBody CountInput body) {

        return RCHL_DPTCountService.carCountOfDPT(body);
    }

    /**
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "VIEW-INSPECTPERSON-COUNT_003", method = RequestMethod.POST)
    @ResponseBody
    public Collection<JCZCount> handCountOfJCZ(@RequestBody CountInput body) {

        return _RCHL_JCZCountService.countOfJCZ(body);
    }

    /**
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "VIEW-INSPECT-SELECT_001", method = RequestMethod.POST)
    @ResponseBody
    public RCHLDetailResult handDetail(@RequestBody DetailInput body) {

        String tableName = body.getTableName();
        if ("inspectperson".equals(tableName)) {
            return _RCHL_DPTDetailService.getPersonDetail(body);
        } else if ("inspectcar".equals(tableName)) {
            return _RCHL_DPTDetailService.getCarDetail(body);
        }

        return null;
    }

}
