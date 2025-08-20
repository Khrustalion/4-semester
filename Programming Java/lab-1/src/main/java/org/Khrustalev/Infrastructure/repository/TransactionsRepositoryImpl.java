package org.Khrustalev.Infrastructure.repository;

import org.Khrustalev.domain.entities.transactions.Transaction;
import org.Khrustalev.domain.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация репозитория для транзакций.
 * Этот класс предоставляет методы для добавления транзакций и получения транзакций по идентификатору учетной записи.
 */
public class TransactionsRepositoryImpl implements TransactionRepository {
    private List<Transaction> transactions;

    public TransactionsRepositoryImpl() {
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction транзакция, которая будет добавлена в репозиторий
     */
    @Override
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Получает список транзакций для указанной учетной записи.
     *
     * @param accountId уникальный идентификатор учетной записи, для которой нужно получить транзакции
     * @return список транзакций, связанных с указанной учетной записью
     */
    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return this.transactions.stream()
                .filter(transaction -> transaction.getAccountId() == accountId)
                .collect(Collectors.toList());
    }
}

