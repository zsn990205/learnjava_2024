import libaray_item.book.BookList;
import libaray_item.user.AdminUser;
import libaray_item.user.NormalUser;
import libaray_item.user.User;

import java.util.Scanner;

//运行后可以测试图书管理系统的主程序
public class Main {
    public static User login() {
        System.out.println("登陆中,请输入您的姓名: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("请输入您的身份,1:管理员 2:普通用户: ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            //是管理员
            return new AdminUser(name);
        } else {
            //是普通用户
            return new NormalUser(name);
        }
    }

    public static void main(String[] args) {
        BookList bookList = new BookList();
        User user = login();
        //这一步走完需要系统在提示我我的下一步操作该干什么了
        while(true) {
            int choice = user.menu();
            System.out.println("choice: "+choice);

            //根据choice的选择决定调用哪些方法

            user.doIoperation(choice,bookList);
        }



    }
}
