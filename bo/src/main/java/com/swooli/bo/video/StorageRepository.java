package com.swooli.bo.video;



/**
 * An enumeration of supported video storage repositories.
 *
 * @author jmcarey
 */
public enum StorageRepository {

    YouTube("YouTube"),
    Vimeo("Vimeo"),
    DailyMotion("DailyMotion");

    private final String displayName;

    StorageRepository(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StorageRepository valueOfByDisplayName(final String displayName) {
        for (final StorageRepository repo : values()) {
            if (repo.getDisplayName().equals(displayName)) {
                return repo;
            }
        }
        return null;
    }
}
