/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.FacetParams;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.hzcominfo.accesslog.param.AccessItemParam;
import com.hzcominfo.accesslog.param.AccessLog;
import com.hzcominfo.accesslog.service.AccessLogService;
import com.hzcominfo.auth.dao.Impl.RoleModelFieldMapDao;
import com.hzcominfo.auth.dao.Impl.RoleModelMapDao;
import com.hzcominfo.auth.model.A_Role;
import com.hzcominfo.auth.model.A_RoleModelFieldMap;
import com.hzcominfo.auth.model.A_RoleModelMap;
import com.hzcominfo.auth.model.AuthUserContext;
import com.hzcominfo.search.cache.SearchCacheBuilder;
import com.hzcominfo.search.cache.SearchModelCache;
import com.hzcominfo.search.log.SearchLogThread;
import com.hzcominfo.search.pojo.SearchModel;
import com.hzcominfo.search.pojo.SearchModelField;
import com.hzcominfo.search.pojo.SearchModelGroup;
import com.hzcominfo.search.result.FieldStats;
import com.hzcominfo.search.result.KeyModelMap;
import com.hzcominfo.search.result.MatchFieldItem;
import com.hzcominfo.search.result.ModelFieldInfo;
import com.hzcominfo.search.result.ModelGroupKeyMap;
import com.hzcominfo.search.result.ModelSearchResult;
import com.hzcominfo.search.result.ResultStats;
import com.hzcominfo.search.result.ResultStatsComparator;
import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author xzh
 */
public class SearchService {

    public static ConcurrentHashMap<String, LinkedList<HttpSolrServer>> solrMap = new ConcurrentHashMap<String, LinkedList<HttpSolrServer>>();

    public static HttpSolrServer GetSolr(String paramUrl) {
        HttpSolrServer localSolr;
        LinkedList<HttpSolrServer> localSolrList = solrMap.get(paramUrl);
        if ((localSolrList != null) && (!localSolrList.isEmpty())) {
            localSolr = localSolrList.removeFirst();
            System.out.println("search url reuse :" + paramUrl);
            return localSolr;
        }
        if (localSolrList == null) {
            localSolrList = new LinkedList<HttpSolrServer>();
            solrMap.put(paramUrl, localSolrList);
        }
        localSolr = new HttpSolrServer(paramUrl);
        localSolr.setParser(new XMLResponseParser());
        System.out.println("search url create :" + paramUrl);
        return localSolr;
    }

    public static void PutSolr(String paramUrl, HttpSolrServer paramSolr) {
        HttpSolrServer localSolr;
        LinkedList<HttpSolrServer> localSolrList = solrMap.get(paramUrl);
        if (localSolrList == null) {
            localSolrList = new LinkedList<HttpSolrServer>();
            solrMap.put(paramUrl, localSolrList);
        }
        localSolrList.addLast(paramSolr);
        System.out.println("search url release :" + paramUrl);
    }

    // 取模型定义信息
    public static SearchModel getModel(String modelName) {
        return SearchDao.getModel(modelName);
    }

    // 取模型组定义信息
    public static ArrayList<SearchModelGroup> getSearchModelGroups() {

        return SearchDao.getSearchModelGroups();
    }

    // 检测当前用户拥有的角色对单个模型是否有访问权限
    public static int checkRoleModelAuth(com.mongodb.DB db, String modelName, HttpServletRequest paramRequest) {
        if (db == null || paramRequest == null || modelName == null || modelName.compareToIgnoreCase("") == 0) {
            return 0;
        }
        return SearchDao.checkRoleModelAuth(db, modelName, paramRequest);
    }

    // 按模型组name取所有有显示权限的模型字段
    public static ArrayList<SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, String modelGroupName,
            HttpServletRequest paramRequest) {
        if (db == null || paramRequest == null || modelGroupName == null
                || modelGroupName.compareToIgnoreCase("") == 0) {
            return null;
        }
        return SearchDao.GetModelFieldDefine(db, modelGroupName, paramRequest);
    }

    // 取模型子段信息
    public static ArrayList<SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, int paramModelDefId) {
        return SearchDao.GetModelFieldDefine(db, paramModelDefId);
    }

    // 根据模型id、模型字段name查询字段信息
    public static ArrayList<SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, int modelId,
            String modelFieldName) {
        if (db == null) {
            return null;
        }
        return SearchDao.GetModelFieldDefine(db, modelId, modelFieldName);
    }

    public static interface SolrInterceptor {
    }

    public static interface SolrQueryHandler extends SolrInterceptor {

        void handle(SolrQuery query);
    }

    public static interface SolrResultProcessor extends SolrInterceptor {

        void process(SolrDocumentList result);
    }

    public static boolean checkInput(SearchInput input) {
        if ((input == null) || (input.getBusiModel() == null) || (input.getBusiModel().isEmpty())
                || (((input.getQ() == null) || (input.getQ().isEmpty())) && ((input.getRawq() == null) || (input.getRawq().isEmpty())))) {
            return false;
        } else {
            return true;
        }
    }

    public static ModelSearchResult DoSearch(SearchInput input,
            HttpServletRequest paramRequest, int paramRawFormat, SolrInterceptor... handlers) {

        if (!checkInput(input)) {
            return null;
        }

        SearchModelCache cache = null;
        try {
            cache = SearchCacheBuilder.buildCache(input);
        } catch (ParseException ex) {
            Logger.getLogger(SearchService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cache == null) {
            return null;
        }

        SearchModel model = cache.getModel();
        List<SearchModelField> modelFields = cache.getModelFields();

        long localBeginTime = System.currentTimeMillis();
        // 当前模型可搜索的条数
        AuthUserContext authinfoContext = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
        A_RoleModelMap modelMap = new A_RoleModelMap();
        modelMap.setRoleID(authinfoContext.getRoles().getRoleID());
        modelMap.setModelname(input.getBusiModel());
        int returnRows = 0;
        String rowStringreturn = RoleModelMapDao.GetInstance().GetModelByRoleIDModel(db, modelMap).getReturnRows();
        if (null != rowStringreturn && !("".equals(rowStringreturn))) {
            returnRows = Integer.valueOf(rowStringreturn);
        }

        // 本次搜索的数据条数
        int rows = input.getRows();
        if (rows > 0) {
            input.setRows(rows > 20 ? 20 : rows);
        } else {
            input.setRows(10);
        }

        // 数据开始位置
        int start = input.getStart();
        if (start < 1) {
            input.setStart(10);
        } else if (start >= 3000) {
            int mod = 3000 % rows;
            if (mod > 0) {
                input.setStart(3000 / rows);
            } else {
                input.setStart((3000 / rows) - 1);
            }
        }

//        if (start >= returnRows) {
//            return null;
//        } else if (start + rows > returnRows) {
//            input.setRows(returnRows - start);
//        }
        System.out.println("search " + input.getBusiModel() + " check model role step :  " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");

        localBeginTime = System.currentTimeMillis();

        // 测试
        // System.out.println("测试
        // list："+GetModelFieldDefine(db,"模型组1",paramRequest).size());
//        SearchModelDefine localModelDefine = GetModelDefine(db, input.getBusiModel());
//        if (localModelDefine == null) {
//            return null;
//        }
//        ArrayList<SearchModelFieldDefine> localFieldDefineList = GetModelFieldDefine(db, model.getId());
//        if (localFieldDefineList == null) {
//            return null;
//        }
        // 通过字段名来找搜索字段名的映射
        HashMap<String, String> localFieldDefineMap = new HashMap<String, String>();
        String localSearchFieldName;
        // 字段名-> 字段定义
        HashMap<String, SearchModelField> localFieldDefineMap3 = new HashMap<>();

        // 显示名 ->字段定义
        HashMap<String, SearchModelField> localFieldDefineMap4 = new HashMap<>();

        ArrayList<SearchModelField> localWildMatchFieldList = new ArrayList<>();
        ArrayList<SearchModelField> localDateFieldList = new ArrayList<>();

        HashMap<String, String> localFieldDefineMap2 = new HashMap<String, String>();

        HashSet<String> localIdNameSet = new HashSet<String>();

        for (SearchModelField modelField : modelFields) {
            int fieldSpec = modelField.getFieldSpec();
            if (fieldSpec == 1) {
                localIdNameSet.add(modelField.getName());
            } else if ((fieldSpec >= 11) && (fieldSpec <= 19)) {
                localDateFieldList.add(modelField);
            }

//            if (localFieldDefine.getDisplayInGrid() == 1) {
//                 //localMainFieldList.add(localFieldDefine);
//            }
            localFieldDefineMap.put(modelField.getName(), modelField.getSourceField());

            if (modelField.getWildMatch() > 0) {
                localWildMatchFieldList.add(modelField);
            }

            localFieldDefineMap3.put(modelField.getName(), modelField);

            localSearchFieldName = localFieldDefineMap2.get(modelField.getDisplayName());
            if ((localSearchFieldName == null) || (modelField.getSourceField().endsWith("_tcn"))) {
                localFieldDefineMap2.put(modelField.getDisplayName(), modelField.getSourceField());
                localFieldDefineMap4.put(modelField.getDisplayName(), modelField);
            }
        }

//        for (SearchModelField modelField : modelFields) {
//            localSearchFieldName = localFieldDefineMap2.get(localFieldDefine.getFieldDisplayName());
//            if ((localSearchFieldName == null) || (localFieldDefine.getSearchFieldName().endsWith("_tcn"))) {
//                localFieldDefineMap2.put(localFieldDefine.getFieldDisplayName(), localFieldDefine.getSearchFieldName());
//                localFieldDefineMap4.put(localFieldDefine.getFieldDisplayName(), localFieldDefine);
//            }
//        }
        System.out.println("search " + input.getBusiModel() + " load model info step :  " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");

        localBeginTime = System.currentTimeMillis();

        SolrQuery parameters = new SolrQuery();
        SearchFormat localFormat = null;
        if ((input.getRawq() == null) || (input.getRawq().isEmpty())) {
            localFormat = SearchFormat.buildFormat(input.getQ(), localFieldDefineMap3, localFieldDefineMap4,
                    localWildMatchFieldList, localDateFieldList);
        }
        SearchFormat localFormat2 = null;
        if ((input.getOldq() != null) && (!input.getOldq().isEmpty())) {
            localFormat2 = SearchFormat.buildFormat(input.getOldq(), localFieldDefineMap3,
                    localFieldDefineMap4, localWildMatchFieldList, localDateFieldList);
        }
        parameters.set("ident", "true");
        parameters.set("fl", "*,score");

        if ((input.getRawq() != null) && (!input.getRawq().isEmpty())) {
            parameters.set("q", input.getRawq());
        } else {
            parameters.set("q", localFormat.searchQ);
            String localIp = paramRequest.getRemoteAddr();
            HttpSession localSession = paramRequest.getSession();
            Object localObject = localSession.getAttribute("AuthInfo");
            if (localObject != null) {

                String localUser = ((AuthUserContext) localObject).getUsers().getUsername();
                SearchLogThread.SetLog(input, localIp, localUser);
            }
        }

        parameters.set("start", input.getStart());
        parameters.set("rows", input.getRows());

        if ((input.getSort() != null) && (!input.getSort().isEmpty())) {
            parameters.set("sort", input.getSort());

        } else if ((model.getSearchSort() != null) && (!model.getSearchSort().isEmpty())) {
            parameters.set("sort", model.getSearchSort());
        } else {
            parameters.set("sort", "score desc");
        }
        parameters.set("defType", "edismax");

        ArrayList<SearchStats> localStatsList = new ArrayList<SearchStats>();
        localStatsList = SearchStats.buildStatsParam(input.getStats(), localFieldDefineMap3, localFieldDefineMap4);

  
        HashMap<String, SearchStats> localStatsMap = new HashMap<String, SearchStats>();
        if (localStatsList != null) {
            parameters.setFacet(true);
            for (SearchStats tmpStats : localStatsList) {
                parameters.addFacetField(tmpStats.fieldName);
                parameters.setFacetPrefix(tmpStats.fieldName, tmpStats.fieldPrefix);
                parameters.setFacetLimit(10000);
                parameters.setFacetMinCount(1);
                parameters.setFacetSort(FacetParams.FACET_SORT_COUNT);

                localStatsMap.put(tmpStats.fieldName, tmpStats);
            }
        }
        System.out.println("search " + input.getBusiModel() + " save log step :  " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");

        localBeginTime = System.currentTimeMillis();

        String urlString = model.getSearchUrl();
        HttpSolrServer solr = GetSolr(urlString);
        System.out.println("search " + input.getBusiModel() + " solr connect step :  " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");

        QueryResponse response = null;
        localBeginTime = System.currentTimeMillis();
        if (handlers != null && handlers.length > 0) {
            for (SolrInterceptor h : handlers) {
                if (h != null && h instanceof SolrQueryHandler) {
                    ((SolrQueryHandler) h).handle(parameters);
                }
            }
        }
        try {
            response = solr.query(parameters);

        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException ex) {
            Logger.getLogger(SearchService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (solr != null) {
            try {
                solr.close();
            } catch (IOException ex) {
                Logger.getLogger(SearchService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (response == null) {
            return null;
        }

        System.out.println("search " + input.getBusiModel() + " solr step : " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");
        SolrDocumentList localDocList = response.getResults();
        for (SolrInterceptor h : handlers) {
            if (null != h && h instanceof SolrResultProcessor) {
                ((SolrResultProcessor) h).process(localDocList);
            }
        }

        ModelSearchResult localSearchResult = new ModelSearchResult();

        localSearchResult.docList = new ArrayList<HashMap<String, String>>();
        localSearchResult.matchFieldList = new ArrayList<ArrayList<MatchFieldItem>>();
        String localSolrField;
        MatchFieldItem localMatchItem;
        ArrayList<MatchFieldItem> localMatchValue;
        HashSet<String> localBlackListSet = (HashSet<String>) paramRequest.getSession().getAttribute("black_list");
        if (localBlackListSet == null) {
            localBlackListSet = SearchDao.GetBlackListSet(db);
            if (localBlackListSet != null) {
                paramRequest.getSession().setAttribute("black_list", localBlackListSet);
            }
        }
        if (localBlackListSet != null) {
            System.out.println("search blacklist size is " + localBlackListSet.size());
        }

        boolean localAdminRole = false;
        AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
        if ((localAuthInfo != null) && (localAuthInfo.getRoles().getRoleName().equals("admin"))) {
            localAdminRole = true;
        }

        if (localAuthInfo != null) {
            System.out.println("search role is :" + localAuthInfo.getRoles().getRoleName());
        }

        localBeginTime = System.currentTimeMillis();

        // 写入搜索的结果 docList
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        for (SolrDocument localDoc : localDocList) {
            // 对每条记录，检查字段是否包含要搜索的内容
            int localInsertFlag = 1;
            HashMap<String, String> localResultValue = new HashMap<String, String>();
            int localFieldIndex = 0;

            // 第三次方案(前) 角色模型字段权限验证
            //String localReplaceStr = null;
            String localFieldStr = null;
            localMatchValue = new ArrayList<MatchFieldItem>();
            localSearchResult.matchFieldList.add(localMatchValue);

            for (SearchModelField modelField : modelFields) {
                String fieldName = modelField.getName();

                localSolrField = localFieldDefineMap.get(fieldName);
                // 判断是否是grid模式，如果是，则过滤一些字段
                if ((input.getGridView() == 1) && (modelField.getDisplayInGrid() == 0)) {
                    localFieldIndex++;
                    continue;
                }
                if ((localSolrField == null) || (localSolrField.isEmpty())) {
                    localResultValue.put(fieldName, "");
                } else {
                    Object localObjectValue = localDoc.get(localSolrField);
                    if (localObjectValue == null) {
                        localResultValue.put(fieldName, "");
                        // System.out.println("field
                        // "+localField.getFieldName()+" not found");
                    } else {
                        String localValueStr = localObjectValue.toString();
                        if ((!localAdminRole) && (localIdNameSet.contains(fieldName))
                                && (localBlackListSet != null) && localBlackListSet.contains(localValueStr)) {
                            localInsertFlag = 0;
                            System.out.println("search have no right to view key " + localValueStr);
                            break;
                        }

                        if (localObjectValue instanceof java.util.Date) {
                            localValueStr = localDateFormat.format((java.util.Date) localObjectValue);
                        }
                        localFieldStr = localValueStr;
                        localResultValue.put(fieldName, localValueStr);
                        // 处理匹配字段
                        if (localFieldIndex > 0) {
                            if ((paramRawFormat == 0) && (localFormat != null)
                                    && ((((localFormat.searchKeyFieldSet != null)
                                    && (!localFormat.searchKeyFieldSet.isEmpty())))
                                    || (((localFormat.searchKeyFieldMap != null)
                                    && (!localFormat.searchKeyFieldMap.isEmpty()))))) {
                                if (((localFormat.searchKeyFieldSet != null)
                                        && (!localFormat.searchKeyFieldSet.isEmpty()))) {
                                    for (String localKeyStr : localFormat.searchKeyFieldSet) {
                                        if (!SearchFormat.isWild(localKeyStr)) {
                                            if (!localFieldStr.startsWith("http:") && !localFieldStr.startsWith("HTTP:")
                                                    && !localFieldStr.startsWith("ftp:")
                                                    && !localFieldStr.startsWith("FTP:")) {
                                                if (localFieldStr.contains(localKeyStr)) {
                                                    localMatchItem = new MatchFieldItem();
                                                    localMatchItem.setFieldName(fieldName);
                                                    localMatchItem.setSearchKey(localKeyStr);
                                                    localMatchItem.setMatchValue(localFieldStr.replaceAll(localKeyStr,
                                                            "<label style=\"color: #FF0000\">" + localKeyStr
                                                            + "</label>"));
                                                    localMatchValue.add(localMatchItem);
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            // ---------------------------------
                            if (input.getOldq() != null && !input.getOldq().isEmpty()) {

                                if ((localFormat2 != null) && ((((localFormat2.searchKeyFieldSet != null)
                                        && (!localFormat2.searchKeyFieldSet.isEmpty())))
                                        || (((localFormat2.searchKeyFieldMap != null)
                                        && (!localFormat2.searchKeyFieldMap.isEmpty()))))) {
                                    if (((localFormat2.searchKeyFieldSet != null)
                                            && (!localFormat2.searchKeyFieldSet.isEmpty()))) {
                                        for (String localKeyStr : localFormat2.searchKeyFieldSet) {
                                            if (!SearchFormat.isWild(localKeyStr)) {
                                                if (!localFieldStr.startsWith("http:")
                                                        && !localFieldStr.startsWith("HTTP:")
                                                        && !localFieldStr.startsWith("ftp:")
                                                        && !localFieldStr.startsWith("FTP:")) {
                                                    if (localFieldStr.contains(localKeyStr)) {
                                                        localMatchItem = new MatchFieldItem();
                                                        localMatchItem.setFieldName(fieldName);
                                                        localMatchItem.setSearchKey(localKeyStr);
                                                        localMatchItem.setMatchValue(localFieldStr.replaceAll(
                                                                localKeyStr, "<label style=\"color: #FF0000\">"
                                                                + localKeyStr + "</label>"));
                                                        localMatchValue.add(localMatchItem);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            // ------------------------------------
                        }

                        // 第三次方案(后) 角色模型字段权限验证
                    }
                }
                localFieldIndex++;
            }

            if (localInsertFlag != 0) {
                localSearchResult.docList.add(localResultValue);
            }
        }

        System.out.println("search " + input.getBusiModel() + " filter step new : " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");

        // 写入字段信息 modelFieldInfoList
        localSearchResult.modelFieldInfoList = new ArrayList<ModelFieldInfo>();
        for (SearchModelField modelField : modelFields) {
            // 过滤grid模式下的一些字段
            if ((input.getGridView() == 1) && (modelField.getDisplayInGrid() == 0)) {
                continue;
            }
            ModelFieldInfo localFieldInfo = new ModelFieldInfo();
            localFieldInfo.fieldName = modelField.getName();
            localFieldInfo.fieldDisplayName = modelField.getDisplayName();// 集合字段显示名称
            localFieldInfo.displayInGrid = modelField.getDisplayInGrid();
            //localFieldInfo.displayWidth = modelField.g.getDisplayWidth();
            localFieldInfo.fieldSpec = modelField.getFieldSpec();
            localSearchResult.modelFieldInfoList.add(localFieldInfo);
        }
        localSearchResult.start = localDocList.getStart();
        localSearchResult.numFound = localDocList.getNumFound();
        localSearchResult.rows = localDocList.size();
        if (localSearchResult.rows > localSearchResult.docList.size()) {
            localSearchResult.rows = localSearchResult.docList.size();
            localSearchResult.numFound = localSearchResult.rows + localDocList.getStart();
        }
        List<FacetField> localFacetList = response.getFacetFields();
        if ((localFacetList != null) && (!localFacetList.isEmpty())) {
            localSearchResult.fieldStatsList = new ArrayList<FieldStats>();
            for (FacetField tmpField : localFacetList) {
                SearchStats localSearchStats = localStatsMap.get(tmpField.getName());
                if (localSearchStats == null) {
                    continue;
                }

                FieldStats tmpFieldStats = new FieldStats();
                tmpFieldStats.fieldName = tmpField.getName();
                List<Count> tmpCountList = tmpField.getValues();
                if ((tmpCountList == null) || (tmpCountList.isEmpty())) {
                    continue;
                }
                tmpFieldStats.fieldStatsDataMap = new TreeMap<String, Long>();
                int tmpValueOff = 0;
                if ((localSearchStats.statsValueLen > 0) && (localSearchStats.fieldPrefix != null)) {
                    tmpValueOff = localSearchStats.fieldPrefix.length();
                }
                String localNewKey;
                Long localNewValue;
                int localOff;
                for (Count tmpCount : tmpCountList) {
                    if (tmpValueOff > 0) {
                        localNewKey = tmpCount.getName();
                        if ((localNewKey != null) && (localNewKey.length() >= tmpValueOff)) {
                            localOff = tmpValueOff + localSearchStats.statsValueLen;
                            if (localOff > localNewKey.length()) {
                                localOff = localNewKey.length();
                            }

                            localNewKey = localNewKey.substring(0, localOff);
                            localNewValue = tmpFieldStats.fieldStatsDataMap.get(localNewKey);
                            if (localNewValue == null) {
                                tmpFieldStats.fieldStatsDataMap.put(localNewKey, tmpCount.getCount());
                            } else {
                                localNewValue = (long) localNewValue + tmpCount.getCount();
                                tmpFieldStats.fieldStatsDataMap.put(localNewKey, localNewValue);
                            }
                        }
                    } else {
                        tmpFieldStats.fieldStatsDataMap.put(tmpCount.getName(), tmpCount.getCount());
                    }
                }
                localSearchResult.fieldStatsList.add(tmpFieldStats);
            }
        }

        PutSolr(urlString, solr);
        return localSearchResult;
    }

//    public static ModelSearchResult DoSearch_save(com.mongodb.DB db, SearchParam paramSearchParam,
//            HttpServletRequest paramRequest, int paramRawFormat) {
//        if ((paramSearchParam == null) || (paramSearchParam.getBusiModel() == null)
//                || (paramSearchParam.getBusiModel().isEmpty())
//                || (((paramSearchParam.getQ() == null) || (paramSearchParam.getQ().isEmpty()))
//                && ((paramSearchParam.getRawq() == null) || (paramSearchParam.getRawq().isEmpty())))) {
//            return null;
//        }
//        Calendar localTime = Calendar.getInstance();
//        long localBeginTime = localTime.getTimeInMillis();
//        // 当前模型可搜索的条数
//        AuthUserContext authinfoContext = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
//        A_RoleModelMap modelMap = new A_RoleModelMap();
//        modelMap.setRoleID(authinfoContext.getRoles().getRoleID());
//        modelMap.setModelname(paramSearchParam.getBusiModel());
//        int returnRows = 0;
//        String rowStringreturn = RoleModelMapDao.GetInstance().GetModelByRoleIDModel(db, modelMap).getReturnRows();
//        if (null != rowStringreturn && !("".equals(rowStringreturn))) {
//            returnRows = Integer.valueOf(rowStringreturn);
//        }
//        int start = paramSearchParam.getStart();// 数据开始位置
//        int rows = paramSearchParam.getRows();// 本次搜索的数据条数
//        if (start >= returnRows) {
//            return null;
//        } else if (start + rows > returnRows) {
//            paramSearchParam.setRows(returnRows - start);
//        }
//        Calendar localTime2 = Calendar.getInstance();
//        long localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " check model role step :  "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//
//        localTime = Calendar.getInstance();
//        localBeginTime = localTime.getTimeInMillis();
//
//        // 测试
//        // System.out.println("测试
//        // list："+GetModelFieldDefine(db,"模型组1",paramRequest).size());
//        SearchModelDefine localModelDefine = GetModelDefine(db, paramSearchParam.getBusiModel());
//        if (localModelDefine == null) {
//            return null;
//        }
//        ArrayList<SearchModelFieldDefine> localFieldDefineList = GetModelFieldDefine(db,
//                localModelDefine.getModelDefId());
//        if (localFieldDefineList == null) {
//            return null;
//        }
//
//        // 通过字段名来找搜索字段名的映射
//        HashMap<String, String> localFieldDefineMap = new HashMap<String, String>();
//        String localSearchFieldName;
//        // 字段名-> 字段定义
//        HashMap<String, SearchModelFieldDefine> localFieldDefineMap3 = new HashMap<String, SearchModelFieldDefine>();
//
//        // 显示名 ->字段定义
//        HashMap<String, SearchModelFieldDefine> localFieldDefineMap4 = new HashMap<String, SearchModelFieldDefine>();
//
//        ArrayList<SearchModelFieldDefine> localWildMatchFieldList = new ArrayList<SearchModelFieldDefine>();
//        ArrayList<SearchModelFieldDefine> localDateFieldList = new ArrayList<SearchModelFieldDefine>();
//        // ArrayList < SearchModelFieldDefine > localMainFieldList=new
//        // ArrayList< SearchModelFieldDefine>();
//        HashSet<String> localIdNameSet = new HashSet<String>();
//        for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
//            if (localFieldDefine.getFieldSpec() == 1) {
//                localIdNameSet.add(localFieldDefine.getFieldName());
//            }
//            if (localFieldDefine.getDisplayInGrid() == 1) {
//                // localMainFieldList.add(localFieldDefine);
//            }
//            localFieldDefineMap.put(localFieldDefine.getFieldName(), localFieldDefine.getSearchFieldName());
//            if (localFieldDefine.getWildMatch() > 0) {
//                localWildMatchFieldList.add(localFieldDefine);
//            }
//            if ((localFieldDefine.getFieldSpec() >= 11) && ((localFieldDefine.getFieldSpec() <= 19))) {
//                localDateFieldList.add(localFieldDefine);
//            }
//            localFieldDefineMap3.put(localFieldDefine.getFieldName(), localFieldDefine);
//        }
//        HashMap<String, String> localFieldDefineMap2 = new HashMap<String, String>();
//        for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
//            localSearchFieldName = localFieldDefineMap2.get(localFieldDefine.getFieldDisplayName());
//            if ((localSearchFieldName == null) || (localFieldDefine.getSearchFieldName().endsWith("_tcn"))) {
//                localFieldDefineMap2.put(localFieldDefine.getFieldDisplayName(), localFieldDefine.getSearchFieldName());
//                localFieldDefineMap4.put(localFieldDefine.getFieldDisplayName(), localFieldDefine);
//            }
//        }
//        localTime2 = Calendar.getInstance();
//        localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " load model info step :  "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//
//        localTime = Calendar.getInstance();
//        localBeginTime = localTime.getTimeInMillis();
//
//        SolrQuery parameters = new SolrQuery();
//        SearchFormat localFormat = null;
//        if ((paramSearchParam.getRawq() == null) || (paramSearchParam.getRawq().isEmpty())) {
//            localFormat = SearchFormat.buildFormat(paramSearchParam.getQ(), localFieldDefineMap3, localFieldDefineMap4,
//                    localWildMatchFieldList, localDateFieldList);
//        }
//        SearchFormat localFormat2 = null;
//        if ((paramSearchParam.getOldq() != null) && (!paramSearchParam.getOldq().isEmpty())) {
//            localFormat2 = SearchFormat.buildFormat(paramSearchParam.getOldq(), localFieldDefineMap3,
//                    localFieldDefineMap4, localWildMatchFieldList, localDateFieldList);
//        }
//        parameters.set("ident", "true");
//        parameters.set("fl", "*,score");
//
//        if ((paramSearchParam.getRawq() != null) && (!paramSearchParam.getRawq().isEmpty())) {
//            parameters.set("q", paramSearchParam.getRawq());
//        } else {
//            parameters.set("q", localFormat.searchQ);
//            String localIp = paramRequest.getRemoteAddr();
//            HttpSession localSession = paramRequest.getSession();
//            Object localObject = localSession.getAttribute("AuthInfo");
//            if (localObject != null) {
//
//                String localUser = ((AuthUserContext) localObject).getUsers().getUsername();
//                SearchLogThread.SetLog(paramSearchParam, localIp, localUser);
//            }
//        }
//        parameters.set("start", paramSearchParam.getStart());
//        if (paramSearchParam.getRows() == 0) {
//            parameters.set("rows", 10);
//        } else {
//            parameters.set("rows", paramSearchParam.getRows());
//        }
//        if ((paramSearchParam.getSort() != null) && (!paramSearchParam.getSort().isEmpty())) {
//            parameters.set("sort", localModelDefine.getSearchSort());
//        } else if ((localModelDefine.getSearchSort() != null) && (!localModelDefine.getSearchSort().isEmpty())) {
//            parameters.set("sort", paramSearchParam.getSort());
//        } else {
//            parameters.set("sort", "score desc");
//        }
//        parameters.set("defType", "edismax");
//        // int nMaxMainField=localMainFieldList.size();
//        // int nIndex=0;
//        // String localQfStr="";
//        // for(SearchModelFieldDefine tmpFieldDefine:localMainFieldList)
//        // {
//        // if(nIndex>0)
//        // {
//        // localQfStr+=" + ";
//        // }
//        // localQfStr+=tmpFieldDefine.getSearchFieldName()+"^"+Integer.toString(nMaxMainField-nIndex);
//        // nIndex++;
//        // }
//        // if(nMaxMainField>0)
//        // {
//        // parameters.set("qf", localQfStr);
//        // }
//        ArrayList<SearchStats> localStatsList = new ArrayList<SearchStats>();
//        localStatsList = SearchStats.buildStatsParam(paramSearchParam.getStats(), localFieldDefineMap3,
//                localFieldDefineMap4);
//        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        HashMap<String, SearchStats> localStatsMap = null;
//        if (localStatsList != null) {
//            localStatsMap = new HashMap<String, SearchStats>();
//            parameters.setFacet(true);
//            for (SearchStats tmpStats : localStatsList) {
//                parameters.addFacetField(tmpStats.fieldName);
//                parameters.setFacetPrefix(tmpStats.fieldName, tmpStats.fieldPrefix);
//                parameters.setFacetLimit(10000);
//                parameters.setFacetMinCount(1);
//                parameters.setFacetSort(FacetParams.FACET_SORT_COUNT);
//
//                localStatsMap.put(tmpStats.fieldName, tmpStats);
//            }
//
//        }
//        localTime2 = Calendar.getInstance();
//        localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " save log step :  "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//        localTime = Calendar.getInstance();
//        localBeginTime = localTime.getTimeInMillis();
//
//        String urlString = localModelDefine.getSearchUrl();
//        HttpSolrServer solr = GetSolr(urlString);
//
//        localTime2 = Calendar.getInstance();
//        localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " solr connect step :  "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//
//        QueryResponse response;
//        localTime = Calendar.getInstance();
//        localBeginTime = localTime.getTimeInMillis();
//        try {
//            response = solr.query(parameters);
//            if (response == null) {
//                solr.shutdown();
//                return null;
//            }
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//            solr.shutdown();
//            return null;
//        }
//        localTime2 = Calendar.getInstance();
//        localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " solr step : "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//        SolrDocumentList localDocList = response.getResults();
//        ModelSearchResult localSearchResult = new ModelSearchResult();
//        localSearchResult.docList = new ArrayList<HashMap<String, String>>();
//        localSearchResult.matchFieldList = new ArrayList<ArrayList<MatchFieldItem>>();
//        String localSolrField;
//        String localSearchValue;
//        MatchFieldItem localMatchItem;
//        ArrayList<MatchFieldItem> localMatchValue;
//        HashSet<String> localBlackListSet = (HashSet<String>) paramRequest.getSession().getAttribute("black_list");
//        if (localBlackListSet == null) {
//            localBlackListSet = SearchDao.GetBlackListSet(db);
//            if (localBlackListSet != null) {
//                paramRequest.getSession().setAttribute("black_list", localBlackListSet);
//            }
//        }
//        boolean localAdminRole = false;
//        AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
//        if ((localAuthInfo != null) && (localAuthInfo.getRoles().getRoleName().equals("admin"))) {
//            localAdminRole = true;
//        }
//
//        localTime = Calendar.getInstance();
//        localBeginTime = localTime.getTimeInMillis();
//
//        // 写入搜索的结果 docList
//        for (SolrDocument localDoc : localDocList) {
//            // 对每条记录，检查字段是否包含要搜索的内容
//            int localInsertFlag = 1;
//            HashMap<String, String> localResultValue = new HashMap<String, String>();
//            int localFieldIndex = 0;
//            // 第三次方案(前) 角色模型字段权限验证
//            HashMap<String, String> fieldMap = new HashMap<String, String>();
//            A_Role roleList = ((AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo")).getRoles();
//            A_RoleModelFieldMap modelFieldMap = new A_RoleModelFieldMap();
//            modelFieldMap.setRoleID(roleList.getRoleID());
//            modelFieldMap.setModelname(paramSearchParam.getBusiModel());
//            LinkedList<A_RoleModelFieldMap> modelFieldMapsList = RoleModelFieldMapDao.GetInstance().GetListByRoleID(db,
//                    modelFieldMap);
//            for (A_RoleModelFieldMap _modelField : modelFieldMapsList) {
//                fieldMap.put(_modelField.getFieldname(), _modelField.getFieldname());
//            }
//            String localReplaceStr = null;
//            String localFieldStr = null;
//            localMatchValue = new ArrayList<MatchFieldItem>();
//            localSearchResult.matchFieldList.add(localMatchValue);
//            for (SearchModelFieldDefine localField : localFieldDefineList) {
//                localSolrField = localFieldDefineMap.get(localField.getFieldName());
//                // 判断是否是grid模式，如果是，则过滤一些字段
//                if ((paramSearchParam.getGridView() == 1) && (localField.getDisplayInGrid() == 0)) {
//                    localFieldIndex++;
//                    continue;
//                }
//                if ((localSolrField == null) || (localSolrField.isEmpty())) {
//                    localResultValue.put(localField.getFieldName(), "");
//                } else {
//                    Object localObjectValue = localDoc.get(localSolrField);
//                    if (localObjectValue == null) {
//                        localResultValue.put(localField.getFieldName(), "");
//                        // System.out.println("field
//                        // "+localField.getFieldName()+" not found");
//                    } else {
//                        String localValueStr = localObjectValue.toString();
//                        if ((!localAdminRole) && (localIdNameSet.contains(localField.getFieldName()))
//                                && (localBlackListSet != null) && localBlackListSet.contains(localValueStr)) {
//                            localInsertFlag = 0;
//                            break;
//                        }
//                        if (localObjectValue instanceof java.util.Date) {
//                            localValueStr = localDateFormat.format((java.util.Date) localObjectValue);
//                        }
//                        localFieldStr = localValueStr;
//                        if (localFieldIndex > 0) {
//                            if ((paramRawFormat == 0) && (localFormat != null)
//                                    && ((localField.getFieldSpec() < 1) || (localField.getFieldSpec() > 3))
//                                    && ((((localFormat.searchKeyFieldSet != null)
//                                    && (!localFormat.searchKeyFieldSet.isEmpty())))
//                                    || (((localFormat.searchKeyFieldMap != null)
//                                    && (!localFormat.searchKeyFieldMap.isEmpty()))))) {
//                                if (((localFormat.searchKeyFieldSet != null)
//                                        && (!localFormat.searchKeyFieldSet.isEmpty()))) {
//                                    for (String localKeyStr : localFormat.searchKeyFieldSet) {
//                                        if (!SearchFormat.isWild(localKeyStr)) {
//                                            if (!localValueStr.startsWith("http:") && !localValueStr.startsWith("HTTP:")
//                                                    && !localValueStr.startsWith("ftp:")
//                                                    && !localValueStr.startsWith("FTP:")) {
//                                                localValueStr = localValueStr.replaceAll(localKeyStr,
//                                                        "<label style=\"color: #FF0000\">" + localKeyStr + "</label>");
//                                            }
//                                        }
//                                    }
//                                }
//                                if (((localFormat.searchKeyFieldMap != null)
//                                        && (!localFormat.searchKeyFieldMap.isEmpty()))) {
//                                    localReplaceStr = localFormat.searchKeyFieldMap.get(localField.getFieldName());
//                                    if ((localReplaceStr != null) && (!localReplaceStr.isEmpty())) {
//                                        if (!localValueStr.startsWith("http:") && !localValueStr.startsWith("HTTP:")
//                                                && !localValueStr.startsWith("ftp:")
//                                                && !localValueStr.startsWith("FTP:")) {
//                                            localValueStr = localValueStr.replaceAll(localReplaceStr,
//                                                    "<label style=\"color: #FF0000\">" + localReplaceStr + "</label>");
//                                        }
//
//                                    }
//                                }
//
//                            }
//                            // ---------------------------------
//                            if (paramSearchParam.getOldq() != null && !paramSearchParam.getOldq().isEmpty()) {
//
//                                if ((localFormat2 != null)
//                                        && ((localField.getFieldSpec() < 1) || (localField.getFieldSpec() > 3))
//                                        && ((((localFormat2.searchKeyFieldSet != null)
//                                        && (!localFormat2.searchKeyFieldSet.isEmpty())))
//                                        || (((localFormat2.searchKeyFieldMap != null)
//                                        && (!localFormat2.searchKeyFieldMap.isEmpty()))))) {
//                                    if (((localFormat2.searchKeyFieldSet != null)
//                                            && (!localFormat2.searchKeyFieldSet.isEmpty()))) {
//                                        for (String localKeyStr : localFormat2.searchKeyFieldSet) {
//                                            if (!SearchFormat.isWild(localKeyStr)) {
//                                                if (!localValueStr.startsWith("http:")
//                                                        && !localValueStr.startsWith("HTTP:")
//                                                        && !localValueStr.startsWith("ftp:")
//                                                        && !localValueStr.startsWith("FTP:")) {
//                                                    localValueStr = localValueStr.replaceAll(localKeyStr,
//                                                            "<label style=\"color: #FF0000\">" + localKeyStr
//                                                            + "</label>");
//                                                }
//                                            }
//                                        }
//                                    }
//                                    if (((localFormat2.searchKeyFieldMap != null)
//                                            && (!localFormat2.searchKeyFieldMap.isEmpty()))) {
//                                        localReplaceStr = localFormat2.searchKeyFieldMap.get(localField.getFieldName());
//                                        if ((localReplaceStr != null) && (!localReplaceStr.isEmpty())) {
//                                            if (!localValueStr.startsWith("http:") && !localValueStr.startsWith("HTTP:")
//                                                    && !localValueStr.startsWith("ftp:")
//                                                    && !localValueStr.startsWith("FTP:")) {
//                                                localValueStr = localValueStr.replaceAll(localReplaceStr,
//                                                        "<label style=\"color: #FF0000\">" + localReplaceStr
//                                                        + "</label>");
//                                            }
//
//                                        }
//                                    }
//
//                                }
//                            }
//                            // ------------------------------------
//                        }
//                        // 处理匹配字段
//                        if (localFieldIndex > 0) {
//                            if ((paramRawFormat == 0) && (localFormat != null)
//                                    && ((((localFormat.searchKeyFieldSet != null)
//                                    && (!localFormat.searchKeyFieldSet.isEmpty())))
//                                    || (((localFormat.searchKeyFieldMap != null)
//                                    && (!localFormat.searchKeyFieldMap.isEmpty()))))) {
//                                if (((localFormat.searchKeyFieldSet != null)
//                                        && (!localFormat.searchKeyFieldSet.isEmpty()))) {
//                                    for (String localKeyStr : localFormat.searchKeyFieldSet) {
//                                        if (!SearchFormat.isWild(localKeyStr)) {
//                                            if (!localFieldStr.startsWith("http:") && !localFieldStr.startsWith("HTTP:")
//                                                    && !localFieldStr.startsWith("ftp:")
//                                                    && !localFieldStr.startsWith("FTP:")) {
//                                                if (localFieldStr.indexOf(localKeyStr) != -1) {
//                                                    localMatchItem = new MatchFieldItem();
//                                                    localMatchItem.setFieldName(localField.getFieldName());
//                                                    localMatchItem.setSearchKey(localKeyStr);
//                                                    localMatchItem.setMatchValue(localFieldStr.replaceAll(localKeyStr,
//                                                            "<label style=\"color: #FF0000\">" + localKeyStr
//                                                            + "</label>"));
//                                                    localMatchValue.add(localMatchItem);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
//                            }
//                            // ---------------------------------
//                            if (paramSearchParam.getOldq() != null && !paramSearchParam.getOldq().isEmpty()) {
//
//                                if ((localFormat2 != null) && ((((localFormat2.searchKeyFieldSet != null)
//                                        && (!localFormat2.searchKeyFieldSet.isEmpty())))
//                                        || (((localFormat2.searchKeyFieldMap != null)
//                                        && (!localFormat2.searchKeyFieldMap.isEmpty()))))) {
//                                    if (((localFormat2.searchKeyFieldSet != null)
//                                            && (!localFormat2.searchKeyFieldSet.isEmpty()))) {
//                                        for (String localKeyStr : localFormat2.searchKeyFieldSet) {
//                                            if (!SearchFormat.isWild(localKeyStr)) {
//                                                if (!localFieldStr.startsWith("http:")
//                                                        && !localFieldStr.startsWith("HTTP:")
//                                                        && !localFieldStr.startsWith("ftp:")
//                                                        && !localFieldStr.startsWith("FTP:")) {
//                                                    if (localFieldStr.indexOf(localKeyStr) != -1) {
//                                                        localMatchItem = new MatchFieldItem();
//                                                        localMatchItem.setFieldName(localField.getFieldName());
//                                                        localMatchItem.setSearchKey(localKeyStr);
//                                                        localMatchItem.setMatchValue(localFieldStr.replaceAll(
//                                                                localKeyStr, "<label style=\"color: #FF0000\">"
//                                                                + localKeyStr + "</label>"));
//                                                        localMatchValue.add(localMatchItem);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                }
//                            }
//                            // ------------------------------------
//                        }
//
//                        // 第三次方案(后) 角色模型字段权限验证
//                        String str = fieldMap.get(localField.getFieldName());
//                        if (str != null && str.compareToIgnoreCase("") != 0) {
//                            localResultValue.put(localField.getFieldName(), localValueStr);
//                        } else {
//                            localResultValue.put(localField.getFieldName(), "*");
//                        }
//
//                        // 第二次方案 有角色模型字段访问权限验证 会降低搜索的效率
//                        // 检测当前用户拥有的角色是否对模型字段有访问权限 无权限则使用*代替模型字段的值 角色模型字段权限验证
//                        // ArrayList<AuthRoleInfo> roleList = ((AuthInfo)
//                        // paramRequest.getSession().getAttribute("AuthInfo")).getAuthRoleList();
//                        // int i = 0;
//                        // i = AuthService.checkRoleAccess(db, roleList,
//                        // paramSearchParam.getBusiModel(),
//                        // localField.getFieldName());
//                        // if(i > 0){
//                        // localResultValue.put(localField.getFieldName(),
//                        // localValueStr);
//                        // }else{
//                        // localResultValue.put(localField.getFieldName(), "*");
//                        // }
//                        // 第一次的方案 无角色模型字段访问权限验证
//                        // localResultValue.put(localField.getFieldName(),
//                        // localValueStr);
//                    }
//                }
//                localFieldIndex++;
//            }
//            if (localInsertFlag != 0) {
//                localSearchResult.docList.add(localResultValue);
//            }
//        }
//        localTime2 = Calendar.getInstance();
//        localEndTime = localTime2.getTimeInMillis();
//        System.out.println("search " + paramSearchParam.getBusiModel() + " filter step : "
//                + Long.toString(localEndTime - localBeginTime + 1) + " ms ");
//
//        // 写入字段信息 modelFieldInfoList
//        localSearchResult.modelFieldInfoList = new ArrayList<ModelFieldInfo>();
//        for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
//            // 过滤grid模式下的一些字段
//            if ((paramSearchParam.getGridView() == 1) && (localFieldDefine.getDisplayInGrid() == 0)) {
//                continue;
//            }
//            ModelFieldInfo localFieldInfo = new ModelFieldInfo();
//            localFieldInfo.fieldName = localFieldDefine.getFieldName();
//            localFieldInfo.fieldDisplayName = localFieldDefine.getFieldDisplayName();// 集合字段显示名称
//            localFieldInfo.displayInGrid = localFieldDefine.getDisplayInGrid();
//            localFieldInfo.displayWidth = localFieldDefine.getDisplayWidth();
//            localFieldInfo.fieldSpec = localFieldDefine.getFieldSpec();
//            localSearchResult.modelFieldInfoList.add(localFieldInfo);
//        }
//        localSearchResult.start = localDocList.getStart();
//        localSearchResult.numFound = localDocList.getNumFound();
//        localSearchResult.rows = localDocList.size();
//        if (localSearchResult.rows > localSearchResult.docList.size()) {
//            localSearchResult.rows = localSearchResult.docList.size();
//            localSearchResult.numFound = localSearchResult.rows + localDocList.getStart();
//        }
//        List<FacetField> localFacetList = response.getFacetFields();
//        if ((localFacetList != null) && (!localFacetList.isEmpty())) {
//            localSearchResult.fieldStatsList = new ArrayList<FieldStats>();
//            for (FacetField tmpField : localFacetList) {
//                SearchStats localSearchStats = localStatsMap.get(tmpField.getName());
//                if (localSearchStats == null) {
//                    continue;
//                }
//
//                FieldStats tmpFieldStats = new FieldStats();
//                tmpFieldStats.fieldName = tmpField.getName();
//                List<Count> tmpCountList = tmpField.getValues();
//                if ((tmpCountList == null) || (tmpCountList.isEmpty())) {
//                    continue;
//                }
//                tmpFieldStats.fieldStatsDataMap = new TreeMap<String, Long>();
//                int tmpValueOff = 0;
//                if ((localSearchStats.statsValueLen > 0) && (localSearchStats.fieldPrefix != null)) {
//                    tmpValueOff = localSearchStats.fieldPrefix.length();
//                }
//                String localNewKey;
//                Long localNewValue;
//                int localOff;
//                for (Count tmpCount : tmpCountList) {
//                    if (tmpValueOff > 0) {
//                        localNewKey = tmpCount.getName();
//                        if ((localNewKey != null) && (localNewKey.length() >= tmpValueOff)) {
//                            localOff = tmpValueOff + localSearchStats.statsValueLen;
//                            if (localOff > localNewKey.length()) {
//                                localOff = localNewKey.length();
//                            }
//
//                            localNewKey = localNewKey.substring(0, localOff);
//                            localNewValue = tmpFieldStats.fieldStatsDataMap.get(localNewKey);
//                            if (localNewValue == null) {
//                                tmpFieldStats.fieldStatsDataMap.put(localNewKey, tmpCount.getCount());
//                            } else {
//                                localNewValue = (long) localNewValue + tmpCount.getCount();
//                                tmpFieldStats.fieldStatsDataMap.put(localNewKey, localNewValue);
//                            }
//                        }
//                    } else {
//                        tmpFieldStats.fieldStatsDataMap.put(tmpCount.getName(), tmpCount.getCount());
//                    }
//                }
//                localSearchResult.fieldStatsList.add(tmpFieldStats);
//            }
//        }
//        PutSolr(urlString, solr);
//        return localSearchResult;
//    }

    public static String ModelSearchResultToString(ModelSearchResult paramSearchResult){

        /*
         * 格式 Rows,数据，是个数组，依次是key/value Fields,也是个数组，保存字段信息 start,rows,numFound
         */
        ArrayNode localRowsNode = JsonNodeFactory.instance.arrayNode();
        for (HashMap<String, String> localMapValue : paramSearchResult.docList) {
            ObjectNode localNode = JsonNodeFactory.instance.objectNode();
            for (Entry<String, String> localEntry : localMapValue.entrySet()) {
                localNode.put(localEntry.getKey(), localEntry.getValue());
            }
            localRowsNode.add(localNode);
        }
        ArrayNode localFieldsNode = JsonNodeFactory.instance.arrayNode();
        for (ModelFieldInfo localFieldInfo : paramSearchResult.modelFieldInfoList) {
            ObjectNode localNode = JsonNodeFactory.instance.objectNode();
            localNode.put("fieldName", localFieldInfo.fieldName);
            if ((localFieldInfo.fieldDisplayName == null) || (localFieldInfo.fieldDisplayName.isEmpty())) {
                localNode.put("fieldDisplayName", localFieldInfo.fieldName);
            } else {
                localNode.put("fieldDisplayName", localFieldInfo.fieldDisplayName);
            }
            localNode.put("displayInGrid", localFieldInfo.displayInGrid);
            if (localFieldInfo.displayWidth != 0) {
                localNode.put("displayWidth", localFieldInfo.displayWidth);
            }
            if (localFieldInfo.fieldSpec != 0) {
                localNode.put("fieldSpec", localFieldInfo.fieldSpec);
            }
            localFieldsNode.add(localNode);
        }

        ObjectNode localResultNode = JsonNodeFactory.instance.objectNode();
        //ArrayNode localIdNode = JsonNodeFactory.instance.arrayNode();
//        if (paramIdList != null) {
//            for (SearchModelIdDefine localIdDefine : paramIdList) {
//                ObjectNode localNode = JsonNodeFactory.instance.objectNode();
//                localNode.put("idFieldName", localIdDefine.getIdFieldName());
//                localNode.put("nameFieldName", localIdDefine.getNameFieldName());
//                localIdNode.add(localNode);
//            }
//            localResultNode.put("idFields", localIdNode);
//        }

        localResultNode.put("Rows", localRowsNode);
        localResultNode.put("Fields", localFieldsNode);
        localResultNode.put("start", paramSearchResult.start);
        localResultNode.put("rows", paramSearchResult.rows);
        localResultNode.put("numFound", paramSearchResult.numFound);
        if ((paramSearchResult.fieldStatsList != null) && (!paramSearchResult.fieldStatsList.isEmpty())) {
            ArrayNode localStatsListNode = JsonNodeFactory.instance.arrayNode();
            for (FieldStats tmpStats : paramSearchResult.fieldStatsList) {
                ObjectNode localStatsNode = JsonNodeFactory.instance.objectNode();
                localStatsNode.put("fieldName", tmpStats.fieldName);
                ArrayNode localValueListNode = JsonNodeFactory.instance.arrayNode();
                tmpStats.fieldStatsDataList = new ArrayList<ResultStats>();
                for (Map.Entry<String, Long> tmpEntry : tmpStats.fieldStatsDataMap.entrySet()) {
                    ResultStats tmpResultStats = new ResultStats();
                    tmpResultStats.name = tmpEntry.getKey();
                    tmpResultStats.total = tmpEntry.getValue();
                    tmpStats.fieldStatsDataList.add(tmpResultStats);
                }
                Collections.sort(tmpStats.fieldStatsDataList, new ResultStatsComparator());

                for (ResultStats tmpResultStats : tmpStats.fieldStatsDataList) {
                    ObjectNode localValueNode = JsonNodeFactory.instance.objectNode();
                    localValueNode.put("fieldValue", tmpResultStats.name);
                    localValueNode.put("statsValue", tmpResultStats.total);
                    localValueListNode.add(localValueNode);
                }
                localStatsNode.put("statsList", localValueListNode);
                localStatsListNode.add(localStatsNode);
            }
            localResultNode.put("Stats", localStatsListNode);
        }

        if ((paramSearchResult.matchFieldList != null) && (!paramSearchResult.matchFieldList.isEmpty())) {
            ArrayNode localValueNode = JsonNodeFactory.instance.arrayNode();
            for (ArrayList<MatchFieldItem> tmpValue : paramSearchResult.matchFieldList) {
                ArrayNode localMatchValueNode = JsonNodeFactory.instance.arrayNode();
                for (MatchFieldItem tmpFieldItem : tmpValue) {
                    ObjectNode localFieldNode = JsonNodeFactory.instance.objectNode();
                    localFieldNode.put("searchKey", tmpFieldItem.getSearchKey());
                    localFieldNode.put("fieldName", tmpFieldItem.getFieldName());
                    localFieldNode.put("matchValue", tmpFieldItem.getMatchValue());
                    localMatchValueNode.add(localFieldNode);
                }
                localValueNode.add(localMatchValueNode);
            }
            localResultNode.put("MatchRows", localValueNode);
        }

        return localResultNode.toString();
    }

    public static ObjectNode ModelDefineListToJsonObject(ArrayList<SearchModelDefine> paramModelDefineList) {
        /*
         * 格式 Rows,数据，是个数组,模型定义列表
         */
        ArrayNode localDefineListNode = JsonNodeFactory.instance.arrayNode();
        for (SearchModelDefine localModelDefine : paramModelDefineList) {
            ObjectNode localNode = JsonNodeFactory.instance.objectNode();
            localNode.put("modelName", localModelDefine.getModelName());
            localNode.put("modelDisplayName", localModelDefine.getModelDisplayName());
            localNode.put("selectedDefault", localModelDefine.getSelectedDefault());
            localDefineListNode.add(localNode);
        }
        ObjectNode localResultNode = JsonNodeFactory.instance.objectNode();
        localResultNode.put("Rows", localDefineListNode);
        return localResultNode;
    }

    public static ObjectNode ModelFieldDefineListToJsonObject(
            ArrayList<SearchModelFieldDefine> paramModelFieldDefineList) {
        /*
         * 格式 Rows,数据，是个数组,模型字段定义列表
         */
        ArrayNode localDefineListNode = JsonNodeFactory.instance.arrayNode();
        for (SearchModelFieldDefine localModelFiledDefine : paramModelFieldDefineList) {
            ObjectNode localNode = JsonNodeFactory.instance.objectNode();
            localNode.put("fieldName", localModelFiledDefine.getFieldName());
            localNode.put("searchFieldName", localModelFiledDefine.getSearchFieldName());
            localNode.put("fieldDisplayName", localModelFiledDefine.getFieldDisplayName());
            localNode.put("wildMatch", localModelFiledDefine.getWildMatch());
            localNode.put("fieldSpec", localModelFiledDefine.getFieldSpec());
            localDefineListNode.add(localNode);
        }
        ObjectNode localResultNode = JsonNodeFactory.instance.objectNode();
        localResultNode.put("Rows", localDefineListNode);
        return localResultNode;
    }

    public static ObjectNode ModelGroupDefineListToJsonObject(
            ArrayList<SearchModelGroupDefine> paramModelGroupDefineList) {
        /*
         * 格式 Rows,数据，是个数组,模型定义列表
         */
        if (paramModelGroupDefineList == null) {
            return null;
        }
        ArrayNode localModelGroupDefineListNode = JsonNodeFactory.instance.arrayNode();
        for (SearchModelGroupDefine localModelGroupDefine : paramModelGroupDefineList) {
            ObjectNode localModelGroupNode = JsonNodeFactory.instance.objectNode();
            localModelGroupNode.put("groupName", localModelGroupDefine.getModelName());
            localModelGroupNode.put("groupDisplayName", localModelGroupDefine.getModelGroupDisplayName());
            localModelGroupNode.put("selectedDefault", localModelGroupDefine.getSelectedDefault());
            ArrayNode localModelDefineListNode = JsonNodeFactory.instance.arrayNode();
            if (null != localModelGroupDefine.getModelList()) {
                for (SearchModelDefine localModelDefine : localModelGroupDefine.getModelList()) {
                    ObjectNode localModelNode = JsonNodeFactory.instance.objectNode();
                    localModelNode.put("modelName", localModelDefine.getModelName());
                    localModelNode.put("modelDisplayName", localModelDefine.getModelDisplayName());
                    localModelNode.put("selectedDefault", localModelDefine.getSelectedDefault());
                    localModelDefineListNode.add(localModelNode);
                }
            }
            localModelGroupNode.put("groupList", localModelDefineListNode);
            ArrayNode localKeyListNode = JsonNodeFactory.instance.arrayNode();
            if (null != localModelGroupDefine.getKeyList()) {
                for (ModelGroupKeyMap localKey : localModelGroupDefine.getKeyList()) {
                    ObjectNode localModelNode = JsonNodeFactory.instance.objectNode();
                    localModelNode.put("keyName", localKey.keyName);
                    localModelNode.put("keyDisplayName", localKey.keyDisplayName);
                    ArrayNode localKeyeListNode = JsonNodeFactory.instance.arrayNode();
                    if (null != localKey.keyModelList) {
                        for (KeyModelMap localKeyModel : localKey.keyModelList) {
                            ObjectNode localModelNode1 = JsonNodeFactory.instance.objectNode();
                            localModelNode1.put("keyModelName", localKeyModel.modelName);
                            localModelNode1.put("keyModelDisplayName", localKeyModel.modelDisplayName);
                            localModelNode1.put("keyFieldName", localKeyModel.fieldName);
                            localKeyeListNode.add(localModelNode1);
                        }
                    }
                    localModelNode.put("keyModelList", localKeyeListNode);
                    localKeyListNode.add(localModelNode);
                }
            }
            localModelGroupNode.put("keyList", localKeyListNode);
            localModelGroupDefineListNode.add(localModelGroupNode);
        }
        ObjectNode localResultNode = JsonNodeFactory.instance.objectNode();
        localResultNode.put("Rows", localModelGroupDefineListNode);
        return localResultNode;
    }

    public static String ModelDefineListToString(ArrayList<SearchModelDefine> paramModelDefineList) {
        // 把模型定义数据转化为json格式的字符串
        // 只提供数据
        ObjectNode localNode = ModelDefineListToJsonObject(paramModelDefineList);
        if (localNode == null) {
            return null;
        }
        // 转换为字符串输出
        return localNode.toString();
    }

    public static String ModelGroupDefineListToString(ArrayList<SearchModelGroupDefine> paramModelGroupDefineList) {
        // 把模型定义数据转化为json格式的字符串
        // 只提供数据
        ObjectNode localNode = ModelGroupDefineListToJsonObject(paramModelGroupDefineList);
        if (localNode == null) {
            return null;
        }
        // 转换为字符串输出
        return localNode.toString();
    }

    public static String ModelFieldDefineListToString(ArrayList<SearchModelFieldDefine> paramModelFieldDefineList) {
        // 把模型定义数据转化为json格式的字符串
        // 只提供数据
        ObjectNode localNode = ModelFieldDefineListToJsonObject(paramModelFieldDefineList);
        if (localNode == null) {
            return null;
        }
        // 转换为字符串输出
        return localNode.toString();
    }

    public static ArrayList<SearchModel> getSearchModels() {
        return SearchDao.GetModelDefineList();
    }

    public static ArrayList<SearchModelIdDefine> GetModelIdDefineList(com.mongodb.DB db, int paramModelDefId) {
        return SearchDao.GetModelIdDefineList(db, paramModelDefId);
    }

    public static void saveSearchAccessLog(com.mongodb.DB db, SearchParam paramSearch, String paramIp,
            String paramUser) {
        /*        
         */
        AccessLog localAccessLog = new AccessLog();
        localAccessLog.setAccessLogId(0);
        localAccessLog.setFuncName("search");
        localAccessLog.setIpAddr(paramIp);
        localAccessLog.setUserName(paramUser);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String localTimeStr = localDateFormat.format(Calendar.getInstance(TimeZone.getTimeZone("GMT+08")).getTime());
        localAccessLog.setAccessTime(localTimeStr);
        ArrayList<AccessItemParam> localAccessItemList = new ArrayList<AccessItemParam>();
        AccessItemParam localAccessItem = new AccessItemParam();
        localAccessItem.setAccessLogId(0);
        localAccessItem.setItemName(paramSearch.getBusiModel());
        localAccessItem.setItemParamName("q");
        localAccessItem.setItemParamValue(paramSearch.getQ());
        localAccessItemList.add(localAccessItem);
        AccessLogService.AddAccessLog(db, localAccessLog, localAccessItemList);
    }

    public static SearchBulkResult DoSearchBulk(com.mongodb.DB db, SearchBulkParam paramSearchBulkParam,
            HttpServletRequest paramRequest) {
        if ((paramSearchBulkParam == null) || (paramSearchBulkParam.getSearchItemList() == null)
                || (paramSearchBulkParam.getSearchItemList().isEmpty())) {
            return null;
        }
        SearchBulkResult localBulkResult = new SearchBulkResult();
        localBulkResult.setBulkResultItem(new ArrayList<BulkResultItem>());
        for (SearchItem tmpItem : paramSearchBulkParam.getSearchItemList()) {
            SearchModelDefine localModelDefine = GetModelDefine(db, tmpItem.getBusiModel());
            if (localModelDefine == null) {
                continue;
            }
            if ((tmpItem.getQ() == null) || (tmpItem.getQ().isEmpty())) {
            }
            ArrayList<SearchModelFieldDefine> localFieldDefineList = GetModelFieldDefine(db,
                    localModelDefine.getModelDefId());
            if (localFieldDefineList == null) {
                continue;
            }

            // 通过字段名来找搜索字段名的映射
            HashMap<String, String> localFieldDefineMap = new HashMap<String, String>();
            String localSearchFieldName;
            // 字段名-> 字段定义
            HashMap<String, SearchModelFieldDefine> localFieldDefineMap3 = new HashMap<String, SearchModelFieldDefine>();

            // 显示名 ->字段定义
            HashMap<String, SearchModelFieldDefine> localFieldDefineMap4 = new HashMap<String, SearchModelFieldDefine>();

            ArrayList<SearchModelFieldDefine> localWildMatchFieldList = new ArrayList<SearchModelFieldDefine>();
            ArrayList<SearchModelFieldDefine> localDateFieldList = new ArrayList<SearchModelFieldDefine>();
            HashSet<String> localIdNameSet = new HashSet<String>();
            for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
                if (localFieldDefine.getFieldSpec() == 1) {
                    localIdNameSet.add(localFieldDefine.getFieldName());
                }
                localFieldDefineMap.put(localFieldDefine.getFieldName(), localFieldDefine.getSearchFieldName());
                if (localFieldDefine.getWildMatch() > 0) {
                    localWildMatchFieldList.add(localFieldDefine);
                }
                if ((localFieldDefine.getFieldSpec() >= 11) && ((localFieldDefine.getFieldSpec() <= 19))) {
                    localDateFieldList.add(localFieldDefine);
                }
                localFieldDefineMap3.put(localFieldDefine.getFieldName(), localFieldDefine);
            }
            HashMap<String, String> localFieldDefineMap2 = new HashMap<String, String>();
            for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
                localSearchFieldName = localFieldDefineMap2.get(localFieldDefine.getFieldDisplayName());
                if ((localSearchFieldName == null) || (localFieldDefine.getSearchFieldName().endsWith("_tcn"))) {
                    localFieldDefineMap2.put(localFieldDefine.getFieldDisplayName(),
                            localFieldDefine.getSearchFieldName());
                    localFieldDefineMap4.put(localFieldDefine.getFieldDisplayName(), localFieldDefine);
                }
            }
            SearchFormat localFormat = null;
            localFormat = SearchFormat.buildFormat(tmpItem.getQ(), localFieldDefineMap3, localFieldDefineMap4,
                    localWildMatchFieldList, localDateFieldList);
            if (localFormat == null) {
                continue;
            }

            String urlString = localModelDefine.getSearchUrl();
            HttpSolrServer solr = GetSolr(urlString);
            SolrQuery parameters = new SolrQuery();
            parameters.set("q", localFormat.searchQ);
            parameters.set("start", (int) 0);
            parameters.set("rows", (int) 10);
            parameters.set("defType", "edismax");

            QueryResponse response;
            try {
                response = solr.query(parameters);
                if (response == null) {
                    solr.shutdown();
                    continue;
                }
            } catch (SolrServerException e) {
                e.printStackTrace();
                solr.shutdown();
                continue;
            }
            SolrDocumentList localDocList = response.getResults();
            BulkResultItem localResultItem = new BulkResultItem();
            localResultItem.setBusiModel(tmpItem.getBusiModel());
            localResultItem.setTotalNum(localDocList.getNumFound());
            localResultItem.setModelIndex(tmpItem.getModelIndex());
            localBulkResult.getBulkResultItem().add(localResultItem);
            PutSolr(urlString, solr);
        }
        if (localBulkResult.getBulkResultItem().isEmpty()) {
            return null;
        }
        return localBulkResult;
    }

    public static String BulkResultToString(SearchBulkResult paramSearchBulkResult) {
        /*
         * 格式 Rows,数据，是个数组，依次是key/value busiModel totalNum
         */

        ObjectNode localResultNode = JsonNodeFactory.instance.objectNode();
        if ((paramSearchBulkResult.getBulkResultItem() != null)
                && (!paramSearchBulkResult.getBulkResultItem().isEmpty())) {
            ArrayNode localItemListNode = JsonNodeFactory.instance.arrayNode();
            for (BulkResultItem tmpItem : paramSearchBulkResult.getBulkResultItem()) {
                ObjectNode localItemNode = JsonNodeFactory.instance.objectNode();
                localItemNode.put("busiModel", tmpItem.getBusiModel());
                localItemNode.put("totalNum", tmpItem.getTotalNum());
                localItemNode.put("modelIndex", tmpItem.getModelIndex());
                localItemListNode.add(localItemNode);
            }
            localResultNode.put("Rows", localItemListNode);
        }
        return localResultNode.toString();
    }

    public static JsonNode getBusiNameByField(com.mongodb.DB db, String strBody) {
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode(); // 根节点
        if (strBody == null) {
            rootNode.put("resultCode", -1);
            System.out.println(rootNode);
            return rootNode;
        }
        JsonNode requestRootNode = null;
        ObjectMapper localMapper = new ObjectMapper();
        try {
            requestRootNode = localMapper.readTree(strBody);
            String queryString = requestRootNode.get("fieldName").getTextValue().toUpperCase();
            JsonNode resultNode = SearchDao.getBusiNameByField(db, queryString);
            rootNode.put("resultCode", 1);
            rootNode.put("fieldInBusi", resultNode);
            return rootNode;
        } catch (Exception ex) {
            rootNode.put("resultCode", -1);
            Logger.getLogger(SearchService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return rootNode;
        }
    }
}
