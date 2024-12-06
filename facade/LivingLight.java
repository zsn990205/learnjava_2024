package com.example.demo.facade;

public class LivingLight implements Light{
    @Override
    public void on() {
        System.out.println("打开卧室灯");
    }

    @Override
    public void off() {
        System.out.println("关闭卧室灯");
    }
}
