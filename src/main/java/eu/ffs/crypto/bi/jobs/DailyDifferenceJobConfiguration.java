package eu.ffs.crypto.bi.jobs;

import eu.ffs.crypto.bi.mapper.DailyDifferenceRowMapper;
import eu.ffs.crypto.bi.persistence.entity.DailyDifference;
import eu.ffs.crypto.bi.persistence.repo.DailyDifferenceRepository;
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

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class DailyDifferenceJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private DailyDifferenceRepository dailyDifferenceRepository;

    @Bean
    public JdbcCursorItemReader<DailyDifference> dailyDifferenceJobReader() {
        JdbcCursorItemReader<DailyDifference> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);

        String sql_delta_selection =
                "select DATE(END_TIME) from BATCH_JOB_INSTANCE i\n" +
                        "                join BATCH_JOB_EXECUTION e on i.JOB_INSTANCE_ID = e.JOB_INSTANCE_ID \n" +
                        "                join (select job_instance_id, max(job_execution_id) job_execution_id from BATCH_JOB_EXECUTION group by job_instance_id) e_sub on e_sub.job_execution_id = e.JOB_EXECUTION_ID and e_sub.job_instance_id = e.JOB_INSTANCE_ID\n" +
                        "                where \n" +
                        "                job_name = 'dailyDifferenceDataJob' and\n" +
                        "                status = 'COMPLETED'\n" +
                        "                order by e.job_execution_id desc limit 1";

        String sql = "select currentRank, cast(rank_day_before as signed)-cast(currentRank as signed) rankDailyChangeAbs, date, id, close, dailyChangeAbs, dailyChangePercent from (\n" +
                "select \n" +
                "ROW_NUMBER() OVER (PARTITION BY act_data.date ORDER BY act_data.market_cap DESC) AS currentRank,\n" +
                "ROW_NUMBER() OVER (PARTITION BY day_before.date ORDER BY day_before.market_cap DESC) AS rank_day_before,\n" +
                "act_data.date,\n" +
                "act_data.id,\n" +
                "act_data.close,\n" +
                "act_data.close-day_before.close as dailyChangeAbs,\n" +
                "((act_data.close-day_before.close)/day_before.close)*100 as dailyChangePercent \n" +
                "from cmchistorical_item act_data \n" +
                "join cmchistorical_item day_before on act_data.id=day_before.id \n" +
                "where act_data.id = day_before.id and act_data.date > ('2016-01-01') " +
                //                "and act_data.date > (" + sql_delta_selection + ")\n" +
                "and act_data.date = DATE_ADD(day_before.date, INTERVAL 1 day)) a";

        reader.setSql(sql);
        reader.setRowMapper(new DailyDifferenceRowMapper());

        return reader;
    }

    @Bean
    public ItemProcessor<DailyDifference,DailyDifference> dailyDifferenceJobProcessor() {
        return o -> o;
    }

    @Bean
    public ItemWriter<DailyDifference> dailyDifferenceJobWriter() {
        return list -> dailyDifferenceRepository.save(list);
    }

    @Bean
    public Job dailyDifferenceDataJob() {
        return jobBuilderFactory.get("dailyDifferenceDataJob")
                .incrementer(new RunIdIncrementer())
                .flow(dailyDifferenceJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step dailyDifferenceJobStep1() {
        return stepBuilderFactory.get("dailyDifferenceJobStep1")
                .<DailyDifference, DailyDifference>chunk(10)
                .reader(dailyDifferenceJobReader())
                .processor(dailyDifferenceJobProcessor())
                .writer(dailyDifferenceJobWriter())
                .build();
    }

    public JobExecution perform() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder().addString(
                "DailyDifferenceJob",
                String.valueOf(System.currentTimeMillis())
        ).toJobParameters();

        JobExecution execution = jobLauncher.run(dailyDifferenceDataJob(), parameters);
        System.out.println("Job finished with status :" + execution.getStatus());
        return execution;
    }
}