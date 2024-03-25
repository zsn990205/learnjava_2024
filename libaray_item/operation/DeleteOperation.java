package libaray_item.operation;

import libaray_item.book.Book;
import libaray_item.book.BookList;

import java.util.Scanner;

//删除图书
//删除的步骤是一个个向前覆盖
//并且将之前的要删除的下标的位置置空
public class DeleteOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("删除图书");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您想要删除的书的名字");
        String name = scanner.nextLine();
        //先查找这本书在不在数组里
        int curSize = bookList.getUseSized();
        int pos = -1;
        int i = 0;
        for(; i < curSize; i++) {
            Book book_delete = bookList.getBook(i);
            if(book_delete.getName().equals(name)) {
                //此时就找到了我要删除的图书对应的下标
                pos = i;
                break;
            }
        }
        if(i == curSize) {
            System.out.println("没有找到您要删除的图书");
            return;
        }
            //找到了
            //找到的时候以此把这本书的后面的书向前覆盖-->使用循环
            int j = pos;
            for(; j < curSize-1; j++) {
                //现在我们找到的书的位置在j
                //我们将j后位置的书拿出来,放到j位置
                Book book = bookList.getBook(j+1);
                bookList.setBook(j,book);
            }
            //此时的j代表的是最后位置
            bookList.setBook(j,null);
            bookList.setUseSized(curSize-1);
            System.out.println("删除成功!");
        }

    }

