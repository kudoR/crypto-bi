package eu.ffs.crypto.bi.persistence.repo;

import eu.ffs.crypto.bi.persistence.entity.Correlation;
import eu.ffs.crypto.bi.persistence.entity.CorrelationPK;
import org.springframework.data.repository.CrudRepository;

public interface CorrelationRepository extends CrudRepository<Correlation, CorrelationPK> {
}
