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
	 * 사용자 티켓의 예매 티켓 식별자를 리턴하는 메소드
	 * 
	 * @return 티켓의 예매 티켓 식별자
	 */
	int getID() {
		return ID;
	}

	/**
	 * 객체 생성 시 사용자 티켓의 예매 티켓 식별자를 저장하는 메소드
	 * 
	 * @param iD 예매 티켓 식별자
	 */
	void setID(int iD) {
		ID = iD;
	}

	/**
	 * 사용자 티켓의 공연 제목을 리턴하는 메소드
	 * 
	 * @return 티켓의 공연 제목
	 */
	String getTitle() {
		return title;
	}

	/**
	 * 객체 생성 시 사용자 티켓의 공연 제목을 저장하는 메소드
	 * 
	 * @param title 티켓 공연 제목
	 */
	void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 사용자 티켓의 티켓 예매 날짜를 리턴하는 메소드
	 * 
	 * @return 티켓 예매 날짜
	 */
	String getOrderDate() {
		return orderDate;
	}

	/**
	 * 객체 생성 시 사용자 티켓의 티켓 예매 날짜를 저장하는 메소드
	 * 
	 * @param orderDate 티켓 예매 날짜
	 */
	void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
}