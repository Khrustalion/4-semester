package org.Khrustalev.application.commands;

import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.application.exceptions.NegativeAmountException;
import org.Khrustalev.application.exceptions.NotEnoughMoneyException;

/**
 * Команда для снятия средств с учетной записи.
 * Этот класс реализует команду для снятия средств с указанной учетной записи через сервис `AccountService`.
 */
public class WithdrawFundsCommand implements AbstractCommand {
    private final int accountId;
    private final double amount;
    private final AccountService accountService;

    /**
     * Конструктор для создания команды снятия средств с учетной записи.
     *
     * @param accountService сервис для работы с учетными записями
     * @param accountId уникальный идентификатор учетной записи
     * @param amount сумма средств для снятия
     */
    public WithdrawFundsCommand(AccountService accountService, int accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.accountService = accountService;
    }

    /**
     * Выполняет команду снятия средств с учетной записи.
     * Снимает указанную сумму с баланса учетной записи и возвращает строку с подтверждением снятия.
     * В случае ошибки (например, если недостаточно средств, сумма отрицательна или учетная запись не существует),
     * возвращается сообщение об ошибке.
     *
     * @return строка с подтверждением успешного снятия средств или сообщением об ошибке
     */
    @Override
    public String execute()  {
        try {
            accountService.withdrawFunds(accountId, amount);

            return String.format("Withdraw Funds. AccountId: %d", this.accountId);
        }
        catch (NotEnoughMoneyException | NegativeAmountException | AccountDoesNotExistException e) {
            return "Error: " + e.getMessage();
        }
    }
}

