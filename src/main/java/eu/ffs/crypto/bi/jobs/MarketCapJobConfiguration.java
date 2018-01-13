package eu.ffs.crypto.bi.jobs;

import eu.ffs.crypto.bi.mapper.MarketCapHistoricalItemMapper;
import eu.ffs.crypto.bi.persistence.entity.MarketCapHistoricalItem;
import eu.ffs.crypto.bi.persistence.repo.MarketCapHistoricalItemRepository;
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
public class MarketCapJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private MarketCapHistoricalItemRepository repository;

    @Bean
    public JdbcCursorItemReader<MarketCapHistoricalItem> reader() {
        JdbcCursorItemReader<MarketCapHistoricalItem> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);

        String sql = this.getQuery();
        reader.setSql(sql);

        reader.setRowMapper(new MarketCapHistoricalItemMapper());

        return reader;
    }

    private String getQuery() {
        String qry = "with base_data as (\n" +
                "select date, sum(market_cap) market_cap from cmchistorical_item group by DATE)\n" +
                "select \n" +
                "act.date, \n" +
                "act.market_cap, \n" +
                "act.market_cap-pre_day.market_cap pre_day_diff, \n" +
                "((act.market_cap-pre_day.market_cap)/pre_day.market_cap)*100 pre_day_diff_percent,\n" +
                "act.market_cap-pre_week.market_cap pre_week_diff, \n" +
                "((act.market_cap-pre_week.market_cap)/pre_week.market_cap)*100 pre_week_diff_percent,\n" +
                "act.market_cap-pre_month.market_cap pre_month_diff, \n" +
                "((act.market_cap-pre_month.market_cap)/pre_month.market_cap)*100 pre_month_diff_percent \n" +
                "from base_data act \n" +
                "join base_data pre_day on pre_day.date=DATE_SUB(act.date,INTERVAL 1 day)\n" +
                "join base_data pre_week on pre_week.date=DATE_SUB(act.date,INTERVAL 1 week)\n" +
                "join base_data pre_month on pre_month.date=DATE_SUB(act.date,INTERVAL 1 month)\n" +
                "order by act.date desc";
        return qry;
    }

    @Bean
    public ItemProcessor<MarketCapHistoricalItem, MarketCapHistoricalItem> processor() {
        return o -> o;
    }

    @Bean
    public ItemWriter<MarketCapHistoricalItem> writer() {
        return list -> repository.save(list);
    }

    @Bean
    public Job marketCapDataJob() {
        return jobBuilderFactory.get("marketCapDataJob")
                .incrementer(new RunIdIncrementer())
                .flow(marketCapJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step marketCapJobStep1() {
        return stepBuilderFactory.get("marketCapJobStep1")
                .<MarketCapHistoricalItem, MarketCapHistoricalItem>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public JobExecution perform() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder().addString(
                "marketCapDataJob",
                String.valueOf(System.currentTimeMillis())
        ).toJobParameters();

        JobExecution execution = jobLauncher.run(marketCapDataJob(), parameters);
        System.out.println("Job finished with status :" + execution.getStatus());
        return execution;
    }
}
