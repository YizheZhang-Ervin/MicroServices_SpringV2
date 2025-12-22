package com.ervin.springbatch.Processor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ProcessorDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("processorDemoReader")
    private ItemReader<? extends Customer> processorDemoReader;
    @Autowired
    @Qualifier("processorDemoWriter")
    private ItemWriter<? super Customer> processorDemoWriter;
    @Autowired
    private ItemProcessor<? super Customer,? extends Customer> processorDemoProcessor;
    @Autowired
    private ItemProcessor<? super Customer,? extends Customer> processorDemoProcessor2;
    @Bean
    public Job processorDemoJob(){
        return jobBuilderFactory.get("processorDemoJob").start(processorDemoStep()).build();
    }
    @Bean
    public Step processorDemoStep(){
        return stepBuilderFactory.get("itemWriterDbDemoStep").<Customer,Customer>chunk(2)
                .reader(processorDemoReader)
//                .processor(processorDemoProcessor)   // 使用一种处理方式
                .processor(process()) // 使用多种处理
                .writer(processorDemoWriter)
                .build();
    }
    // 使用多种处理方式
    @Bean
    public CompositeItemProcessor<Customer,Customer> process(){
        CompositeItemProcessor<Customer,Customer> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<? super Customer,? extends Customer>> delegates = new ArrayList<>();
        delegates.add(processorDemoProcessor);
        delegates.add(processorDemoProcessor2);
        processor.setDelegates(delegates);
        return processor;
    }
}
