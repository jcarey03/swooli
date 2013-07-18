package com.swooli.web.util.facebook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swooli.bo.user.Gender;
import com.swooli.bo.user.UserProfile;
import com.swooli.web.security.AuthenticatedAspect;
import com.swooli.web.util.WebUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for making remote Facebook API requests.
 *
 * Methods throwing IOException provide a user friendly error message.
 *
 * @author jmcarey
 */
public class FacebookApi {

    private static final Logger logger = LoggerFactory.getLogger(FacebookApi.class);

    public static class AccessToken {

        private final String token;

        private final int expires;

        public AccessToken(final String token, final int expires) {
            this.token = token;
            this.expires = expires;
        }

        public String getToken() {
            return token;
        }

        public int getExpires() {
            return expires;
        }

    }

    private final String appId;

    private final String appSecret;

    public FacebookApi(final String appId, final String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getLoginDialogRedirectUrl(final HttpServletRequest request, final String state) {
        return String.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&state=%s",
            appId,
            encodeLoginFacebookUrl(request),
            state);
    }

    public UserProfile getUserProfile(final AccessToken accessToken) throws IOException {

        final String url = String.format("https://graph.facebook.com/me?access_token=%s", accessToken.getToken());

        final HttpGet httpGet = new HttpGet(url);

        final ResponseHandler<UserProfile> handler = new ResponseHandler<UserProfile>() {
            @Override
            public UserProfile handleResponse(final HttpResponse response) throws IOException {

                if(response.getStatusLine().getStatusCode() != 200) {
                    logger.warn(String.format("Failed retrieving Facebook profile for '%s'.  Status line: '%s'", url, response.getStatusLine()));
                    throw new IOException("Failed to access your Facebook profile.  Try again later or login using your email and password.");
                }

                final HttpEntity entity = response.getEntity();

                final Map<String, Object> result;
                try {
                    final ObjectMapper mapper = new ObjectMapper();
                    result = mapper.readValue(entity.getContent(), new TypeReference<HashMap<String, Object>>() {});
                } catch(final IOException ioe) {
                    logger.warn(String.format("Failed parsing Facebook profile for '%s'", url), ioe);
                    throw new IOException("Failed reading your Facebook profile.  Try again later or login using your email and password.");
                }

                final UserProfile userProfile = new UserProfile();

                userProfile.setFbId((String) result.get("id"));

                userProfile.setFirstName((String) result.get("first_name"));
                userProfile.setLastName((String) result.get("last_name"));

                final String genderValue = (String) result.get("gender");
                if(genderValue != null) {
                    final Gender gender = Gender.valueOf(genderValue.toUpperCase());
                    if(gender == null) {
                        userProfile.setGender(Gender.UNSPECIFIED);
                    } else {
                        userProfile.setGender(gender);
                    }
                }

                final StringBuilder errorMessage = new StringBuilder();
                if(userProfile.getFbId() == null) {
                    errorMessage.append("id ");
                }
                if(userProfile.getFirstName() == null) {
                    errorMessage.append("first_name ");
                }
                if(userProfile.getLastName() == null) {
                    errorMessage.append("last_name ");
                }
                if(userProfile.getGender() == null) {
                    errorMessage.append("gender ");
                }

                if(errorMessage.length() > 0) {
                    errorMessage.insert(0, "Failed to extract values from Facebook API response for url '%s': ");
                    logger.warn(String.format(errorMessage.toString(), url));
                    throw new IOException("Failed to obtain required profile information from your Facebook profile.  Try again later or login using your email and password.");
                }

                userProfile.setSessionExpiry(-1);

                final long now = new Date().getTime();
                userProfile.setCreationDate(now);
                userProfile.setLastLoginDate(now);

                return userProfile;
            }
        };

        final HttpClient client = new DefaultHttpClient();
        try {
            return client.execute(httpGet, handler);
        } finally {
            client.getConnectionManager().shutdown();
        }

    }

    public AccessToken getAccessToken(final HttpServletRequest request, final String code) throws IOException {

        final String url = String.format(
            "https://graph.facebook.com/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
            appId,
            encodeLoginFacebookUrl(request),
            appSecret,
            code);

        final HttpClient client = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);

        final ResponseHandler<List<NameValuePair>> handler = new ResponseHandler<List<NameValuePair>>() {
            @Override
            public List<NameValuePair> handleResponse(final HttpResponse response) throws IOException {

                if(response.getStatusLine().getStatusCode() != 200) {
                    logger.warn(String.format("Failed obtaining access from Facebook for '%s'.  Status line: '%s'", url, response.getStatusLine()));
                    throw new IOException("Failed obtaining access from Facebook.  Try later or create an account using a email and password.");
                }

                final HttpEntity entity = response.getEntity();
                final String entityAsStr = EntityUtils.toString(entity);
                return URLEncodedUtils.parse(entityAsStr, Charset.forName("utf8"));

            }
        };

        final List<NameValuePair> responseFields = client.execute(httpGet, handler);
        String accessToken = null;
        int expires = -1;
        for(final NameValuePair nvPair : responseFields ){
            switch (nvPair.getName()) {
                case "access_token":
                    accessToken = nvPair.getValue();
                    break;
                case "expires":
                    try {
                        expires = Integer.parseInt(nvPair.getValue());
                    } catch(final NumberFormatException nfe) {
                        logger.warn(String.format("Failed parsing Facebook access token's expires value '%s'", nvPair.getValue()), nfe);
                    }
                    break;
            }
        }

        final StringBuilder errorMessage = new StringBuilder();
        if(accessToken == null) {
            errorMessage.append("access_token ");
        }
        if(expires < 0) {
            errorMessage.append("expires ");
        }

        if(errorMessage.length() > 0) {
            errorMessage.insert(0, "Failed to extract values from Facebook API response for url '%s': ");
            logger.warn(String.format(errorMessage.toString(), url));
            throw new IOException("Failed to obtain access to your Facebook profile.  Try again later or login using your email and password.");
        } else {
            return new AccessToken(accessToken, expires);
        }

    }

    private String encodeLoginFacebookUrl(final HttpServletRequest req) {
        final StringBuffer loginUrl = req.getRequestURL();
        final String redirectUrl = req.getParameter(AuthenticatedAspect.REDIRECT_URL_PARAM_NAME);
        if(StringUtils.isNotBlank(redirectUrl)) {
            final String encodedRedirectUrl = WebUtils.encodeUrl(redirectUrl);
            loginUrl.append("?").append(AuthenticatedAspect.REDIRECT_URL_PARAM_NAME).append("=").append(encodedRedirectUrl);
        }
        return WebUtils.encodeUrl(loginUrl.toString());
    }

}