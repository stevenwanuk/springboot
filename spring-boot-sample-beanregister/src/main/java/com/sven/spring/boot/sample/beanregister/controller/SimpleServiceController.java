package com.sven.spring.boot.sample.beanregister.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.spring.boot.sample.beanregister.service.ISimpleService;

//@Lazy
@RestController
public class SimpleServiceController
{

    @Autowired
    private List<ISimpleService> simpleServiceList;
    

    @RequestMapping("/all")
    public String getAllService() 
    {

        String result = "";
        for (ISimpleService service : simpleServiceList) {
            
            result += service.getName() + " ";
        }
        return result;
    }
}
