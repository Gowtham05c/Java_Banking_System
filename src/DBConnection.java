import java.sql.*;

public class DBConnection {
    static Connection con;

    public static Connection getConnection() {
        try {
            if(con == null){
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bankdb",
                        "root",
                        "1234"
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
