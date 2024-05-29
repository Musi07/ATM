import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ATM {
    static int balance = 5000; // balance is static to maintain the same balance for all instances
    static List<Account> accounts = new ArrayList<>(); // To store account information
    static int currentAccountIndex = -1; // Variable to keep track of the currently inserted card's index

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            System.out.println("*Automated Teller Machine*");
            System.out.println("1. Insert Card");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose the operation you want to perform: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    insertCard(sc, executor);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
                    executor.shutdown();
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    // Method to create an account
    static void createAccount(Scanner scanner) {
        System.out.println("*** Create Account ***");
        // Get account details from the user
        System.out.print("Enter card number: ");
        int cardNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Set PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter blood group: ");
        String bloodGroup = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter nationality: ");
        String nationality = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter mobile number: ");
        String mobileNumber = scanner.nextLine();
        // Save the account details
        Account account = new Account(cardNumber, name, bloodGroup, age, nationality, address, mobileNumber, pin);
        accounts.add(account);
        System.out.println("Account created successfully!");
    }

    // Method to insert card and perform operations
    static void insertCard(Scanner scanner, ExecutorService executor) {
        System.out.println("*** Insert Card ***");
        // Verify the card (you can implement card verification logic here)
        // For simplicity, let's assume card verification is successful
        // Show options after card insertion
        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Exit");
            System.out.print("Choose the operation you want to perform: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    // Method for user login
    static void login(Scanner scanner) {
        System.out.println("*** Login ***");
        System.out.print("Enter card number: ");
        int cardNumber = scanner.nextInt();
        System.out.print("Enter PIN: ");
        int pin = scanner.nextInt();
        // Check if the provided card number and PIN match any account
        for (Account account : accounts) {
            if (account.cardNumber == cardNumber && account.pin == pin) {
                System.out.println("Login successful!");
                currentAccountIndex = accounts.indexOf(account);
                performOperations(scanner);
                return;
            }
        }
        System.out.println("Invalid card number or PIN. Login failed.");
    }

    // Method to perform operations after login
    static void performOperations(Scanner scanner) {
        while (true) {
            System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. Show Receipt");
            System.out.println("5. Print Receipt");
            System.out.println("6. Show Transaction History");
            System.out.println("7. Currency Converter");
            System.out.println("8. Logout");
            System.out.print("Choose the operation you want to perform: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    performWithdraw(scanner);
                    break;
                case 2:
                    performDeposit(scanner);
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    showReceipt();
                    break;
                case 5:
                    printReceipt();
                    break;
                case 6:
                    showTransactionHistory();
                    break;
                case 7:
                    currencyConverter(scanner);
                    break;
                case 8:
                    currentAccountIndex = -1; // Reset current account index
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    static void performWithdraw(Scanner scanner) {
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();
        if (!verifyPIN(pin)) {
            System.out.println("Invalid PIN. Transaction aborted.");
            return;
        }
        System.out.print("Enter money to be withdrawn: ");
        int withdraw = scanner.nextInt();
        Runnable withdrawTask = () -> {
            synchronized (ATM.class) {
                if (balance >= withdraw) {
                    balance -= withdraw;
                    System.out.println("Please collect your money");
                    logTransaction("Withdraw", withdraw);
                } else {
                    System.out.println("Insufficient Balance");
                }
            }
        };
        withdrawTask.run();
        System.out.println("");
    }

    static void performDeposit(Scanner scanner) {
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();
        if (!verifyPIN(pin)) {
            System.out.println("Invalid PIN. Transaction aborted.");
            return;
        }
        System.out.print("Enter money to be deposited: ");
        int deposit = scanner.nextInt();
        Runnable depositTask = () -> {
            synchronized (ATM.class) {
                balance += deposit;
                System.out.println("Your Money has been successfully deposited");
                logTransaction("Deposit", deposit);
            }
        };
        depositTask.run();
        System.out.println("");
    }

    static void checkBalance() {
        System.out.println("Balance: " + balance);
        System.out.println("");
    }

    static void showReceipt() {
        if (currentAccountIndex == -1) {
            System.out.println("Please insert your card to show receipt.");
            return;
        }
        Account account = accounts.get(currentAccountIndex);
        System.out.println("*** Receipt ***");
        System.out.println("Date and Time: " + new Date());
        // Display transaction details
        System.out.println("Name: " + account.name);
        System.out.println("Blood Group: " + account.bloodGroup);
        System.out.println("Age: " + account.age);
        System.out.println("Nationality: " + account.nationality);
        System.out.println("Address: " + account.address);
        System.out.println("Mobile Number: " + account.mobileNumber);
        System.out.println("Balance: " + balance);
        System.out.println("Transaction History: ");
        // Assuming you have a method in Account class to get transaction history
        List<String> transactionHistory = account.getTransactionHistory();
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
        System.out.println("");
    }

    static void printReceipt() {
        if (currentAccountIndex == -1) {
            System.out.println("Please insert your card to print receipt.");
            return;
        }
        Account account = accounts.get(currentAccountIndex);

        JFrame frame = new JFrame("ATM Receipt");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setBackground(Color.WHITE);

        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("*** Receipt ***\n");
        receiptContent.append("Date and Time: ").append(new Date()).append("\n");
        receiptContent.append("Name: ").append(account.name).append("\n");
        receiptContent.append("Blood Group: ").append(account.bloodGroup).append("\n");
        receiptContent.append("Age: ").append(account.age).append("\n");
        receiptContent.append("Nationality: ").append(account.nationality).append("\n");
        receiptContent.append("Address: ").append(account.address).append("\n");
        receiptContent.append("Mobile Number: ").append(account.mobileNumber).append("\n");
        receiptContent.append("Balance: ").append(balance).append("\n");
        receiptContent.append("Transaction History:\n");

        
        List<String> transactionHistory = account.getTransactionHistory();
        for (String transaction : transactionHistory) {
            receiptContent.append(transaction).append("\n");
        }

        receiptArea.setText(receiptContent.toString());

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    static void showTransactionHistory() {
        if (currentAccountIndex == -1) {
            System.out.println("Please insert your card to view transaction history.");
            return;
        }
        Account account = accounts.get(currentAccountIndex);
        System.out.println("*** Transaction History ***");
        List<String> transactionHistory = account.getTransactionHistory();
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
        System.out.println("");
    }

    static boolean verifyPIN(int pin) {
        if (currentAccountIndex == -1) {
            System.out.println("Please insert your card to verify PIN.");
            return false;
        }
        Account account = accounts.get(currentAccountIndex);
        return account.pin == pin;
    }

    static void logTransaction(String type, int amount) {
        if (currentAccountIndex != -1) {
            Account account = accounts.get(currentAccountIndex);
            account.addTransaction(type, amount);
        }
    }

    static void currencyConverter(Scanner scanner) {
        System.out.println("*** Currency Converter ***");
        System.out.println("Choose the currency to convert to:");
        System.out.println("1. USD");
        System.out.println("2. EUR");
        System.out.println("3. GBP");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        double convertedAmount = 0;
        String currencySymbol = "";

        switch (choice) {
            case 1:
                convertedAmount = convertToUSD(balance);
                currencySymbol = NumberFormat.getCurrencyInstance(Locale.US).getCurrency().getSymbol();
                break;
            case 2:
                convertedAmount = convertToEUR(balance);
                currencySymbol = NumberFormat.getCurrencyInstance(Locale.FRANCE).getCurrency().getSymbol();
                break;
            case 3:
                convertedAmount = convertToGBP(balance);
                currencySymbol = NumberFormat.getCurrencyInstance(Locale.UK).getCurrency().getSymbol();
                break;
            default:
                System.out.println("Invalid choice. Conversion aborted.");
                return;
        }

        System.out.println("Converted Balance: " + currencySymbol + String.format("%.2f", convertedAmount));
    }

    static double convertToUSD(int amount) {
        double exchangeRate = 0.012; // Example exchange rate
        return amount * exchangeRate;
    }

    static double convertToEUR(int amount) {
        double exchangeRate = 0.011; // Example exchange rate
        return amount * exchangeRate;
    }

    static double convertToGBP(int amount) {
        double exchangeRate = 0.009; // Example exchange rate
        return amount * exchangeRate;
    }
}

class Account {
    int cardNumber;
    String name;
    String bloodGroup;
    int age;
    String nationality;
    String address;
    String mobileNumber;
    int pin;
    List<String> transactionHistory = new ArrayList<>();

    public Account(int cardNumber, String name, String bloodGroup, int age, String nationality, String address, String mobileNumber, int pin) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.nationality = nationality;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String type, int amount) {
        String transaction = type + " : " + amount;
        transactionHistory.add(transaction);
    }
}

