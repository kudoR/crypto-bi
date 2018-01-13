package eu.ffs.crypto.bi.mapper;

import eu.ffs.crypto.bi.persistence.entity.MarketCapHistoricalItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketCapHistoricalItemMapper implements RowMapper<MarketCapHistoricalItem> {
    @Override
    public MarketCapHistoricalItem mapRow(ResultSet resultSet, int i) throws SQLException {

        MarketCapHistoricalItem marketCapHistoricalItem = new MarketCapHistoricalItem();
        marketCapHistoricalItem.setDate(resultSet.getDate(0));
        marketCapHistoricalItem.setMarketCap(resultSet.getLong(1));
        marketCapHistoricalItem.setPreDayDiff(resultSet.getLong(2));
        marketCapHistoricalItem.setPreDayDiffPercent(resultSet.getBigDecimal(3));
        marketCapHistoricalItem.setPreWeekDiff(resultSet.getLong(4));
        marketCapHistoricalItem.setPreWeekDiffPercent(resultSet.getBigDecimal(5));
        marketCapHistoricalItem.setPreMonthDiff(resultSet.getLong(6));
        marketCapHistoricalItem.setPreMonthDiffPercent(resultSet.getBigDecimal(7));
        return marketCapHistoricalItem;
    }


}
