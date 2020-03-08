package landsky.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import landsky.library.config.BusinessException;
import landsky.library.entity.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wcn
 * @since 2020-03-03
 */
public interface IUserService extends IService<User> {

    User register(User user) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    User login(String telphone,String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException;

    Integer countAllUser();

    Map<String, Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby,Integer categoryId,String tags) throws IOException;

}
