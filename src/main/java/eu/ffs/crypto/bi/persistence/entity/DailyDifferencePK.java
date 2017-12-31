package eu.ffs.crypto.bi.persistence.entity;

import java.io.Serializable;
import java.sql.Date;

public class DailyDifferencePK implements Serializable {

    String id;
    Date date;

    public DailyDifferencePK() {
    }

    public DailyDifferencePK(String id, Date date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
