package com.swooli.bo.user;

/**
 * The status a of user's account.
 *
 * @author jmcarey
 */
public enum AccountStatus {

    PENDING("Pending"),
    ACTIVE("Active"),
    DISABLED("Disabled");

    private String displayName;

    AccountStatus(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AccountStatus valueOfByDisplayName(final String displayName) {
        for (final AccountStatus status : values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return null;
    }

}
