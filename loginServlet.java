package servlet;

import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class loginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.读取参数中的用户名和密码
        req.setCharacterEncoding("utf8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //2.验证参数是否合理
        if(username == null || username.length() == 0 || password == null || password.length() == 0) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("您输入的用户名或密码为空");
            return;
        }
        //查询数据库验证密码和用户名是否正确
        UserDao userDao = new UserDao();
        User user = userDao.getUserByName(username);
            if(user == null) {
                //此时表示用户名不存在
                resp.setContentType("text/html;charset=utf8");
                resp.getWriter().write("您输入的用户名或密码不存在");
                return;
            }
            if(!password.equals(user.getPassword())) {
                //此时表示密码错误
                resp.setContentType("text/html;charset=utf8");
                resp.getWriter().write("您输入的密码或用户名错误");
                return;
            }
            //3.创建会话
            HttpSession session = req.getSession(true);
            session.setAttribute("user",user);
            //4.跳转到主页
            resp.sendRedirect("blog_list.html");
        }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //通过这个方法来反馈当前用户的登录状态
        //就是查看会话是否存在
        HttpSession session = req.getSession(false);
        if(session == null) {
            //表示当前的用户不存在
            resp.setStatus(403);
            return;
        }
        User user = (User)session.getAttribute("user");
        if(user == null) {
            //user对象不存在,同样也属于未登录状态
            resp.setStatus(403);
            return;
        }
        //此时表明两个都存在,返回200
        resp.setStatus(200);
    }
}

