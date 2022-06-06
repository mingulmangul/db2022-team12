package DB2022Team12;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * 뮤지컬 관련 정보를 저장하는 클래스
 * 
 * @author mingulmangul
 */
class Musical {

	/**
	 * 뮤지컬 회차 정보를 저장하는 클래스<br>
	 * 공연 시각과 잔여 좌석 수를 저장합니다.
	 * 
	 * @author mingulmangul
	 */
	private class TimeInfo {
		String time;
		int remainSeat;

		public TimeInfo(String time, int remainSeat) {
			this.time = time;
			this.remainSeat = remainSeat;
		}
	}

	/**
	 * 뮤지컬 회차 정보를 저장하는 맵<br>
	 * key: 날짜, value: [시각, 잔여 좌석 수]
	 */
	private HashMap<String, Vector<TimeInfo>> schedules;

	private String title, summary, price, score;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;
	private int userReview = 0;

	/**
	 * 뮤지컬 관련 정보를 모두 가져오는 쿼리<br>
	 * 뮤지컬 정보, 평균 별점 정보, 상영 극장 정보를 가져옵니다.
	 */
	private final static String GET_MUSICAL_QUERY = "SELECT * FROM (db2022_musical NATURAL LEFT JOIN db2022_avg_rate) JOIN db2022_theater ON db2022_musical.theater_name = db2022_theater.name WHERE db2022_musical.title = ?";

	/** 뮤지컬 회차 정보를 가져오는 쿼리 */
	private final static String GET_MUSICAL_SCHEDULE_QUERY = "SELECT * FROM db2022_musical NATURAL JOIN db2022_musical_schedule WHERE title = ? ORDER BY date, time";

	/** 로그인한 유저의 리뷰를 가져오는 쿼리 */
	private final static String GET_REVIEW_QUERY = "SELECT rate FROM db2022_review WHERE musical_title = ? AND member_id = ?";

	/**
	 * 뮤지컬 클래스의 생성자<br>
	 * 뮤지컬 제목을 받아 데이터베이스로부터 해당 뮤지컬에 대한 정보를 가져와서 저장합니다.
	 * 
	 * @param musical 뮤지컬 제목
	 */
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

	/**
	 * 뮤지컬의 공연 날짜 정보 벡터를 리턴하는 메소드<br>
	 * {@link #schedules}에 key로 저장된 date(공연 날짜)들을 Vector 형식으로 리턴합니다.
	 * 
	 * @return 공연 날짜 정보를 담은 벡터
	 */
	Vector<String> getDateVector() {
		Vector<String> dateVector = new Vector<>();
		for (String date : schedules.keySet())
			dateVector.add(date);
		return dateVector;
	}

	/**
	 * 뮤지컬의 공연 시각 정보 벡터를 리턴하는 메소드<br>
	 * {@link #schedules}에서 date를 key로 갖는 value({@link TimeInfo} vector)를 가져와
	 * time(공연 시각)만 추출해 리턴합니다.
	 * 
	 * @param date 공연 날짜
	 * @return 공연 시각 정보를 담은 벡터
	 */
	Vector<String> getTimeVector(String date) {
		Vector<String> timeVector = new Vector<>();
		for (TimeInfo info : schedules.get(date))
			timeVector.add(info.time);
		return timeVector;
	}

	/**
	 * 뮤지컬의 잔여 좌석 수를 리턴하는 메소드<br>
	 * 공연 날짜와 시각을 받아 해당 회차의 잔여 좌석 수를 리턴합니다.
	 * 
	 * @param date 공연 날짜
	 * @param time 공연 시각
	 * @return 잔여 좌석 수
	 */
	int getRemainSeat(String date, String time) {
		for (TimeInfo info : schedules.get(date))
			if (info.time.equals(time))
				return info.remainSeat;
		return 0;
	}

	/**
	 * 뮤지컬의 잔여 좌석 수를 차감하는 메소드<br>
	 * 공연 날짜와 시각을 받아 해당 회차의 잔여 좌석 수를 1 감소시킵니다.
	 * 
	 * @param date 공연 날짜
	 * @param time 공연 시각
	 */
	void reduceRemainSeat(String date, String time) {
		for (TimeInfo info : schedules.get(date))
			if (info.time.equals(time))
				info.remainSeat--;
	}

	/**
	 * 유저의 리뷰 점수를 리턴하는 메소드<br>
	 * 유저의 리뷰가 존재하지 않으면 0을 리턴합니다.
	 * 
	 * @return 유저의 리뷰 점수
	 */
	int getUserReview() {
		return userReview;
	}

	/**
	 * 유저의 리뷰 점수를 설정하는 메소드<br>
	 * 
	 * @param rate 유저의 평점
	 */
	void setUserReview(int rate) {
		this.userReview = rate;
	}

	/**
	 * 뮤지컬 제목을 리턴하는 메소드
	 * 
	 * @return 뮤지컬 제목
	 */
	String getTitle() {
		return title;
	}

	/**
	 * 뮤지컬 줄거리를 리턴하는 메소드
	 * 
	 * @return 뮤지컬 줄거리
	 */
	String getSummary() {
		return summary;
	}

	/**
	 * 뮤지컬 예매가를 리턴하는 메소드
	 * 
	 * @return 뮤지컬 예매가
	 */
	String getPrice() {
		return price;
	}

	/**
	 * 뮤지컬 평균 별점을 리턴하는 메소드
	 * 
	 * @return 뮤지컬 평균 별점
	 */
	String getScore() {
		return score;
	}

	/**
	 * 뮤지컬 공연장 이름을 리턴하는 메소드
	 * 
	 * @return 뮤지컬 공연장 이름
	 */
	String getTheaterName() {
		return theaterName;
	}

	/**
	 * 뮤지컬 공연장 주소를 리턴하는 메소드
	 * 
	 * @return 뮤지컬 공연장 주소
	 */
	String getTheaterAddress() {
		return theaterAddress;
	}

	/**
	 * 뮤지컬 공연장 전화번호를 리턴하는 메소드
	 * 
	 * @return 뮤지컬 공연장 전화번호
	 */
	String getTheaterPhone() {
		return theaterPhone;
	}

	/**
	 * 뮤지컬 공연장 규모를 리턴하는 메소드
	 * 
	 * @return 뮤지컬 공연장 규모
	 */
	String getTheaterSize() {
		return theaterSize;
	}
}