package landsky.library.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import landsky.library.entity.Doc;
import landsky.library.service.IDocService;
import landsky.library.service.impl.DocServiceImpl;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

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
    public Map<String, Object> searchES(String keyword, String tags, Integer categoryId, Integer pageIndex, Integer pageSize,Integer orderBy) throws IOException {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> docs= docService.searchES(keyword, tags, categoryId, pageIndex == null ? 0 : (pageIndex - 1) * pageSize, pageSize == null ? 10 : pageSize, orderBy);
        result.put("success", true);
        result.put("object", docs);
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

    static String folder;
    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            folder = "D:\\";
        } else {
            folder = "/home/";
        }
    }

    @GetMapping("/{id}")
    public  Map<String, Object> queryOne(@PathVariable("id") String id) {
        Map<String, Object> result = new HashMap<>();
        if (id == null || id.isEmpty()) {
            result.put("success", false);
            result.put("message", "blank");
            return result;
        }
        result.put("success", true);
        result.put("object", docService.getById(id));
        return result;
    }

    @RequestMapping("/multi_downliad")
    public void download(HttpServletResponse response, @RequestParam("ids[]") String[] ids) {
        if (ids == null) {
            return;
        }
        String path = folder + rotatingHash(Arrays.toString(ids),64) + ".txt";
        File file = new File(path);


        try (FileOutputStream outputStream= new FileOutputStream(file);){
            for (String id : ids) {
                Doc doc = docService.getById(id);
                outputStream.write(doc.getName().getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(doc.getDocument().getBytes(Charset.defaultCharset()));
                outputStream.write("\n".getBytes());
                outputStream.write("__________________________________________________________".getBytes());
                outputStream.write("\n".getBytes());
                doc.setDownload(doc.getDownload() + 1);
                docService.updateById(doc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream is = new FileInputStream(new File(path))
             ; OutputStream os = response.getOutputStream();) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");
            IOUtils.copy(is, os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    static int rotatingHash(String key, int prime)
    {
        int hash, i;
        for (hash=key.length(), i=0; i<key.length(); ++i)
            hash = (hash<<4)^(hash>>28)^key.charAt(i);
        return (hash % prime);
    }



    public void generateHtml(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));
        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(mdContent);

        System.out.println(htmlContent);
    }
    public static void main(String[] args) throws IOException {
        DocController pageGenerator = new DocController();
        pageGenerator.generateHtml(new File("D:\\README.md"));
    }
}
