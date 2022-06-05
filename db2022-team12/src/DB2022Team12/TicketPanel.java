package DB2022Team12;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class TicketPanel extends JPanel {

	private JDialog ticketDialog;
	private JButton ticketDialogBtn, bookBtn;
	private JLabel noticeLabel, musicalLabel1, musicalLabel2, dateTimeLabel, theaterLabel1, theaterLabel2, remainLabel1,
			remainLabel2, priceLabel1, priceLabel2;
	private JComboBox<String> dateSelector, timeSelector;

	// 다이얼로그 제목
	private String dialogTitle;

	// 예매하려는 공연의 정보를 담은 Musical 객체
	private Musical musical;

	// JComboBox의 default 값으로 사용할 빈 선택지
	private final static String EMPTY_ITEM = "======";

	// 선택한 날짜와 시간에 대한 뮤지컬 회차 정보를 가져오는 쿼리
	private final static String GET_SCHEDULE_QUERY = "SELECT id, remain_seat FROM db2022_musical_schedule WHERE title = ? AND date = ? AND time = ?";

	// 예매한 티켓 정보를 DB에 삽입하는 쿼리
	private final static String INSERT_TICKET_QUERY = "INSERT INTO db2022_ticket(musical_title, musical_schedule, member_id, order_date) VALUES (?, ?, ?, ?)";

	// 티켓 예매 성공 시 해당 공연의 남은 좌석 수를 갱신하는 쿼리
	private final static String UPDATE_REMAIN_SEAT_QUERY = "UPDATE db2022_musical_schedule SET remain_seat = remain_seat - 1 WHERE id = ?";

	// 티켓 구매 패널 레이아웃 설정
	// 예매하려는 공연의 정보를 담은 Musical 객체를 전달 받음
	public TicketPanel(Musical musical) {
		this.musical = musical;
		ticketDialogBtn = new JButton("티켓 구매");
		ticketDialogBtn.addActionListener(new TicketDialogCreator());
		this.add(ticketDialogBtn);
	}

	// <티켓 구매> 버튼에 대한 리스너
	// 티켓 예매 다이얼로그를 생성
	private class TicketDialogCreator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dialogTitle = "티켓 예매 | " + musical.getTitle();

			// 미로그인 유저인 경우, 예매 불가 알림
			if (User.getId() == null) {
				NotificationClass.createNotifDialog(dialogTitle, "로그인이 필요합니다");
				return;
			}

			// 티켓 예매 다이얼로그 생성
			ticketDialog = new JDialog();
			ticketDialog.setTitle(dialogTitle);
			ticketDialog.setSize(500, 500);
			ticketDialog.setLayout(new GridLayout(3, 1));

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(5, 2));
			JPanel btnPanel = new JPanel();
			JPanel dateTimePanel = new JPanel();

			noticeLabel = new JLabel();
			noticePanel.add(noticeLabel);

			musicalLabel1 = new JLabel("뮤지컬");
			musicalLabel2 = new JLabel(musical.getTitle());

			dateTimeLabel = new JLabel("날짜/시간 선택");

			// 날짜 정보 목록 불러오기
			Vector<String> dateList = musical.getDateVector();
			// dateSelector를 빈 선택지로 초기화하기 위해 목록 맨 앞에 삽입
			dateList.insertElementAt(EMPTY_ITEM, 0);
			dateSelector = new JComboBox<String>(dateList);
			dateSelector.addActionListener(new dateSelectorListener());

			// timeSelector는 빈 선택지로 초기화
			String[] emptyList = { EMPTY_ITEM };
			timeSelector = new JComboBox<String>(emptyList);
			timeSelector.addActionListener(new timeSelectorListener());

			theaterLabel1 = new JLabel("극장");
			theaterLabel2 = new JLabel(musical.getTheaterName());
			remainLabel1 = new JLabel("잔여 좌석");
			remainLabel2 = new JLabel();
			priceLabel1 = new JLabel("예매가");
			priceLabel2 = new JLabel(musical.getPrice());

			inputPanel.add(musicalLabel1);
			inputPanel.add(musicalLabel2);

			inputPanel.add(dateTimeLabel);
			dateTimePanel.add(dateSelector);
			dateTimePanel.add(timeSelector);
			inputPanel.add(dateTimePanel);

			inputPanel.add(theaterLabel1);
			inputPanel.add(theaterLabel2);
			inputPanel.add(remainLabel1);
			inputPanel.add(remainLabel2);
			inputPanel.add(priceLabel1);
			inputPanel.add(priceLabel2);

			bookBtn = new JButton("예매하기");
			bookBtn.addActionListener(new bookBtnListener());
			btnPanel.add(bookBtn);

			ticketDialog.add(noticePanel);
			ticketDialog.add(inputPanel);
			ticketDialog.add(btnPanel);
			ticketDialog.setVisible(true);
		}

	}

	// dateSelector에 대한 리스너
	// 날짜를 선택할 때마다 timeSelector의 리스트를 변경
	private class dateSelectorListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// timeSelector의 리스트 초기화
			timeSelector.removeAllItems();
			// 사용자가 선택한 날짜
			String selectedDate = (String) dateSelector.getSelectedItem();

			// 사용자가 빈 선택지를 선택했다면, timeSelector의 리스트에 빈 선택지 추가
			if (selectedDate.equals(EMPTY_ITEM)) {
				timeSelector.addItem(EMPTY_ITEM);
				return;
			}

			// 선택한 날짜에 대한 모든 공연 시각을 timeSelector의 리스트에 추가
			Vector<String> timeList = musical.getTimeVector(selectedDate);
			for (String time : timeList) {
				timeSelector.addItem(time);
			}
		}

	}

	// timeSelector에 대한 리스너
	// 날짜와 시각을 선택할 때마다 remainLabel2의 내용 변경
	private class timeSelectorListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 사용자가 선택한 날짜와 시각
			String selectedDate = (String) dateSelector.getSelectedItem();
			String selectedTime = (String) timeSelector.getSelectedItem();

			// 해당 회차에 대한 남은 좌석 수 표시
			if (selectedDate == null || selectedTime == null || selectedDate.equals(EMPTY_ITEM)
					|| selectedTime.equals(EMPTY_ITEM)) {
				// 사용자가 빈 선택지를 선택했다면, 텍스트 비우기
				remainLabel2.setText("");
			} else {
				int remainSeat = musical.getRemainSeat(selectedDate, selectedTime);
				if (remainSeat == 0)
					remainLabel2.setText("매진");
				else
					remainLabel2.setText(Integer.toString(remainSeat));
			}
		}

	}

	// <예매하기> 버튼에 대한 리스너
	// DB에 예매한 티켓 정보를 삽입
	private class bookBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 사용자가 선택한 날짜 및 시각
			String selectedDate = (String) dateSelector.getSelectedItem();
			String selectedTime = (String) timeSelector.getSelectedItem();

			// 사용자가 회차 정보를 선택하지 않은 경우, 버튼 동작 X
			if (selectedDate == null || selectedTime == null || selectedDate.equals(EMPTY_ITEM)
					|| selectedTime.equals(EMPTY_ITEM)) {
				noticeLabel.setText("날짜와 시간을 선택해주세요");
				return;
			}

			// 남은 좌석 수가 0이면 예매 불가
			if (musical.getRemainSeat(selectedDate, selectedTime) == 0) {
				NotificationClass.createNotifDialog(dialogTitle, "해당 공연은 매진되었습니다");
				return;
			}

			Connection conn = null;
			PreparedStatement getStmt = null, insertStmt = null, updateStmt = null;

			try {
				// DB 연결 및 Statement 객체 생성
				conn = new ConnectionClass().getConnection();
				getStmt = conn.prepareStatement(GET_SCHEDULE_QUERY);
				insertStmt = conn.prepareStatement(INSERT_TICKET_QUERY);
				updateStmt = conn.prepareStatement(UPDATE_REMAIN_SEAT_QUERY);

				// 트랜잭션 단위 : 회차 정보 가져오기 + 예매 티켓 정보 삽입 + 남은 좌석 수 차감
				conn.setAutoCommit(false);

				// 회차 정보 가져오기
				getStmt.setString(1, musical.getTitle());
				getStmt.setString(2, selectedDate);
				getStmt.setString(3, selectedTime);
				ResultSet rs = getStmt.executeQuery();
				rs.next();
				int musicalSchedule = rs.getInt("id");
				int remainSeat = rs.getInt("remain_seat");

				// 남은 좌석 수가 0이면 예매 불가
				if (remainSeat == 0)
					throw new SQLException();

				// 예매 티켓 정보 삽입하기
				insertStmt.setString(1, musical.getTitle());
				insertStmt.setInt(2, musicalSchedule);
				insertStmt.setString(3, User.getId());
				insertStmt.setString(4, DateClass.getCurrentDate());
				insertStmt.executeUpdate();

				// 해당 회차의 남은 좌석 수 차감하기
				updateStmt.setInt(1, musicalSchedule);
				updateStmt.executeUpdate();
				musical.reduceRemainSeat(selectedDate, selectedTime);

				// 예매 성공 시 트랜잭션 커밋
				conn.commit();
				conn.setAutoCommit(true);

				// 예매 성공 알림창 생성 및 티켓 예매 다이얼로그 닫기
				NotificationClass.createNotifDialog(dialogTitle, "예매가 완료되었습니다");
				ticketDialog.dispose();

			} catch (SQLException sqle) {
				// 예매 실패 시 트랜잭션 롤백
				if (conn != null) {
					try {
						conn.rollback();
						System.out.println("Rollback : 티켓 예매");
					} catch (SQLException rbError) {
						System.out.println(rbError);
					}
				}
				System.out.println(sqle);
				NotificationClass.createNotifDialog(dialogTitle, "예매에 실패했습니다");
			} finally {
				// DB 연결 닫기
				try {
					if (conn != null)
						conn.close();
					if (getStmt != null)
						getStmt.close();
					if (insertStmt != null)
						insertStmt.close();
					if (updateStmt != null)
						updateStmt.close();
				} catch (SQLException closeError) {
					System.out.println(closeError);
				}
			}
		}

	}
}
