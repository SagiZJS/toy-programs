package jishun.sorm.po;

import java.sql.*;
import java.util.*;

@SuppressWarnings("unused")
public final class T_time {

    private java.sql.Date date;

    private java.lang.Integer ID;

    private java.sql.Time time;

    private java.sql.Timestamp timestamp;

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public void setID(java.lang.Integer ID) {
        this.ID = ID;
    }

    public void setTime(java.sql.Time time) {
        this.time = time;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public java.lang.Integer getID() {
        return ID;
    }

    public java.sql.Time getTime() {
        return time;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

}