package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.domain.entities.transactions.Transaction;

import java.util.stream.Collectors;

/**
 * Команда для получения списка транзакций учетной записи.
 * Этот класс реализует команду для получения всех транзакций, связанных с учетной записью,
 * через сервис `AccountService`.
 */
public class CheckTransactionsCommand implements AbstractCommand {
    private final AccountService accountService;
    private final int accountId;

    /**
     * Конструктор для создания команды получения списка транзакций учетной записи.
     *
     * @param accountService сервис для работы с учетными записями
     * @param accountId уникальный идентификатор учетной записи
     */
    public CheckTransactionsCommand(AccountService accountService, int accountId) {
        this.accountService = accountService;
        this.accountId = accountId;
    }

    /**
     * Выполняет команду получения всех транзакций для указанной учетной записи.
     * Возвращает строковое представление всех транзакций или сообщение об ошибке,
     * если учетная запись не существует.
     *
     * @return строка, представляющая все транзакции учетной записи, или сообщение об ошибке
     */
    @Override
    public String execute() {
        try {
            String result = this.accountService.getTransactions(this.accountId).stream()
                    .map(Transaction::toString)
                    .collect(Collectors.joining("\n"));

            return result;
        }
        catch (AccountDoesNotExistException e) {
            return "Error: " + e.getMessage();
        }
    }
}
