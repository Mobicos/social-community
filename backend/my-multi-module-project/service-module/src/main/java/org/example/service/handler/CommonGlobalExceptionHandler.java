package org.example.service.handler;

import org.example.model.JsonResponse;
import org.example.model.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器，用于捕获并统一处理系统中抛出的异常
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    /**
     * 处理所有类型的异常
     *
     * @param request HttpServletRequest对象，包含请求相关信息
     * @param e       捕获到的异常对象
     * @return JsonResponse<String> 统一格式的响应结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {

        String errorMsg = e.getMessage();
        if (e instanceof ConditionException) {
            // 如果是自定义的条件异常，获取其错误码和错误信息
            String errorCode = ((ConditionException) e).getCode();
            return new JsonResponse<>(errorCode, errorMsg);
        } else {
            // 其他类型的异常统一返回500错误码
            return new JsonResponse<>("500", errorMsg);
        }
    }
}
