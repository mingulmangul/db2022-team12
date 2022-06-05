package DB2022Team12;

class UserReview {
	int ID;
	String title;
	int rate;
	
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