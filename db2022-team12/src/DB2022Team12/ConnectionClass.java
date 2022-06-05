package DB2022Team12;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionClass {

	// Database 연결 정보
	private String baseUrl = "jdbc:mysql://localhost:3306/";
	private String dbName = "DB2022Team12";
	private String username = "root";
	private String password = "soominkim4250";

	// SQLException error code
	public static int ER_DUP_KEY = 1022;
	public static int ER_DUP_ENTRY = 1062;

	// Database와 연결을 생성하는 메소드
	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(baseUrl + dbName, username, password);
		System.out.println("Database is connected!!");
		return conn;
	}

}
