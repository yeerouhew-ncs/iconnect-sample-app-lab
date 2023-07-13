package com.ncs.iconnect.sample.lab.generated.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class UnauthenticatedException extends AbstractThrowableProblem {

    public UnauthenticatedException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.UNAUTHORIZED);
    }
}
