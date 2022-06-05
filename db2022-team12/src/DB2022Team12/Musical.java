package DB2022Team12;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Musical {
	
	private class DateInfo {
		String id, date, time;
	}
	
	private ArrayList<DateInfo> dateInfoList;
	private String[] id, date, time;
	private String title, summary, price, avgRate;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;

	// 뮤지컬 관련 정보를 모두 가져오는 쿼리 (뮤지컬 정보 + 평균 별점 정보 + 상영 극장 정보)
	// 뮤지컬 테이블과 평균 별점 뷰를 NATURAL LEFT JOIN 한 후, 이를 극장 테이블과 JOIN 
	private final String GET_MUSICAL_QUERY = "SELECT * "
			+ "FROM (musical NATURAL LEFT JOIN avg_rate) "
			+ "JOIN theater ON musical.theater_name = theater.name "
			+ "WHERE musical.title = ?";
	
	public Musical(String musical) {
		try (
			Connection conn = new ConnectionClass().getConnection();
			PreparedStatement pStmt = conn.prepareStatement(GET_MUSICAL_QUERY);
		) {
			pStmt.setString(1, musical);
			ResultSet rs = pStmt.executeQuery();
			
			// 첫 번째 튜플에서 공통 정보(제목, 공연장, 가격, 줄거리) 저장
			rs.next();
			this.title = rs.getString("title");
			this.theaterName = rs.getString("theater_name");
			this.price = rs.getString("price");
			this.summary = rs.getString("summary");
			
			System.out.println(price);
			System.out.println(rs.getString("date"));
			
			while (rs.next()) {
				System.out.println("begin");
				
			}
			System.out.println("end");
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
	}
}