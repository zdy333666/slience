/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report.controller;

import cn.slience.report.service.ApiCountService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class ReportController {

    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ApiCountService apiCountService;

    /**
     *
     * @param range
     * @return
     */
    @RequestMapping(value = "api_count/query", method = RequestMethod.GET)
    @ResponseBody
    public String handQueryApiCountInTimeRange(@RequestParam(required = false) String range) {

        if (range == null || range.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();

            range = sdf.format(new Date(now.getTime() - 1000 * 60 * 60 * 10)) + "," + sdf.format(now); //"2016-08-21 22:00:00,2016-08-22 08:00:00";
        }

        String[] ranges = range.split(",");
        if (ranges.length != 2) {
            return "range 参数格式不正确!";
        }

        Collection<Map<String, Object>> rows = apiCountService.queryApiCountInTimeRange(ranges);

        StringBuilder result = new StringBuilder();

        result.append("<table style='margin: 50px auto auto auto;border: 0'>");
        result.append("<tr><th>序号</th>").append("<th>时间</th>").append("<th>总次数</th>").append("<th>一小时次数</th></tr>");

        int index = 0;
        for (Map<String, Object> row : rows) {
            index++;
            result.append("<tr>");
            result.append("<td style='text-align: center;padding: 5px 10px 5px 10px;'>").append(index).append("</td>");
            result.append("<td style='text-align: center;padding: 5px 10px 5px 10px;'>").append(row.get("add_time")).append("</td>");
            result.append("<td style='text-align: center;padding: 5px 10px 5px 10px;'>").append(row.get("total_frequency")).append("</td>");
            result.append("<td style='text-align: center;padding: 5px 10px 5px 10px;'>").append(row.get("incr")).append("</td>");
            result.append("</tr>");

        }
        result.append("</table>");

        return result.toString();
    }

    /**
     *
     * @param range
     * @return
     */
    @RequestMapping(value = "api_count/export", method = RequestMethod.GET)
    public void handQueryApiCountInTimeRangeAndExportToExcel(HttpServletResponse response, @RequestParam(required = false) String range) throws IOException {

        if (range == null || range.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            range = sdf.format(new Date(now.getTime() - 1000 * 60 * 60 * 10)) + "," + sdf.format(now); //"2016-08-21 22:00:00,2016-08-22 08:00:00";
        }

        String[] ranges = range.split(",");
        if (ranges.length != 2) {
            return;
        }

        Collection<Map<String, Object>> data = apiCountService.queryApiCountInTimeRange(ranges);

        byte[] result = apiCountService.transformApiCountToExcel(data);

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString()+ ".xls");
        response.getOutputStream().write(result);
    }

}
