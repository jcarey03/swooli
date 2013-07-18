package com.swooli.bo.user;

/**
 * The gender a of user.
 *
 * @author jmcarey
 */
public enum Gender {

    DEFAULT("I am..."),
    MALE("male"),
    FEMALE("female"),
    UNSPECIFIED("unspecified");

    private String displayName;

    Gender(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Gender valueOfByDisplayName(final String displayName) {
        for (final Gender status : values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return null;
    }

}
