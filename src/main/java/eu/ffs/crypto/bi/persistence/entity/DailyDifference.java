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

    public DailyDifference() {
    }

    public DailyDifference(String id, Date date, BigDecimal close, BigDecimal dailyChangeAbs, BigDecimal dailyChangePercent) {
        this.pk = new DailyDifferencePK(id, date);
        this.close = close;
        this.dailyChangeAbs = dailyChangeAbs;
        this.dailyChangePercent = dailyChangePercent;
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
