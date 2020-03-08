package landsky.library.config;

import lombok.Data;

public enum EmBusinessError {
    NO_OBJECT_FOUND(10001, "请求对象不存在"),
    UNKNOWN_FOUND(10002, "未知错误"),
    NO_HANDLER_FOUND(10003, "未找到对应handler"),
    BIND_EXCEPTION_ERROR(10004, "请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005,"请求参数校验失败"),
    REGISTER_EXCEPTION_ERROR(20001, "注册失败，用户已存在"),
    LOGIN_FAIL(20002,"手机号或密码错误"),

    //admin相关错误
    ADMIN_SHOULD_LOGIN(30001,"管理员需要先登录");

    private Integer errCode;
    private String errMsg;

    EmBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
