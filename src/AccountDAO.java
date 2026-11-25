import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    // Save accounts into DB
    public boolean createAccount(Account account){
        String query="insert into account (accountnum, holdername, holderage, balance) values (?,?,?,?)";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(query)) 
        {
            ps.setString(1, account.getAccountnum());
            ps.setString(2, account.getHoldername());
            ps.setInt(3,account.getHolderage());
            ps.setDouble(4, account.getBalance());
        return ps.executeUpdate()==1;

        } catch (SQLException e) {
            throw new IllegalStateException("Could not save account to the database", e);// Using to prevent printing misleading info when this Execption arises. 
        }
    }

    // Fetch accounts from DB
    public List<Account> getAllAccounts(){
        List <Account> accountslist=new ArrayList<Account>();
        String query="Select * from account";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(query);
        ResultSet rs=ps.executeQuery()) 
        {
            while (rs.next()) {
                Account account= new Account(
                    rs.getString("holdername"),
                    rs.getInt("holderage"),
                    rs.getString("accountnum"),
                    rs.getDouble("balance")
                );
            accountslist.add(account);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve all accounts", e);// Using to prevent printing misleading info when this Execption arises. 
        }    
        return accountslist;
    }

    // Search accounts by accountnumber over DB.
    // Eventhough we could do it over accountslist but it fetches all accounts first from start which isn't efficient and doing it over DB lets us to fetch only the required/ account.
    public Account getAccountByNumber(String accountnumber){
        String query="Select * from account where accountnum=?";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(query)) 
        {
            ps.setString(1, accountnumber);// if inner try block invokes problem then it could be handles by outer try's catch block
            // Used if not while because rs is intended to retrieve/fetch only 1 specific account from DB
            try (ResultSet rs=ps.executeQuery()) {
                 if(rs.next()){
                return new Account(
                    rs.getString("holdername"),
                    rs.getInt("holderage"),
                    rs.getString("accountnum"),
                    rs.getDouble("balance")
                );
            }
            }   
        }
        catch(SQLException e){
            throw new IllegalStateException("Could not retrieve account " + accountnumber,e);
        }
        return null;
    }

    // Deposit Money over DB (This updates balance directly in MySQL without loading every account into an ArrayList)
    public boolean depositMoney(String accountnumber, double amount){
        String query="update account set balance=balance + ? where accountnum=?";
         try (Connection con=DBConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(query)) 
        {
            ps.setDouble(1, amount);
            ps.setString(2, accountnumber);
            return ps.executeUpdate()==1;
        }
        catch(SQLException e){
             throw new IllegalStateException("Could not deposit money into account " + accountnumber,e);
        }
    }

    // Withdraw Money over DB
    public boolean withdrawMoney(String accountnumber, double amount){
        String query="update account set balance=balance - ? where accountnum=? AND balance>=?";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(query)) 
        {
            ps.setDouble(1, amount);
            ps.setString(2, accountnumber);
            ps.setDouble(3, amount);
            return ps.executeUpdate()==1;
        }
        catch(SQLException e){
            throw new IllegalStateException("Could not withdraw money from account " + accountnumber,e); 
        }
    }

    // Transfer Money over DB
    public boolean transferMoney(String sender, String receiver, double amount) {
    String withdrawSql =
            "UPDATE account SET balance = balance - ? " +
            "WHERE accountnum = ? AND balance >= ?";

    String depositSql =
            "UPDATE account SET balance = balance + ? " +
            "WHERE accountnum = ?";

    try (Connection con = DBConnection.getConnection()) 
    {
        con.setAutoCommit(false);

            try 
            {
                int withdrawn;
                int deposit;
    
                try (PreparedStatement ps = con.prepareStatement(withdrawSql)) {
                    ps.setDouble(1, amount);
                    ps.setString(2, sender);
                    ps.setDouble(3, amount);
                    withdrawn = ps.executeUpdate();
                }

                if (withdrawn != 1) {
                    con.rollback(); // Will Undo changes back to that last last Commit or last autocommit disabled
                    return false;
                }

                try (PreparedStatement ps = con.prepareStatement(depositSql)) {
                    ps.setDouble(1, amount);
                    ps.setString(2, receiver);
                    deposit= ps.executeUpdate();
                }
                if (deposit != 1) {
                        con.rollback();
                        return false;
                    }
                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e; // Rethrow SQLException to the outermost catch block(Note: it will run finally first before doing this)
                } 
                finally {
                    con.setAutoCommit(true); 
                    }

    } catch (SQLException e) {
        throw new IllegalStateException("Could not transfer money from account " + sender +
        " to account " + receiver,
        e);  
    }
}
    public boolean deleteAccount(String accountnumber){
        String query="delete from account where accountnum=? AND balance=0";
        try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) 
        {
            ps.setString(1, accountnumber);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        throw new IllegalStateException("Could not delete account " + accountnumber,
        e); 
    }
    }

// Creating this so that we don't need to load all accounts by calling getAllAccounts() each time for just checking accountlist empty oi not.
    public boolean hasAnyAccounts(){
        String query="Select 1 from account Limit 1";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(query);
        ResultSet rs=ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not check whether any accounts exist",
            e);
        }
        
    }   

    
    // using getAccountCount() to handle zero or one account to avoid using getAllAccounts()
    //DB creates a temporary resultset with just 1 column for our query. This virtual table doesn't retain columns of original table, which is why we access the value using column index 1.
    public int getAccountCount() {
        String query = "SELECT COUNT(*) FROM account";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve account count", e);
        }
    }

}


