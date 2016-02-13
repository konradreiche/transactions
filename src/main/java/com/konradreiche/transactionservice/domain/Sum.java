package com.konradreiche.transactionservice.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by konrad on 12/02/16.
 */
public class Sum {
    private BigDecimal sum;

    public Sum() {}

    public Sum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
