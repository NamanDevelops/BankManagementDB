
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Bank {
    
   // Using Scanner object declared in Main
    private final Scanner scanner;
    public Bank(Scanner scanner){
        this.scanner=scanner;
    }
    private final AccountDAO accountDAO=new AccountDAO();
    
    // Helper Method For Reading accountnum
    public String readAccountNum(String message){
        System.out.println(message);
            return scanner.nextLine();      
    }
    

    
    public void createAccount(){
        String accountnumber=readAccountNum("Enter Account Number :");
        ValidationUtil result=ValidationUtil.ValidationLogic(accountnumber);    
        if(!result.isValid()){  // Runs when validation fail
            System.out.println(result.getMessage());
            return;
        }
    
            if(!ValidationUtil.uniqueAccountNo(accountnumber,accountDAO)){
                System.out.println("This Already Exists, Account no Should be Unique always");    
                return;
            
            } 

       System.out.println("Enter Account holder Name :");
       String holdername=scanner.nextLine();
       if(!ValidationUtil.isHolderNameValid(holdername)){
            System.out.println("Invalid Input, Please enter a valid Name"); 
            return;
       }

       System.out.println("Enter Account holder Age :");
       int holderage=0;   
       try {
          holderage=scanner.nextInt();
          scanner.nextLine();     
       } catch (InputMismatchException e) {
        System.out.println("Invalid Input, Age must be an integer");
        scanner.nextLine();
        return;
       }
       if(!ValidationUtil.isHolderAgeValid(holderage)){
        System.out.println("Age can't be 0 or negative");       
        return;
       }

        System.out.println("Enter Initial Balance in rs (No Unit):");
        double holderbalance = 0;
        try {
            holderbalance = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input, Balance must be a number");
            scanner.nextLine();
            return;
        }
        if(!ValidationUtil.isHolderBalanceValid(holderbalance)){
        System.out.println("Invalid balance: balance must be zero or greater finite number");       
        return;
       }

       Account account=new Account(holdername, holderage, accountnumber, holderbalance);
       if(accountDAO.createAccount(account)){
        System.out.println("\nAccount Creation Successfull");
       }
       else{
         System.out.println("\nAccount Creation Failed");
       }
      
    }


    public void viewAllAccount(){
        List <Account> accountslist=accountDAO.getAllAccounts();
        if(accountslist.isEmpty()){
            System.out.println("No Account Created till Now");
            return;
        }
        System.out.println("Deatails of All Account Holders:");
        for (Account account : accountslist) {
            System.out.println(account);
        }
    }


    public void searchAccount(){
      
        if(!accountDAO.hasAnyAccounts()){   // Used this method
             System.out.println("No Account Created till Now, So can't Search ");
             return ;
        }

        String accountnumber=readAccountNum("Enter Account Number :");
        ValidationUtil result=ValidationUtil.ValidationLogic(accountnumber);    
        if(!result.isValid()){  // Runs when validation fail
            System.out.println(result.getMessage());
            return;
        }
            Account account=accountDAO.getAccountByNumber(accountnumber);
            if(account!=null){
                System.out.println(account);    // isExist=account
            }
            else{
                System.out.println("\nThis Account Number Doesn't Exist");
                return;
            }
        
    }


    public void depositMoney(){
        if(!accountDAO.hasAnyAccounts()){
             System.out.println("No Account Created till Now, So can't Deposit Money ");
             return ;
        }
    
        String accountnumber=readAccountNum("Enter Account Number :");
        ValidationUtil result=ValidationUtil.ValidationLogic(accountnumber);    
        if(!result.isValid()){  // Runs when validation fail
            System.out.println(result.getMessage());
            return;
        }
        // Checking Entered AccNo Exists Or Not 
       
                Account account=accountDAO.getAccountByNumber(accountnumber);
                if(account!=null)
                {
                    System.out.println("Enter Amount For Deposit :");
                    double amount = 0;
                        try{   
                            amount = scanner.nextDouble();
                            if(amount==0){
                                            System.out.println("Invalid Input, Zero rs can't be deposit ");
                                            return ;
                                        }
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input, amount must be a number");
                            scanner.nextLine();
                            return;
                        }
                        if(!ValidationUtil.isHolderBalanceValid(amount)){  // IK Should Use isholderAmountValid() but i played little to avoid write another method to validate +,- 
                        System.out.println("Invalid amount: amount must be a positive finite number");       
                        return;
                        }
                            if((accountDAO.depositMoney(accountnumber, amount))){
                                System.out.println(amount +"rs Deposited Succesfully");
                            }
                            else{
                                System.out.println(" Depositing Money Failed.");
                            }
                    
                }
                else{
                    System.out.println("\nThis Account Number Doesn't Exist");
                    return;
                }
    }

    public void withdrawMoney(){
       if(!accountDAO.hasAnyAccounts()){
             System.out.println("No Account Created till Now, So can't Withdraw Money ");
             return ;
        }

        String accountnumber=readAccountNum("Enter Account Number :");
        ValidationUtil result=ValidationUtil.ValidationLogic(accountnumber);    
        if(!result.isValid()){  // Runs when validation fail
            System.out.println(result.getMessage());
            return;
        }
        // Checking Entered AccNo Exists Or Not 
       
                Account account=accountDAO.getAccountByNumber(accountnumber);
                if(account!=null)
                {
                
                    if(!ValidationUtil.hasBalance(account)){
                            System.out.println("'Error!, Withdrawing Money Not Possible', This Account has Zero Balance");
                            return;
                    }
                    System.out.println("Enter Amount For Withdrawl :");
                    double amount = 0;
                        try {
                            amount = scanner.nextDouble();
                            if(amount==0){
                                    System.out.println("Invalid Input, Zero rs can't be Withdraw ");
                                    return ;
                            }
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input, amount must be a number");
                            scanner.nextLine();
                            return;
                        }
                    if(!ValidationUtil.isHolderBalanceValid(amount)){   
                        System.out.println("Invalid amount: amount must be a positive finite number");       
                        return;
                    } 
                        // Calling withdrawMoney()
                            if(accountDAO.withdrawMoney(accountnumber, amount)){
                                    System.out.println(amount +"rs Withdrawn Succesfully"); 
                            }
                            else{
                                System.out.println("'Withdrawl Failed!', This Account has Insufficient Balance to Withdraw Money");
                            }
                }
                else{
                    System.out.println("\nThis Account Number Doesn't Exist");
                    return;
                }
    }


    public void transferMoney(){
        int accountCount = accountDAO.getAccountCount();

        if (accountCount == 0) {
            System.out.println("No Account Created till Now, So can't transfer Money");
            return;
        }
        if (accountCount == 1) {
            System.out.println("'Error!, Transfer Money Not Possible', Only 1 Account is Created till Now");
            return;
        }
        
        String senderaccountnumber=readAccountNum("Enter Sender's Account Number :");
        ValidationUtil senderresult=ValidationUtil.ValidationLogic(senderaccountnumber);    
        if(!senderresult.isValid()){  // Runs when validation fail
            System.out.println(senderresult.getMessage());
            return;
        }
        // Checking Entered Sender's AccNo Exists Or Not 
            Account senderAccNumExist=accountDAO.getAccountByNumber(senderaccountnumber);
            if(senderAccNumExist!=null)
            {
                    // Checking Does the Sender has any(not zero) Balance to Transfer
                    if(!ValidationUtil.hasBalance(senderAccNumExist)){
                        System.out.println("'Error!, Transfer Money Not Possible', Sender's Account has Zero Balance");
                        return;
                    }
                    
                    String receiveraccountnumber=readAccountNum("Enter Receiver's Account Number :");
                    ValidationUtil receiverresult=ValidationUtil.ValidationLogic(receiveraccountnumber);    
                    if(!receiverresult.isValid()){  // Runs when validation fail
                        System.out.println(receiverresult.getMessage());
                        return;
                    }
                    // Sender & Receiver Can't be Same
                        if(senderaccountnumber.equals(receiveraccountnumber)){
                            System.out.println("'Error!', Sender and Receiver Can't belongs to the same Account Number");
                            return ;
                        }
                        // Checking Entered Receiver's AccNo Exists Or Not 
                        Account receiverAccNumExist=accountDAO.getAccountByNumber(receiveraccountnumber);
                        if(receiverAccNumExist!=null)
                        {
                            System.out.println("Enter Amount that You want to Trasfer in rs (No Unit)  :");
                            double amount = 0;
                            try{
                                    amount = scanner.nextDouble();
                                    if(amount==0){
                                        System.out.println("Invalid Input, Zero rs can't be transfer ");
                                        return ;
                                    }
                                    scanner.nextLine();
                            } catch (InputMismatchException e) {
                                    System.out.println("Invalid Input, amount must be a number");
                                    scanner.nextLine();
                                    return;
                            }
                            if(!ValidationUtil.isHolderBalanceValid(amount)){
                                System.out.println("Invalid amount: amount must be a positive finite number");  
                                return;
                            }
                            
                                // Does the Sender has Sufficient Balance to Transfer, has checked in transferMoney()
                                if (accountDAO.transferMoney(senderaccountnumber,receiveraccountnumber,amount)) {

                                    System.out.println(
                                        "'Transfer Successful', " + amount + "rs Transferred");
                                } else {
                                    System.out.println(
                                        "Transfer failed: sender has insufficient balance to transfer");
                                }
                        }
                        else{
                            System.out.println("\nThis Receiver's Account Number Doesn't Exist");
                            return;
                        }
            }
            else{
                    System.out.println("\nThis Sender's Account Number Doesn't Exist");
                    return;
                }
       
    }


    public void deleteAccount(){
       if(!accountDAO.hasAnyAccounts()){
             System.out.println("No Account Created till Now, So can't Delete Accounts ");
             return ;
        }

        String accountnumber=readAccountNum("Enter Account Number :");
        ValidationUtil result=ValidationUtil.ValidationLogic(accountnumber);    
        if(!result.isValid()){  // Runs when validation fail
            System.out.println(result.getMessage());
            return;
        }
        // Checking Entered AccNo Exists Or Not 
    
            Account account=accountDAO.getAccountByNumber(accountnumber);
            if(account!=null)
            {
                   
                if(accountDAO.deleteAccount(accountnumber)){
                    //list.remove(account);
                    System.out.println("Account Deleted Successfully");
                }
                else{
                    System.out.println("Account Deletion Failed: Can't delete account with remaining Balance ");
                }
                
            }
            else{
                System.out.println("\nThis Account Number Doesn't Exist");
                return;
            }
    }
}
