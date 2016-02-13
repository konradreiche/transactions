package com.konradreiche.transactionservice.domain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by konrad on 13/02/16.
 */
public class TransactionRepository implements ITransactionRepository {

    Map<Long, Transaction> transactionsById = new ConcurrentHashMap<>();
    Map<String, Set<Transaction>> transactionsByType = new ConcurrentHashMap<>();


    @Override
    public void add(Transaction transaction) {
        // Initialize parent transaction
        if (transaction.getParentId() != null) {
            transaction.setParent(transactionsById.get(transaction.getParentId()));
        }

        // Extend by type map
        Set<Transaction> transactions = transactionsByType.get(transaction.getType());
        if (transactions == null) {
            transactions = ConcurrentHashMap.newKeySet();
            transactionsByType.put(transaction.getType(), transactions);
        }
        transactions.add(transaction);
        transactionsById.put(transaction.getId(), transaction);
    }

    @Override
    public Transaction find(Long id) {
        return transactionsById.get(id);
    }

    @Override
    public List<Long> findIdsByType(String type) {
        return transactionsByType.get(type).stream().map(t -> t.getId()).collect(Collectors.toList());
    }
}
