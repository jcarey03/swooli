package com.swooli.web.controller;

import com.swooli.ApplicationProperties;
import com.swooli.bl.FacebookService;
import com.swooli.bl.UserService;
import com.swooli.bo.user.UserProfile;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.security.StringDigester;
import com.swooli.web.bean.BasicLoginBean;
import com.swooli.web.bean.FacebookLoginBean;
import com.swooli.web.security.AuthenticatedAspect;
import com.swooli.web.util.CookieUtils;
import com.swooli.web.util.WebUtils;
import com.swooli.web.util.facebook.FacebookApi;
import com.swooli.web.util.facebook.FacebookApi.AccessToken;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final String FB_LOGIN_ERROR_ATTR = "fbLoginError";

    private static final String BASIC_LOGIN_ERROR_ATTR = "basicLoginError";

    @Autowired
    private UserService userService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private FacebookApi facebookApi;

    @RequestMapping("/login")
    public String login(
            @RequestParam(required=false) String loginType,
            final BasicLoginBean basicLoginBean,
            final FacebookLoginBean fbLoginBean,
            @CookieValue(value="swooli-fb-state", required=false) Cookie fbStateCookie,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        if("basic".equals(loginType)) {

            UserProfile userProfile;
            try {
                userProfile = userService.retrieveUserProfileByEmail(basicLoginBean.getEmail());
                if(!StringDigester.INSTANCE.matches(basicLoginBean.getPassword(), userProfile.getPassword())) {
                    userProfile = null;
                }
            } catch(final ObjectNotFoundException onfe) {
                userProfile = null;
            }

            if(userProfile == null) {
                model.addAttribute(BASIC_LOGIN_ERROR_ATTR, "Email or password does not match.");
                return "login";
            } else {

                final int maxAge;
                if(basicLoginBean.isRememberMe()) {
                    maxAge = (int) TimeUnit.SECONDS.convert(ApplicationProperties.getRememberMeDays(), TimeUnit.DAYS);
                } else {
                    maxAge = -1;
                }

                userProfile.setSessionExpiry(maxAge);
                userService.loginUser(userProfile.getId(), maxAge);
                CookieUtils.addSessionCookie(userProfile.getId(), maxAge, response);

                return "redirect:" + getSuccessfulLoginRedirectUrl(request);
            }
        } else if("facebook".equals(loginType) || (fbLoginBean != null && fbLoginBean.isPopulated())) {
            if(StringUtils.isBlank(fbLoginBean.getCode())) {
                if(StringUtils.isBlank(fbLoginBean.getError())) {
                    return beginFacebookLogin(request, response);
                } else {
                    logger.info(String.format("Facebook login error: [%s, %s, %s]", fbLoginBean.getError(), fbLoginBean.getError_reason(), fbLoginBean.getError_description()));
                    model.addAttribute(FB_LOGIN_ERROR_ATTR, "Swooli did not receive permission to your Facebook account.");
                    return "login";
                }
            } else if(fbStateCookie != null && StringUtils.isNotBlank(fbLoginBean.getState())) {
                return finishFacebookLogin(request, response, model, fbStateCookie, fbLoginBean.getState(), fbLoginBean.getCode());
            } else {
                model.addAttribute(FB_LOGIN_ERROR_ATTR, "Swooli was unable to access your Facebook account.  Try again later or login using your email and password.");
                return "login";
            }
        } else {
            // request to view login page
            model.addAttribute("basicLoginBean", basicLoginBean);
            return "login";
        }
    }

    private String beginFacebookLogin(final HttpServletRequest request, final HttpServletResponse response) {
        // create unique state value
        final String stateId = StringDigester.INSTANCE.createRandomDigest();
        final String stateValue = StringDigester.INSTANCE.createRandomDigest();

        facebookService.createLoginState(stateId, stateValue);
        CookieUtils.addFacebookStateCookie(stateId, response);

        return "redirect:" + facebookApi.getLoginDialogRedirectUrl(request, stateValue);
    }

    private String finishFacebookLogin(final HttpServletRequest request, final HttpServletResponse response,
            final Model model, final Cookie stateCookie, final String state, final String code) {

        final String persisedStateValue = WebUtils.decodeUrl(facebookService.retrieveLoginState(stateCookie.getValue()));

        if(state.equals(persisedStateValue)) {

            final UserProfile fbUserProfile;
            try {

                // get access token
                final AccessToken accessToken = facebookApi.getAccessToken(request, code);

                // get facebook profile info
                fbUserProfile = facebookApi.getUserProfile(accessToken);

            } catch(final IOException ioe) {
                model.addAttribute(FB_LOGIN_ERROR_ATTR, ioe.getMessage() + " Swooli has been notified of the issue.");
                return"login";
            }

            // get profile from database
            UserProfile userProfile;
            boolean profileExists = false;
            try {
                userProfile = userService.retrieveUserProfileByFacebookId(fbUserProfile.getFbId());
                profileExists = true;
                // update the last login time
                userService.loginUser(userProfile.getId(), null);
            } catch(final ObjectNotFoundException onfe) {
                if(profileExists) {
                    throw onfe;
                } else {
                    // no profile exists, so create a user profile
                    userProfile = fbUserProfile;
                    userService.createUserProfile(userProfile);
                }
            }

            // add session cookie to response
            CookieUtils.addSessionCookie(userProfile.getId(), userProfile.getSessionExpiry(), response);

            return "redirect:" + getSuccessfulLoginRedirectUrl(request);

        } else {
            // CSRF
            model.addAttribute(FB_LOGIN_ERROR_ATTR, "Failed verifying integrity of Facebook connect.  Swooli has been notified of the issue.");
            return "login";
        }
    }

    private String getSuccessfulLoginRedirectUrl(final HttpServletRequest request) {
        final String redirectUrlParamValue = request.getParameter(AuthenticatedAspect.REDIRECT_URL_PARAM_NAME);
        if(StringUtils.isBlank(redirectUrlParamValue)) {
            return "/";
        } else {
            return redirectUrlParamValue;
        }
    }

}

































//    @RequestMapping(value="/facebook")
//    public String facebookLogin(
//            @RequestParam(value="code", required=false) String code,
//            @RequestParam(value="state", required=false) String state,
//            @RequestParam(value="error_reason", required=false) String errorReason,
//            @RequestParam(value="error", required=false) String error,
//            @RequestParam(value="error_description", required=false) String errorDescription,
//            @CookieValue(value="swooli-fb-state", required=false) Cookie fbStateCookie,
//            RedirectAttributes redirectAttrs,
//            HttpServletRequest httpRequest,
//            HttpServletResponse httpResponse) throws IOException {
//
//        final FacebookApi fbApi = new FacebookApi(
//            ApplicationProperties.get("swooli.facebook.app.id"),
//            ApplicationProperties.get("swooli.facebook.private.key"));
//
//        if(StringUtils.isBlank(code)) {
//
//            // check errors because user declined
//            if(StringUtils.isBlank(error)) {
//
//                // create unique state value
//                final String stateId = createRandomDigest();
//                final String stateValue = createRandomDigest();
//
//                facebookService.createLoginState(stateId, stateValue);
//                CookieUtils.addFacebookStateCookie(stateId, httpResponse);
//
//                return "redirect:" + fbApi.getLoginDialogRedirectUrl(httpRequest, stateValue);
//
//            }
//
//            redirectAttrs.addAttribute(FB_LOGIN_ERROR_ATTR, "It seems you declined Swooli permission to access your Facebook account.");
//            return "redirect:/login";
//
//        } else if(fbStateCookie != null && StringUtils.isNotBlank(state)) {
//
//            final String persisedStateValue = URLDecoder.decode(facebookService.retrieveLoginState(fbStateCookie.getValue()), "utf8");
//
//            if(state.equals(persisedStateValue)) {
//
//                final UserProfile fbUserProfile;
//                try {
//
//                    // get access token
//                    final AccessToken accessToken = fbApi.getAccessToken(httpRequest, code);
//
//                    // get facebook profile info
//                    fbUserProfile = fbApi.getUserProfile(accessToken);
//
//                } catch(final IOException ioe) {
//                    redirectAttrs.addAttribute(FB_LOGIN_ERROR_ATTR, ioe.getMessage() + " Swooli has been notified of the issue.");
//                    return"redirect:/login";
//                }
//
//                // get profile from database
//                UserProfile userProfile;
//                boolean profileExists = false;
//                try {
//                    userProfile = userService.retrieveUserProfileByFacebookId(fbUserProfile.getFbId());
//                    profileExists = true;
//                    // update the last login time
//                    userService.loginUser(userProfile.getId(), null);
//                } catch(final ObjectNotFoundException onfe) {
//                    if(profileExists) {
//                        throw onfe;
//                    } else {
//                        // no profile exists, so create a user profile
//                        userProfile = fbUserProfile;
//                        userService.createUserProfile(userProfile);
//                    }
//                }
//
//                // add session cookie to response
//                CookieUtils.addSessionCookie(userProfile.getId(), userProfile.getSessionExpiry(), httpResponse);
//
//                return "redirect:" + getSuccessfulLoginRedirectUrl(httpRequest);
//
//            } else {
//                // CSRF
//                redirectAttrs.addAttribute(FB_LOGIN_ERROR_ATTR, "Failed verifying integrity of Facebook connect.  Swooli has been notified of the issue.");
//                return "redirect:/login";
//            }
//
//        } else {
//
//            redirectAttrs.addAttribute(FB_LOGIN_ERROR_ATTR, "Swooli encounted an issue and apologizes for not being able to log you in.  Try again later or login using your email and password.  Swooli has been notified of the issue.");
//            return "redirect:/login";
//
//        }
//
//    }
//
//    @RequestMapping(method=RequestMethod.GET)
//    public String getLogin() {
//        return "login";
//    }
//
//    @RequestMapping(method=RequestMethod.POST)
//    public String submitLogin(
//            @RequestParam String email,
//            @RequestParam String password,
//            @RequestParam(value="rememberMe", defaultValue="false") String rememberMe,
//            Model model,
//            HttpServletRequest httpRequest,
//            HttpServletResponse httpResponse) throws IOException {
//
//        UserProfile userProfile;
//        try {
//            userProfile = userService.retrieveUserProfileByEmail(email);
//            if(!StringDigester.INSTANCE.matches(password, userProfile.getPassword())) {
//                userProfile = null;
//            }
//        } catch(final ObjectNotFoundException onfe) {
//            userProfile = null;
//        }
//
//        if(userProfile == null) {
//            model.addAttribute(EMAIL_PASSWORD_LOGIN_ERROR_ATTR, "Email or password does not match.");
//            return "login";
//        } else {
//
//            final int maxAge;
//            if(Boolean.valueOf(rememberMe)) {
//                maxAge = (int) TimeUnit.DAYS.convert(ApplicationProperties.getRememberMeDays(), TimeUnit.SECONDS);
//            } else {
//                maxAge = -1;
//            }
//
//            userProfile.setSessionExpiry(maxAge);
//
//            userService.loginUser(userProfile.getId(), maxAge);
//            CookieUtils.addSessionCookie(userProfile.getId(), maxAge, httpResponse);
//
//            return "redirect:" + getSuccessfulLoginRedirectUrl(httpRequest);
//        }
//
//    }
