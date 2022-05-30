import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainClass {

	public static void main(String[] args) {
		
		String baseUrl = "jdbc:mysql://localhost:3306/";
		String dbName = "test";
		String username = "root";
		String password = "1234";
		
		try (
			Connection conn = DriverManager.getConnection(baseUrl + dbName, username, password);
			Statement stmt = conn.createStatement();
		){
			System.out.println("Database is connected!!");
			stmt.executeUpdate("INSERT sample VALUES (3);");
			System.out.println("success");
		} catch (SQLException sqle) {
			System.out.println("Error >> " + sqle);
		}
		
	}

}
