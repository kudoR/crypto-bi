package eu.ffs.crypto.bi.jobs;

import eu.ffs.crypto.bi.mapper.CorrelationMapper;
import eu.ffs.crypto.bi.persistence.entity.Correlation;
import eu.ffs.crypto.bi.persistence.repo.CorrelationRepository;
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
public class CorrelationJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CorrelationRepository repository;

    @Bean
    public JdbcCursorItemReader<Correlation> reader() {
        JdbcCursorItemReader<Correlation> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);

        String sql = this.getCorrelationQuery(MYSQL_DATETIMERANGE.LAST_6_MONTHS);
        reader.setSql(sql);

        BeanPropertyRowMapper<Correlation> correlationBeanPropertyRowMapper = BeanPropertyRowMapper.newInstance(Correlation.class);

        reader.setRowMapper(correlationBeanPropertyRowMapper);

        return reader;
    }

    // id - correlatedId - correlation
    private String getCorrelationQuery(String range) {
        String qry = "WITH \n" +
                "basedata AS (SELECT c1.date, c1.id id_1, c2.id id_2, c1.close x, c2.close y FROM cmchistorical_item c1, cmchistorical_item c2,cmcglobal_item symbols\n" +
                "WHERE symbols.id = c1.id and c1.date>= DATE_ADD(CURDATE(), " + range + ") AND c2.id!=c1.id AND c1.date=c2.date),\n" +
                "precalc AS (SELECT id_1, id_2, AVG(x) avg_x, AVG(Y) avg_y, (STDDEV_SAMP(x) * STDDEV_SAMP(Y)) division FROM basedata group by id_1, id_2)\n" +
                "select p.id_1 id, p.id_2 correlatedId, SUM((x - p.avg_x) * (Y - p.avg_y)) / ((count(x) -1) * p.division) correlation  from basedata b\n" +
                " join precalc p on b.id_1=p.id_1 and b.id_2 = p.id_2\n" +
                " group by p.id_1, p.id_2";
        return qry;
    }

    @Bean
    public ItemProcessor<Correlation, Correlation> processor() {
        return o -> o;
    }

    @Bean
    public ItemWriter<Correlation> writer() {
        return list -> repository.save(list);
    }

    @Bean
    public Job correlationDataJob() {
        return jobBuilderFactory.get("correlationDataJob")
                .incrementer(new RunIdIncrementer())
                .flow(correlationJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step correlationJobStep1() {
        return stepBuilderFactory.get("correlationJobStep1")
                .<Correlation, Correlation>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public JobExecution perform() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder().addString(
                "CorrelationJob",
                String.valueOf(System.currentTimeMillis())
        ).toJobParameters();

        JobExecution execution = jobLauncher.run(correlationDataJob(), parameters);
        System.out.println("Job finished with status :" + execution.getStatus());
        return execution;
    }
}