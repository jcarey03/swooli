package com.swooli.web.filter;

import com.swooli.bl.UserService;
import com.swooli.bo.user.UserProfile;
import com.swooli.dao.DaoException;
import com.swooli.security.UserSessionContext;
import com.swooli.security.UserSessionContextHolder;
import com.swooli.web.util.CookieUtils;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class UserSessionFilter extends AbstractStaticResourceFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(!isStaticResource(request)) {

            final String sessionId = CookieUtils.getSessionId(request);
            if(sessionId != null) {

                final WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                final UserService userService = ctx.getBean("userService", UserService.class);

                try {
                    final UserProfile userProfile = userService.retrieveUserProfile(sessionId);

                    // set cookie to not persist
                    if(isSessionExpired(userProfile)) {
                        final Cookie sessionCookie = CookieUtils.createSessionCookie(sessionId, -1);
                        response.addCookie(sessionCookie);
                    }

                    // create user session context and add to thread local
                    final UserSessionContext userSessionCtx = new UserSessionContext();
                    userSessionCtx.setUserProfile(userProfile);
                    UserSessionContextHolder.set(userSessionCtx);

                } catch(final DaoException de) {
                    CookieUtils.deleteSessionCookie(response);
                }
            }
        }

        // continue filter chain
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserSessionContextHolder.unset();
        }

    }

    private boolean isSessionExpired(final UserProfile profile) {
        final long sessionExpiry = profile.getSessionExpiry();
        if(sessionExpiry > 0) {
            return new Date().before(new Date(profile.getSessionExpiry()));
        } else {
            return false;
        }
    }

}