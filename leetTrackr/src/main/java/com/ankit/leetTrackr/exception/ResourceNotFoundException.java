package com.ankit.leetTrackr.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String problemNOtFound) {
        super(problemNOtFound);
    }
}
