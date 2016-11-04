/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.dao;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */

@Repository
public class DefaultPhotoDao {

    private List<Byte> bytes;
    private boolean _enableCache = true;

    public DefaultPhotoDao() {

    }

    public DefaultPhotoDao(boolean enableCache) {
        _enableCache = enableCache;
    }

    public byte[] excute() {

        if (bytes == null || !_enableCache) {
            try {
                InputStream inStream = DefaultPhotoDao.class.getResourceAsStream("/com/hzcominfo/photo/toux_pic2.jpg");
                if (inStream == null) {
                    return null;
                }

                bytes = new LinkedList<>();
                byte[] localBuffer = new byte[102400];
                int len = 0;

                while ((len = inStream.read(localBuffer)) != -1) {
                    for (int i = 0; i < len; i++) {
                        bytes.add(localBuffer[i]);
                    }
                }
                inStream.close();
            } catch (Exception ex) {
                Logger.getLogger(DefaultPhotoDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (bytes == null || bytes.isEmpty()) {
            return null;
        }

        int photoSize = bytes.size();
        byte[] photo = new byte[photoSize];
        for (int i = 0; i < photoSize; i++) {
            photo[i] = bytes.get(i);
        }

        return photo;
    }

}
