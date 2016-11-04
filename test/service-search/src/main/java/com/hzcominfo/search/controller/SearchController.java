/*
 * 专门提供搜索服务的，返回一个视图，并且捆绑属性
 */
package com.hzcominfo.search.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hzcominfo.search.pojo.SearchModel;
import com.hzcominfo.search.pojo.SearchModelGroup;
import com.hzcominfo.search.result.ModelSearchResult;
import com.hzcominfo.search.service.SearchBulkParam;
import com.hzcominfo.search.service.SearchBulkResult;
import com.hzcominfo.search.service.SearchModelFieldDefine;
import com.hzcominfo.search.service.SearchInput;
import com.hzcominfo.search.service.SearchService;
import com.hzcominfo.search.service.SearchService.SolrQueryHandler;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author xzh
 */
@Controller
public class SearchController {
//
//    @RequestMapping(value = "searchview", method = RequestMethod.POST)
//    protected ModelAndView handleViewRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody SearchInput input) throws Exception {
//        // url参数
//        // busiModel,必须提供，业务模型，指出是搜索那种业务应用，通常就是指哪个core。
//        // gridView，是否是grid视图，如果是1，那么则返回的字段不是完整的字段。缺省为0，表示发送完整的字段
//        // viewName,视图名字，缺省使用内部的视图名，通常就是业务模型的名称。
//        // q,必须提供，搜索的关键字
//        // start,缺省为0，返回的结果从偏移量多少开始。
//        // rows,缺省为10，本次查询返回多少条记录。
//        // sort,可以没有，按哪个字段排序.
//
//        SearchModel model = SearchService.getModel(input.getBusiModel());
//        if (model == null) {
//            return null;
//        }
//        ModelSearchResult localSearchResult = SearchService.DoSearch(input, request, 0);
//        if (localSearchResult == null) {
//            return null;
//        }
//        ModelAndView localMav = new ModelAndView();
//        String localViewName = input.getViewName();
//        if ((localViewName == null) || (localViewName.isEmpty())) {
//            localMav.setViewName(model.getViewName());
//        } else {
//            localMav.setViewName(localViewName);
//        }
//        localMav.addObject("model_search_result", localSearchResult);
////        ArrayList<SearchModelIdDefine> localIdList = SearchService.GetModelIdDefineList(db,
////                localModelDefine.getModelDefId());
////
////        if (localIdList != null) {
////            ArrayList<ModelIdField> localFieldList = new ArrayList<ModelIdField>();
////            for (SearchModelIdDefine localIdDefine : localIdList) {
////                ModelIdField localField = new ModelIdField();
////                localField.idFieldName = localIdDefine.getIdFieldName();
////                localField.nameFieldName = localIdDefine.getNameFieldName();
////                localFieldList.add(localField);
////            }
////            localMav.addObject("id_fields", localFieldList);
////        }
//
//        return localMav;
//    }

    /**
     *
     * @param request
     * @param response
     * @param input
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    protected ModelSearchResult search(HttpServletRequest request, HttpServletResponse response, @RequestBody SearchInput input) throws Exception {
        // url参数
        // busiModel,必须提供，业务模型，指出是搜索那种业务应用，通常就是指哪个core。
        // gridView，是否是grid视图，如果是1，那么则返回的字段不是完整的字段。缺省为0，表示发送完整的字段
        // viewName,视图名字，缺省使用内部的视图名，通常就是业务模型的名称。
        // q,必须提供，搜索的关键字
        // start,缺省为0，返回的结果从偏移量多少开始。
        // rows,缺省为10，本次查询返回多少条记录。
        // sort,可以没有，按哪个字段排序.

        String localValue = input.getBusiModel();
        if ((localValue == null) || (localValue.isEmpty())) {
            return null;
        }

        localValue = input.getQ();
        String localRawValue = input.getRawq();
        if (localRawValue != null) {
            // localRawValue=URLDecoder.decode(localRawValue,"UTF-8");
        }

        if ((localValue == null) && (localRawValue == null)) {
            return null;
        }

        localValue = request.getParameter("rawformat");
        final int localRawFormat = localValue != null ? Integer.parseInt(localValue) : 0;

        ModelSearchResult searchResult = null;
        try {
            searchResult = SearchService.DoSearch(input, request, localRawFormat, (SolrQueryHandler[]) request.getAttribute("solrHandlers"));
//            if (localSearchResult == null) {
//                return null;
//            }
//            String result = SearchService.ModelSearchResultToString(localSearchResult);
//
//            response.setContentType("text/html;charset=UTF-8");
//            response.getWriter().write(result);
        } catch (Exception e) {
            throw e;
        }

        return searchResult;
    }

    /**
     *
     * @return @throws Exception
     */
    @RequestMapping(value = "getSearchModels", method = RequestMethod.POST)
    @ResponseBody
    protected ArrayList<SearchModel> getSearchModels() throws Exception {
        return SearchService.getSearchModels();
    }

    /**
     *
     * @return @throws Exception
     */
    @RequestMapping(value = "getSearchModelGroups", method = RequestMethod.POST)
    @ResponseBody
    protected List<SearchModelGroup> getSearchModelGroups() throws Exception {
        return SearchService.getSearchModelGroups();
    }

    @RequestMapping("/modelFieldQuery.htm")
    @ResponseBody
    protected List<SearchModelFieldDefine> handleModelFieldQueryRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<SearchModelFieldDefine> modelFieldList = SearchService.GetModelFieldDefine(db, request.getParameter("modelGroup"), request);
        // ArrayList <SearchModelGroupDefine> localModelGroupDefineList =
        // SearchService.GetModelGroupDefine(db);

        //SearchService.ModelFieldDefineListToString(modelFieldList)
        return modelFieldList;
    }

    @RequestMapping(value = "searchBulk", method = RequestMethod.POST)
    protected void searchBulk(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) throws Exception {
        // 入口，json数据
        // 返回json数据

        long localBeginTime = System.currentTimeMillis();

        SearchBulkParam localBulkParam = SearchBulkParam.StringToSearchBulkparam(body);
        if (localBulkParam == null) {
            return;
        }

        // ------------------------------------------
        SearchBulkResult localSearchResult = SearchService.DoSearchBulk(db, localBulkParam, request);

        if (localSearchResult == null) {
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        String localResultStr = SearchService.BulkResultToString(localSearchResult);
        response.getWriter().write(localResultStr);

        System.out.println("SearchBulk  return in  " + Long.toString(System.currentTimeMillis() - localBeginTime + 1) + " ms ");
    }

    @RequestMapping("/getBusiNameByField.htm")
    protected void handleQueryGABInfoServiceRequest(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String body) throws Exception {

        JsonNode resultNode = SearchService.getBusiNameByField(configdb, body);

        String resultStr = null;

        if (resultNode != null) {
            resultStr = resultNode.toString();
        }
        response.getWriter().write(resultStr);
    }

}
