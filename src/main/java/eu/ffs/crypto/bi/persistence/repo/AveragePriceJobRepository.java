package eu.ffs.crypto.bi.persistence.repo;

import eu.ffs.crypto.bi.persistence.entity.AveragePrice;
import eu.ffs.crypto.bi.persistence.entity.AveragePricePK;
import eu.ffs.crypto.bi.persistence.entity.MarketCapHistoricalItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface AveragePriceJobRepository extends CrudRepository<AveragePrice, AveragePricePK>{
}
