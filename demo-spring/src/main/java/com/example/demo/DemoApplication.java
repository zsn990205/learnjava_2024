package com.example.demo;

import com.example.demo.component.UserComp;
import com.example.demo.config.BeanConfig;
import com.example.demo.config.UserConfig;
import com.example.demo.config.UserInfo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		//spring上下文
		ApplicationContext context = SpringApplication.run(DemoApplication.class,args);
		//从context中获取bean
		//UserController bean = context.getBean(UserController.class);
		//bean.doController();

		//从context中获取bean
		UserService service = context.getBean(UserService.class);
		service.doService();

		UserRepo repos = context.getBean(UserRepo.class);
		repos.doRepo();

		UserComp comp = context.getBean(UserComp.class);
		comp.doCompo();

		UserConfig config = context.getBean(UserConfig.class);
		config.doConfig();

		UserInfo userConfig1 = (UserInfo)context.getBean("userInfo3");
		System.out.println(userConfig1);

	}

}
