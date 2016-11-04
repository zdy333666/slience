/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.util;

import com.hzcominfo.gabinfo.conf.GABInfoConfiguration;
import com.dragonsoft.node.adapter.comm.RbspCall;
import com.dragonsoft.node.adapter.comm.RbspConsts;
import com.dragonsoft.node.adapter.comm.RbspService;
import com.hzcominfo.gabinfo.pojo.GABInfoInput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 调用公安部信息查询接口
 *
 * @author zdy
 */
public class GABInfoFetcher {

    /**
     * 公安部信息查询接口
     *
     * @param input
     * @return
     */
    public static String fetch(GABInfoInput input) {

        /*
         [QueryQGRK]
         ReceiverID=S10-00000066
         DataObjectCode=A090
         ReturnFields=WHCD,HYZK,CSRQ,XM,JGSSX,CYM,XB,FWCS,CSD,CSDXZ,ZZXZ,SFZH,BYQK,MZ,RYBH,HKSZD,XP
         InfoCodeMode=0
        
         * @param serviceName 
         * @param condition 信息查询条件
         * @param userCardId 用户身份证号
         * @param userName 用户名
         * @param userDept 用户单位
         */
        String dataObjectCode = input.getDataObjectCode();
        Map<String, String> condition = input.getCondition();
        String userCardId = input.getUserCardId();
        String userName = input.getUserName();
        String userDept = input.getUserDept();
        String[] fields = input.getFields();

        if (fields == null) {
            fields = new String[0];
        }

        //信息查询条件
        StringBuilder conditionBuilder = new StringBuilder();

        Iterator<Entry<String, String>> it = condition.entrySet().iterator();
        Entry<String, String> entry = it.next();
        conditionBuilder.append(entry.getKey()).append("='").append(entry.getValue()).append("'");

        while (it.hasNext()) {
            conditionBuilder.append(" AND ").append(entry.getKey()).append("='").append(entry.getValue()).append("'");
        }

        String conditionStr = conditionBuilder.toString();

        //String condition = "(XM='李勇') AND (SFZH='XXXXXXXX')";
        RbspService service = new RbspService(GABInfoConfiguration.CLIENT_ID, input.getServerId()); //"C00-10001694", "S10-00000066");
        //用户信息-必须填写系统使用者的真实身份信息
        service.setUserCardId(userCardId);
        service.setUserName(userName);
        service.setUserDept(userDept);

        RbspCall call = service.createCall();
        call.setUrl(GABInfoConfiguration.SERVER_URL);
        call.setMethod(RbspConsts.METHOD_QUERY);

        Map<String, Object> params = new HashMap<>();
        params.put("DataObjectCode", dataObjectCode); //"A090"
        params.put("InfoCodeMode", "1");
        //查询条件见接口信息-字段说明-输入项
        params.put("Condition", conditionStr);// "SFZH='330103199107170010'");
        //结果信息见接口信息-字段说明-输出项
        params.put("RequiredItems", fields);

        return call.invoke(params);
    }

}
