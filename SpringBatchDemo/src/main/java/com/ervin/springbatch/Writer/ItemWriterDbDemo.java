package com.ervin.springbatch.Writer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class ItemWriterDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("itemWriterDbDemoReader")
    private ItemReader<Customer> itemWriterDbDemoReader;
    @Autowired
    @Qualifier("itemWriterDbDemoWriter")
    private ItemWriter<? super Customer> itemWriterDbDemoWriter;
    @Bean
    public Job itemWriterDbDemoJob(){
        return jobBuilderFactory.get("itemWriterDbDemoJob").start(itemWriterDbDemoStep()).build();
    }
    @Bean
    public Step itemWriterDbDemoStep(){
        return stepBuilderFactory.get("itemWriterDbDemoStep").<Customer,Customer>chunk(2)
                .reader(itemWriterDbDemoReader)
                .writer(itemWriterDbDemoWriter)
                .build();
    }

}
