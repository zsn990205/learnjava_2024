package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从会话中拿到用户信息并返回
        HttpSession session = req.getSession(false);
        if(session == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("您当前未登录");
            return;
        }
        User user = (User)session.getAttribute("user");
        if(user == null) {
            //说明当前没有这个用户
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前用户不存在");
            return;
        }
        //说明上面两个条件都不符合
        //把user对象转成json返回给浏览器
        resp.setContentType("application/json;charset=utf8");
        //注意user中含有password,这个是要进行加密操作的
        //需要把这里的密码设置为空
        user.setPassword("");
        String respJson = objectMapper.writeValueAsString(user);
        resp.getWriter().write(respJson);
    }
}
