package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.commands.AddAccountCommand;
import org.Khrustalev.application.contracts.AccountService;

import java.util.List;

/**
 * Обработчик команд для добавления нового аккаунта.
 * Этот обработчик обрабатывает команды формата:
 *  * - "add account [accountId]"
 *
 * Создается команда {@link AddAccountCommand} для выполнения операции добавления аккаунта,
 * если запрос соответствует ожидаемому формату.
 */
public class HandlerAddAccount extends HandlerBase {
    private final AccountService accountService;

    /**
     * Конструктор обработчика для добавления аккаунта.
     *
     * @param accountService сервис для работы с аккаунтами
     */
    public HandlerAddAccount(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Обрабатывает запрос для добавления нового аккаунта.
     *
     * @param request список строк, представляющих запрос
     * @return команду для добавления аккаунта или передает запрос следующему обработчику в цепочке
     */
    @Override
    public AbstractCommand handle(List<String> request) {
        if (canHandle(request)) {
            int accountId = Integer.parseInt(request.get(2));

            return new AddAccountCommand(accountService, accountId);
        }

        return nextHandler == null ? null : nextHandler.handle(request);
    }

    /**
     * Проверяет, может ли этот обработчик обработать текущий запрос.
     * Запрос должен быть в формате {@code "add account <accountId>"}.
     *
     * @param request список строк, представляющих запрос
     * @return {@code true}, если запрос соответствует добавлению аккаунта, иначе {@code false}
     */
    private boolean canHandle(List<String> request) {
        return request.size() == 3 &&
                request.get(0).equals("add") &&
                request.get(1).equals("account");
    }
}
