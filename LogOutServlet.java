package servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //拿到会话对象
        HttpSession session = req.getSession(false);
        if(session == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前您尚未登录");
            return;
        }
        //删除其中的user属性
        session.removeAttribute("user");
        //跳转到博客登录页面
        resp.sendRedirect("login.html");
    }
}
