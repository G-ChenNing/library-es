package landsky.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import landsky.library.entity.Category;
import landsky.library.mapper.CategoryMapper;
import landsky.library.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wcn
 * @since 2020-03-08
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
