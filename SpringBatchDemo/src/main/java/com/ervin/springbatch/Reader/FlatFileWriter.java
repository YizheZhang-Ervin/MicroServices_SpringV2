package com.ervin.springbatch.Reader;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("flatFileWriter")
public class FlatFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        for(Customer c:list){
            System.out.println(c);
        }
    }
}
