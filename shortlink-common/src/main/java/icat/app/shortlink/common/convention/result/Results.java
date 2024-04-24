package icat.app.shortlink.common.convention.result;

import icat.app.shortlink.common.convention.errorcode.BaseErrorCode;
import icat.app.shortlink.common.convention.errorcode.IErrorCode;
import icat.app.shortlink.common.convention.exception.AbstractException;

import java.util.Optional;

public final class Results {

    /**
     * 构造无内容成功响应
     * @return Result对象
     */
    public static Result success() {
        return new Result()
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 构造携带成功消息的成功响应
     * @param message 成功消息
     * @return Result对象
     */
    public static Result success(String message) {
        return new Result()
                .setCode(Result.SUCCESS_CODE)
                .setMessage(message);
    }

    /**
     * 构造携带数据的成功响应
     * @param data 数据
     * @return Result对象
     */
    public static Result success(Object data) {
        return new Result()
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     */
    public static Result failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    public static Result failure(IErrorCode errorCode) {
        return new Result()
                .setCode(errorCode.code())
                .setMessage(errorCode.message());
    }

}
