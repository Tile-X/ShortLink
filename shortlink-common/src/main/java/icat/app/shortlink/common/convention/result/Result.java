package icat.app.shortlink.common.convention.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = 8633292427103764982L;

    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "0";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 判断请求是否成功
     * @return 请求成功状态
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public <T> T getData(Class<T> clazz) { return clazz.cast(data); }

}
