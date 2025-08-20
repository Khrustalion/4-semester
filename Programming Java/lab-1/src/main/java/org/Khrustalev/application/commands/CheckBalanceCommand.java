package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;

/**
 * Команда для проверки баланса учетной записи.
 * Этот класс реализует команду для получения текущего баланса учетной записи через сервис `AccountService`.
 */
public class CheckBalanceCommand implements AbstractCommand {
    private final int accountId;
    private final AccountService accountService;

    /**
     * Конструктор для создания команды проверки баланса учетной записи.
     *
     * @param accountService сервис для работы с учетными записями
     * @param accountId уникальный идентификатор учетной записи
     */
    public CheckBalanceCommand(AccountService accountService, int accountId) {
        this.accountId = accountId;
        this.accountService = accountService;
    }

    /**
     * Выполняет команду для получения баланса учетной записи.
     * Возвращает строковое представление баланса или сообщение об ошибке, если учетная запись не существует.
     *
     * @return строка с текущим балансом учетной записи или сообщением об ошибке
     */
    @Override
    public String execute() {
        try {
            double balance = accountService.getFunds(this.accountId);

            return String.format("Account: %s Balance: %.2f", this.accountId, balance);
        }
        catch (AccountDoesNotExistException e) {
            return "Error: " + e.getMessage();
        }
    }
}

