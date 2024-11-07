import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Message {
    public String from;
    public String to;
    public String message;
}

@WebServlet("/message")
public class messageServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    //引入数据库就不需要了
    //private List<Message> messageList = new ArrayList<>();
    private DataSource dataSource = new MysqlDataSource();
    @Override
    public void init() {
        //1.创建数据源
        ((MysqlDataSource)dataSource).setURL("jdbc:mysql://127.0.0.1:3306/message_wall?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource)dataSource).setUser("root");
        ((MysqlDataSource)dataSource).setPassword("rootroot");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取前端发来的数据把这个数据保存到服务器这边
        Message message = objectMapper.readValue(req.getInputStream(),Message.class);
        System.out.println("请求中收到的message: " + message);
        //使用一个集合类 把所有的内容保存进去
        //但是把数据存到内存中并不可取 一刷新可能就没了
        //最好的结果是存到数据库中
        //messageList.add(message);

        //从数据库中插入数据
        try {
            save(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.setStatus(200);
        resp.getWriter().write("ok");
    }

    private void save(Message message) throws SQLException {
        //通过这个方法将数据插入到数据库中
        //1.创建数据源(见上)

        //2.建立连接
        Connection connection = dataSource.getConnection();

        //3.构造sql语句
        String sql = "insert into message values(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, message.from);
        statement.setString(2, message.to);
        statement.setString(3, message.message);

        //4.执行sql
        statement.executeUpdate();

        //5.回收资源
        statement.close();
        connection.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("application/json; charset=utf8");

        //从数据库中查询数据
        List<Message> messageList = null;
        try {
            messageList = load();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String responseJson = objectMapper.writeValueAsString(messageList);
        resp.getWriter().write(responseJson);
    }

    private List<Message> load() throws SQLException {
        //从数据库中查询数据
        //1.建立连接
        Connection connection = dataSource.getConnection();
        //2.构造sql
        String sql = "select * from message";
        PreparedStatement statement = connection.prepareStatement(sql);
        //3.执行sql
        ResultSet resultSet = statement.executeQuery();
        //4.遍历结果集
        List<Message> list = new ArrayList<>();
        while(resultSet.next()) {
            Message message = new Message();
            message.from = resultSet.getString("from");
            message.to = resultSet.getString("to");
            message.message = resultSet.getString("message");
            list.add(message);
        }
        //5.关闭资源
        resultSet.close();
        connection.close();
        statement.close();
        //6.返回list
        return list;
    }
}
