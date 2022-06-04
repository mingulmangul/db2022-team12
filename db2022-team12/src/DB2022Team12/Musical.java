package DB2022Team12;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

class Musical {
	
	// 뮤지컬 날짜 정보를 저장하는 맵: <공연 날짜, 같은 날짜에 대한 공연 시각들을 저장하는 리스트>
	private HashMap<String, Vector<String>> dateInfo;
	
	private String title, summary, price, score, remainSeat;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;

	// 뮤지컬 관련 정보를 모두 가져오는 쿼리 (뮤지컬 정보 + 평균 별점 정보 + 상영 극장 정보)
	// 뮤지컬 테이블과 평균 별점 뷰를 NATURAL LEFT JOIN 한 후, 이를 극장 테이블과 JOIN 
	private final static String GET_MUSICAL_QUERY = "SELECT * "
			+ "FROM (musical NATURAL LEFT JOIN avg_rate) "
			+ "JOIN theater ON musical.theater_name = theater.name "
			+ "WHERE musical.title = ?";
	
	// 뮤지컬 날짜 정보를 가져오는 쿼리
	private final static String GET_MUSICAL_DATE_QUERY = "SELECT * "
			+ "FROM musical NATURAL JOIN musical_date "
			+ "WHERE title = ?";
	
	public Musical(String musical) {
		try (
			Connection conn = new ConnectionClass().getConnection();
			PreparedStatement musicalStmt = conn.prepareStatement(GET_MUSICAL_QUERY);
			PreparedStatement dateStmt = conn.prepareStatement(GET_MUSICAL_DATE_QUERY);
		) {
			// 뮤지컬 정보 가져오기
			musicalStmt.setString(1, musical);
			ResultSet rs = musicalStmt.executeQuery();
			
			// 뮤지컬 정보 저장
			rs.next();
			this.title = rs.getString("title");
			this.price = rs.getString("price");
			this.remainSeat = rs.getString("remain_seat");
			this.summary = rs.getString("summary");
			this.score = rs.getString("score");
			this.theaterName = rs.getString("theater_name");
			this.theaterAddress = rs.getString("address");
			this.theaterPhone = rs.getString("phone");
			this.theaterSize = rs.getString("size");

			// 뮤지컬 날짜 정보 가져오기
			dateStmt.setString(1, musical);
			rs = dateStmt.executeQuery();
			
			// 뮤지컬 날짜 정보 저장
			dateInfo = new HashMap<>();
			String date, time;
			while (rs.next()) {
				date = rs.getString("date");
				time = rs.getString("time");
				
				// <key: date, value: time을 저장하는 Vector> 형식으로 저장
				Vector<String> v = dateInfo.getOrDefault(date, new Vector<String>());
				v.add(time);
				dateInfo.put(date, v);
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
	}
	
	Vector<String> getDateVector() {
		Vector<String> dateVector = new Vector<>();
		for (String date: dateInfo.keySet())
			dateVector.add(date);
		return dateVector;
	}
	
	Vector<String> getTimeVector(String date) {
		return dateInfo.get(date);
	}

	String getTitle() {
		return title;
	}

	String getSummary() {
		return summary;
	}

	String getPrice() {
		return price;
	}

	String getScore() {
		return score;
	}

	String getRemainSeat() {
		return remainSeat;
	}

	String getTheaterName() {
		return theaterName;
	}

	String getTheaterAddress() {
		return theaterAddress;
	}

	String getTheaterPhone() {
		return theaterPhone;
	}

	String getTheaterSize() {
		return theaterSize;
	}
}
