package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.domain.entities.accounts.Account;

/**
 * Команда для добавления новой учетной записи.
 * Этот класс реализует команду для создания новой учетной записи с использованием сервиса учетных записей.
 */
public class AddAccountCommand implements AbstractCommand {
    private final AccountService accountService;
    private final int accountId;

    /**
     * Конструктор для создания команды добавления новой учетной записи.
     *
     * @param accountService сервис для управления учетными записями
     * @param accountId уникальный идентификатор новой учетной записи
     */
    public AddAccountCommand(AccountService accountService, int accountId) {
        this.accountService = accountService;
        this.accountId = accountId;
    }

    /**
     * Выполняет команду добавления новой учетной записи.
     * Создает новую учетную запись и возвращает строковое сообщение о успешном добавлении.
     *
     * @return строка с подтверждением успешного добавления учетной записи и ее идентификатора
     */
    @Override
    public String execute() {
        Account account = this.accountService.addAccount(this.accountId);

        return String.format("Account has been added successfully. AccountId: %d", account.getAccountId());
    }
}
