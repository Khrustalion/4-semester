package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.CheckTransactionsCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик команд для запроса транзакций по аккаунту.
 * Этот обработчик обрабатывает команды формата:
 * - "check transaction [accountId]"
 * Если запрос соответствует формату, создается {@link CheckTransactionsCommand} для получения транзакций счета.
 */
public class HandlerCheckTransactions extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для запроса транзакций счета.
     *
     * @param accountService сервис для получения транзакций счета
     */
    public HandlerCheckTransactions(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запрос на получение транзакций счета.
     *
     * @param request список строк, представляющих запрос (например, ["check", "transaction", "accountId"])
     * @return {@link CheckTransactionsCommand}, если запрос корректен; иначе передает запрос следующему обработчику в цепочке
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            int accountId = Integer.parseInt(request.get(2));

            return new CheckTransactionsCommand(accountService, accountId);
        }

        return nextHandler == null ? null : nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли текущий обработчик обработать запрос.
     * Запрос должен быть в формате: "check transaction [accountId]".
     *
     * @param request список строк, представляющих запрос
     * @return {@code true}, если запрос соответствует ожидаемому формату; {@code false} в противном случае
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 3 &&
                request.get(0).equals("check") &&
                request.get(1).equals("transaction");
    }
}
