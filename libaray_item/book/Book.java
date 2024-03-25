package libaray_item.book;

//在这个类中,我们写的是书的一些属性
//包括书的名字 书的作者的名字 书的类型(比如说是文学/科幻)之类的书
//书的价格 以及书有没有被借出
public class Book {

    private String name;
    private String author;
    private int price;
    private String type;
    private boolean IsBorrowed;

    public Book(String name, String author, int price, String type) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBorrowed() {
        return IsBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        IsBorrowed = borrowed;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' + ","+
                ((isBorrowed() == true) ? "已被借出" : "未被借出" )+
                '}';
    }

}
