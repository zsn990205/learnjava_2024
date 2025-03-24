package com.bite.demo.proxy;

import java.lang.reflect.Proxy;
import net.sf.cglib.proxy.Enhancer;

//通过此方法执行
public class MainProxy {
    public static void main(String[] args) {
        //目标对象  代理接口
        HouseSubject target = new RealHouseSubject();
        //目标对象2: 代理类
        HouseSubjectClass subjectClass = new HouseSubjectClass();
        //HouseSubject houser = new RealHouseSubject();
        //静态代理的方式
//        HouseProxy proxy = new HouseProxy(houser);
//        proxy.rentHouse();

        //动态代理的方式
        //1.JDK代理
//        HouseSubject proxy = (HouseSubject) Proxy.newProxyInstance(
//                target.getClass().getClassLoader(),
//                new Class[]{HouseSubject.class},
//                new JDKInvocation(target));
//        proxy.saleHouse();
//        proxy.rentHouse();

        //2.CGLib代理
        HouseSubject proxy3 = (HouseSubject) Enhancer.create(target.getClass(),
                new CGLibIntercepter(target));
        proxy3.rentHouse();

        //CGLib代理类
        HouseSubjectClass proxy4 = (HouseSubjectClass) Enhancer.create(
                subjectClass.getClass(), new CGLibIntercepter(subjectClass));
        proxy4.rentHouse();
    }
}
