package com.bite.demo.proxy;

public class HouseProxy implements HouseSubject{
    //目标对象 --房东
    private HouseSubject houseSubject;
    public HouseProxy(HouseSubject houseSubject) {
        this.houseSubject = houseSubject;
    }

    @Override
    public void rentHouse() {
        System.out.println("开始租房子代理...");
        houseSubject.rentHouse();
        System.out.println("结束租房子代理...");
    }

    @Override
    public void saleHouse() {
        System.out.println("开始卖房子代理...");
        houseSubject.saleHouse();
        System.out.println("结束卖房子代理...");
    }
}
