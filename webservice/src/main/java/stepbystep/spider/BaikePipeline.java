package stepbystep.spider;

import stepbystep.local.BaikeDb;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static stepbystep.spider.BaiKeCommonValue.KEY_BAI_KE_CELL;
import static stepbystep.spider.BaiKeCommonValue.KEY_CATEGORY;
import static stepbystep.spider.BaiKeCommonValue.KEY_CATEGORY_URL_LIST;


/**
 * Created by dy on 2017/7/6.
 */
public class BaikePipeline implements Pipeline {

    public BaikePipeline() {
    }

    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> map =  resultItems.getAll();
        if (map.containsKey(KEY_BAI_KE_CELL)) {
            BaiKeCell baiKeCell = (BaiKeCell) map.get(KEY_BAI_KE_CELL);
            BaikeDb.getInstance().insertBaikeCell(baiKeCell);
        } else if (map.containsKey(KEY_CATEGORY)) {
           List<String> list = (List<String>) map.get(KEY_CATEGORY_URL_LIST);
           String category = map.get(KEY_CATEGORY).toString();
           List<Integer> itemIds = new ArrayList<Integer>();
           for (String s : list) {
               s = s.replace("http://baike.baidu.com/view/", "");
               s = s.replace(".html", "");
               int itemId = Integer.parseInt(s);
               itemIds.add(itemId);
           }
           BaikeDb.getInstance().insertGategoryAndIds(itemIds, category);
        }
    }

}
