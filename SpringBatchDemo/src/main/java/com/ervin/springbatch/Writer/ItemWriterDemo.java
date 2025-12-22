package com.ervin.springbatch.Writer;

import com.ervin.springbatch.Reader.Customer;
import com.ervin.springbatch.Reader.ItemReaderDemo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("myWriter")
    private ItemWriter<String> myWriter;
    @Bean
    public Job itemWriterDemoJob(){
        return jobBuilderFactory.get("itemWriterDemoJob").start(itemWriterDemoStep()).build();
    }
    @Bean
    public Step itemWriterDemoStep(){
        return stepBuilderFactory.get("itemWriterDemoStep").<String,String>chunk(2)
                .reader(myRead())
                .writer(myWriter)
                .build();
    }

    @Bean
    public ItemReader<String> myRead(){
        List<String> items = new ArrayList<>();
        for(int i=1;i<=50;i++){
            items.add("java"+i);
        }
        return new ListItemReader<String>(items);
    }

}
