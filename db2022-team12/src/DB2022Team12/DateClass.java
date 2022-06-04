package DB2022Team12;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

class DateClass {
	// 현재 날짜를 리턴하는 메소드
	public static String getCurrentDate() {
		// Calendar 객체 생성 및 타임존 설정
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		// DB에 저장할 수 있도록 날짜 형식 변경
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		return formatter.format(cal.getTime());
	}
}
