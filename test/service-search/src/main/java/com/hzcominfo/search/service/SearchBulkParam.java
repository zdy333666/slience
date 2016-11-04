/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * @author xzh
 */
public class SearchBulkParam {
    private ArrayList < SearchItem > searchItemList;

    /**
     * @return the searchItemList
     */
    public ArrayList < SearchItem > getSearchItemList() {
        return searchItemList;
    }

    /**
     * @param searchItemList the searchItemList to set
     */
    public void setSearchItemList(ArrayList < SearchItem > searchItemList) {
        this.searchItemList = searchItemList;
    }
    
    public static SearchBulkParam StringToSearchBulkparam(String paramInput )
    {
        try
        {
    
            ObjectMapper localMapper=new ObjectMapper();
            JsonNode localNode=localMapper.readTree(paramInput);
            ObjectNode localObjectNode=(ObjectNode)localNode;
            SearchBulkParam localParam=new SearchBulkParam();
            JsonNode localValueObject;
            localValueObject=localObjectNode.get("query");
            if((localValueObject!=null)&&(localValueObject.isArray()))
            {
                ArrayNode localArrayNode=(ArrayNode)localValueObject;
                localParam.setSearchItemList(new ArrayList < SearchItem >() );
                for(JsonNode localTypeNode:localArrayNode)
                {
                    localValueObject=localTypeNode.get("busiModel");
                    if(localValueObject==null)
                    {
                        continue;
                    }
                    SearchItem localSearchItem=new SearchItem();
                    localSearchItem.setBusiModel(localValueObject.getTextValue());
                    
                    localValueObject=localTypeNode.get("query");
                    if(localValueObject!=null)
                    {
                        localSearchItem.setQ(URLDecoder.decode(localValueObject.getTextValue(),"UTF-8"));
                    }
                    localValueObject=localTypeNode.get("modelIndex");
                    if(localValueObject!=null)
                    {
                        localSearchItem.setModelIndex(localValueObject.getValueAsInt(0));
                    }

                    localParam.getSearchItemList().add(localSearchItem);
                }
            }
            return localParam;
         }
        catch( Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}
