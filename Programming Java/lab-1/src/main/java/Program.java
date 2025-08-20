import org.Khrustalev.Infrastructure.repository.AccountRepositoryImpl;
import org.Khrustalev.Infrastructure.repository.TransactionsRepositoryImpl;
import org.Khrustalev.application.commands.AbstractCommand;
import org.Khrustalev.application.contracts.AccountService;
import org.Khrustalev.application.services.AccountServiceImpl;
import org.Khrustalev.domain.repositories.AccountRepository;
import org.Khrustalev.domain.repositories.TransactionRepository;
import org.Khrustalev.presentation.Console.Handlers.Factories.HandlersFactory;
import org.Khrustalev.presentation.Console.Handlers.Handlable;

import java.util.Arrays;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        AccountRepository accountRepo = new AccountRepositoryImpl();
        TransactionRepository transactionRepo = new TransactionsRepositoryImpl();

        AccountService accountService = new AccountServiceImpl(accountRepo, transactionRepo);
        HandlersFactory handlersFactory = new HandlersFactory(accountService);

        Handlable handler = handlersFactory.createHandlers();

        Scanner scanner = new Scanner(System.in);

        String input;

        while (true) {
            input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Program terminated.!");
                break;
            }

            AbstractCommand command = handler.handle(Arrays.stream(input.split(" ")).toList());

            if (command == null) {
                System.out.println("Unknown command.");
            }
            else {
                System.out.println(command.execute());
            }
        }

    }
}
