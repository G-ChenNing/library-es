package landsky.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import landsky.library.entity.Shop;
import landsky.library.mapper.ShopMapper;
import landsky.library.service.IShopService;
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
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

}
