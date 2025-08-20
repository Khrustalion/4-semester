package org.Khrustalev.application.exceptions;

/**
 * Исключение, которое выбрасывается, если операция с отрицательной суммой невозможна.
 * Это исключение используется для сигнализации о том, что попытка выполнения операции (например, внесения или снятия средств)
 * с отрицательной суммой является недопустимой.
 */
public class NegativeAmountException extends Exception {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message описание ошибки, объясняющее причину выбрасывания исключения
     */
    public NegativeAmountException(String message) {
        super(message);
    }
}
