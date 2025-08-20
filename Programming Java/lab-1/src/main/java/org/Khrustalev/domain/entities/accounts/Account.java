package org.Khrustalev.domain.entities.accounts;

/**
 * Класс, представляющий учетную запись пользователя.
 * Этот класс является DTO (Data Transfer Object) и содержит уникальный идентификатор учетной записи,
 * а также информацию о доступных средствах. Предоставляет методы для получения и изменения баланса.
 */
public class Account {
    /**
     * Уникальный идентификатор учетной записи.
     */
    private final int accountId;

    /**
     * Средства, доступные на учетной записи.
     */
    private double funds;

    /**
     * Создает новую учетную запись с заданным идентификатором и нулевым балансом.
     *
     * @param accountId уникальный идентификатор учетной записи
     */
    public Account(int accountId) {
        this.accountId = accountId;
        this.funds = 0;
    }

    /**
     * Возвращает уникальный идентификатор учетной записи.
     *
     * @return идентификатор учетной записи
     */
    public int getAccountId() {
        return this.accountId;
    }

    /**
     * Возвращает текущий баланс учетной записи.
     *
     * @return доступные средства на счете
     */
    public double getFunds() {
        return this.funds;
    }

    /**
     * Устанавливает новый баланс учетной записи.
     *
     * @param funds сумма средств, которая будет установлена
     */
    public void setFunds(double funds) {
        this.funds = funds;
    }
}
