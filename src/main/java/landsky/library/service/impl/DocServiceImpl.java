package landsky.library.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import landsky.library.entity.Category;
import landsky.library.entity.Doc;
import landsky.library.mapper.DocMapper;
import landsky.library.service.ICategoryService;
import landsky.library.service.IDocService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wcn
 * @since 2020-03-09
 */
@Service
@Slf4j
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements IDocService {
    @Autowired
    private RestHighLevelClient highLevelClient;
    @Autowired
    private ICategoryService categoryService;

    @Override
    public void insertEs() {
        List<Doc> list = list();

        list.forEach(li -> {
            System.out.println(JSONObject.toJSON(li));

//                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
//                xContentBuilder.field("document",li.getDocument());
//                xContentBuilder.field("name",li.getName());
//                xContentBuilder.field("id",li.getId());
//                xContentBuilder.field("tags",li.getTags());
//                xContentBuilder.field("category_id",li.getCategoryId());
//                xContentBuilder.field("category_name",ca.get(li.getCategoryId()));
//                xContentBuilder.endObject();
            Map<String, Object> map = new HashMap<>();
            map.put("document", li.getName() + "。" + li.getDocument());
//            map.put("name", li.getName());
            map.put("id", li.getId());
            map.put("tags", li.getTags());
            map.put("category_id", li.getCategoryId());
            map.put("category_name", ca.get(li.getCategoryId()));
            IndexRequest indexRequest = new IndexRequest("doc").id(li.getId().toString()).source(map);
            IndexResponse index;
            try {
                index = highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public Map<String, Object> searchES(String keyword, String tags, Integer categoryId, Integer from, Integer size, Integer orderBy) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (keyword == null || keyword.isEmpty()) {
            Page<Doc> page = new Page<>();
            Page<Doc> page1 = page(page);
            List<Doc> records = page1.getRecords();
            if (orderBy != null && orderBy == 1) {
                Collections.sort(records);
            }
            result.put("pageIndex", from / size + 1);
            result.put("pageSize", size);
            result.put("pageTotal",page1.getTotal());
            result.put("success", true);
            result.put("object", records);
            return result;
        }

        JSONObject jsonRequestObj = new JSONObject();
        //source
        jsonRequestObj.put("_source", "*");
        //query
        jsonRequestObj.put("query", new JSONObject());
        //构建function score
        jsonRequestObj.getJSONObject("query").put("function_score", new JSONObject());
        //构建function score内的query
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("query", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").put("bool", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").put("must", new JSONArray());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").add(new JSONObject());

        int queryIndex = 0;
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").getJSONObject(queryIndex).put("match", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").put("document", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("document").put("query", keyword);
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("document").put("boost", 0.5);

        //构建第二个query的条件
//        queryIndex++;


        if (tags != null) {
            queryIndex++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("tags", tags);
        }
        if (categoryId != null) {
            queryIndex++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("category_id", categoryId);
        }
        //排序字段
        jsonRequestObj.put("sort", new JSONArray());
        jsonRequestObj.getJSONArray("sort").add(new JSONObject());
        jsonRequestObj.getJSONArray("sort").getJSONObject(0).put("_score", new JSONObject());
        jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "desc");

        //聚合字段
//        jsonRequestObj.put("aggs", new JSONObject());
//        jsonRequestObj.getJSONObject("aggs").put("group_by_tags", new JSONObject());
//        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").put("terms", new JSONObject());
//        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").getJSONObject("terms").put("field", "tags");

        jsonRequestObj.put("from", from);
        jsonRequestObj.put("size", size);

        String reqJson = jsonRequestObj.toJSONString();
        System.out.println(reqJson);

        Request request = new Request("GET", "/doc/_search");
        request.setJsonEntity(reqJson);
        Response response = highLevelClient.getLowLevelClient().performRequest(request);
        String s = EntityUtils.toString(response.getEntity());
        log.info(s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONObject hits = jsonObject.getJSONObject("hits");
        int pageTotal = hits.getJSONObject("total").getInteger("value");
        JSONArray jsonArray = hits.getJSONArray("hits");
        List<Doc> docs = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Integer id = json.getInteger("_id");
            Doc doc = getById(id);
            Category category = categoryService.getById(doc.getCategoryId());
            doc.setCategoryName(category.getName());

            docs.add(doc);
        }
        if (orderBy != null && orderBy == 1) {
            Collections.sort(docs);
        }
        result.put("pageIndex", from / size + 1);
        result.put("pageSize", size);
        result.put("pageTotal",pageTotal);
        result.put("success", true);
        result.put("object", docs);
        return result;
    }

    private static Map<Integer, String> ca = new HashMap<>();

    @PostConstruct
    public void init() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
        queryWrapper.ge("id", 20);
        List<Category> list = categoryService.list(queryWrapper);
        list.forEach(li -> {
            ca.put(li.getId(), li.getName());
        });
//        insertEs();
        log.info("ok");
    }

    public static Map<Integer, String> getCa() {
        return new HashMap<>(ca);
    }
}
