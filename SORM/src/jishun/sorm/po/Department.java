package jishun.sorm.po;

import java.sql.*;
import java.util.*;

@SuppressWarnings("unused")
public final class Department {

    private java.lang.String departmentname;

    private java.lang.Integer departmentID;

    private java.lang.String location;

    public void setDepartmentname(java.lang.String departmentname) {
        this.departmentname = departmentname;
    }

    public void setDepartmentID(java.lang.Integer departmentID) {
        this.departmentID = departmentID;
    }

    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    public java.lang.String getDepartmentname() {
        return departmentname;
    }

    public java.lang.Integer getDepartmentID() {
        return departmentID;
    }

    public java.lang.String getLocation() {
        return location;
    }

}