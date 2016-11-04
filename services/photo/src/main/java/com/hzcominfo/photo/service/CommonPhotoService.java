package com.hzcominfo.photo.service;

import com.hzcominfo.photo.conf.MongoSourceFactory;
import com.hzcominfo.photo.conf.MongoSource;
import com.hzcominfo.photo.dao.DefaultPhotoDao;
import com.hzcominfo.photo.conf.PhotoConfiguration;
import com.hzcominfo.photo.dao.CZRKPhotoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonPhotoService {

    @Autowired
    private CZRKPhotoDao _CZRKPhotoDao;

    @Autowired
    private DefaultPhotoDao _DefaultPhotoDao;

    /**
     * 查询人员照片
     *
     * @param userCardId 用户身份证号
     * @param userName 用户名
     * @param userDept 用户单位
     * @param sfzh 身份证号
     * @return
     * @throws java.lang.Exception
     */
    public byte[] service(String userCardId, String userName, String userDept, String sfzh) throws Exception {

        byte[] photo = null;

        if (PhotoConfiguration.photo_czrk_enabled) {
            MongoSource source = MongoSourceFactory.getSource(PhotoConfiguration.mongo_uri_zpdb);
            photo = _CZRKPhotoDao.excute(source.getClient().getDB(source.getClientURI().getDatabase()), sfzh);
            if (photo != null) {
                return photo;
            }
        }

        if (PhotoConfiguration.photo_gab_enabled) {
//            photo = _GABInfoQGRKDao.QueryQGRKZP(userCardId, userName, userDept, sfzh);
            if (photo != null) {
                return photo;
            }
        }

        if (PhotoConfiguration.photo_gab_enabled) {
            photo = _DefaultPhotoDao.excute();
        }

        return photo;
    }

}
