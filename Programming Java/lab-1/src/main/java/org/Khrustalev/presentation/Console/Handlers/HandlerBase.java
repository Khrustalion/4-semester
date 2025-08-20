package org.Khrustalev.presentation.Console.Handlers;

import org.Khrustalev.application.contracts.AccountService;

/**
 * Абстрактный класс для обработчиков запросов.
 * Этот класс реализует шаблон проектирования "Цепочка обязанностей",
 * позволяя каждому обработчику передавать запрос следующему в цепочке.
 */
public abstract class HandlerBase implements Handlable {
    protected Handlable nextHandler;

    /**
     * Устанавливает следующий обработчик в цепочке.
     * Если текущий обработчик уже имеет следующий обработчик,
     * то передает управление следующему в цепочке.
     *
     * @param next следующий обработчик
     * @return текущий обработчик для поддержки цепочного вызова
     */
    @Override
    public Handlable setNext(Handlable next) {
        if (this.nextHandler == null) {
            this.nextHandler = next;
        }
        else {
            this.nextHandler.setNext(next);
        }

        return this;
    }
}
