package com.swooli.web.bean;

public class FacebookLoginBean {

    private String code;

    private String state;

    private String error_reason;

    private String error;

    private String error_description;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getError_reason() {
        return error_reason;
    }

    public void setError_reason(final String error_reason) {
        this.error_reason = error_reason;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(final String error_description) {
        this.error_description = error_description;
    }

    public boolean isPopulated() {
        return state != null || code != null || error != null;
    }
}