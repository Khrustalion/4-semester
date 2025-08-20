package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.commands.AbstractCommand;

import java.util.List;

/**
 * Интерфейс, представляющий обработчик команд в цепочке ответственности.
 * Каждый обработчик может передавать запрос следующему обработчику в цепочке.
 */
public interface Handlable {

    /**
     * Устанавливает следующий обработчик в цепочке.
     *
     * @param next следующий обработчик для обработки запроса
     * @return текущий обработчик
     */
    public Handlable setNext(Handlable next);

    /**
     * Обрабатывает запрос и возвращает команду, соответствующую запросу.
     *
     * @param request список строк, представляющий запрос
     * @return команду для выполнения запроса
     */
    public AbstractCommand handle(List<String> request);
}
