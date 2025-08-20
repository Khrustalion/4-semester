package org.Khrustalev.Infrastructure.repository;

import org.Khrustalev.domain.entities.accounts.Account;
import org.Khrustalev.domain.repositories.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация репозитория для учетных записей.
 * Этот класс предоставляет методы для добавления, поиска учетных записей и управления средствами на счете.
 */
public class AccountRepositoryImpl implements AccountRepository {
    private List<Account> accounts;

    /**
     * Конструктор для инициализации репозитория.
     * Создает новый список для хранения учетных записей.
     */
    public AccountRepositoryImpl() {
        this.accounts = new ArrayList<Account>();
    }

    /**
     * Добавляет новую учетную запись в репозиторий, если учетная запись с таким идентификатором еще не существует.
     *
     * @param account учетная запись, которую нужно добавить в репозиторий
     */
    @Override
    public void addAccount(Account account) {
        if (findAccountById(account.getAccountId()) == null) {
            this.accounts.add(account);
        }
    }

    /**
     * Находит учетную запись по ее идентификатору.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return учетная запись с указанным идентификатором, или null, если учетная запись не найдена
     */
    @Override
    public Account findAccountById(int accountId) {
        return this.accounts.stream()
                .filter(account -> account.getAccountId() == accountId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Получает список идентификаторов всех учетных записей.
     *
     * @return список уникальных идентификаторов всех учетных записей
     */
    @Override
    public List<Integer> getAccountIds() {
        return this.accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toList());
    }

    /**
     * Снимает средства с учетной записи по указанному идентификатору.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма средств для снятия
     */
    @Override
    public void withdrawFunds(int accountId, double amount) {
        Account account = findAccountById(accountId);

        if (account != null) {
            account.setFunds(account.getFunds() - amount);
        }
    }

    /**
     * Добавляет средства на учетную запись по указанному идентификатору.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма средств для добавления
     */
    @Override
    public void addFunds(int accountId, double amount) {
        Account account = findAccountById(accountId);

        if (account != null) {
            account.setFunds(account.getFunds() + amount);
        }
    }
}