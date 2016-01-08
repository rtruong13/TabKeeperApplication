package com.i.should.what.whatshouldi.ListenPackage.Models;

/**
 * Created by ryan on 7/29/2015.
 */
public enum ListenState {
    LIKE(0), DISLIKE(1), NOTHING(2);

    private final int value;

    ListenState(int value) {
        this.value = value;
    }

    public static ListenState getStateFromInt(int i) {
        if (i == 0) return LIKE;
        if (i == 1) return DISLIKE;
        return NOTHING;
    }

    public int getValue() {
        return value;
    }
}
