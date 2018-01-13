package eu.ffs.crypto.bi.mapper;

import eu.ffs.crypto.bi.persistence.entity.MarketCapHistoricalItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketCapHistoricalItemMapper implements RowMapper<MarketCapHistoricalItem> {
    @Override
    public MarketCapHistoricalItem mapRow(ResultSet resultSet, int i) throws SQLException {

        MarketCapHistoricalItem marketCapHistoricalItem = new MarketCapHistoricalItem();
        marketCapHistoricalItem.setDate(resultSet.getDate("date"));
        marketCapHistoricalItem.setMarketCap(resultSet.getLong("market_cap"));
        marketCapHistoricalItem.setPreDayDiff(resultSet.getLong("pre_day_diff"));
        marketCapHistoricalItem.setPreDayDiffPercent(resultSet.getBigDecimal("pre_day_diff_percent"));
        marketCapHistoricalItem.setPreWeekDiff(resultSet.getLong("pre_week_diff"));
        marketCapHistoricalItem.setPreWeekDiffPercent(resultSet.getBigDecimal("pre_week_diff_percent"));
        marketCapHistoricalItem.setPreMonthDiff(resultSet.getLong("pre_month_diff"));
        marketCapHistoricalItem.setPreMonthDiffPercent(resultSet.getBigDecimal("pre_month_diff_percent"));
        return marketCapHistoricalItem;
    }



}
