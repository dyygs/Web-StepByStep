import stepbystep.local.BaikeDb;
import stepbystep.spider.BaiKe;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dy on 2017/7/13.
 */
@WebServlet("/Init")
public class Init extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        this.doPost(req,res);
        BaikeDb.getInstance().createTable();
        BaiKe baiKe = new BaiKe();
        baiKe.start();
    }

    // 处理post请求
    public void doPost(HttpServletRequest req,HttpServletResponse res){
        // doGet doPost合二为一

    }
}