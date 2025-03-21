package com.bite.demo.proxy;

public class RealHouseSubject implements HouseSubject{
    @Override
    public void rentHouse() {
        System.out.println("我是房东,我要出租房子...");
    }

    @Override
    public void saleHouse() {
        System.out.println("我是房东,我要卖掉房子...");
    }
}
