package com.swooli.bo.user;

/**
 * An enumeration of months.
 *
 * @author jmcarey
 */
public enum Month {

    DEFAULT("Month"),
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    private String displayName;

    Month(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Month valueOfByDisplayName(final String displayName) {
        for (final Month month : values()) {
            if (month.getDisplayName().equals(displayName)) {
                return month;
            }
        }
        return null;
    }

}
