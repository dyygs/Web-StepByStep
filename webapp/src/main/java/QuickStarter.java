import stepbystep.local.BaikeDb;
import stepbystep.spider.BaiKeCell;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by dy on 2017/7/6.
 */
@WebServlet("/QuickStarter")
public class QuickStarter extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        this.doPost(req,res);
        try{
            //设置响应内容类型
            res.setContentType("text/html;charset=UTF-8");
            PrintWriter pw = res.getWriter();
            BaiKeCell baiKeCell = BaikeDb.getInstance().getRandomBaiKeCell();
            if (baiKeCell != null) {
                pw.println("<h5> url =  " +baiKeCell.getUrl() + "\ncontent = " + baiKeCell.getContent() + "\n"+ "</h5>");
            } else {
                pw.println("<h5> Sorry! Can't find a BaiKe </h5>");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 处理post请求
    public void doPost(HttpServletRequest req,HttpServletResponse res){
        // doGet doPost合二为一

    }
}
