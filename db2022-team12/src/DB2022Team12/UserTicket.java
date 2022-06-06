package DB2022Team12;

class UserTicket {
	// 사용자 티켓 정보 저장
	int ID;
	String title;
	String orderDate;
	
	// getter & setter
	
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
	String getOrderDate() {
		return orderDate;
	}
	void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
}