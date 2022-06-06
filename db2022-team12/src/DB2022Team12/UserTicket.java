package DB2022Team12;

/**
 * 사용자 티켓 정보 저장 클래스
 * 
 * @author sonab
 *
 */
class UserTicket {

	int ID;
	String title;
	String orderDate;

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
	String getOrderDate() {
		return orderDate;
	}

	/**
	 * getter & setter
	 */
	void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
}