package DB2022Team12;

class User {
	// 로그인한 유저 정보 저장
	private static String id = null;
	private static String name = null;

	// getter & setter

	static String getId() {
		return id;
	}

	static String getName() {
		return name;
	}

	static void setId(String id) {
		User.id = id;
	}

	static void setName(String name) {
		User.name = name;
	}
}
