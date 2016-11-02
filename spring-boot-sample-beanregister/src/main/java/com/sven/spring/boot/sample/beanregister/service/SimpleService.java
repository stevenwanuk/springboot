package com.sven.spring.boot.sample.beanregister.service;

public class SimpleService implements ISimpleService
{

    public void setName(String name)
    {
        this.name = name;
    }
    private String name;
    @Override
    public String getName()
    {
        return name;
    }

}
