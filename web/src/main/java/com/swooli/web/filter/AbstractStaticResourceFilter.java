package com.swooli.web.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractStaticResourceFilter implements Filter {

    private final Set<Pattern> staticResourceLocations = new HashSet<>();

    private ServletContext servletContext;

    @Override
    public void init(final FilterConfig filterChain) throws ServletException {
        servletContext = filterChain.getServletContext();
        final String staticResourceLocationsValue = filterChain.getInitParameter("staticResourceLocations");
        if(staticResourceLocationsValue != null) {
            for(final String location : staticResourceLocationsValue.split(",")) {
                if(StringUtils.isNotBlank(location)) {
                    staticResourceLocations.add(Pattern.compile(location.trim()));
                }
            }
        }
    }

    @Override
    public void destroy() {}

    protected boolean isStaticResource(final HttpServletRequest request) {
        for(final Pattern location : staticResourceLocations) {
             if(location.matcher(request.getServletPath()).matches()) {
                 return true;
             }
        }
        return false;
    }

    protected Set<Pattern> getStaticResourceLocations() {
        return Collections.unmodifiableSet(staticResourceLocations);
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

}