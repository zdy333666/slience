/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.hzcominfo.gabaggr.dao.GABInfoQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class QGRKServiceTest {

    public static void main(String[] args) {

        //Map<String, String> result = QueryGABQGRKService.queryPersonInfo("330103199107170010", "330103199107170010", "华晓阳", "330100230500");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("SFZH", "330103197012231627");

        String localServiceName = "QueryQGRK";

        List<Map<String, String>> result = new GABInfoQuery().queryGABInfo(localServiceName, paramMap, "330103199107170010", "华晓阳", "330100230500");

        System.out.println("result -- >" + result);

    }

}
