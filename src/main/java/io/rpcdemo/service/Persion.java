package io.rpcdemo.service;

import java.io.Serializable;


public class Persion implements Serializable {

    String name ;
    Integer age ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " " + age ;
    }
}
