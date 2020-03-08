package landsky.library.config;

import lombok.Data;

@Data
public class CommonRes {
    private String status;
    private Object data;

    public static CommonRes create(Object result) {
        return CommonRes.create(result, "success");
    }

    public static CommonRes create(Object result,String status) {
        CommonRes commonRes = new CommonRes();
        commonRes.setData(result);
        commonRes.setStatus(status);
        return commonRes;
    }
}
