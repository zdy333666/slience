/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report.dao;

import cn.slience.report.pojo.ApiCount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class ApiCountDao {

    /**
     *
     * @param conn
     * @param ranges
     * @return
     * @throws SQLException
     */
    public List<ApiCount> getApiCountInTimeRange(Connection conn, long[] ranges) throws SQLException {

        String sql = "SELECT add_time,total_frequency from api_count where add_time>? AND add_time<? order by add_time ASC";

        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setLong(1, ranges[0]);
        psmt.setLong(2, ranges[1]);

        ResultSet rs = psmt.executeQuery();

        List<ApiCount> result = new LinkedList<>();
        while (rs.next()) {
            ApiCount apiCount = new ApiCount();
            apiCount.setAddTime(rs.getLong("add_time"));
            apiCount.setTotalFrequency(Long.parseLong(rs.getString("total_frequency")));

            result.add(apiCount);
        }
        rs.close();
        psmt.close();

        return result;
    }

}
