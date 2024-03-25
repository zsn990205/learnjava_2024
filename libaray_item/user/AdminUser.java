package libaray_item.user;

import libaray_item.operation.*;

import java.util.Scanner;

public class AdminUser extends User{
    public AdminUser(String name) {
        super(name);
        this.ioPerations = new IoPeration[]{new ExitOperation(),new FindOperation(),
                new AddOperation(), new DeleteOperation(), new ShowOperation()};
    }
    public int menu() {
        System.out.println("*******管理员用户*******");
        System.out.println("1.查找图书");
        System.out.println("2.新增图书");
        System.out.println("3.删除图书");
        System.out.println("4.显示图书");
        System.out.println("0.退出系统");
        System.out.println("*****************");

        System.out.println("登陆中,请输入您的操作: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return choice;
    }
}
