package libaray_item.operation;

import libaray_item.book.Book;
import libaray_item.book.BookList;

import java.util.Scanner;

//借阅图书
//要借的书的名字是什么
//要借的书在数组里是否存在
//存在的话 将isBorrwed置为已被借出
public class BorrowedOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("借阅图书");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要借阅的书籍: ");
        String name = scanner.nextLine();
        int currentSize = bookList.getUseSized();
        for(int i = 0; i < currentSize; i++) {
            Book book_borrowed = bookList.getBook(i);
            if(book_borrowed.getName().equals(name)) {
               //初始是false,所以借出的时候需要设置成true
                book_borrowed.setBorrowed(true);
                System.out.println("借阅成功");
                System.out.println(book_borrowed);
                return;
            }
        }
        //借阅的书不存在
        System.out.println("当前没有您要借阅的书籍");
    }
}
