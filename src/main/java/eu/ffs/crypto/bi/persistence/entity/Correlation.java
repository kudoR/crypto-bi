package eu.ffs.crypto.bi.persistence.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "DWH_CORREL")
public class Correlation {

    @EmbeddedId
    CorrelationPK pk;

    BigDecimal correlation;

    public Correlation() {
    }

    public Correlation(String id, String correlatingId, BigDecimal correlation) {
        this.pk = new CorrelationPK(id, correlatingId);
        this.correlation = correlation;
    }

    public BigDecimal getCorrelation() {
        return correlation;
    }

    public void setCorrelation(BigDecimal correlation) {
        this.correlation = correlation;
    }
}
