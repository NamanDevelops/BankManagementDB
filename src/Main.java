
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Bank bank=new Bank(scanner);
        boolean running=true;
        while (running) {
            System.out.println("\n*********************************");
            System.out.println("Banking Management System");
            System.out.println("*********************************");
            System.out.println("1: Create Account");
            System.out.println("2: View All Account");
            System.out.println("3: Search Account");
            System.out.println("4: Deposit Money");
            System.out.println("5: Withdraw Money");
            System.out.println("6: Transfer Money");
            System.out.println("7: Delete Account");
            System.out.println("8: Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter Your Choice:");
            int choice=0;
            try {
                choice=scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Only Integer choice is allowed");
                scanner.nextLine();
            }
            
            switch (choice) {
                case 1:
                    try {
                        bank.createAccount();
                    } catch (IllegalStateException e) {
                        System.out.println("Account database operation failed: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        bank.viewAllAccount();
                    } catch (IllegalStateException e) {
                        System.out.println("Account database operation failed: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        bank.searchAccount();
                    } catch (IllegalStateException e) {
                       System.out.println("Account database operation failed: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        bank.depositMoney();
                    } catch (IllegalStateException e) {
                        System.out.println("Account database operation failed: "+ e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        bank.withdrawMoney();
                    } catch (IllegalStateException e) {
                        System.out.println("Account database operation failed: "+ e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        bank.transferMoney();
                    } catch (IllegalStateException  e) {
                        System.out.println("Account database operation failed:"+ e.getMessage());
                    }
                    break;
                case 7:
                   try {
                     bank.deleteAccount();
                   } catch (IllegalStateException  e) {
                    System.out.println("Account database operation failed: "+ e.getMessage());
                   }
                    break;
                case 8:
                    System.out.println("--------------------------------");
                    System.out.println("Thank You For Using My Project");
                    System.out.println("--------------------------------");
                    running=false;
                    break;

                default:
                    System.out.println("Invalid Choice, Please Enter any of Given Choices");
            }
        }
        scanner.close();
    }
}
