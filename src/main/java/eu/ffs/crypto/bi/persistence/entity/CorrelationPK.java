package eu.ffs.crypto.bi.persistence.entity;

import java.io.Serializable;

public class CorrelationPK implements Serializable {

    String id, correlatingId;

    public CorrelationPK() {
    }

    public CorrelationPK(String id, String correlatingId) {
        this.id = id;
        this.correlatingId = correlatingId;
    }


}
