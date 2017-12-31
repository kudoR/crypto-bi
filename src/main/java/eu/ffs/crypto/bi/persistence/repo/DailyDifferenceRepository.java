package eu.ffs.crypto.bi.persistence.repo;

import eu.ffs.crypto.bi.persistence.entity.DailyDifference;
import eu.ffs.crypto.bi.persistence.entity.DailyDifferencePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDifferenceRepository extends CrudRepository<DailyDifference, DailyDifferencePK> {
}
