package DB2022Team12;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 데이터베이스와의 연결을 생성하는 클래스
 * 
 * @author mingulmangul
 */
class ConnectionClass {

	// 데이터베이스 연결 정보
	private String baseUrl = "jdbc:mysql://localhost:3306/";
	private String dbName = "DB2022Team12";
	private String username = "root";
	private String password = "1234";

	// SQLException error code
	public static int ER_DUP_KEY = 1022;
	public static int ER_DUP_ENTRY = 1062;

	/**
	 * 데이터베이스와 연결을 생성하는 메소드
	 * 
	 * @return 데이터베이스와 연결된 Connection 객체
	 * @throws SQLException 데이터베이스 연결 실패 시 예외를 발생시킵니다.
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(baseUrl + dbName, username, password);
		System.out.println("Database is connected!!");
		return conn;
	}

}
