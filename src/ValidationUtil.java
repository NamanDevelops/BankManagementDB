


public  class ValidationUtil {
    
    private boolean valid;
    private String message;
    public ValidationUtil(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    public boolean isValid() {
        return valid;
    }
    public String getMessage() {
        return message;
    }    

    public static ValidationUtil ValidationLogic(String accountnumber){
           
            if((accountnumber.startsWith("-")))
                 return new ValidationUtil(false,"Account No. Can't be Negative");
                
            if((accountnumber.startsWith("0")))
                return new ValidationUtil(false,"First Digit of Account No can't be Zero");

            if(!(accountnumber.matches("^\\d+$")))
                return new ValidationUtil(false,"Account No. can contain only digits");

            if(accountnumber.length()<9 || accountnumber.length()>18)
                return new ValidationUtil(false,"Length Of Account Number is Wrong ");
                
            return new ValidationUtil(true,"Valid");
    }

    public static boolean uniqueAccountNo(String accountnumber,AccountDAO accountDAO){
            Account account=accountDAO.getAccountByNumber(accountnumber);
            return account==null;
           
    }

    public static boolean isHolderNameValid(String holdername){
        if(!(holdername.matches("[a-zA-Z\' -]+")) || holdername.length()<2|| holdername.length()>50)
            return false;
        return true; // like else
    }
    
    public static boolean isHolderAgeValid(int age){
        if(age<1)
            return false;
        return true;
    }
    public static boolean isHolderBalanceValid(double balance){
        if(!Double.isFinite(balance) || balance<0)
            return false;
        return true;
    }


     public static boolean hasBalance(Account account){   // checks is Account Balance Zero?
        if (account.getBalance()==0) 
            return false;
        return true;
    }
}

