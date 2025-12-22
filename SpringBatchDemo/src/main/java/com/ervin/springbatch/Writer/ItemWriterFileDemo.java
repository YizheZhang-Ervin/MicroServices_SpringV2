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
public class ItemWriterFileDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("itemWriterFileDemoWriter")
    private ItemWriter<? super Customer> itemWriterFileDemoWriter;
    @Autowired
    @Qualifier("itemWriterFileDemoReader")
    private ItemReader<? extends Customer> itemWriterFileDemoReader;
    @Bean
    public Job itemWriterFileDemoJob(){
        return jobBuilderFactory.get("itemWriterFileDemoJob").start(itemWriterFileDemoStep()).build();
    }
    @Bean
    public Step itemWriterFileDemoStep(){
        return stepBuilderFactory.get("itemWriterFileDemoStep").<Customer,Customer>chunk(10)
                .reader(itemWriterFileDemoReader)
                .writer(itemWriterFileDemoWriter)
                .build();
    }
}
