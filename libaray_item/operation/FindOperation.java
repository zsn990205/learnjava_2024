package libaray_item.operation;


import libaray_item.book.Book;
import libaray_item.book.BookList;

import java.util.Scanner;

//查找图书操作
//基本步骤如下:
//获取到当前的书的数量-->遍历booklist数组-->在数组内查找这本图书是否存在
//存在就退出程序-->不存在就是书找不到
public class FindOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("查找图书");
        System.out.println("请输入您要查找的书的名字: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        int currSize = bookList.getUseSized();  //还是获取当前图书的数量
        for(int i = 0; i < currSize; i++) {
            Book bookFind = bookList.getBook(i);
            if(bookFind.getName().equals(name)) {
                System.out.println("查找到了这本书,信息如下:");
                System.out.println(bookFind);
                return;

            }
        }
        //到这个位置代表还没找到这本书
        System.out.println("您要查找的图书不存在!");
    }

}
