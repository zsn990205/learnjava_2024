package libaray_item.operation;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import libaray_item.book.Book;
import libaray_item.book.BookList;

import java.util.Scanner;

//新增图书
//也就是说获取到当前存放的书的数量,在这个后面新放新的图书
//存放图书的变量跟着++
public class AddOperation implements IoPeration{

    @Override
    public void work(BookList bookList) {
        System.out.println("新增图书");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要新增的图书的名字: ");
        String name = scanner.nextLine();
        System.out.println("请输入您要新增的图书的作者: ");
        String author = scanner.nextLine();
        System.out.println("请输入您要新增的图书的类型: ");
        String type = scanner.nextLine();
        System.out.println("请输入您要新增的图书的价格: ");
        int price = scanner.nextInt();
        Book book_insert = new Book(name,author,price,type);
        //检查一下数组里有没有我要加的这本书
        int currSize = bookList.getUseSized();
        for(int i = 0;  i < currSize; i++) {
            Book book = bookList.getBook(i);
            if (book.getName().equals(name)) {
                System.out.println("您已加入过这本书请勿重复加入");
                return;
            }
        }
            //没找到书的时候
            //把这本书放进数组里就行
            if(currSize == 10) {
                System.out.println("书架满了不能存放了!");
            } else {
                bookList.setBook(bookList.getUseSized(), book_insert);
                bookList.setUseSized(currSize + 1);
            }
        }

    }

