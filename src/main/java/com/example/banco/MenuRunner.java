package com.example.banco;

import com.example.banco.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class MenuRunner implements CommandLineRunner {

    private final AccountService accountService;
    private final Scanner scanner;

    public MenuRunner(AccountService accountService) {
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to the Spring Boot CLI Bank! (Java 17)");


        var running = true;
        while (running) {
            displayMenu();
            try {

                var input = scanner.nextLine();


                if (input.isBlank()) {
                    System.out.println("Please enter an option.");
                    continue;
                }

                var option = Integer.parseInt(input);

                switch (option) {
                    case 1: checkBalance(); break;
                    case 2: checkOverdraft(); break;
                    case 3: deposit(); break;
                    case 4: withdraw(); break;
                    case 5: payBill(); break;
                    case 6: checkIfUsingOverdraft(); break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using our services. Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number for the option.");
            } catch (Exception e) {
                System.out.println("Operation Error: " + e.getMessage());
            }
            System.out.println("---------------------------------");
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n*** Main Menu ***");
        System.out.println("1. Check Balance");
        System.out.println("2. Check Overdraft (Limit and Usage)");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Pay a Bill");
        System.out.println("6. Check if Using Overdraft");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // --- Operation Methods ---

    private void checkBalance() {
        var balance = accountService.checkBalance();
        System.out.printf("Your current balance is: R$ %.2f\n", balance);
    }

    private void checkOverdraft() {
        var info = accountService.getOverdraftInfo();
        System.out.println(info);
    }

    private void checkIfUsingOverdraft() {
        if (accountService.isUsingOverdraft()) {
            System.out.println("Yes, you are currently using your overdraft.");
        } else {
            System.out.println("No, you are not using your overdraft.");
        }
    }

    // --- Input Methods ---

    private BigDecimal readValue(String prompt) {
        while(true) {
            try {
                System.out.print(prompt);
                var valueStr = scanner.nextLine();
                valueStr = valueStr.replace(',', '.');

                return new BigDecimal(valueStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Use format 50.00 or 50,00.");
            }
        }
    }

    private void deposit() {
        var amount = readValue("Enter the amount to deposit (e.g., 50.00): ");
        accountService.deposit(amount);
        System.out.println("Deposit successful.");
        checkBalance(); // Mostra o novo saldo
    }

    private void withdraw() {
        var amount = readValue("Enter the amount to withdraw (e.g., 100.00): ");
        accountService.withdraw(amount);
        System.out.println("Withdrawal successful.");
        checkBalance();
    }

    private void payBill() {
        var amount = readValue("Enter the bill amount (e.g., 75.50): ");
        accountService.payBill(amount);
        System.out.println("Bill paid successfully.");
        checkBalance();
    }
}

