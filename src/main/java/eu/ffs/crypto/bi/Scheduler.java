package eu.ffs.crypto.bi;

import eu.ffs.crypto.bi.jobs.CorrelationJobConfiguration;
import eu.ffs.crypto.bi.jobs.DailyDifferenceJobConfiguration;
import eu.ffs.crypto.bi.jobs.MarketCapJobConfiguration;
import eu.ffs.crypto.bi.persistence.repo.MarketCapHistoricalItemRepository;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

//    @Autowired
//    DailyDifferenceJobConfiguration dailyDifferenceJobConfiguration;

    @Autowired
    CorrelationJobConfiguration correlationJobConfiguration;

    @Autowired
    MarketCapJobConfiguration marketCapJobConfiguration;


    @Scheduled(fixedRateString = "60000")
    public void perform() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
//        dailyDifferenceJobConfiguration.perform();
        correlationJobConfiguration.perform();
     //   marketCapJobConfiguration.perform();
    }

}
