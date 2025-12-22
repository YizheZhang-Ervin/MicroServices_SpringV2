package com.ervin.springbatch.Writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiFileItemWriterDemoWriterConfig {
    @Bean
    public FlatFileItemWriter<Customer> multiFlatFileItemWriterDemoWriter() throws Exception {
        // 对象转为字符串输出到文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        String path="f:\\customer.txt";
        writer.setResource(new FileSystemResource(path));
        // 对象转成字符串
        writer.setLineAggregator(new LineAggregator<Customer>() {
            ObjectMapper mapper = new ObjectMapper();
            @Override
            public String aggregate(Customer item) {
                String str = null;
                try{
                    str = mapper.writeValueAsString(item);
                }catch(JsonProcessingException e){
                    e.printStackTrace();
                }
                return str;
            }
        });
        writer.afterPropertiesSet();
        return writer;
    }
    @Bean
    public StaxEventItemWriter<Customer> multiXMLFileItemWriterDemoWriter() throws Exception {
        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String,Class> aliases = new HashMap<>();
        aliases.put("customer",Customer.class);
        marshaller.setAliases(aliases);
        writer.setRootTagName("customers");
        writer.setMarshaller(marshaller);
        String path = "f:\\customer.xml";
        writer.setResource(new FileSystemResource(path));
        writer.afterPropertiesSet();
        return writer;
    }
    // 同时输出到多个文件
    @Bean
    public CompositeItemWriter<Customer> multiFileItemWriterDemoWriter() throws Exception {
        CompositeItemWriter<Customer> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(multiFlatFileItemWriterDemoWriter(),multiXMLFileItemWriterDemoWriter()));
        writer.afterPropertiesSet();
        return writer;
    }

    // 实现分类，分别输出到多个文件
    @Bean
    public ClassifierCompositeItemWriter<Customer> classifiedMultiFileItemWriterDemoWriter(){
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            public ItemWriter<? super Customer> classify(Customer customer) {
                ItemWriter<Customer> write = null;
                try{
                    write = customer.getId()%2==0?multiFlatFileItemWriterDemoWriter():multiXMLFileItemWriterDemoWriter();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return write;
            }
        });
        return writer;
    }
}
