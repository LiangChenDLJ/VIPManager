package com.watsonLiang;

import org.apache.commons.codec.binary.Hex;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DBConnector {
    public enum LoginState {success, usernameNotExist, passwordMismatch, unknownError};
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
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return res;
    }

    public String[][] queryHistory(String id) {
        int i;
        String[][] res = {};
        String query = "SELECT ";
        for(i = 0; i < DataModel.historyAttrDisplay.length; i++){
            if(i > 0) query += ", ";
            query += DataModel.historyAttr[i];
        }
        query += " FROM transhistory WHERE id = " + id + " ;";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String[]> resList = new ArrayList<>();
            while(rs.next()){
                String[] rowData = new String[DataModel.historyAttrDisplay.length];
                for(i = 0; i < DataModel.historyAttrDisplay.length; i++){
                    switch(DataModel.historyDataType[i]){
                        case text:
                            rowData[i] = rs.getString(DataModel.historyAttr[i]);
                            break;
                        case real:
                            rowData[i] = Float.toString(rs.getFloat(DataModel.historyAttr[i]));
                            break;
                        case integer:
                            rowData[i] = Integer.toString(rs.getInt(DataModel.historyAttr[i]));
                            break;
                    }
                }
                resList.add(rowData);
            }
            res = new String[resList.size()][];
            resList.toArray(res);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return res;
    }

    public void insert(String[] attrs){
        String attrnames = "";
        String vals = "";
        for(int i = 0; i < attrs.length; i++){
            if(attrs[i].length() == 0) continue;
            attrnames += DataModel.dataAttr[i] + ",";
            switch(DataModel.dataType[i]){
                case text:
                    vals += "'" + attrs[i] + "',";
                    break;
                case real:
                case integer:
                    vals += attrs[i] + ",";
                    break;
            }
        }
        attrnames += "regtime";
        vals += "datetime()";
        String sql = "INSERT INTO cards (" + attrnames + ") VALUES (" + vals + ");";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void update(String id, boolean toAdd, String value){
        String flag = toAdd ? "+" : "-";
        String sql = "UPDATE cards SET credit = credit " + flag+ " " + value + " WHERE id = " + id + ";";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public LoginState login(String username, String password){
        String query = "SELECT passwordhash, salt FROM loginmsg WHERE username ='" + username + "';";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()) return LoginState.usernameNotExist;
            byte[] passwordbytes = password.getBytes(StandardCharsets.UTF_8);
            String passwordhash = rs.getString("passwordhash");
            String salt = rs.getString("salt");
            byte[] saltbytes = Hex.decodeHex(salt);
            ByteBuffer bb = ByteBuffer.allocate(passwordbytes.length + saltbytes.length);
            bb.put(passwordbytes);
            bb.put(saltbytes);
            byte[] psbytes = bb.array();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(psbytes);
            byte[] psdres = md.digest();
            byte[] dbpsdres = Hex.decodeHex(passwordhash);
            if(Arrays.compare(psdres, dbpsdres) == 0 ) return LoginState.success;
            else return LoginState.passwordMismatch;
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return LoginState.unknownError;
    }

    public void changePassword(String username, String newpassword){
        final int saltLength = 64;
        byte[] newpasswordbytes = newpassword.getBytes(StandardCharsets.UTF_8);
        Random rand = new Random();
        byte[] newsaltbytes = new byte[saltLength];
        rand.nextBytes(newsaltbytes);
        String newsalt = new String(Hex.encodeHex(newsaltbytes));
        ByteBuffer bb = ByteBuffer.allocate(newpasswordbytes.length + newsaltbytes.length);
        bb.put(newpasswordbytes);
        bb.put(newsaltbytes);
        byte[] newpasssaltbytes = bb.array();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(newpasssaltbytes);
            String newhashhex = new String(Hex.encodeHex(md.digest()));
            String sql = "UPDATE loginmsg SET salt = '" + newsalt + "', passwordhash = '" + newhashhex
                    + "' WHERE username = '" + username + "';";

            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
