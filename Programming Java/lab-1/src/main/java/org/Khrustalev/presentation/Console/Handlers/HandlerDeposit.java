package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.DepositFundsCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик команд для пополнения счета.
 * Этот обработчик обрабатывает команды формата:
 * - "deposit [accountId] [amount]"
 * Если запрос соответствует формату, создается {@link DepositFundsCommand} для пополнения счета.
 */
public class HandlerDeposit extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для пополнения счета.
     *
     * @param accountService сервис для работы с аккаунтами
     */
    public HandlerDeposit(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запрос на пополнение счета.
     *
     * @param request список строк, представляющих запрос (например, ["deposit", "accountId", "amount"])
     * @return {@link DepositFundsCommand}, если запрос корректен; иначе передает запрос следующему обработчику в цепочке
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            int accountId = Integer.parseInt(request.get(1));
            int amount = Integer.parseInt(request.get(2));

            return new DepositFundsCommand(this.accountService, accountId, amount);
        }

        return this.nextHandler == null ? null : this.nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли текущий обработчик обработать запрос.
     * Запрос должен быть в формате: "deposit [accountId] [amount]".
     *
     * @param request список строк, представляющих запрос
     * @return {@code true}, если запрос соответствует ожидаемому формату; {@code false} в противном случае
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 3 && request.get(0).equals("deposit");
    }
}
