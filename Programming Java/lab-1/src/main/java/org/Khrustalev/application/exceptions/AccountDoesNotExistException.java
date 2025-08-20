package org.Khrustalev.application.exceptions;

/**
 * Исключение, которое выбрасывается, если учетная запись не существует.
 * Это исключение используется для сигнализации о том, что попытка доступа к учетной записи с заданным идентификатором
 * не удалась, так как такая учетная запись не была найдена в системе.
 */
public class AccountDoesNotExistException extends Exception {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message описание ошибки, объясняющее причину выбрасывания исключения
     */
    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
