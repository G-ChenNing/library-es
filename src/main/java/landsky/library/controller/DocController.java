package landsky.library.controller;


import landsky.library.entity.Doc;
import landsky.library.service.IDocService;
import landsky.library.service.impl.DocServiceImpl;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wcn
 * @since 2020-03-09
 */
@RestController
@RequestMapping("/doc")
public class DocController extends BaseController {

    @Autowired
    private IDocService docService;

    @RequestMapping("/query")
    @ResponseBody
    public Map<String, Object> searchES(String keyword, String tags, Integer categoryId, Integer pageIndex, Integer pageSize) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> stringObjectMap = docService.searchES(keyword, tags, categoryId, pageIndex == null ? 0 : (pageIndex - 1) * pageSize, pageSize == null ? 10 : pageSize);
        result.put("success", true);
        result.put("object", stringObjectMap);
        return result;
    }

    @RequestMapping("/type")
    @ResponseBody
    public Map<String, Object> getType() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("object", DocServiceImpl.getCa());
        return result;
    }

    String folder = "D:\\workspace\\library\\src\\main\\java\\landsky\\library\\controller\\file\\";
    @GetMapping("/{id}")
    public void download(HttpServletResponse response, @PathVariable("id") String id) {
        Doc doc = docService.getById(id);
        String path = folder + doc.getName() + ".txt";
        File file = new File(path);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(doc.getDocument().getBytes(Charset.defaultCharset()));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream is = new FileInputStream(new File(folder, doc.getName() + ".txt"))
             ; OutputStream os = response.getOutputStream();) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");
            IOUtils.copy(is, os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
