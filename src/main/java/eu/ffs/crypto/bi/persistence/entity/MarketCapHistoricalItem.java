package eu.ffs.crypto.bi.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "DWH_MARKET_CAP")
public class MarketCapHistoricalItem {

    @Id
    Date date;
    Long marketCap;
    Long preDayDiff;
    BigDecimal preDayDiffPercent;
    Long preWeekDiff;
    BigDecimal preWeekDiffPercent;
    Long preMonthDiff;
    BigDecimal preMonthDiffPercent;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Long getPreDayDiff() {
        return preDayDiff;
    }

    public void setPreDayDiff(Long preDayDiff) {
        this.preDayDiff = preDayDiff;
    }

    public BigDecimal getPreDayDiffPercent() {
        return preDayDiffPercent;
    }

    public void setPreDayDiffPercent(BigDecimal preDayDiffPercent) {
        this.preDayDiffPercent = preDayDiffPercent;
    }

    public Long getPreWeekDiff() {
        return preWeekDiff;
    }

    public void setPreWeekDiff(Long preWeekDiff) {
        this.preWeekDiff = preWeekDiff;
    }

    public BigDecimal getPreWeekDiffPercent() {
        return preWeekDiffPercent;
    }

    public void setPreWeekDiffPercent(BigDecimal preWeekDiffPercent) {
        this.preWeekDiffPercent = preWeekDiffPercent;
    }

    public Long getPreMonthDiff() {
        return preMonthDiff;
    }

    public void setPreMonthDiff(Long preMonthDiff) {
        this.preMonthDiff = preMonthDiff;
    }

    public BigDecimal getPreMonthDiffPercent() {
        return preMonthDiffPercent;
    }

    public void setPreMonthDiffPercent(BigDecimal preMonthDiffPercent) {
        this.preMonthDiffPercent = preMonthDiffPercent;
    }
}
