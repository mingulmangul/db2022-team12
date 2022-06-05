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

	// 예매하려는 공연의 정보를 담은 Musical 객체
	private Musical musical;

	// JComboBox의 default 값으로 사용할 빈 선택지
	private final static String EMPTY_ITEM = "======";

	// 선택한 날짜와 시간에 대한 뮤지컬 날짜 정보의 식별자를 가져오는 쿼리
	private final static String GET_DATE_ID_QUERY = "SELECT id FROM db2022_musical_date WHERE title = ? AND date = ? AND time = ?";

	// 예매한 티켓 정보를 DB에 삽입하는 쿼리
	private final static String INSERT_TICKET_QUERY = "INSERT INTO db2022_ticket(musical_title, musical_date, member_id, order_date) "
			+ "VALUES (?, ?, ?, ?)";

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
			// 다이얼로그 제목
			String dialogTitle = "티켓 예매 | " + musical.getTitle();
			
			// 미로그인 유저이거나 남은 좌석이 없는 경우, 예매 불가 알림
			if (User.getId() == null) {
				NotificationClass.createNotifDialog(dialogTitle, "로그인이 필요합니다");
				return;
			} else if (musical.getRemainSeat() == 0) {
				NotificationClass.createNotifDialog(dialogTitle, "해당 공연은 매진되었습니다 😢");
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

			noticeLabel = new JLabel("예매 티켓의 정보를 입력해주세요");
			noticePanel.add(noticeLabel);

			musicalLabel1 = new JLabel("공연 제목");
			musicalLabel2 = new JLabel(musical.getTitle());

			dateTimeLabel = new JLabel("날짜 및 시간");

			// 날짜 정보 목록 불러오기
			Vector<String> dateList = musical.getDateVector();
			// dateSelector를 빈 선택지로 초기화하기 위해 목록 맨 앞에 삽입
			dateList.insertElementAt(EMPTY_ITEM, 0);
			dateSelector = new JComboBox<String>(dateList);
			dateSelector.addActionListener(new timeSelectorCreator());

			// timeSelector는 빈 선택지로 초기화
			String[] emptyList = { EMPTY_ITEM };
			timeSelector = new JComboBox<String>(emptyList);

			theaterLabel1 = new JLabel("극장");
			theaterLabel2 = new JLabel(musical.getTheaterName());
			remainLabel1 = new JLabel("남은 좌석 수");
			remainLabel2 = new JLabel(Integer.toString(musical.getRemainSeat()));
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
	private class timeSelectorCreator implements ActionListener {

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

	// <예매하기> 버튼에 대한 리스너
	// DB에 예매한 티켓 정보를 삽입
	private class bookBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 사용자가 선택한 날짜 및 시각
			String selectedDate = (String) dateSelector.getSelectedItem();
			String selectedTime = (String) timeSelector.getSelectedItem();

			// 날짜 정보 id
			int musicalDate;

			try (Connection conn = new ConnectionClass().getConnection();
					PreparedStatement getStmt = conn.prepareStatement(GET_DATE_ID_QUERY);
					PreparedStatement insertStmt = conn.prepareStatement(INSERT_TICKET_QUERY);) {
				
				// TODO: 남은 좌석 수 차감 쿼리 추가하기 + 트랜잭션으로 묶기
				
				// 날짜 정보 가져오기
				getStmt.setString(1, musical.getTitle());
				getStmt.setString(2, selectedDate);
				getStmt.setString(3, selectedTime);
				ResultSet rs = getStmt.executeQuery();
				rs.next();
				musicalDate = rs.getInt("id");

				// 예매 티켓 정보 삽입하기
				insertStmt.setString(1, musical.getTitle());
				insertStmt.setInt(2, musicalDate);
				insertStmt.setString(3, User.getId());
				insertStmt.setString(4, DateClass.getCurrentDate());
				insertStmt.executeUpdate();
				
				// 예매 성공 알림창 생성
				JDialog sucDialog = new JDialog();
				sucDialog.setTitle("티켓 예매 | " + musical.getTitle());
				sucDialog.setSize(200, 100);
				sucDialog.setLayout(new GridLayout(2, 1));
				;

				JLabel sucLabel = new JLabel("예매가 완료되었습니다 :)");
				sucDialog.add(sucLabel);
				JButton confirmBtn = new JButton("확인");
				// <확인> 버튼 누를 시 티켓 예매 창 모두 닫기
				confirmBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sucDialog.dispose();
						ticketDialog.dispose();
					}
				});
				sucDialog.add(confirmBtn);
				sucDialog.setVisible(true);

			} catch (SQLException sqle) {
				System.out.println(sqle);
			}

		}

	}
}
