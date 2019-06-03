package jishun.sorm.po;

import java.sql.*;
import java.util.*;

@SuppressWarnings("unused")
public final class T_users {

    private java.sql.Timestamp regisTime;

    private java.sql.Clob myInfo;

    private java.lang.Integer ID;

    private java.sql.Blob binaryText;

    private java.lang.String pwd;

    private java.lang.String department;

    private java.lang.Integer hashValue;

    private java.lang.String username;

    public void setRegisTime(java.sql.Timestamp regisTime) {
        this.regisTime = regisTime;
    }

    public void setMyInfo(java.sql.Clob myInfo) {
        this.myInfo = myInfo;
    }

    public void setID(java.lang.Integer ID) {
        this.ID = ID;
    }

    public void setBinaryText(java.sql.Blob binaryText) {
        this.binaryText = binaryText;
    }

    public void setPwd(java.lang.String pwd) {
        this.pwd = pwd;
    }

    public void setDepartment(java.lang.String department) {
        this.department = department;
    }

    public void setHashValue(java.lang.Integer hashValue) {
        this.hashValue = hashValue;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.sql.Timestamp getRegisTime() {
        return regisTime;
    }

    public java.sql.Clob getMyInfo() {
        return myInfo;
    }

    public java.lang.Integer getID() {
        return ID;
    }

    public java.sql.Blob getBinaryText() {
        return binaryText;
    }

    public java.lang.String getPwd() {
        return pwd;
    }

    public java.lang.String getDepartment() {
        return department;
    }

    public java.lang.Integer getHashValue() {
        return hashValue;
    }

    public java.lang.String getUsername() {
        return username;
    }

}