import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet("/show")
public class showRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用上述api,把得到的结果构造成一个字符串返回给客户端
        StringBuilder sb = new StringBuilder();
        sb.append(req.getProtocol());
        sb.append("<br>");
        sb.append(req.getMethod());
        sb.append("<br");
        sb.append(req.getRequestURI());
        sb.append("<br");
        sb.append(req.getContextPath());
        sb.append("<br");
        sb.append(req.getQueryString());
        sb.append("<br");

        //获取所有的header
        Enumeration<String> headerNames = req.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = req.getHeader(key);
            sb.append(key +": "+ value + "<br>");
        }
        //告诉浏览器数据的类型是什么
        resp.setContentType("text/html; charset=utf8");
        //将获取的内容打包发到客户端
        resp.getWriter().write(sb.toString());
    }
}
