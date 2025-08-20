package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.CheckAccountIdsCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик для команды проверки всех аккаунтов.
 * Этот обработчик обрабатывает команды формата:
 *  * - "check accounts"
 * Если запрос соответствует этой команде, создается объект команды {@link CheckAccountIdsCommand}.
 */
public class HandlerCheckAccountIds extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для команды проверки всех аккаунтов.
     *
     * @param accountService сервис для работы с аккаунтами
     */
    public HandlerCheckAccountIds(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запросы на просмотр id всех аккаунтов,
     *
     * @param request список строк, представляющий запрос
     * @return объект команды для выполнения, или {@code null}, если команда не распознана
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            return new CheckAccountIdsCommand(accountService);
        }

        return nextHandler == null ? null : nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли данный обработчик обработать запрос.
     * Запрос должен содержать два элемента: "check" и "accounts".
     *
     * @param request список строк, представляющий запрос
     * @return {@code true}, если запрос соответствует формату команды, {@code false} в противном случае
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 2 &&
                request.get(0).equals("check") &&
                request.get(1).equals("accounts");
    }
}
