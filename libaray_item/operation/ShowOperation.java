package libaray_item.operation;

import libaray_item.book.Book;
import libaray_item.book.BookList;

public class ShowOperation implements IoPeration{
    @Override
    public void work(BookList bookList) {
        System.out.println("显示图书");
        int currSize = bookList.getUseSized();  //获取到当前的书的数量
        for(int i = 0; i < currSize; i++) {
            Book book = bookList.getBook(i);
            System.out.println(book);
        }
    }


}
