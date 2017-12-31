package eu.ffs.crypto.bi.persistence.repo;

import eu.ffs.crypto.bi.persistence.entity.MarketCapHistoricalItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface MarketCapHistoricalItemRepository extends CrudRepository<MarketCapHistoricalItem, Date>{
}
