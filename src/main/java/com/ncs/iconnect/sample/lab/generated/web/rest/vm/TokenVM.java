package com.ncs.iconnect.sample.lab.generated.web.rest.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing token information.
 */
public class TokenVM {

    @NotNull
    private String expireDateAsStr;

    public String getExpireDateAsStr() {
        return expireDateAsStr;
    }

    public void setExpireDateAsStr(String expireDateAsStr) {
        this.expireDateAsStr = expireDateAsStr;
    }

    @Override
    public String toString() {
        return "TokenVM{" +
            "expireDateAsStr=" + expireDateAsStr +
            '}';
    }
}
