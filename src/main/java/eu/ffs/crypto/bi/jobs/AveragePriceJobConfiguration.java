package eu.ffs.crypto.bi.jobs;

import eu.ffs.crypto.bi.persistence.entity.AveragePrice;
import eu.ffs.crypto.bi.persistence.repo.AveragePriceJobRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class AveragePriceJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private AveragePriceJobRepository repository;

    @Bean
    public JdbcCursorItemReader<AveragePrice> reader() {
        JdbcCursorItemReader<AveragePrice> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);

        String sql = getQuery();
        reader.setSql(sql);

        reader.setRowMapper(new BeanPropertyRowMapper<>());

        return reader;
    }


    private String getQuery() {
        String qry = "WITH athData AS (\n" +
                "SELECT id, MAX(high) ath\n" +
                "FROM cmchistorical_item\n" +
                "GROUP BY id)\n" +
                "select id, max(date), max(high) from cmchistorical_item c where high=(select ath from athData where id=c.id limit 1) group by id";
        return qry;
    }

    @Bean
    public ItemProcessor<AveragePrice, AveragePrice> processor() {
        return o -> o;
    }

    @Bean
    public ItemWriter<AveragePrice> writer() {
        return list -> repository.save(list);
    }

    @Bean
    public Job averagePriceDataJob() {
        return jobBuilderFactory.get("averagePriceDataJob")
                .incrementer(new RunIdIncrementer())
                .flow(averagePriceJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step averagePriceJobStep1() {
        return stepBuilderFactory.get("averagePriceJobStep1")
                .<AveragePrice, AveragePrice>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public JobExecution perform() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder().addString(
                "AveragePriceJob",
                String.valueOf(System.currentTimeMillis())
        ).toJobParameters();

        JobExecution execution = jobLauncher.run(averagePriceDataJob(), parameters);
        System.out.println("Job finished with status :" + execution.getStatus());
        return execution;
    }
}