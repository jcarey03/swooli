package com.swooli.bo.user;

/**
 * An enumeration of countries.
 *
 * @author jmcarey
 */
public enum Country {

    DEFAULT("I am located in..."),
    USA("United States");

    private String displayName;

    Country(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Country valueOfByDisplayName(final String displayName) {
        for (final Country country : values()) {
            if (country.getDisplayName().equals(displayName)) {
                return country;
            }
        }
        return null;
    }

}
