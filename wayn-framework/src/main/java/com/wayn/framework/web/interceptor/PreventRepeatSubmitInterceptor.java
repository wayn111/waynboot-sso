package com.wayn.framework.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayn.common.annotation.RepeatSubmit;
import com.wayn.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 防重复提交拦截器
 */
@Slf4j
@Component
public abstract class PreventRepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request)) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    try (PrintWriter writer = response.getWriter()) {
                        writer.print(new ObjectMapper().writeValueAsString(R.error("不允许重复提交，请稍后再试")));
                        writer.flush();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request 请求对象
     * @return boolean
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request) throws Exception;


}
