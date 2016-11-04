/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report.service;

import cn.slience.report.config.ReportConfiguration;
import cn.slience.report.dao.ApiCountDao;
import cn.slience.report.pojo.ApiCount;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class ApiCountService {

    private final Logger logger = LoggerFactory.getLogger(ApiCountService.class);

    @Autowired
    private ApiCountDao apiCountDao;

    public ApiCountService() {

    }

    /**
     *
     * @param ranges
     * @return
     */
    public Collection<Map<String, Object>> queryApiCountInTimeRange(String[] ranges) {

        TreeMap<Long, Map<String, Object>> result = new TreeMap<>();

        Connection conn = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //起始时间
            String startTime = ranges[0];

            //截止时间
            String endTime = ranges[1];

            long[] ranges_l = new long[2];
            ranges_l[0] = sdf.parse(startTime).getTime();
            ranges_l[1] = endTime == null ? Long.MAX_VALUE : sdf.parse(endTime).getTime();

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(ReportConfiguration.bggr_jdbc_url, ReportConfiguration.bggr_jdbc_username, ReportConfiguration.bggr_jdbc_password);
            List<ApiCount> apiCounts = apiCountDao.getApiCountInTimeRange(conn, ranges_l);
            conn.close();
            conn = null;

            long temp_total_frequency = 0L;
            for (ApiCount apiCount : apiCounts) {
                long add_time = apiCount.getAddTime();
                long total_frequency = apiCount.getTotalFrequency();

                Map<String, Object> item = new HashMap<>();
                item.put("add_time", sdf.format(new Date(add_time)));
                item.put("total_frequency", total_frequency);

                if (temp_total_frequency > 0) {
                    item.put("incr", total_frequency - temp_total_frequency);
                    temp_total_frequency = total_frequency;
                } else {
                    temp_total_frequency = total_frequency;
                    item.put("incr", "");
                }

                result.put(add_time, item);
            }
        } catch (ParseException | ClassNotFoundException | SQLException ex) {
            logger.error("", ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    logger.error("", ex);
                }
            }
        }

        return result.descendingMap().values();
    }

    /**
     *
     * @param data
     * @return
     */
    public byte[] transformApiCountToExcel(Collection<Map<String, Object>> data) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();//声明一个工作簿

        HSSFSheet sheet = workbook.createSheet("sheet1");//声明一个表格

        sheet.setDefaultColumnWidth(10);//le表格宽度为200字节

        HSSFCellStyle style = workbook.createCellStyle();//生成一个样式

//        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//
//        // 生成一个字体
//        HSSFFont font = workbook.createFont();
//
//        font.setColor(HSSFColor.VIOLET.index);
//
//        font.setFontHeightInPoints((short) 12);
//
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//
//        style.setFont(font);   // 把字体应用到当前的样式
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();

        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();

        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        // 把字体应用到当前的样式
        style2.setFont(font2);
         // 声明一个画图的顶级管理器
        //HSSFPatriarch patriarch = sheet.createDrawingPatriarch(); 

        //设置标题行
        HSSFRow h_row = sheet.createRow(0);

        //序号
        HSSFCell h_cell0 = h_row.createCell(0);
        h_cell0.setCellStyle(style);
        h_cell0.setCellValue(new HSSFRichTextString("序号"));

        //时间
        HSSFCell h_cell1 = h_row.createCell(1);
        h_cell1.setCellStyle(style);
        h_cell1.setCellValue(new HSSFRichTextString("时间"));

        //总次数
        HSSFCell h_cell2 = h_row.createCell(2);
        h_cell2.setCellStyle(style);
        h_cell2.setCellValue(new HSSFRichTextString("总次数"));

        //一小时次数
        HSSFCell h_cell3 = h_row.createCell(3);
        h_cell3.setCellStyle(style);
        h_cell3.setCellValue(new HSSFRichTextString("一小时次数"));

        //遍历数据
        int i = 0;
        for (Map<String, Object> dataValueMap : data) {
            i++;
            HSSFRow r_row = sheet.createRow(i);

            //序号
            HSSFCell r_cell0 = r_row.createCell(0);
            r_cell0.setCellStyle(style);
            r_cell0.setCellValue(new HSSFRichTextString(String.valueOf(i)));

            //时间
            HSSFCell r_cell1 = r_row.createCell(1);
            r_cell1.setCellStyle(style);
            r_cell1.setCellValue(new HSSFRichTextString(String.valueOf(dataValueMap.get("add_time"))));

            //总次数
            HSSFCell r_cell2 = r_row.createCell(2);
            r_cell2.setCellStyle(style);
            r_cell2.setCellValue(new HSSFRichTextString(String.valueOf(dataValueMap.get("total_frequency"))));

            //一小时次数
            HSSFCell r_cell3 = r_row.createCell(3);
            r_cell3.setCellStyle(style);
            r_cell3.setCellValue(new HSSFRichTextString(String.valueOf(dataValueMap.get("incr"))));

        }
        //index++;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);

        return os.toByteArray();
    }
}
