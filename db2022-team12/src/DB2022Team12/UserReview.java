package DB2022Team12;

/**
 * 사용자 리뷰 정보 저장
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
	 * getter & setter
	 */
	int getID() {
		return ID;
	}

	/**
	 * getter & setter
	 */
	void setID(int iD) {
		ID = iD;
	}

	/**
	 * getter & setter
	 */
	String getTitle() {
		return title;
	}

	/**
	 * getter & setter
	 */
	void setTitle(String title) {
		this.title = title;
	}

	/**
	 * getter & setter
	 */
	int getRate() {
		return rate;
	}

	/**
	 * getter & setter
	 */
	void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * getter & setter
	 */
	String getTime() {
		return time;
	}

	/**
	 * getter & setter
	 */
	void setTime(String time) {
		this.time = time;
	}
}