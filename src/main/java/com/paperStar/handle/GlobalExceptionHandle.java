package com.paperStar.handle;
import com.paperStar.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        Result r = new Result();
        r.setCode(e.getCode());
        r.setMsg(e.getMsg());
        return r;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error("error");
    }
}
