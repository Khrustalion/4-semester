package org.Khrustalev.domain.entities.transactions;

/**
 * Представляет финансовую транзакцию для учетной записи.
 * Этот класс хранит ID учетной записи и тип транзакции.
 */
public class Transaction {

    /**
     * ID учетной записи, связанной с транзакцией.
     */
    private final int accountId;

    /**
     * Тип транзакции (например, депозит, снятие средств и т.д.).
     */
    private final TransactionType transactionType;

    public Transaction(int accountId, TransactionType transactionType) {
        this.accountId = accountId;
        this.transactionType = transactionType;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    /**
     * Возвращает строковое представление транзакции.
     * Строка включает в себя ID учетной записи и тип транзакции.
     *
     * @return строковое представление транзакции
     */
    @Override
    public String toString() {
        return String.format("%s | %s", this.accountId, this.transactionType.toString());
    }
}


