package com.ervin.springbatch.Writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class ItemWriterFileDemoWriterConfig{
    @Bean
    public FlatFileItemWriter<Customer> itemWriterFileDemoWriter() throws Exception {
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
}
