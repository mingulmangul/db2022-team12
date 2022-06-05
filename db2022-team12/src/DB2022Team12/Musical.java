package DB2022Team12;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

class Musical {

	// 뮤지컬 회차 정보 중 시각, 남은 좌석 수를 저장
	private class TimeInfo {
		String time;
		int remainSeat;

		public TimeInfo(String time, int remainSeat) {
			this.time = time;
			this.remainSeat = remainSeat;
		}
	}

	// 뮤지컬 회차 정보를 저장하는 맵
	// key: 날짜, value: <시각, 남은 좌석 수>
	private HashMap<String, Vector<TimeInfo>> schedules;

	private String title, summary, price, score;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;
	private int userReview = 0;

	// 뮤지컬 관련 정보를 모두 가져오는 쿼리 (뮤지컬 정보 + 평균 별점 정보 + 상영 극장 정보)
	// 뮤지컬 테이블과 평균 별점 뷰를 NATURAL LEFT JOIN 한 후, 이를 극장 테이블과 JOIN
	private final static String GET_MUSICAL_QUERY = "SELECT * FROM (db2022_musical NATURAL LEFT JOIN db2022_avg_rate) JOIN db2022_theater ON db2022_musical.theater_name = db2022_theater.name WHERE db2022_musical.title = ?";

	// 뮤지컬 회차 정보를 가져오는 쿼리
	private final static String GET_MUSICAL_SCHEDULE_QUERY = "SELECT * FROM db2022_musical NATURAL JOIN db2022_musical_schedule WHERE title = ? ORDER BY date, time";

	// 로그인한 유저의 리뷰를 가져오는 쿼리
	private final static String GET_REVIEW_QUERY = "SELECT rate FROM db2022_review WHERE musical_title = ? AND member_id = ?";

	public Musical(String musical) {
		try (Connection conn = new ConnectionClass().getConnection();
				PreparedStatement musicalStmt = conn.prepareStatement(GET_MUSICAL_QUERY);
				PreparedStatement dateStmt = conn.prepareStatement(GET_MUSICAL_SCHEDULE_QUERY);
				PreparedStatement reviewStmt = conn.prepareStatement(GET_REVIEW_QUERY);) {
			// 뮤지컬 정보 가져오기
			musicalStmt.setString(1, musical);
			ResultSet rs = musicalStmt.executeQuery();

			// 뮤지컬 정보 저장
			rs.next();
			this.title = rs.getString("title");
			this.price = rs.getString("price");
			this.summary = rs.getString("summary");
			this.score = rs.getString("score");
			this.theaterName = rs.getString("theater_name");
			this.theaterAddress = rs.getString("address");
			this.theaterPhone = rs.getString("phone");
			this.theaterSize = rs.getString("size");

			// 뮤지컬 회차 정보 가져오기
			dateStmt.setString(1, musical);
			rs = dateStmt.executeQuery();

			// 뮤지컬 회차 정보 저장
			schedules = new HashMap<>();
			String date, time;
			int remainSeat;
			while (rs.next()) {
				date = rs.getString("date");
				time = rs.getString("time");
				remainSeat = rs.getInt("remain_seat");

				Vector<TimeInfo> v = schedules.getOrDefault(date, new Vector<TimeInfo>());
				v.add(new TimeInfo(time, remainSeat));
				schedules.put(date, v);
			}

			// 로그인 유저라면, 해당 유저의 리뷰 정보 가져오기
			if (User.getId() != null) {
				reviewStmt.setString(1, musical);
				reviewStmt.setString(2, User.getId());
				rs = reviewStmt.executeQuery();

				// 유저의 리뷰가 존재한다면, 저장
				if (rs.next()) {
					this.userReview = rs.getInt("rate");
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
	}

	// HashMap의 key로 저장된 date들을 Vector로 변환해서 리턴
	Vector<String> getDateVector() {
		Vector<String> dateVector = new Vector<>();
		for (String date : schedules.keySet())
			dateVector.add(date);
		return dateVector;
	}

	// date를 key로 갖는 time vector를 리턴
	Vector<String> getTimeVector(String date) {
		Vector<String> timeVector = new Vector<>();
		for (TimeInfo info : schedules.get(date))
			timeVector.add(info.time);
		return timeVector;
	}

	// 특정 date와 time에 대한 remainSeat를 리턴
	int getRemainSeat(String date, String time) {
		for (TimeInfo info : schedules.get(date))
			if (info.time.equals(time))
				return info.remainSeat;
		return 0;
	}

	// 특정 date와 time에 대한 remainSeat를 1 감소
	void reduceRemainSeat(String date, String time) {
		for (TimeInfo info : schedules.get(date))
			if (info.time.equals(time))
				info.remainSeat--;
	}

	// getter & setter

	int getUserReview() {
		return userReview;
	}

	void setUserReview(int rate) {
		this.userReview = rate;
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