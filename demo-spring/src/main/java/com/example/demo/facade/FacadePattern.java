package com.example.demo.facade;

public class FacadePattern {
    public void lightOn() {
        HallLight hallLight = new HallLight();
        hallLight.on();
        DingLight dingLight = new DingLight();
        dingLight.on();
        LivingLight livingLight = new LivingLight();
        livingLight.on();

    }
}
