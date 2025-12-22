package com.ervin.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
public class NestedDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Job childJob001;
    @Autowired
    private Job childJob002;
    @Autowired
    private JobLauncher launcher;
    @Bean
    public Job parentJob(JobRepository repository, PlatformTransactionManager transactionManager){
        return jobBuilderFactory.get("parentJob").start(childJobOne(repository,transactionManager)).next(childJobTwo(repository,transactionManager)).build();
    }
    private Step childJobOne(JobRepository repository, PlatformTransactionManager transactionManager){
        return new JobStepBuilder(new StepBuilder("childJobOne")).job(childJob001)
            .launcher(launcher).repository(repository).transactionManager(transactionManager).build();
    }
    private Step childJobTwo(JobRepository repository, PlatformTransactionManager transactionManager){
        return new JobStepBuilder(new StepBuilder("childJobTwo")).job(childJob002)
                .launcher(launcher).repository(repository).transactionManager(transactionManager).build();
    }
}
