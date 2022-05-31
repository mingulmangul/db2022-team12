import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
	
	private String baseUrl = "jdbc:mysql://localhost:3306/";
	private String dbName = "DB2022Team12";
	private String username = "root";
	private String password = "1234";
	
	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(baseUrl + dbName, username, password);
		System.out.println("Database is connected!!");
		return conn;
	}
	
}
