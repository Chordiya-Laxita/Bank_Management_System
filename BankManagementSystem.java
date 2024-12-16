import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BankManagementSystem {
    static double Balance = 0;
    static String currentUser = "";

    public static void main(String[] args) {
        System.out.println("1. Login \n2. Registration");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                if (Login()) {
                    Bankoperation();
                } else {
                    System.out.println("Login failed. Please try again.");
                }
                break;
            case 2:
                Registration();
                break;
            default:
                System.out.println("Invalid option. Exiting...");
                break;
        }
    }

    public static void Registration() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Name:");
        String Name = sc.nextLine();
        System.out.println("Enter Password:");
        String Password = sc.nextLine();
        System.out.println("Enter Account No.:");
        int AccountNo = sc.nextInt();
        double initialBalance = 0.0; // Ensure balance is added

        try {
            FileWriter writer = new FileWriter("Data.txt", true);
            writer.write(Name + "," + Password + "," + AccountNo + "," + initialBalance + "\n");
            writer.close();
            System.out.println("Registration successful!");
        } catch (IOException e) {
            System.out.println("An error occurred during registration.");
            e.printStackTrace();
        }
    }


    public static void Bankoperation() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1: Check Balance");
            System.out.println("2: Deposit Balance");
            System.out.println("3: Withdraw Balance");
            System.out.println("4: Exit");
            System.out.println("Enter your Option:");

            int option = sc.nextInt();
            switch (option) {
                case 1:
                    CheckBalance();
                    break;
                case 2:
                    Deposit();
                    break;
                case 3:
                    Withdraw();
                    break;
                case 4:
                    Exit();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static boolean Login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Name:");
        String name = sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();

        try {
            File file = new File("Data.txt");
            if (!file.exists()) {
                System.out.println("No registered users found. Please register first.");
                return false;
            }

            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String[] credentials = fileReader.nextLine().split(",");
                if (credentials[0].equals(name) && credentials[1].equals(password)) {
                    System.out.println("Login successful!");
                    currentUser = name;


                    if (credentials.length >= 4) {
                        Balance = Double.parseDouble(credentials[3]); // Load the balance
                    } else {
                        Balance = 0.0; // Default balance if missing
                    }
                    return true;
                }
            }
            System.out.println("Invalid username or password.");
        } catch (IOException e) {
            System.out.println("An error occurred during login.");
            e.printStackTrace();
        }
        return false;
    }


    public static void CheckBalance() {
        System.out.println("Your Current Balance is: " + Balance);
    }

    public static void Deposit() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Amount to deposit:");
        double Amount = sc.nextDouble();
        Balance += Amount;
        System.out.println("Amount has been deposited.");
        updateBalanceInFile();
        CheckBalance();
    }

    public static void Withdraw() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Amount to withdraw:");
        double Amount = sc.nextDouble();
        if (Balance < Amount) {
            System.out.println("Insufficient Balance.");
        } else {
            Balance -= Amount;
            System.out.println("Amount has been withdrawn.");
        }
        updateBalanceInFile();
        CheckBalance();
    }

    public static void updateBalanceInFile() {
        try {
            File file = new File("Data.txt");
            if (!file.exists()) {
                System.out.println("Data file not found.");
                return;
            }

            ArrayList<String> updatedData = new ArrayList<>();
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(currentUser)) {
                    updatedData.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + Balance);
                } else {
                    updatedData.add(line);
                }
            }
            fileReader.close();

            PrintWriter writer = new PrintWriter(new FileWriter("Data.txt", false));
            for (String data : updatedData) {
                writer.println(data);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while updating the balance.");
            e.printStackTrace();
        }
    }

    public static void Exit() {
        System.out.println("Thank you for using the Bank Management System.");
    }
}
