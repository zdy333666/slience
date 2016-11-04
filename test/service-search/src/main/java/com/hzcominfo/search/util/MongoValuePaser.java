/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class MongoValuePaser {

    private static final Logger logger = LoggerFactory.getLogger(MongoValuePaser.class);

    public static int parseInt(Object object) {
        int value = 0;

        if (object == null) {
            return value;
        }

        if (object instanceof Long) {
            value = ((Long) object).intValue();
        } else if (object instanceof Double) {
            value = ((Double) object).intValue();
        } else if (object instanceof Float) {
            value = ((Float) object).intValue();
        } else if (object instanceof Integer) {
            value = (Integer) object;
        } else {
            try {
                value = Integer.parseInt(object.toString());
            } catch (Exception e) {
                logger.debug(null, e);
            }
        }
        return value;
    }

    public static long parseLong(Object object) {
        long value = 0L;

        if (object == null) {
            return value;
        }
        if (object instanceof Long) {
            value = (Long) object;
        } else if (object instanceof Double) {
            value = ((Double) object).longValue();
        } else if (object instanceof Float) {
            value = ((Float) object).longValue();
        } else if (object instanceof Integer) {
            value = ((Integer) object).longValue();
        } else {
            try {
                value = Long.parseLong(object.toString());
            } catch (Exception e) {
                logger.debug(null, e);
            }
        }
        return value;
    }

    public static String parseString(Object object) {
        String value = null;

        if (object == null) {
            return value;
        }
        if (object instanceof Date) {
            value = new SimpleDateFormat("yyyyMMddHHmmss").format((Date) object);
        } else {
            value = object.toString();
        }

        return value;
    }

    public static Date parseDate(Object object) {
        Date value = null;

        if (object == null) {
            return value;
        }
        if (object instanceof Date) {
            value = (Date) object;
        }

        return value;
    }

    public static byte[] parseBytes(Object object) {
        byte[] value = null;

        if (object == null) {
            return null;
        }
        if (object instanceof byte[]) {
            value = (byte[]) object;
        }

        return value;
    }

}
