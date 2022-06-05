package DB2022Team12;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class ReviewInsertionPanel extends JPanel {

	private JPanel noticePanel, reviewPanel, scorePanel, btnPanel;
	private JLabel noticeLabel;
	private JRadioButton[] scoreBtn;
	private JButton submitBtn;
	private ButtonGroup btnGroup;

	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체
	private Musical musical;

	// 별점(0~5)
	private final static String[] scoreList = { "★", "★★", "★★★", "★★★★", "★★★★★" };

	// 사용자가 작성한 리뷰를 DB에 삽입하는 쿼리
	private final static String INSERT_REVIEW_QUERY = "INSERT INTO review(musical_title, member_id, rate, written_at) VALUES (?, ?, ?, ?)";

	// 이전에 작성했던 리뷰를 갱신하는 쿼리
	private final static String UPDATE_REVIEW_QUERY = "UPDATE review SET rate = ? WHERE musical_title = ? AND member_id = ?";

	// 리뷰 작성 패널 레이아웃 설정
	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체를 전달 받음
	public ReviewInsertionPanel(Musical musical) {
		this.musical = musical;
		this.setSize(300, 100);
		this.setLayout(new GridLayout(2, 1));

		noticePanel = new JPanel();
		reviewPanel = new JPanel();

		noticeLabel = new JLabel("리뷰 등록");
		noticePanel.add(noticeLabel);

		scorePanel = new JPanel();
		btnPanel = new JPanel();

		// 별점(0~5점)을 나타내는 라디오 버튼 생성
		scoreBtn = new JRadioButton[5];
		btnGroup = new ButtonGroup();
		for (int i = 0; i < 5; i++) {
			scoreBtn[i] = new JRadioButton(scoreList[i]);
			btnGroup.add(scoreBtn[i]);
			scorePanel.add(scoreBtn[i]);
		}

		// 이미 존재하는 리뷰 정보가 있다면, 해당 별점을 기본으로 선택
		int selectedRate = musical.getUserReview();
		if (selectedRate != 0)
			scoreBtn[selectedRate - 1].setSelected(true);

		submitBtn = new JButton("등록하기");
		submitBtn.addActionListener(new submitBtnListener());
		btnPanel.add(submitBtn);

		reviewPanel.add(scorePanel);
		reviewPanel.add(btnPanel);
		this.add(noticePanel);
		this.add(reviewPanel);
	}

	// <등록하기> 버튼에 대한 리스너
	// 사용자로부터 입력 받은 리뷰(별점)를 DB에 저장
	private class submitBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 알림창 제목
			String dialogTitle = "리뷰 등록 | " + musical.getTitle();

			// 미로그인 유저인 경우, 리뷰 등록 불가
			if (User.getId() == null) {
				NotificationClass.createNotifDialog(dialogTitle, "로그인이 필요합니다");
				return;
			}

			// 사용자가 입력한 리뷰 정보(사용자가 선택한 라디오 버튼 번호 + 1)
			int rate = 0;
			for (int i = 0; i < 5; i++) {
				if (scoreBtn[i].isSelected())
					rate = i + 1;
			}

			// 별점이 선택되지 않은 경우, 버튼이 동작하지 않음
			if (rate == 0) {
				noticeLabel.setText("별점을 선택해주세요");
				return;
			}

			try (Connection conn = new ConnectionClass().getConnection();
					PreparedStatement insertStmt = conn.prepareStatement(INSERT_REVIEW_QUERY);
					PreparedStatement updateStmt = conn.prepareStatement(UPDATE_REVIEW_QUERY);) {

				// 이전에 작성한 리뷰가 있다면 새로 등록, 없다면 기존 리뷰 변경
				if (musical.getUserReview() == 0) {
					insertStmt.setString(1, musical.getTitle());
					insertStmt.setString(2, User.getId());
					insertStmt.setInt(3, rate);
					insertStmt.setString(4, DateClass.getCurrentDate());
					insertStmt.executeUpdate();

					// 리뷰 등록 성공 알림창 생성
					NotificationClass.createNotifDialog(dialogTitle, "리뷰가 등록되었습니다 :)");
				} else {
					System.out.println(rate);
					updateStmt.setInt(1, rate);
					updateStmt.setString(2, musical.getTitle());
					updateStmt.setString(3, User.getId());
					updateStmt.executeUpdate();

					// 리뷰 변경 성공 알림창 생성
					NotificationClass.createNotifDialog(dialogTitle, "리뷰가 변경되었습니다 :)");
				}

				musical.setUserReview(rate);

			} catch (SQLException sqle) {
				System.out.println(sqle);
			}
		}

	}
}