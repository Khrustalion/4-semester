package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.CheckBalanceCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик команд для запроса баланса счета.
 * Этот обработчик обрабатывает команды формата:
 * - "check balance [accountId]"
 * Если запрос соответствует формату, создается {@link CheckBalanceCommand} для проверки баланса счета.
 */
public class HandlerCheckBalance extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для запроса баланса счета.
     *
     * @param accountService сервис для получения баланса счета
     */
    public HandlerCheckBalance(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запрос на проверку баланса счета.
     *
     * @param request список строк, представляющих запрос (например, ["check", "balance", "accountId"])
     * @return {@link CheckBalanceCommand}, если запрос корректен; иначе передает запрос следующему обработчику в цепочке
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            int accountId = Integer.parseInt(request.get(2));

            return new CheckBalanceCommand(this.accountService, accountId);
        }

        return this.nextHandler == null ? null : this.nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли текущий обработчик обработать запрос.
     * Запрос должен быть в формате: "check balance [accountId]".
     *
     * @param request список строк, представляющих запрос
     * @return {@code true}, если запрос соответствует ожидаемому формату; {@code false} в противном случае
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 3 &&
                request.get(0).equals("check") &&
                request.get(1).equals("balance");
    }
}
