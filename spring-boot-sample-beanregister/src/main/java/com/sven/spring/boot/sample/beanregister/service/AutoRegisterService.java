package com.sven.spring.boot.sample.beanregister.service;

import org.springframework.stereotype.Service;

@Service
public class AutoRegisterService extends SimpleService
{
    @Override
    public String getName()
    {
        return "AutoRegisterService";
    }
}
