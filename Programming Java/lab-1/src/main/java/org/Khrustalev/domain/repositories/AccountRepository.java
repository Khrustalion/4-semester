package org.Khrustalev.domain.repositories;

import org.Khrustalev.domain.entities.accounts.Account;

import java.util.List;


/**
 * Интерфейс для репозитория учетных записей.
 * Этот интерфейс предоставляет методы для добавления, поиска и управления учетными записями.
 */
public interface AccountRepository {

    /**
     * Добавляет новую учетную запись в репозиторий.
     *
     * @param account учетная запись, которая будет добавлена в репозиторий
     */
    public void addAccount(Account account);

    /**
     * Находит учетную запись по ее уникальному идентификатору.
     *
     * @param accountId уникальный идентификатор учетной записи
     * @return учетная запись с заданным идентификатором, или null, если запись не найдена
     */
    public Account findAccountById(int accountId);

    /**
     * Получает список идентификаторов всех учетных записей.
     *
     * @return список уникальных идентификаторов всех учетных записей
     */
    public List<Integer> getAccountIds();

    /**
     * Снимает средства с учетной записи.
     *
     * @param accountId уникальный идентификатор учетной записи, с которой будут сняты средства
     * @param amount сумма средств для снятия
     */
    public void withdrawFunds(int accountId, double amount);

    /**
     * Добавляет средства на учетную запись.
     *
     * @param accountId уникальный идентификатор учетной записи, на которую будут добавлены средства
     * @param amount сумма средств для добавления
     */
    public void addFunds(int accountId, double amount);
}
