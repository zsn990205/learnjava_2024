package libaray_item.user;

import libaray_item.book.BookList;
import libaray_item.operation.IoPeration;


public abstract class User {

    protected String name;  //protected在同一个包内可以使用
    protected IoPeration ioPerations[];  //定义一个数组存放所需操作

    public User(String name) {
        this.name = name;
    }

    public void doIoperation(int choice, BookList bookList) {
        ioPerations[choice].work(bookList);
    }

    public abstract int menu();
}
