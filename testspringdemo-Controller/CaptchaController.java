package com.example.demo.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RequestMapping("/admin")
@RestController
public class CaptchaController {
    private static final String KAPTCHA_SESSION = "HOME_KAPTCHA_SESSION_KEY";
    private static final String KAPTCHA_DATE = "HOME_KAPTCHA_SESSION_DATE";
    //验证码的过期时间
    private static final Long SESSION_TIME = 60*1000L;
    //验证成功:true
    //验证失败:false

    /*
    1.从session中获取验证码
    2.比对和前端获取到的验证码
     */
    @RequestMapping("/check")
    public Boolean check(String captcha, HttpSession session) {
        if(!StringUtils.hasLength(captcha)) {
            return false;
        }
       //1.从session中获取验证码
         String saveCaptcha = (String)session.getAttribute(KAPTCHA_SESSION);
         Date date = (Date) session.getAttribute(KAPTCHA_DATE);
       //2.比对和前端获取到的验证码
        if(captcha.equals(saveCaptcha)) {
            //比对日期是否相等
            //当前的时间-生成验证码过期的时间<验证码超时的时间就表示正确的
            if(date == null || System.currentTimeMillis() - date.getTime() < SESSION_TIME) {
                return true;
            }
        }
        return false;
    }
}
