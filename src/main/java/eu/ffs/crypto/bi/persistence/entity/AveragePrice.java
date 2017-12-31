package eu.ffs.crypto.bi.persistence.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "DWH_AVG_PRICE")
public class AveragePrice {
    @EmbeddedId
    AveragePricePK pk;

    BigDecimal price;

    public AveragePrice(AveragePricePK pk, BigDecimal price) {
        this.pk = pk;
        this.price = price;
    }

    public AveragePrice() {

    }
}
