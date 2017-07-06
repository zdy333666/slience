/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.PayDAO;
import com.lyzhkj.fhl.pojo.DbResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class PayService {

    @Autowired
    private PayDAO payDAO;

    public DbResult runBatch_insert_storeExchange(String citizenId, int throwExchangeInt, int recycleExchangeInt, int pickExchangeInt, String cashierId) {

        return payDAO.runBatch_insert_storeExchange(citizenId, throwExchangeInt, recycleExchangeInt, pickExchangeInt, cashierId);
    }
}
