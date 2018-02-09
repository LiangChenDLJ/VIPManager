package com.watsonLiang;

public class DataModel {
    public enum AttributeType{integer, real, text};

    public static int attrIndex(String attr){
        for(int i =0; i < dataAttr.length; i++){
            if(dataAttr[i].equals(attr)) return i;
        }
        return 0;
    }

    public static int displayIndex(String display){
        for(int i =0; i < dataAttr.length; i++){
            if(dataAttrDisplay[i].equals(display)) return i;
        }
        return 0;
    }

    public static AttributeType[] dataType = {AttributeType.integer, AttributeType.text, AttributeType.text, AttributeType.text, AttributeType.real};
    public static String[] searchAttrDisplay = {"ID", "姓名", "身份证", "手机号"};
    public static String[] dataAttr = {"ID", "name", "idcard", "phone", "credit"};
    public static String[] dataAttrDisplay = {"ID", "姓名", "身份证", "手机号", "积分"};

}
