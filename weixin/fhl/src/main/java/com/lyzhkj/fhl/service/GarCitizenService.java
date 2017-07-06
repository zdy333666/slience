/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.pojo.GarCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class GarCitizenService {

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    public GarCitizen findGarCitizenByQR(String qrStr) {

        return garCitizenDAO.findGarCitizenByQR(qrStr);
    }

    public GarCitizen findGarCitizenById(String citizenId) {

        return garCitizenDAO.findGarCitizenById(citizenId);
    }

}
