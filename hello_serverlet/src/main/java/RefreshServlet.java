import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//让页面每?s刷新一次
@WebServlet("/refresh")
public class RefreshServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("refresh","1");
        //写入当前的一个时间戳
        resp.getWriter().write(""+ System.currentTimeMillis());
    }
}
