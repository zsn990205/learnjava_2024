package libaray_item.operation;

import libaray_item.book.Book;
import libaray_item.book.BookList;

import java.util.Scanner;

//归还图书
public class ReturnOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("返还图书");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要归还的书籍: ");
        String name = scanner.nextLine();
        int currentSize = bookList.getUseSized();
        for(int i = 0; i < currentSize; i++) {
            Book book_borrowed = bookList.getBook(i);
            if(book_borrowed.getName().equals(name)) {
                //借出的时候是true,所以返还的时候需要设置成false
                book_borrowed.setBorrowed(false);
                System.out.println("归还成功");
                System.out.println(book_borrowed);
                return;
            }
        }
        //归还的书不存在
        System.out.println("当前没有您要归还的书籍");
    }
    }

