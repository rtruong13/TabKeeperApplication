package com.i.should.what.whatshouldi.MoviesPackage.Models;

/**
 * Created by ryan on 8/29/2015.
 */
public enum MoviesState {
    ADDED(0), WATCHED(1);

    private final int value;

    MoviesState(int value) {
        this.value = value;
    }

    public static MoviesState getStateFromInt(int i) {
        if (i == 0) return ADDED;
        return WATCHED;
    }

    public int getValue() {
        return value;
    }
}
