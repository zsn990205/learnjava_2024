package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/getAuthorInfo")
public class AuthorInfoServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.先拿到请求中的blogId
        String blogId = req.getParameter("blogId");
        if(blogId == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("请求中缺少blogId");
            return;
        }
        //2.在blog表中查询blog对象
        BlogDao blogDao = new BlogDao();
        Blog blog = blogDao.getBlog(Integer.parseInt(blogId));
        if(blog == null) {
            //表示此时的blog没找到
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前的blogId没查到");
            return;
        }
        //3.根据blog对象中的userId,从user表中查找对应作者
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(blog.getUserId());
        if(user == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前的userId没查到");
            return;
        }
        //4.确定找到user了把这个对象返回到浏览器这边
        //需要将其转换成json格式
        user.setPassword("");
        String respJson = objectMapper.writeValueAsString(user);
        resp.setContentType("application/json,charset=utf8");
        resp.getWriter().write(respJson);
    }
}
