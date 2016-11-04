/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.service;

import com.hzcominfo.photo.conf.MongoSourceFactory;
import com.hzcominfo.photo.conf.MongoSource;
import com.hzcominfo.photo.conf.PhotoConfiguration;
import com.hzcominfo.photo.dao.JWRYPhotoDao;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class JWRYPhotoService {

    @Autowired
    private JWRYPhotoDao _JWRYPhotoDao;

    public byte[] service(String sfzh) throws UnknownHostException {
        MongoSource source = MongoSourceFactory.getSource(PhotoConfiguration.mongo_uri_rtdb);
        return _JWRYPhotoDao.excute(source.getClient().getDB(source.getClientURI().getDatabase()), sfzh);
    }
}
