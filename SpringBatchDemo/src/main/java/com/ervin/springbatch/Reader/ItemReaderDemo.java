package com.ervin.springbatch.Reader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Job itemReaderJob(){
        return jobBuilderFactory.get("itemReaderJob").start(itemReaderDemoStep()).build();
    }
    @Bean
    public Step itemReaderDemoStep(){
        // chunk read/process/write
        return stepBuilderFactory.get("itemReaderDemoStep").<String,String>chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list->{for(String item:list){System.out.println(item);}})
                .build();
    }
    @Bean
    public MyReader itemReaderDemoRead(){
        List<String> data = Arrays.asList("AA","BB","CC");
        return new MyReader(data);
    }
}
