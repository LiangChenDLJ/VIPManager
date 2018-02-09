package com.watsonLiang;

import java.sql.*;
import java.util.ArrayList;

public class DBConnector {
    /**
     * Connect to a sample database
     */
    DBConnector(String url){
        this.url = "jdbc:sqlite:" + url;
        connect();
    }

    String url;
    Connection conn = null;

    public String[][] query(String attr, String val) {
        String[][] res = {};
        String query = "SELECT * FROM cards";
        if(val.length() == 0)
            query += ";";
        else {
            switch (DataModel.dataType[DataModel.attrIndex(attr)]) {
                case text:
                    query += " WHERE " + attr + " LIKE '%" + val + "%';";
                    break;
                case integer:
                case real:
                    query += " WHERE " + attr + " = " + val + ";";
                    break;
            }
        }
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String[]> resList = new ArrayList<>();
            while(rs.next()){
                String[] rowData = new String[DataModel.dataAttr.length];
                for(int i = 0; i < DataModel.dataAttr.length; i++){
                    switch(DataModel.dataType[i]){
                        case text:
                            rowData[i] = rs.getString(DataModel.dataAttr[i]);
                            break;
                        case real:
                            rowData[i] = Float.toString(rs.getFloat(DataModel.dataAttr[i]));
                            break;
                        case integer:
                            rowData[i] = Integer.toString(rs.getInt(DataModel.dataAttr[i]));
                            break;
                    }
                }
                resList.add(rowData);
            }
            res = new String[resList.size()][];
            resList.toArray(res);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return res;
    }

    public void connect() {
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
