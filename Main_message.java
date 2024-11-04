import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.bind.Element;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        //test02();
        //test03();
        //test04();
        //test05();
        //test06();
        test07();
        }

    private static void test07() throws InterruptedException, IOException {
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.baidu.com/");
        webDriver.findElement(By.cssSelector("#kw")).sendKeys("软件测试");
        webDriver.findElement(By.cssSelector("#su")).click();
        sleep(3000);
        File file = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File("E://rjTest.png"));
    }

    private static void test06() throws InterruptedException {
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.baidu.com/");
        webDriver.findElement(By.cssSelector("#kw")).sendKeys("520");
        webDriver.findElement(By.cssSelector("#su")).click();
        sleep(3000);
        //找到图片按钮
        WebElement element = webDriver.findElement(By.cssSelector("#\\32  > div > div > div._content-border_zc167_4.contentBorder_6TrvU.cu-border.sc-aladdin.sc-cover-card > div > div.sc-header_5wgf6 > div.avatar-wrapper_2Tw9Y > div > a > img"));
        //鼠标右击
        Actions actions = new Actions(webDriver);
        sleep(3000);
        actions.moveToElement(element).contextClick().perform();
    }

    private static void test05() throws InterruptedException {
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.baidu.com/");
        webDriver.findElement(By.cssSelector("#kw")).sendKeys("521");
        webDriver.findElement(By.cssSelector("#su")).click();
        sleep(3000);
        //control+a
        webDriver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"A");
        sleep(3000);
        //control+x
        webDriver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"X");
        sleep(3000);
        //control+v
        webDriver.findElement(By.cssSelector("#kw")).sendKeys(Keys.CONTROL,"V");
        sleep(3000);
    }

    private static void test04() throws InterruptedException {
        //打开百度首页
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get("https://www.baidu.com");
        //搜索521 #kw-->表示搜素框
        webDriver.findElement(By.cssSelector("#kw")).sendKeys("521");
        webDriver.findElement(By.cssSelector("#su")).click();
        sleep(3000);
        //浏览器后退
        webDriver.navigate().back();
        //强制等待3秒
        sleep(3000);
        webDriver.navigate().refresh();
        //浏览器前进
        webDriver.navigate().forward();
        //滚动条演示
        sleep(3000);
        //10000表示浏览器页面的最底部
        ((JavascriptExecutor)webDriver).executeScript("document.documentElement.scrollTop=10000");
        webDriver.manage().window().maximize();
        sleep(3000);
        //全屏模式
        webDriver.manage().window().fullscreen();
        sleep(3000);
        webDriver.manage().window().setSize(new Dimension(600,1000));
    }

    private static void test03() {
        //创建驱动
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        //打开百度首页
        webDriver.get("https://www.baidu.com/");
        //判断元素是否可以被点击
        //显示等待会
        WebDriverWait wait = new WebDriverWait(webDriver,3000);
        //表达式框框中的元素是否可以被点击
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#bottom_layer > div > p:nth-child(6) > a")));
    }

    private static void test02() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get("https://www.baidu.com");
        String url = webDriver.getCurrentUrl();
        String title = webDriver.getTitle();
        if(url.equals("https://www.baidu.com/") && title.equals("百度一下，你就知道")) {
            System.out.println("测试通过");
            System.out.println("当前路径是: " + url + " ,"+ "当前标题是: " + title);
        } else {
            System.out.println("测试不通过");
        }
    }

        private static void test01() throws InterruptedException {
            ChromeOptions options = new ChromeOptions();
            //允许所有请求
            options.addArguments("--remote-allow-origins=*");
            WebDriver webDriver = new ChromeDriver(options);
            //打开百度首页
            webDriver.get("https://www.baidu.com");
            //找到百度搜索输入框
            WebElement element = webDriver.findElement(By.cssSelector(".s_ipt"));
            //输入软件测试
            element.sendKeys("软件测试");
            //找百度一下的按钮并点击一下
            webDriver.findElement(By.cssSelector("#su")).click();
            sleep(3000);
            //校验
            //找到搜索结果
            List<WebElement> elements = webDriver.findElements(By.cssSelector("a em"));
            int flg = 0;
            for(int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getText().contains("测试")) {
                    System.out.println("测试通过");
                    flg = 1;
                    break;
                }
            }
            if(flg == 0) {
                System.out.println("测试不通过");
            }
        }
    }


