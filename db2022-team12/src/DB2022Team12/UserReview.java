package DB2022Team12;

class UserReview {
	// 사용자 리뷰 정보 저장
	int ID;
	String title;
	int rate;
	
	// getter & setter
	
	String time;
	int getID() {
		return ID;
	}
	void setID(int iD) {
		ID = iD;
	}
	String getTitle() {
		return title;
	}
	void setTitle(String title) {
		this.title = title;
	}
	int getRate() {
		return rate;
	}
	void setRate(int rate) {
		this.rate = rate;
	}
	String getTime() {
		return time;
	}
	void setTime(String time) {
		this.time = time;
	}
}