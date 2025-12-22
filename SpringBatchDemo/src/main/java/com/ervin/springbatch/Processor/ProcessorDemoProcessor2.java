package com.ervin.springbatch.Processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProcessorDemoProcessor2 implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        if(item.getId()%2==0){
            return item;
        }else{
            return null;
        }

    }
}
