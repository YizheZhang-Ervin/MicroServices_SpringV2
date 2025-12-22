package com.ervin.springbatch.Processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProcessorDemoProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer item) {
        Customer cus = new Customer();
        cus.setId(item.getId());
        cus.setFirstName(item.getFirstName().toUpperCase());
        cus.setLastName(item.getLastName());
        cus.setBirthday(item.getBirthday());
        return cus;
    }
}
