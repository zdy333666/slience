/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

/**
 *
 * @author xzh
 */
public class ModelFieldInfo {

    public String fieldName;		//	字段名
    public String fieldDisplayName;	//	字段显示名
    public int displayInGrid;
    public int displayWidth;
    public int fieldSpec;   //  1 人  ,2 车 ,3 案
    public int addressFlag; // 0 非地址字段 1 地址字段

    public int listFieldFlag; //是否列表字段 0：列表字段，1：非列表字段  list_field_flag
    public int largeIconFieldFlag; //是否大图标字段 0：大图标字段，1：非大图标字段  large_icon_field_flag
    public int polymerizerFlag; //聚合字段标志 0：普通字段，1：人员聚合，2：车辆聚合，3：案件聚合  polymerizer_flag
    public int detailFlag; //是否详表字段 0：详表字段，1：非详表字段 detail_flag

    public int listDisplayOrder; //列表显示顺序 list_display_order
    public int largeIconDisplayOrder; //大图标显示顺序 large_icon_display_order
    public int latticeNum; //格式 lattice_num
    public int rowNum; //行数 row_num
    public int pkField; //是否主键字段 pk_field
    public int displayFieldNameFlag; //是否显示字段名称 display_field_name_flag
    public int statisticsFilterField; //统计筛选字段 statistics_filter_field
    public String statisticsFilterCode1; //统计筛选编码1 statistics_filter_code1
    public String statisticsFilterCode2; //统计筛选编码2 statistics_filter_code2
    public String statisticsFilterCode3; //统计筛选编码3 statistics_filter_code3
    public int statisticsFilterFieldOrder; //统计筛选字段顺序 statistics_filter_field_order
}
