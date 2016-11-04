/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cominfo4
 */
public class G20Report {

    public static void jjmain(String[] args) {

        //起始时间
        String startTime = "2016-08-21 00:00:00";

        //截止时间
        String endTime = "2016-08-22 00:01:00";

        
        
        //------------------------------------------------
        Connection conn = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@10.118.159.106:1521:hzga106", "bggr", "bggr123456");

            String sql = "SELECT add_time,total_frequency from api_count where add_time>? AND add_time<? order by add_time DESC";

            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setLong(1, sdf.parse(startTime).getTime());// sdf.parse(startTime).getTime());
            psmt.setLong(2, endTime == null ? Long.MAX_VALUE : sdf.parse(endTime).getTime());

            ResultSet rs = psmt.executeQuery();

            TreeMap<Long, Long> rows = new TreeMap<>();
            while (rs.next()) {
                rows.put(rs.getLong("add_time"), Long.parseLong(rs.getString("total_frequency")));
            }
            rs.close();
            psmt.close();
            conn.close();
            conn=null;

            TreeMap<Long, Map<String, Object>> result = new TreeMap<>();

            long temp_total_frequency = 0L;
            for (long add_time : rows.keySet()) {
                long total_frequency = rows.get(add_time);

                Map<String, Object> item = new HashMap<>();
                item.put("add_time", sdf.format(new Date(add_time)));
                item.put("total_frequency", total_frequency);

                if (temp_total_frequency > 0) {
                    item.put("incr", total_frequency - temp_total_frequency);
                    temp_total_frequency = total_frequency;
                } else {
                    temp_total_frequency = total_frequency;
                    item.put("incr", null);
                }

                result.put(add_time, item);
            }

            int n = 0;
            for (Map<String, Object> value : result.descendingMap().values()) {
                n++;
                System.out.println(n + " --> " + value);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}
