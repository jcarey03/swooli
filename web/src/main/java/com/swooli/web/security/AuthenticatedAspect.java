package com.swooli.web.security;

import com.swooli.security.UserSessionContext;
import com.swooli.security.UserSessionContextHolder;
import com.swooli.web.util.WebUtils;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Aspect
@Component
public class AuthenticatedAspect {

    public static final String REDIRECT_URL_PARAM_NAME = "redirectUrl";

    @Around("@annotation(com.swooli.security.Authenticated)")
    public String processAttribute(ProceedingJoinPoint joinPoint) throws Throwable{

        final UserSessionContext ctx = UserSessionContextHolder.get();
        if(ctx == null) {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            final String queryString = request.getQueryString();
            final String requestUrl = (queryString == null) ?
                request.getRequestURL().toString() :
                request.getRequestURL().append("?").append(request.getQueryString()).toString();

            return String.format("redirect:login?%s=%s", REDIRECT_URL_PARAM_NAME, WebUtils.encodeUrl(requestUrl));

        } else {
            return (String) joinPoint.proceed(); //continue on the intercepted method
        }
    }

}
