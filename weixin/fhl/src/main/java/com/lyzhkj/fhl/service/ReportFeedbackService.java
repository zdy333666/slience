/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarAccusationDAO;
import com.lyzhkj.fhl.dto.ReportInput;
import com.lyzhkj.fhl.pojo.GarAccusation;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class ReportFeedbackService {

    @Autowired
    private GarAccusationDAO garAccusationDAO;

    public boolean report(ReportInput input) {

        boolean b = false;
        try {
            b = garAccusationDAO.addReport(input);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return b;
    }

    public boolean hasUnHandByUserId(String userId) {

        return garAccusationDAO.hasUnHandByUserId(userId);
    }

    public GarAccusation viewDetail(String userId) {

        return garAccusationDAO.findByUserId(userId);
    }

}
