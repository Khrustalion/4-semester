package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.application.exceptions.NegativeAmountException;

/**
 * Команда для внесения средств на учетную запись.
 * Этот класс реализует команду для добавления средств на указанную учетную запись через сервис `AccountService`.
 */
public class DepositFundsCommand implements AbstractCommand {
    private final AccountService accountService;
    private final int accountId;
    private final double amount;

    /**
     * Конструктор для создания команды внесения средств на учетную запись.
     *
     * @param accountService сервис для работы с учетными записями
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма средств для внесения
     */
    public DepositFundsCommand(AccountService accountService, int accountId, double amount) {
        this.accountService = accountService;
        this.accountId = accountId;
        this.amount = amount;
    }

    /**
     * Выполняет команду внесения средств на учетную запись.
     * Добавляет указанную сумму на баланс учетной записи и возвращает строку с подтверждением внесения.
     * В случае ошибки (например, если учетная запись не существует или сумма отрицательна),
     * возвращается сообщение об ошибке.
     *
     * @return строка с подтверждением успешного внесения средств или сообщением об ошибке
     */
    @Override
    public String execute() {
        try {
            accountService.addFunds(this.accountId, this.amount);

            return String.format("Deposit Funds. AccountId: %d", this.accountId);
        }
        catch (AccountDoesNotExistException | NegativeAmountException e) {
            return "Error: " + e.getMessage();
        }
    }
}


