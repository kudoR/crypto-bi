package eu.ffs.crypto.bi.persistence.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "DWH_DAILY_DIFFERENCE")
public class DailyDifference {

    @EmbeddedId
    DailyDifferencePK pk;
    BigDecimal close;
    BigDecimal dailyChangeAbs;
    BigDecimal dailyChangePercent;
    int currentPlace;
    int dailyChangePlace;

    public DailyDifference() {
    }

    public DailyDifference(String id, Date date, BigDecimal close, BigDecimal dailyChangeAbs, BigDecimal dailyChangePercent, int currentPlace, int dailyChangePlace) {
        this.pk = new DailyDifferencePK(id, date);
        this.close = close;
        this.dailyChangeAbs = dailyChangeAbs;
        this.dailyChangePercent = dailyChangePercent;
        this.currentPlace = currentPlace;
        this.dailyChangePlace = dailyChangePlace;
    }

    public int getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(int currentPlace) {
        this.currentPlace = currentPlace;
    }

    public int getDailyChangePlace() {
        return dailyChangePlace;
    }

    public void setDailyChangePlace(int dailyChangePlace) {
        this.dailyChangePlace = dailyChangePlace;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getDailyChangeAbs() {
        return dailyChangeAbs;
    }

    public void setDailyChangeAbs(BigDecimal dailyChangeAbs) {
        this.dailyChangeAbs = dailyChangeAbs;
    }

    public BigDecimal getDailyChangePercent() {
        return dailyChangePercent;
    }

    public void setDailyChangePercent(BigDecimal dailyChangePercent) {
        this.dailyChangePercent = dailyChangePercent;
    }
}
