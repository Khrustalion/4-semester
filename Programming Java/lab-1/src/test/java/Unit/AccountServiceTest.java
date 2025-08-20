package Unit;

import org.Khrustalev.Infrastructure.repository.AccountRepositoryImpl;
import org.Khrustalev.Infrastructure.repository.TransactionsRepositoryImpl;
import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.exceptions.AccountDoesNotExistException;
import org.Khrustalev.application.exceptions.NegativeAmountException;
import org.Khrustalev.application.exceptions.NotEnoughMoneyException;
import org.Khrustalev.application.services.AccountServiceImpl;
import org.Khrustalev.domain.repositories.AccountRepository;
import org.Khrustalev.domain.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    @Test
    public void GetFunds_ShouldThrowException_WhenAccountDoesNotExist() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        assertThrows(AccountDoesNotExistException.class, () -> accountService.getFunds(1));
    }

    @Test
    public void AddFunds_ShouldThrowException_WhenAccountDoesNotExist() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);


        assertThrows(AccountDoesNotExistException.class, () -> accountService.addFunds(1, 100));
    }

    @Test
    public void AddFunds_ShouldThrowException_WhenNegativeAmount() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        assertThrows(NegativeAmountException.class, () -> accountService.addFunds(1, -100));
    }

    @Test
    public void AddFunds_ShouldSetCorrectDeposit() throws NegativeAmountException, AccountDoesNotExistException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        accountService.addFunds(1, 100);

        int expected = 100;

        assertEquals(expected, accountService.getFunds(1));
    }

    @Test
    public void AddFunds_ShouldAddCorrectFunds() throws NegativeAmountException, AccountDoesNotExistException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        accountService.addFunds(1, 100);
        accountService.addFunds(1, 100);

        int expected = 200;

        assertEquals(expected, accountService.getFunds(1));
    }

    @Test void addFunds_ShouldAddCorrectFunds_WhenAmountZero() throws NegativeAmountException, AccountDoesNotExistException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        accountService.addFunds(1, 0);

        int expected = 0;

        assertEquals(expected, accountService.getFunds(1));
    }

    @Test
    public void WithdrawFunds_ShouldSetCorrectFunds_WhenFundsEqualsAmount() throws NegativeAmountException, AccountDoesNotExistException, NotEnoughMoneyException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        accountService.addFunds(1, 100);
        accountService.withdrawFunds(1, 100);

        int expected = 0;

        assertEquals(expected, accountService.getFunds(1));
    }

    @Test
    public void WithdrawFunds_ShouldSetCorrectFunds_WhenFundsMoreAmount() throws NegativeAmountException, AccountDoesNotExistException, NotEnoughMoneyException {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        accountService.addFunds(1, 100);
        accountService.withdrawFunds(1, 99);

        int expected = 1;

        assertEquals(expected, accountService.getFunds(1));
    }

    @Test
    public void WithdrawFunds_ShouldThrowException_WhenAccountDoesNotExist() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        assertThrows(AccountDoesNotExistException.class, () -> accountService.withdrawFunds(1, 100));
    }

    @Test
    public void WithdrawFunds_ShouldThrowException_WhenNotEnoughMoney() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        assertThrows(NotEnoughMoneyException.class, () -> accountService.withdrawFunds(1, 100));
    }

    @Test
    public void WithdrawFunds_ShouldThrowException_WhenNegativeAmount() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);

        accountService.addAccount(1);

        assertThrows(NegativeAmountException.class, () -> accountService.withdrawFunds(1, -100));
    }

    @Test
    public void getTransactions_ShouldThrowException_WhenAccountDoesNotExist() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository);


        assertThrows(AccountDoesNotExistException.class, () -> accountService.getTransactions(1));
    }
}
