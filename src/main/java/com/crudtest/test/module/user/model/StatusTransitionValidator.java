package com.crudtest.test.module.user.model;

public class StatusTransitionValidator {

    public static boolean canTransition(Status current, Status next) {

        switch (current) {
            case PENDING:
                return next == Status.ACTIVE || next == Status.INACTIVE;
            case ACTIVE:
                return next == Status.INACTIVE || next == Status.SUSPENDED || next == Status.DELETED;
            case INACTIVE:
                return next == Status.ACTIVE;
            case SUSPENDED:
                return next == Status.ACTIVE || next == Status.DELETED;
            case DELETED:
                return false;
            default:
                return false;
        }
    }
}
