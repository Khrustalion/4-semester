package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.WithdrawFundsCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик для команды снятия средств с аккаунта.
 * Этот обработчик обрабатывает команды формата:
 *  * - "withdraw [accountId] [amount]"
 *
 * Если запрос соответствует этой команде, создается объект команды {@link WithdrawFundsCommand}.
 */
public class HandlerWithdraw extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для команды снятия средств.
     *
     * @param accountService сервис для работы с аккаунтами
     */
    public HandlerWithdraw(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запросы и передает управление дальше в цепочке,
     *
     * @param request список строк, представляющий запрос
     * @return объект команды для выполнения снятия средств, или {@code null}, если команда не распознана
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            int accountId = Integer.parseInt(request.get(1));
            int amount = Integer.parseInt(request.get(2));

            return new WithdrawFundsCommand(this.accountService, accountId, amount);
        }

        return this.nextHandler == null ? null : this.nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли данный обработчик обработать запрос.
     * Запрос должен содержать три элемента: "withdraw", идентификатор аккаунта и сумму.
     *
     * @param request список строк, представляющий запрос
     * @return {@code true}, если запрос соответствует формату команды, {@code false} в противном случае
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 3 && request.get(0).equals("withdraw");
    }
}
