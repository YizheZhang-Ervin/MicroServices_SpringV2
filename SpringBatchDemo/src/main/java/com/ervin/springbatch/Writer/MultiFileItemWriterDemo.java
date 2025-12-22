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
public class MultiFileItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("multiFileItemWriterDemoReader")
    private ItemReader<Customer> multiFileItemWriterDemoReader;
    @Autowired
    @Qualifier("multiFileItemWriterDemoWriter")
    private ItemWriter<Customer> multiFileItemWriterDemoWriter;
    // 使用分类的时候
//    @Autowired
//    @Qualifier("multiFlatFileItemWriterDemoWriter")
//    private ItemStreamWriter<Customer> multiFlatFileItemWriterDemoWriter;
//    @Autowired
//    @Qualifier("multiXMLFileItemWriterDemoWriter")
//    private ItemStreamWriter<Customer> multiXMLFileItemWriterDemoWriter;
//    @Autowired
//    @Qualifier("classifiedMultiFileItemWriterDemoWriter")
//    private ItemWriter<Customer> classifiedMultiFileItemWriterDemoWriter;

    @Bean
    public Job multiFileItemWriterDemoJob(){
        return jobBuilderFactory.get("multiFileItemWriterDemoJob").start(multiFileItemWriterDemoStep()).build();
    }
    @Bean
    public Step multiFileItemWriterDemoStep(){
        return stepBuilderFactory.get("multiFileItemWriterDemoStep").<Customer,Customer>chunk(10)
                .reader(multiFileItemWriterDemoReader).writer(multiFileItemWriterDemoWriter).build();
        // 使用分类的时候
//        return stepBuilderFactory.get("multiFileItemWriterDemoStep").<Customer,Customer>chunk(10)
//                .reader(multiFileItemWriterDemoReader).writer(classifiedMultiFileItemWriterDemoWriter).
//                stream(multiFlatFileItemWriterDemoWriter).stream(multiXMLFileItemWriterDemoWriter).build();
    }
}
