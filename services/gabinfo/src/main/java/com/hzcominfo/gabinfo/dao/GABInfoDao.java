/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.dao;

import com.dragonsoft.pci.exception.InvokeServiceException;
import com.hzcominfo.gabinfo.pojo.GABInfoInput;
import com.hzcominfo.gabinfo.util.GABInfoFetcher;
import com.hzcominfo.gabinfo.util.XMLResultParser;
import java.util.List;
import java.util.Map;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */
@Repository
public class GABInfoDao {

    /**
     * 公安部信息查询-返回单条记录
     *
     * @param input
     * @return
     * @throws InvokeServiceException
     * @throws org.dom4j.DocumentException
     */
    public Map<String, String> fetchRow(GABInfoInput input) throws DocumentException, Exception {

        verifyInput(input);

        String resultStr = GABInfoFetcher.fetch(input);
        if (resultStr == null || resultStr.trim().isEmpty()) {
            return null;
        }

        return XMLResultParser.parseRow(resultStr);
    }

    /**
     * 公安部信息查询-返回多条记录
     *
     * @param input
     * @return
     * @throws InvokeServiceException
     * @throws org.dom4j.DocumentException
     */
    public List<Map<String, String>> fetchRows(GABInfoInput input) throws DocumentException, Exception {

        verifyInput(input);

        String resultStr = GABInfoFetcher.fetch(input);
        if (resultStr == null || resultStr.trim().isEmpty()) {
            return null;
        }

        return XMLResultParser.parseRows(resultStr);
    }

    /**
     * 验证公安部信息查询接口输入参数是否有效
     *
     * @param input
     * @return
     */
    private void verifyInput(GABInfoInput input) throws IllegalArgumentException {

        String serverId = input.getServerId();
        if (serverId == null || serverId.trim().isEmpty()) {
            throw new IllegalArgumentException("the input.serverId is invalid");
        }

        String dataObjectCode = input.getDataObjectCode();
        if (dataObjectCode == null || dataObjectCode.trim().isEmpty()) {
            throw new IllegalArgumentException("the input.dataObjectCode is invalid");
        }

        Map<String, String> condition = input.getCondition();
        if (condition == null || condition.isEmpty()) {
            throw new IllegalArgumentException("the input.condition is invalid");
        }

        String userCardId = input.getUserCardId();
        if (userCardId == null || userCardId.trim().isEmpty()) {
            throw new IllegalArgumentException("the input.userCardId is invalid");
        }

        String userName = input.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("the input.userName is invalid");
        }

        String userDept = input.getUserDept();
        if (userDept == null || userDept.trim().isEmpty()) {
            throw new IllegalArgumentException("the input.userDept is invalid");
        }
    }

}
