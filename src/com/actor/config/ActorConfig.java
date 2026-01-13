package com.actor.config;

public class ActorConfig {
    private static final int SIZE = 10;

    int maxActors;
    int defaultInboxSize;

    public ActorConfig(int maxActors) {
        this.maxActors = maxActors;
        defaultInboxSize = SIZE;
    }

    public ActorConfig(int maxActors, int defaultInboxSize) {
        this.maxActors = maxActors;
        this.defaultInboxSize = defaultInboxSize;
    }

    public int getMaxActors() {
        return maxActors;
    }

    public int getDefaultInboxSize() {
        return defaultInboxSize;
    }
}
