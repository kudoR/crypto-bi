package eu.ffs.crypto.bi.mapper;

import eu.ffs.crypto.bi.persistence.entity.DailyDifference;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyDifferenceRowMapper implements RowMapper<DailyDifference> {

    @Override
    public DailyDifference mapRow(ResultSet resultSet, int i) throws SQLException {

        return new DailyDifference(
                resultSet.getString("id"),
                (Date) resultSet.getObject("date"),
                resultSet.getBigDecimal("close"),
                resultSet.getBigDecimal("dailyChangeAbs"),
                resultSet.getBigDecimal("dailyChangePercent"),
                resultSet.getInt("currentRank"),
                resultSet.getInt("rankDailyChangeAbs"));
    }
}
