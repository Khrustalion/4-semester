package org.Khrustalev.presentation.Console.Handlers.Factories;

import org.Khrustalev.application.commands.AddAccountCommand;
import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.presentation.Console.Handlers.*;

/**
 * Фабрика для создания цепочки обработчиков команд.
 * Этот класс создает и связывает обработчики команд, каждый из которых обрабатывает конкретную команду,
 * такую как добавление аккаунта, проверка баланса, снятие средств и другие.
 */
public class HandlersFactory {
    private final AccountService accountService;

    /**
     * Конструктор фабрики обработчиков.
     *
     * @param accountService сервис для работы с аккаунтами
     */
    public HandlersFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Создает цепочку обработчиков команд и возвращает начальный обработчик.
     * Каждый обработчик в цепочке передает запрос следующему, если не может его обработать.
     *
     * @return начальный обработчик в цепочке обработчиков
     */
    public Handlable createHandlers() {
        Handlable handlerCheckTransactions = new HandlerCheckTransactions(this.accountService);
        Handlable handlerCheckAccountsIds = new HandlerCheckAccountIds(this.accountService).setNext(handlerCheckTransactions);
        Handlable handlerCheckBalance = new HandlerCheckBalance(this.accountService).setNext(handlerCheckAccountsIds);
        Handlable handlerDeposit = new HandlerDeposit(this.accountService).setNext(handlerCheckBalance);
        Handlable handlerWithdraw = new HandlerWithdraw(this.accountService).setNext(handlerDeposit);
        Handlable handlerAddAccount = new HandlerAddAccount(this.accountService).setNext(handlerWithdraw);

        return handlerAddAccount;
    }
}
