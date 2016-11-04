/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.service;

import com.hzcominfo.rchl.conf.RCHLConfiguration;
import com.hzcominfo.rchl.dao.RCHL_DPTCarDetailDao;
import com.hzcominfo.rchl.dao.RCHL_DPTPersonDetailDao;
import com.hzcominfo.rchl.pojo.DetailInput;
import com.hzcominfo.rchl.pojo.RCHLCarDetailResult;
import com.hzcominfo.rchl.pojo.RCHLPersonDetailResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class RCHL_DPTDetailService {

    @Autowired
    private RCHL_DPTPersonDetailDao _RCHL_DPTPersonDetailDao;

    @Autowired
    private RCHL_DPTCarDetailDao _RCHL_DPTCarDetailDao;

    /**
     *
     * @param input
     */
    public RCHLPersonDetailResult getPersonDetail(DetailInput input) {

        return _RCHL_DPTPersonDetailDao.getDetail(RCHLConfiguration.solr_uri_ryhc, input);
    }

    public RCHLCarDetailResult getCarDetail(DetailInput input) {

        return _RCHL_DPTCarDetailDao.getDetail(RCHLConfiguration.solr_uri_clhc, input);
    }

}
