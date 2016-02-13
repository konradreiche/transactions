package com.konradreiche.transactionservice.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by konrad on 13/02/16.
 */
public interface ITransactionRepository {

    void add(Transaction transaction);

    Transaction find(Long id);

    List<Long> findIdsByType(String type);
}
