package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;

import java.util.stream.Collectors;

import java.util.stream.Collectors;

/**
 * Команда для получения списка всех идентификаторов учетных записей.
 * Этот класс реализует команду для получения списка идентификаторов всех учетных записей
 * через сервис `AccountService`.
 */
public class CheckAccountIdsCommand implements AbstractCommand {
    private AccountService accountService;

    /**
     * Конструктор для создания команды, которая получает список идентификаторов учетных записей.
     *
     * @param accountService сервис для работы с учетными записями
     */
    public CheckAccountIdsCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Выполняет команду для получения списка всех идентификаторов учетных записей.
     * Возвращает строковое представление списка идентификаторов, разделенных точкой с запятой.
     *
     * @return строка с перечислением всех идентификаторов учетных записей
     */
    @Override
    public String execute() {
        String result = accountService.getAccountIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("; "));
        return result;
    }
}
