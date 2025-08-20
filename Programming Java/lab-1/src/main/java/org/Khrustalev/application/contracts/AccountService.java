package org.Khrustalev.application.contracts;

import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.application.exceptions.NegativeAmountException;
import org.Khrustalev.application.exceptions.NotEnoughMoneyException;
import org.Khrustalev.domain.entities.accounts.Account;
import org.Khrustalev.domain.entities.transactions.Transaction;

import java.util.List;

/**
 * Интерфейс для работы с учетными записями пользователей.
 * Этот интерфейс предоставляет методы для создания учетных записей, получения информации о балансе,
 * внесения и снятия средств, а также получения истории транзакций.
 */
public interface AccountService {

    /**
     * Создает новую учетную запись с заданным идентификатором.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return созданная учетная запись
     */
    Account addAccount(int accountId);

    /**
     * Возвращает список идентификаторов всех учетных записей.
     *
     * @return список идентификаторов учетных записей
     */
    List<Integer> getAccountIds();

    /**
     * Возвращает текущий баланс для учетной записи с заданным идентификатором.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return текущий баланс учетной записи
     * @throws AccountDoesNotExistException если учетная запись не найдена
     */
    double getFunds(int accountId) throws AccountDoesNotExistException;

    /**
     * Снимает указанную сумму средств с учетной записи.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма для снятия
     * @throws NotEnoughMoneyException если на счету недостаточно средств
     * @throws NegativeAmountException если сумма отрицательная
     * @throws AccountDoesNotExistException если учетная запись не найдена
     */
    void withdrawFunds(int accountId, double amount) throws NotEnoughMoneyException,
            NegativeAmountException,
            AccountDoesNotExistException;

    /**
     * Вносит указанную сумму средств на учетную запись.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма для внесения
     * @throws NegativeAmountException если сумма отрицательная
     * @throws AccountDoesNotExistException если учетная запись не найдена
     */
    void addFunds(int accountId, double amount) throws NegativeAmountException,
            AccountDoesNotExistException;

    /**
     * Возвращает список всех транзакций для указанной учетной записи.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return список транзакций
     * @throws AccountDoesNotExistException если учетная запись не найдена
     */
    List<Transaction> getTransactions(int accountId) throws AccountDoesNotExistException;
}
