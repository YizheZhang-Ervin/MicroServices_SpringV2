package com.ervin.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {
    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    // 任务对象
    @Bean
    public Job job001(){
        return jobBuilderFactory.get("job001").start(step001()).build();
    }
    @Bean
    public Step step001(){
        return stepBuilderFactory.get("step001").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is Job001-Step001");
                return RepeatStatus.FINISHED;
            }
        }).build();
    }
}
