package libaray_item.book;

//在这个类中我们描述的是书架的类型
//
public class BookList {
    private Book[] books;
    private int useSized;  //记录书架实际存放的书的数量

    public BookList() {
        this.books = new Book[10];
        this.books[0] = new Book("三国演义","罗贯中",10,"小说");
        this.books[1] = new Book("西游记","吴承恩",9,"小说");
        this.books[2] = new Book("红楼梦","曹雪芹",19,"小说");

        this.useSized = 3;
    }

    public int getUseSized() {
        return useSized;
    }

    public void setUseSized(int useSized) {
        this.useSized = useSized;
    }

    public Book getBook(int pos) {
        return books[pos];
    }

    public void setBook(int pos, Book book) {
        //在pos位置放一本book书
        books[pos] = book;
    }
}
