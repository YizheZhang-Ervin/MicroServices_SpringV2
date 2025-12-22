package com.ervin.springbatch.Reader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class ItemReaderXMLDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("xmlFileWriter")
    private ItemWriter<? super Customer> xmlFileWriter;
    @Bean
    public Job itemReaderXMLJob(){
        return jobBuilderFactory.get("itemReaderXMLJob").start(itemReaderXMLStep()).build();
    }
    @Bean
    public Step itemReaderXMLStep(){
        return stepBuilderFactory.get("itemReaderXMLStep").<Customer,Customer>chunk(2)
                .reader(xmlFileReader())
                .writer(xmlFileWriter)
                .build();
    }
    @Bean
    public StaxEventItemReader<Customer> xmlFileReader(){
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("customer.xml"));
        reader.setFragmentRootElementName("customer"); // ROOT TAG
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        Map<String,Class> map = new HashMap<>();
        map.put("customer",Customer.class);
        unmarshaller.setAliases(map);
        reader.setUnmarshaller(unmarshaller);
        return reader;
    }
}
