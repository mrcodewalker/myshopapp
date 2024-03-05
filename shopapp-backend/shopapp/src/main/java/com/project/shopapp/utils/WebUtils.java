package com.project.shopapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
public class WebUtils {
    public static HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
