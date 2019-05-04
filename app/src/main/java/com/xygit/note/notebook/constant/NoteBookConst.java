package com.xygit.note.notebook.constant;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/17
 */

public class NoteBookConst {

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_SINGIN_INVALID = -1001;
    //1:完成日期顺序
    public static final String TODO_ORDERBY_ONE = "1";
    //2.完成日期逆序
    public static final String TODO_ORDERBY_TWO = "2";
    //3.创建日期顺序
    public static final String TODO_ORDERBY_THREE = "3";
    //4.创建日期逆序(默认)
    public static final String TODO_ORDERBY_FOUR = "4";

    public static final String INETENT_START_TYPE = "startType";
    public static final String INETENT_PARAM_ENTITY = "entity";
    public static final String INETENT_PARAM_SUB_ENTITY = "subEntity";
    public static final String INETENT_PARAM_DATA = "data";
    public static final String INETENT_PARAM_SUB_DATA = "subData";
    public static final String INETENT_PARAM_URL = "url";

    public static final String TODO_STATUS_NOT_DONE = "0";
    public static final String TODO_STATUS_DONE = "1";
    public static final int TODO_PRIORITY_IMPORTANT = 1;
    public static final int TODO_PRIORITY_COMMON = 2;
}
