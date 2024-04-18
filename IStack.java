package Stack;

public interface IStack {

    //放入元素入栈
    void push(int x);
    //元素出栈
    int pop();
    //输出栈顶元素
    int peek();
    //求栈长
    int size();
    //栈是否为满
    boolean empty();
    //栈是否空
    boolean full();
}
