package DB2022Team12;

/**
 * 사용자 리뷰 정보 저장 클래스
 * 
 * @author sonab
 *
 */
class UserReview {

	int ID;
	String title;
	int rate;
	String time;

	/**
	 * 사용자 리뷰의 리뷰 식별자를 리턴하는 메소드
	 * 
	 * @return 리뷰 식별자
	 */
	int getID() {
		return ID;
	}

	/**
	 * 객체 생성 시 사용자 리뷰의 리뷰 식별자를 저장하는 메소드
	 * 
	 * @param iD 리뷰 식별자
	 */
	void setID(int iD) {
		ID = iD;
	}

	/**
	 * 사용자 리뷰의 공연 제목을 리턴하는 메소드
	 * 
	 * @return 리뷰 공연 제목
	 */
	String getTitle() {
		return title;
	}

	/**
	 * 객체 생성 시 사용자 리뷰의 공연 제목을 저장하는 메소드
	 * 
	 * @param title 공연 제목 
	 */
	void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 사용자 리뷰의 평점을 리턴하는 메소드
	 * 
	 * @return 리뷰의 평점
	 */
	int getRate() {
		return rate;
	}

	/**
	 * 객체 생성 시 사용자 리뷰의 평점을 저장하는 메소드
	 * 
	 * @param rate 평점
	 */
	void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * 사용자 리뷰의 리뷰 작성 날짜를 리턴하는 메소드
	 * 
	 * @return 리뷰 작성 날짜
	 */
	String getTime() {
		return time;
	}

	/**
	 * 객체 생성 시 사용자 리뷰의 리뷰 작성 날짜를 저장하는 메소드
	 * 
	 * @param time 리뷰 작성 날짜
	 */
	void setTime(String time) {
		this.time = time;
	}
}