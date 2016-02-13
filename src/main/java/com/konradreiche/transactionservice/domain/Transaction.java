package com.konradreiche.transactionservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by konrad on 11/02/16.
 */
public class Transaction {

    private long id;
    private BigDecimal amount;
    private String type;
    private Long parentId;

    @JsonIgnore
    private Transaction child;

    @JsonIgnore
    private Transaction parent;

    public Transaction() {}

    public Transaction(long id, double amount, String type) {
        this.id = id;
        this.amount = BigDecimal.valueOf(amount);
        this.type = type;
    }
    public Transaction(long id, double amount, String type, long parentId) {
        this(id, amount, type);
        this.parentId = parentId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Transaction getParent() {
        return parent;
    }

    public void setParent(Transaction parent) {
        this.parent = parent;
        parent.setChild(this);
    }

    @JsonIgnore
    public Long getParentId() {
        return parentId;
    }

    @JsonProperty
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Transaction getChild() {
        return child;
    }

    public void setChild(Transaction child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
