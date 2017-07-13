package stepbystep.spider;


import utils.StringUtil;

/**
 * Created by dy on 2017/7/5.
 */
public class BaiKeCell {

    private int id;

    private String content;

    private String detail;

    private String url;

    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return StringUtil.connectStrings("http://baike.baidu.com/view/", String.valueOf(id), ".html");
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
