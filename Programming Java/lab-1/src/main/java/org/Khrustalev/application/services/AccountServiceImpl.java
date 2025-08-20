package org.Khrustalev.application.services;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.application.exceptions.NegativeAmountException;
import org.Khrustalev.application.exceptions.NotEnoughMoneyException;
import org.Khrustalev.domain.entities.accounts.Account;
import org.Khrustalev.domain.entities.transactions.Transaction;
import org.Khrustalev.domain.entities.transactions.TransactionType;
import org.Khrustalev.domain.repositories.AccountRepository;
import org.Khrustalev.domain.repositories.TransactionRepository;

import java.text.MessageFormat;
import java.util.List;

/**
 * Реализация сервиса управления учетными записями пользователей.
 * Этот класс предоставляет функциональность для создания учетных записей, пополнения и снятия средств,
 * получения баланса и истории транзакций.
 */
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Конструктор для создания экземпляра сервиса управления учетными записями.
     *
     * @param accountRepository репозиторий для работы с учетными записями
     * @param transactionRepository репозиторий для работы с транзакциями
     */
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Добавляет новую учетную запись с заданным идентификатором.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return созданную учетную запись
     */
    @Override
    public Account addAccount(int accountId) {
        Account account = new Account(accountId);

        this.accountRepository.addAccount(account);

        return account;
    }

    /**
     * Возвращает список всех идентификаторов учетных записей.
     *
     * @return список идентификаторов учетных записей
     */
    @Override
    public List<Integer> getAccountIds() {
        return this.accountRepository.getAccountIds();
    }

    /**
     * Получает баланс учетной записи по идентификатору.
     *
     * @param accountId идентификатор учетной записи
     * @return текущий баланс учетной записи
     * @throws AccountDoesNotExistException если учетная запись с заданным идентификатором не найдена
     */
    @Override
    public double getFunds(int accountId) throws AccountDoesNotExistException {
        Account account = this.accountRepository.findAccountById(accountId);

        if (account == null)
            throw new AccountDoesNotExistException(MessageFormat.format("Not found Account with {0} id", accountId));

        this.transactionRepository.addTransaction(new Transaction(accountId, TransactionType.CHECK_BALANCE));

        return account.getFunds();
    }

    /**
     * Снимает средства с учетной записи.
     *
     * @param accountId идентификатор учетной записи
     * @param amount сумма для снятия
     * @throws NotEnoughMoneyException если на счету недостаточно средств
     * @throws NegativeAmountException если сумма отрицательная
     * @throws AccountDoesNotExistException если учетная запись с заданным идентификатором не найдена
     */
    @Override
    public void withdrawFunds(int accountId, double amount) throws NotEnoughMoneyException,
            NegativeAmountException,
            AccountDoesNotExistException{
        if (amount < 0)
            throw new NegativeAmountException(MessageFormat.format("Negative amount {0} ", amount));

        Account account = this.accountRepository.findAccountById(accountId);

        if (account == null)
            throw new AccountDoesNotExistException(MessageFormat.format("Not found Account with {0} id", accountId));

        if (account.getFunds() < amount)
            throw new NotEnoughMoneyException(MessageFormat.format("Not enough money for {0} funds", amount));

        this.accountRepository.withdrawFunds(accountId, amount);
        this.transactionRepository.addTransaction(new Transaction(accountId, TransactionType.WITHDRAW));
    }

    /**
     * Добавляет средства на счет.
     *
     * @param accountId идентификатор учетной записи
     * @param amount сумма для пополнения
     * @throws NegativeAmountException если сумма отрицательная
     * @throws AccountDoesNotExistException если учетная запись с заданным идентификатором не найдена
     */
    @Override
    public void addFunds(int accountId, double amount) throws NegativeAmountException,
            AccountDoesNotExistException {
        if (amount < 0)
            throw new NegativeAmountException(MessageFormat.format("Negative amount {0} ", amount));

        Account account = this.accountRepository.findAccountById(accountId);

        if (account == null)
            throw new AccountDoesNotExistException(MessageFormat.format("Not found Account with {0} id", accountId));

        this.accountRepository.addFunds(accountId, amount);
        this.transactionRepository.addTransaction(new Transaction(accountId, TransactionType.DEPOSIT));
    }

    /**
     * Получает список транзакций для учетной записи по ее идентификатору.
     *
     * @param accountId идентификатор учетной записи
     * @return список транзакций
     * @throws AccountDoesNotExistException если учетная запись с заданным идентификатором не найдена
     */
    @Override
    public List<Transaction> getTransactions(int accountId) throws AccountDoesNotExistException {
        Account account = this.accountRepository.findAccountById(accountId);

        if (account == null)
            throw new AccountDoesNotExistException(MessageFormat.format("Not found Account with {0} id", accountId));

        return this.transactionRepository.getTransactionsByAccountId(accountId);
    }
}
