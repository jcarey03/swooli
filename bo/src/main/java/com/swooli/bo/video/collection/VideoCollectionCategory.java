package com.swooli.bo.video.collection;

public enum VideoCollectionCategory {

    EVERYTHING("Everything"),
    COOKING("Cooking");

    private final String displayName;

    VideoCollectionCategory(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static VideoCollectionCategory valueOfByDisplayName(final String displayName) {
        for (final VideoCollectionCategory value : values()) {
            if (value.getDisplayName().equals(displayName)) {
                return value;
            }
        }
        return null;
    }

}
