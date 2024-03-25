package libaray_item.operation;

import libaray_item.book.BookList;

public class ExitOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("退出图书");
        //直接退出
        System.exit(0);

    }
}
