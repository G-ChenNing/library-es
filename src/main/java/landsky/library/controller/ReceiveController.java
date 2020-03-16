package landsky.library.controller;

import com.alibaba.fastjson.JSONObject;
import landsky.library.entity.Doc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/receive")
public class ReceiveController {
    @PostMapping("/plc")
    @ResponseBody
    public JSONObject reviceplc(@RequestBody String data) {
        log.info(data);
        JSONObject result = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            result.put("success", true);
            result.put("message", "推送成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "推送失败，未符合格式");
        }

//        List<Doc> list = list();
//
//        list.forEach(li -> {
//            System.out.println(JSONObject.toJSON(li));
//
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("document", li.getName() + "。" + li.getDocument());
////            map.put("name", li.getName());
//            map.put("id", li.getId());
//            map.put("tags", li.getTags());
//            map.put("category_id", li.getCategoryId());
//            IndexRequest indexRequest = new IndexRequest("doc").id(li.getId().toString()).source(map);
//            IndexResponse index;
//            try {
//                index = highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
        return result;
    }

}
