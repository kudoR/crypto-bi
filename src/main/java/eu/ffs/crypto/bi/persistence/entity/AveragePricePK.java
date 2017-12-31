package eu.ffs.crypto.bi.persistence.entity;

import java.io.Serializable;
import java.time.Period;

public class AveragePricePK implements Serializable {

    String id;
    String period;

    public AveragePricePK(String id, Period period) {
        this.id = id;
        this.period = period.toString();
    }

    public AveragePricePK() {
    }
}
