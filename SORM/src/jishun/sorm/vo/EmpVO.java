package jishun.sorm.vo;

import java.sql.Timestamp;

public class EmpVO {
    
    private java.sql.Timestamp regisTime;
    
    private java.lang.Integer userid;
    
    private java.lang.String username;
    
    private java.lang.String department;
    
    private java.lang.String location;

    public EmpVO() {
    }

    public java.sql.Timestamp getRegisTime() {
        return regisTime;
    }

    public void setRegisTime(java.sql.Timestamp regisTime) {
        this.regisTime = regisTime;
    }

    public java.lang.Integer getUserid() {
        return userid;
    }

    public void setUserid(java.lang.Integer userid) {
        this.userid = userid;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getDepartment() {
        return department;
    }

    public void setDepartment(java.lang.String department) {
        this.department = department;
    }

    public java.lang.String getLocation() {
        return location;
    }

    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    public EmpVO(Timestamp regisTime, Integer userid, String username, String department, String location) {
        super();
        this.regisTime = regisTime;
        this.userid = userid;
        this.username = username;
        this.department = department;
        this.location = location;
    }

}
