package com.ervin.springbatch.Reader;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("dbJdbcWriter")
public class dbJdbcWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> items) throws Exception {
        for(User u:items){
            System.out.println(u);
        }
    }
}
