package org.Khrustalev.domain.repositories;

import org.Khrustalev.domain.entities.transactions.Transaction;

import java.util.List;

/**
 * Интерфейс для репозитория транзакций.
 * Этот интерфейс предоставляет методы для добавления транзакций и получения транзакций по идентификатору учетной записи.
 */
public interface TransactionRepository {
    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction транзакция, которая будет добавлена в репозиторий
     */
    public void addTransaction(Transaction transaction);

    /**
     * Получает список транзакций для указанной учетной записи.
     *
     * @param accountId уникальный идентификатор учетной записи, для которой нужно получить транзакции
     * @return список транзакций, связанных с указанной учетной записью
     */
    public List<Transaction> getTransactionsByAccountId(int accountId);
}
