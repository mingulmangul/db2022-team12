package DB2022Team12;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 현재 날짜 정보를 생성하는 클래스
 * 
 * @author mingulmangul
 */
class DateClass {

	/**
	 * 현재 날짜를 리턴하는 메소드<br>
	 * Asia/Seoul 시간대를 사용합니다.
	 * 
	 * @return 현재 날짜 정보 문자열<br>
	 *         yyyy-MM-dd 형식으로 리턴합니다.
	 */
	public static String getCurrentDate() {
		// Calendar 객체 생성 및 타임존 설정
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		// DB에 저장할 수 있도록 날짜 형식 변경
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		return formatter.format(cal.getTime());
	}
}