package stepbystep.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

import static stepbystep.spider.BaiKeCommonValue.KEY_BAI_KE_CELL;
import static stepbystep.spider.BaiKeCommonValue.KEY_CATEGORY;
import static stepbystep.spider.BaiKeCommonValue.KEY_CATEGORY_URL_LIST;


/**
 * Created by dy on 2017/7/5.
 */
public class BaiKe implements PageProcessor {
    public static final String URL_LIST_SUFFIX = "/\\w+";
    public static final String URL_LIST_PREFIX = "https://baike.baidu.com";
    public static final String URL_CAT_SUFFIX = "http://baike\\.baidu\\.com/view/\\d+\\.html";
    public static final String URL_POST_PREFIX = "https://dyygs.github.io";

    private Site site = Site
            .me()
            .setSleepTime(3000)
            .setDomain("baike.baidu.com")
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    public void process(Page page) {
        if (page.getUrl().regex("/view/.*").match()) {
            BaiKeCell baiKeCell = new BaiKeCell();
            baiKeCell.setContent(page.getHtml().xpath("//div[@class='lemma-summary']/allText()").toString());
            page.putField(KEY_BAI_KE_CELL, baiKeCell);
        }
        if (page.getUrl().toString().equals("https://baike.baidu.com")) {
            List<String> postList = page.getHtml().xpath("//dl[@class='cat ']").links().all();
            page.addTargetRequests(postList);
        }
        if (page.getUrl().regex("https://baike\\.baidu\\.com/\\w+").match()) {
            String a = page.getHtml().xpath("//div[@class='slider-item f-l item']//div[@class='clearfix article']//div[@class='pic']").toString();
            List<String> postList = page.getHtml().xpath("//div[@class='slider-item f-l item']//div[@class='clearfix article']//div[@class='pic']")
                    .links().regex(URL_CAT_SUFFIX).all();
            if (postList.size() == 0) {
                page.setSkip(true);
            } else {
                page.addTargetRequests(postList);
                page.putField(KEY_CATEGORY_URL_LIST, postList);
                page.putField(KEY_CATEGORY, page.getUrl().replace("https://baike.baidu.com/", ""));
            }
        }
    }

    public Site getSite() {
        return site;
    }

    public void start() {
        Spider
                .create(new BaiKe())
                .addUrl("https://baike.baidu.com")
                .addPipeline(new BaikePipeline())
                .thread(5)
                .run();

    }
}
