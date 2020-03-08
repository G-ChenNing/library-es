package landsky.library.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        return result;
    }

}
