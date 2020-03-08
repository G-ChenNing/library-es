package landsky.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import landsky.library.entity.Seller;
import landsky.library.mapper.SellerMapper;
import landsky.library.service.ISellerService;
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
public class SellerServiceImpl extends ServiceImpl<SellerMapper, Seller> implements ISellerService {

}
