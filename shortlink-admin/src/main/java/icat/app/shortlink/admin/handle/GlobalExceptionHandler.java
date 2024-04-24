package icat.app.shortlink.admin.handle;

import icat.app.shortlink.common.convention.errorcode.BaseErrorCode;
import icat.app.shortlink.common.convention.exception.AbstractException;
import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = AbstractException.class)
    public Result abstractException(HttpServletRequest request, AbstractException e) {
        if (e.getCause() != null) {
            log.error("[{}] {} [ERROR] {}", request.getMethod(), request.getRequestURL().toString(), e.toString(), e.getCause());
            return Results.failure(e);
        }
        log.error("[{}] {} [ERROR] {}", request.getMethod(), request.getRequestURL().toString(), e.toString());
        return Results.failure(e);
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error("[{}] {} [SYSTEM_ERROR] {}", request.getMethod(), request.getRequestURL().toString(), e.toString());
        return Results.failure(BaseErrorCode.SERVICE_ERROR);
    }

}
