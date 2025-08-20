package org.Khrustalev.application.exceptions;

/**
 * Исключение, которое выбрасывается, если на счете недостаточно средств для выполнения операции.
 * Это исключение используется для сигнализации о том, что попытка выполнить операцию (например, снятие средств)
 * не удалась из-за недостаточного баланса на счету.
 */
public class NotEnoughMoneyException extends Exception {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message описание ошибки, объясняющее причину выбрасывания исключения
     */
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
