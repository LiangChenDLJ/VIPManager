package com.watsonLiang;

import java.util.ArrayList;

public class DataModel {
    public enum AttributeType{integer, real, text};

    public static int attrIndex(String attr){
        for(int i =0; i < dataAttr.length; i++){
            if(dataAttr[i].equals(attr)) return i;
        }
        return 0;
    }

    public static int displayIndex(String display){
        for(int i =0; i < dataAttrDisplay.length; i++){
            if(dataAttrDisplay[i].equals(display)) return i;
        }
        return 0;
    }

    public static AttributeType[] dataType = {AttributeType.integer, AttributeType.text, AttributeType.text, AttributeType.text, AttributeType.real, AttributeType.text};
    public static String[] searchAttrDisplay = {"ID", "姓名", "身份证", "手机号"};
    public static String[] searchAttr = {"ID", "name", "idcard", "phone"};
    public static String[] dataAttr = {"ID", "name", "idcard", "phone", "credit", "regtime"};
    public static String[] dataAttrDisplay = {"ID", "姓名", "身份证", "手机号", "积分"};
    public static String[] historyHeadAttrDisplay = {"ID", "姓名", "身份证", "手机号", "积分", "注册时间"};

    public static AttributeType[] historyDataType = {AttributeType.text, AttributeType.real};
    public static String[] historyAttrDisplay = {"时间", "积分变动"};
    public static String[] historyAttr = {"time", "creditchange"};


    public static boolean[] formatCheck(String[] attrs, String[] vals){
        boolean[] res = new boolean[attrs.length];
        for(int i = 0; i < attrs.length; i++){
            res[i] = formatCheck(attrs[i], vals[i]);
        }
        return res;
    }

    public static boolean formatCheck(String attr, String val){
        switch(dataType[attrIndex(attr)]){
            case text: return true;
            case real: return val.matches("[\\+-]?[0-9]*(\\.[0-9]+)?");
            case integer: return val.matches("[0-9]*");
        }
        return false;
    }

}
