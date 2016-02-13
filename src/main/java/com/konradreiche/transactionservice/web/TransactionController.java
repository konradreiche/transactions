package com.konradreiche.transactionservice.web;

import com.konradreiche.transactionservice.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactionservice/transaction")
public class TransactionController {

    ITransactionRepository repository;

    public TransactionController() {
        this.repository = new TransactionRepository();
    }

    public TransactionController(ITransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Asymptotic behaviour:
     *
     *  - usage of hash tables have amortized run time of O(1)
     *  - retrieve parent O(1), set parent O(1)
     *  - retrieve type hash table O(1)
     *  - initialize new hash table O(1)
     *  - extend list of a transaction type O(1)
     *  - add transaction by id to hash table O(1)
     *
     *  - all operations are in sum still O(1) amortized, otherwise worst case O(n)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Result createOrUpdate(@PathVariable long id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        repository.add(transaction);
        return Result.OK;
    }

    /**
     * Asymptotic behaviour: see above O(1)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Transaction get(@PathVariable Long id) {
        return repository.find(id);
    }

    /**
     * Asymptotic behaviour: see above O(1)
     */
    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    List<Long> getByTypes(@PathVariable String type) {
        return repository.findIdsByType(type);
    }

    /**
     * Asymptotic behaviour:
     *
     * - iterating over all children, worst case visiting all
     *   transactions and they are linked to each other:
     *
     *   O(n)
     */
    @RequestMapping(value = "/sum/{id}", method = RequestMethod.GET)
    Sum sum(@PathVariable Long id) {
        List<Transaction> transactions = new ArrayList<>();
        Transaction current = repository.find(id);
        while (current != null) {
            transactions.add(current);
            current = current.getChild();
        }
        return new Sum(transactions.stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}