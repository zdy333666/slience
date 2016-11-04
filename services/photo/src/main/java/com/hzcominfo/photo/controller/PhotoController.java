/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.controller;

import com.hzcominfo.photo.conf.PhotoConfiguration;
import com.hzcominfo.photo.service.CommonPhotoService;
import com.hzcominfo.photo.service.JWRYPhotoService;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zdy
 */
@Controller
public class PhotoController {

    private final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    @Autowired
    private CommonPhotoService _CommonPhotoService;

    @Autowired
    private JWRYPhotoService _JWRYPhotoService;

    /**
     *
     * @param response
     * @param sfzh
     */
    @RequestMapping(value = "common", method = RequestMethod.GET)
    public void handleCommonPhotoQuery(HttpServletResponse response, @RequestParam(required = true) String sfzh) {

        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 15 && sfzh.length() != 18)) {
            return;
        }

        String userCardId = PhotoConfiguration.userCardId;
        String userName = PhotoConfiguration.userName;
        String userDept = PhotoConfiguration.userDept;

        try {
            byte[] result = _CommonPhotoService.service(userCardId, userName, userDept, sfzh);
            if (result != null) {
                response.getOutputStream().write(result);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    /**
     *
     * @param response
     * @param userCardId
     * @param userName
     * @param sfzh
     * @param userDept
     */
    //@RequestMapping(value = "", method = RequestMethod.GET)
    public void handleCommonPhotoQuery(HttpServletResponse response,
            @RequestParam(required = true) String sfzh,
            @RequestParam(required = true) String userCardId,
            @RequestParam(required = true) String userName,
            @RequestParam(required = true) String userDept) {

        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 15 && sfzh.length() != 18)) {
            return;
        }

        if (userCardId == null || userCardId.trim().isEmpty()) {
            return;
        }

        if (userName == null || userName.trim().isEmpty()) {
            return;
        }

        if (userDept == null || userDept.trim().isEmpty()) {
            return;
        }

        try {
            byte[] result = _CommonPhotoService.service(userCardId, userName, userDept, sfzh);
            if (result != null) {
                response.getOutputStream().write(result);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    /**
     *
     * @param response
     * @param body
     */
    //@RequestMapping(value = "common", method = RequestMethod.POST)
    public void handleCommonPhotoQuery(HttpServletResponse response, @RequestBody Map<String, String> body) {

        String userCardId = body.get("userCardId"); //用户身份证号
        if (userCardId == null || userCardId.trim().isEmpty()) {
            return;
        }

        String userName = body.get("userName"); //用户名
        if (userName == null || userName.trim().isEmpty()) {
            return;
        }

        String userDept = body.get("userDept"); //用户单位
        if (userDept == null || userDept.trim().isEmpty()) {
            return;
        }

        String sfzh = body.get("sfzh"); //待查身份证号
        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 15 && sfzh.length() != 18)) {
            return;
        }

        try {
            byte[] result = _CommonPhotoService.service(userCardId, userName, userDept, sfzh);
            if (result != null) {
                response.getOutputStream().write(result);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    /**
     *
     * @param response
     * @param sfzh
     */
    @RequestMapping(value = "jwry/{sfzh}", method = RequestMethod.GET)
    public void handleJWRYPhotoQuery(HttpServletResponse response, @RequestParam(required = true) String sfzh) {

        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 15 && sfzh.length() != 18)) {
            return;
        }

        try {
            byte[] result = _JWRYPhotoService.service(sfzh);
            if (result != null) {
                response.getOutputStream().write(result);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

}
