package libaray_item.user;

import libaray_item.operation.*;

import java.util.Scanner;

public class NormalUser extends User{
    public NormalUser(String name) {
        super(name);
        this.ioPerations = new IoPeration[]{new ExitOperation(),new FindOperation(),
                new BorrowedOperation(), new ReturnOperation()};
    }

    //不同用户能看到的菜单是不同的
    public int menu() {
        System.out.println("*******普通用户*******");
        System.out.println("1.查找图书");
        System.out.println("2.借阅图书");
        System.out.println("3.归还图书");
        System.out.println("0.退出系统");
        System.out.println("*****************");

        System.out.println("登陆中,请输入您的操作: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return choice;
    }
}
