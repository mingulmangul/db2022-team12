package DB2022Team12;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class ReviewInsertionPanel extends JPanel {
	
	private JPanel reviewPanel, scorePanel, btnPanel;
	private JRadioButton[] scoreBtn;
	private JButton submitBtn;
	private ButtonGroup btnGroup;

	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체
	private Musical musical;
	
	// 별점(0~5)
	private final static String[] scoreList = {"★", "★★", "★★★", "★★★★", "★★★★★"};
	
	// 사용자가 작성한 리뷰를 DB에 삽입하는 쿼리
	private final static String INSERT_REVIEW_QUERY = "INSERT INTO review(musical_title, member_id, rate, written_at) "
			+ "VALUES (?, ?, ?, ?)";
	
	// 리뷰 작성 패널 레이아웃 설정
	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체를 전달 받음
	public ReviewInsertionPanel(Musical musical) {
		this.musical = musical;
		reviewPanel = new JPanel();
		reviewPanel.setSize(300, 100);
		
		scorePanel = new JPanel();
		btnPanel = new JPanel();

		// 별점(0~5점)을 나타내는 라디오 버튼 생성
		scoreBtn = new JRadioButton[5];
		btnGroup = new ButtonGroup();
		for (int i=0; i<5; i++) {
			scoreBtn[i] = new JRadioButton(scoreList[i]);
			btnGroup.add(scoreBtn[i]);
			scorePanel.add(scoreBtn[i]);
		}
		
		submitBtn = new JButton("등록하기");
		submitBtn.addActionListener(new submitBtnListener());
		btnPanel.add(submitBtn);
		
		reviewPanel.add(scorePanel);
		reviewPanel.add(btnPanel);
		this.add(reviewPanel);
	}
	
	// <등록하기> 버튼에 대한 리스너
	// 사용자로부터 입력 받은 리뷰(별점)를 DB에 저장
	private class submitBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 사용자가 입력한 리뷰 정보(사용자가 선택한 라디오 버튼 번호 + 1)
			int rate = 0;
			
			for (int i=0; i<5; i++) {
				if (scoreBtn[i].isSelected())
					rate = i + 1;
			}
			
			// 별점이 선택되지 않은 경우, 버튼이 동작하지 않음
			if (rate == 0)
				return;

			try (
				Connection conn = new ConnectionClass().getConnection();
				PreparedStatement pStmt = conn.prepareStatement(INSERT_REVIEW_QUERY);
			){
				pStmt.setString(1, musical.getTitle());
				pStmt.setString(2, User.getId());
				pStmt.setInt(3, rate);
				pStmt.setString(4, DateClass.getCurrentDate());
				pStmt.executeUpdate();
				
				// TODO: 알림창 생성 클래스 따로 만들기
				// 리뷰 등록 성공 알림창 생성
				JDialog sucDialog = new JDialog();
				sucDialog.setTitle("리뷰 등록 | " + musical.getTitle());
				sucDialog.setSize(200, 100);
				sucDialog.setLayout(new GridLayout(2, 1));

				JLabel sucLabel = new JLabel("리뷰가 등록되었습니다 :)");
				sucDialog.add(sucLabel);
				JButton confirmBtn = new JButton("확인");
				// <확인> 버튼 누를 시 알림창 닫기
				confirmBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sucDialog.dispose();
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
