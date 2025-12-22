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
public class ItemWriterXMLDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("itemWriterXMLDemoReader")
    private ItemReader<Customer> itemWriterXMLDemoReader;
    @Autowired
    @Qualifier("itemWriterXMLDemoWriter")
    private ItemWriter<Customer> itemWriterXMLDemoWriter;
    @Bean
    public Job itemWriterXMLDemoJob(){
        return jobBuilderFactory.get("itemWriterXMLDemoJob").start(itemWriterXMLDemoStep()).build();
    }
    @Bean
    public Step itemWriterXMLDemoStep(){
        return stepBuilderFactory.get("itemWriterXMLDemoStep").<Customer,Customer>chunk(10)
                .reader(itemWriterXMLDemoReader)
                .writer(itemWriterXMLDemoWriter)
                .build();
    }
}
