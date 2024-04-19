package Stack;

import java.util.Arrays;
import java.util.Stack;

public class MyStack implements IStack{
    //底层是数组
    private int[] elem;
    private int usedSize;
    private static final int Default = 10;

    public MyStack() {
        elem = new int[Default];
    }

    //栈的压入弹出
    public boolean IsPopOrder (int[] pushV, int[] popV) {
        Stack<Integer> stack = new Stack<>();
        int j = 0;
        for(int i = 0; i < pushV.length; i++) {
                stack.push(pushV[i]);
                //判断为空必须在前
                while(!stack.empty() && stack.peek() == popV[j] && j <popV.length) {
                    stack.pop();
                    j++;
                }
            }
        if(stack.empty()) {
            return true;
        }
       return false;
     }

    //有效的括号
    public boolean isValid(String s) {
        //1.先遍历整个字符串
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);   //右括号
            //2.判断是不是左括号
            if(ch == '{' || ch == '(' || ch == '[') {
                stack.push(ch);
            } else{
             //3.遇到右括号了
             //看看和右括号匹配不匹配
                if(stack.empty()) {
                    return false;
                }
            char ch2 = stack.peek();   //是左括号
            if((ch == '}' && ch2 == '{') || (ch == ']' && ch2 =='[') || (ch == ')' && ch2 =='(')) {
                stack.pop();
            } else {
                return false;
            }
            }
        }
        //当字符串遍历完并且栈中非空,则说明此时也不是匹配括号
        if(!stack.empty()) {
            return false;
        }
        return true;
    }

    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for(String x : tokens) {
            if(!isOperation(x)) {
                stack.push(Integer.parseInt(x));
            } else {
                int num2 = stack.pop();
                int num1 = stack.pop();
                switch(x) {
                    case "+" :
                    stack.push(num1+num2);
                    break;
                    case "-" :
                        stack.push(num1-num2);
                        break;
                    case "*" :
                        stack.push(num1*num2);
                        break;
                    case "/" :
                        stack.push(num1/num2);
                        break;
                }
            }
        }
        return stack.pop();
    }

    private boolean isOperation(String s) {
        if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
            return true;
        }
        return false;
    }

    @Override
    public void push(int x) {
        if(full()) {
            Arrays.copyOf(elem,elem.length*2);
        } else {
            elem[usedSize] = x;
            usedSize++;
        }
    }

    @Override
    public int pop() {
        if(empty()) {
            throw new myException("栈空了");
        }
        int old = elem[usedSize-1];
        usedSize--;  //相当于删除
        return old;
    }

    @Override
    public int peek() {
        if(empty()) {
            throw new myException("栈恐空了");
        }
        int old = elem[usedSize-1];
        return old;
    }

    @Override
    public int size() {
       if(empty()) {
           return 0;
       }
       return usedSize;
    }

    @Override
    public boolean empty() {
        if(usedSize == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean full() {
        if(usedSize == elem.length) {
            return true;
        }
        return false;
    }
}
