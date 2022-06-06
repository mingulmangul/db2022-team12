package DB2022Team12;

/**
 * 로그인한 유저 정보를 저장하는 클래스
 * 
 * @author mingulmangul
 */
class User {
	// 로그인한 유저 정보 저장
	private static String id = null;
	private static String name = null;

	/**
	 * 로그인 유저의 아이디를 리턴하는 메소드
	 * 
	 * @return 로그인 유저의 아이디
	 */
	static String getId() {
		return id;
	}

	/**
	 * 로그인 유저의 이름을 리턴하는 메소드
	 * 
	 * @return 로그인 유저의 이름
	 */
	static String getName() {
		return name;
	}

	/**
	 * 로그인 시 유저의 아이디를 저장하는 메소드
	 * 
	 * @param id 유저의 아이디
	 */
	static void setId(String id) {
		User.id = id;
	}

	/**
	 * 로그인 시 유저의 이름을 저장하는 메소드
	 * 
	 * @param name 유저의 이름
	 */
	static void setName(String name) {
		User.name = name;
	}
}
