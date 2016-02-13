package com.konradreiche.transactionservice.domain;

/**
 * Created by konrad on 13/02/16.
 */
public class Result {

    public static final Result OK = new Result("ok");

    String status;

    public Result() {}

    public Result(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
