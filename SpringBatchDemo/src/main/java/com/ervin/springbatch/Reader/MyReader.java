package com.ervin.springbatch.Reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;


public class MyReader implements ItemReader<String> {
    private Iterator<String> iterator;
    @Override
    public String read() throws Exception, UnexpectedInputException{
        if(iterator.hasNext()){
            return this.iterator.next();
        }else{
            return null;
        }
    }
    public MyReader(List<String> list){
        this.iterator = list.iterator();
    }
}
