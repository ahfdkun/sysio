package io.rpcdemo.service;


public class MyCar implements Car {

    @Override
    public String ooxx(String msg) {
        return "server res "+ msg;
    }

    @Override
    public Persion oxox(String name, Integer age) {
        Persion p = new Persion();
        p.setName(name);
        p.setAge(age);
        return p;
    }
}
