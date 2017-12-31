package eu.ffs.crypto.bi.mapper;

import eu.ffs.crypto.bi.persistence.entity.Correlation;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CorrelationMapper implements RowMapper<Correlation> {
    @Override
    public Correlation mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Correlation(resultSet.getString("id"), resultSet.getString("correlatedId"), resultSet.getBigDecimal("correlation"));
    }
}
