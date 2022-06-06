package DB2022Team12;

class UserTicket {
	int ID;
	String title;
	String orderDate;
	
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