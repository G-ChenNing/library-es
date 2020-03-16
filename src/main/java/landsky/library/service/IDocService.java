package landsky.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import landsky.library.entity.Doc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wcn
 * @since 2020-03-09
 */
public interface IDocService extends IService<Doc> {
    public void insertEs();
    Map<String, Object> searchES(String keyword, String tags, Integer categoryId, Integer from, Integer size, Integer orderBy)throws IOException;

}
