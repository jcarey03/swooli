package com.swooli.web.util;

import com.swooli.security.StringDigester;
import com.swooli.security.StringEncrypter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String SESSION_COOKIE_NAME = "swooli-session-id";

    public static String FB_STATE_COOKIE_NAME = "swooli-fb-state";

    private static String SESSION_COOKIE_VALUE_SEPARATOR = "___";

    public static void addFacebookStateCookie(final String state, final HttpServletResponse response) {
        final Cookie stateCookie = new Cookie(FB_STATE_COOKIE_NAME, state);
        response.addCookie(stateCookie);
    }

    public static Cookie getFacebookStateCookie(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(final Cookie cookie : cookies) {
                if(FB_STATE_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void deleteFacebookStateCookie(final HttpServletResponse response) {
        final Cookie sessionCookie = new Cookie(FB_STATE_COOKIE_NAME, null);
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
    }

    public static Cookie getSessionCookie(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(final Cookie cookie : cookies) {
                if(SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void deleteSessionCookie(final HttpServletResponse response) {
        final Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, null);
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
    }

    public static void addSessionCookie(final String sessionId, final int maxAge, final HttpServletResponse response) {
        response.addCookie(createSessionCookie(sessionId, maxAge));
    }

    public static Cookie createSessionCookie(final String sessionId, final int maxAge) {
        final Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, createSessionCookieValue(sessionId));
        sessionCookie.setMaxAge(maxAge);
        sessionCookie.setPath("/");
        return sessionCookie;
    }

    public static String createSessionCookieValue(final String sessionId) {

        // format: <cipher text of profile id><separator><md5 hash of user profile id>

        final String cipherText = StringEncrypter.INSTANCE.encrypt(sessionId);
        final String hash = StringDigester.INSTANCE.digest(sessionId);
        return new StringBuilder(cipherText).append(SESSION_COOKIE_VALUE_SEPARATOR).append(hash).toString();
    }

    public static String getSessionId(final HttpServletRequest request) {

        final Cookie sessionCookie = getSessionCookie(request);
        if(sessionCookie == null) {
            return null;
        }

        final String cookieValue = sessionCookie.getValue();
        final int sepIdx = cookieValue.lastIndexOf(SESSION_COOKIE_VALUE_SEPARATOR);
        if(sepIdx < 0) {
            return null;
        } else {

            final String cipherText = cookieValue.substring(0, sepIdx);
            final String plainText = StringEncrypter.INSTANCE.decrypt(cipherText);
            final String hash = cookieValue.substring(sepIdx + SESSION_COOKIE_VALUE_SEPARATOR.length());

            if(StringDigester.INSTANCE.matches(plainText, hash)) {
                return plainText;
            } else {
                return null;
            }
        }
    }

}