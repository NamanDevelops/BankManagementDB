import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private  static final String Url=System.getenv("DB_URL");
    private static final String User=System.getenv("DB_USER");
    private static final String Password=System.getenv("DB_PASSWORD");
    public static Connection getConnection() throws SQLException{
        if (Url == null || User == null || Password == null) {
            throw new IllegalStateException(
                    "Database environment variables are not configured."); // This will propagate like DBConnection>AccountDAO>Bank>Main
        }
        return DriverManager.getConnection(Url,User,Password);
    }
}
