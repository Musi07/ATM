import java.util.*;

public class ATM {
    static int balance = 5000; // balance is static to maintain the same balance for all instances
    static List<Account> accounts = new ArrayList<>(); // To store account information
    static int currentAccountIndex = -1; // Variable to keep track of the currently inserted card's index

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("*Automated Teller Machine*");
            System.out.println("1. Insert Card");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose the operation you want to perform: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    insertCard(sc);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
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
    static void insertCard(Scanner scanner) {
        System.out.println("*** Insert Card ***");

        // Verify the card (you can implement card verification logic here)
        // For simplicity, let's assume card verification is successful

        // Show options after card insertion
        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Exit");
            System.out.print("Choose the operation you want to perform:");

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
            System.out.println("6. Logout");
            System.out.print("Choose the operation you want to perform:");

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

        System.out.print("Enter money to be withdrawn:");
        int withdraw = scanner.nextInt();
        if (balance >= withdraw) {
            balance -= withdraw;
            System.out.println("Please collect your money");
        } else {
            System.out.println("Insufficient Balance");
        }
        System.out.println("");
    }

    static void performDeposit(Scanner scanner) {
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();
        if (!verifyPIN(pin)) {
            System.out.println("Invalid PIN. Transaction aborted.");
            return;
        }

        System.out.print("Enter money to be deposited:");
        int deposit = scanner.nextInt();
        balance += deposit;
        System.out.println("Your Money has been successfully deposited");
        System.out.println("");
    }

    static void checkBalance() {
        System.out.println("Balance : " + balance);
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
        System.out.println("Balance : " + balance);
        System.out.println("");
    }

    static void printReceipt() {
        if (currentAccountIndex == -1) {
            System.out.println("Please insert your card to print receipt.");
            return;
        }

        Account account = accounts.get(currentAccountIndex);

        // Printing receipt on paper (You can implement printing logic here)
        System.out.println("*** Receipt ***");
        System.out.println("Date and Time: " + new Date());
        // Display transaction details
        System.out.println("Name: " + account.name);
        System.out.println("Blood Group: " + account.bloodGroup);
        System.out.println("Age: " + account.age);
        System.out.println("Nationality: " + account.nationality);
        System.out.println("Address: " + account.address);
        System.out.println("Mobile Number: " + account.mobileNumber);
        System.out.println("Balance : " + balance);
        System.out.println("");
    }

    static boolean verifyPIN(int pin) {
        // Check if the provided PIN matches any account's PIN
        for (Account account : accounts) {
            if (account.pin == pin) {
                currentAccountIndex = accounts.indexOf(account);
                return true;
            }
        }
        return false;
    }

    // Inner class representing an Account
    static class Account {
        int cardNumber;
        String name;
        String bloodGroup;
        int age;
        String nationality;
        String address;
        String mobileNumber;
        int pin;

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
    }
}