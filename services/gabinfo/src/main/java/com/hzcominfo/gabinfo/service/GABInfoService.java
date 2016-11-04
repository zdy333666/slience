/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.service;

import com.dragonsoft.pci.exception.InvokeServiceException;
import com.hzcominfo.gabinfo.dao.GABInfoDao;
import com.hzcominfo.gabinfo.pojo.GABInfoInput;
import java.util.List;
import java.util.Map;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zdy
 */
@Service
public class GABInfoService {

    @Autowired
    private GABInfoDao dao;

    /**
     *
     * @param input
     * @return @throws InvokeServiceException
     * @throws DocumentException
     */
    public Map<String, String> fetchRow(GABInfoInput input) throws Exception {

        return dao.fetchRow(input);
    }

    /**
     *
     * @param input
     * @return
     * @throws InvokeServiceException
     * @throws DocumentException
     */
    public List<Map<String, String>> fetchRows(GABInfoInput input) throws Exception {

        return dao.fetchRows(input);
    }

}
