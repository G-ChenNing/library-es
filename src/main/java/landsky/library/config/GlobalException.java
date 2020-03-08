//package landsky.library.config;
//
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@ControllerAdvice
//public class GlobalException {
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public CommonRes doError(HttpServletRequest request, HttpServletResponse response, Exception ex) {
//        if (ex instanceof BusinessException) {
//            return CommonRes.create(((BusinessException) ex).getCommonError(), "fail");
//        } else if (ex instanceof NoHandlerFoundException) {
//            CommonError commonError = new CommonError(EmBusinessError.NO_HANDLER_FOUND);
//            return CommonRes.create(commonError, "fail");
//        }  else if (ex instanceof ServletRequestBindingException) {
//            CommonError commonError = new CommonError(EmBusinessError.BIND_EXCEPTION_ERROR);
//            return CommonRes.create(commonError, "fail");
//        } else {
//            CommonError commonError = new CommonError(EmBusinessError.UNKNOWN_FOUND);
//            return CommonRes.create(commonError, "fail");
//        }
//    }
//
//}
