package DB2022Team12;

class User {
	private static String id = null;
	private static String name = null;
	
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
